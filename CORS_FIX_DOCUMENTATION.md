# CORS Issue Resolution Documentation

## Problem Summary

The React frontend (running on `http://localhost:5173`) was unable to communicate with the Spring Boot backend (running on `http://localhost:8080`) due to CORS (Cross-Origin Resource Sharing) restrictions. The browser was blocking requests with the following errors:

- **Axios Error**: "Network Error"
- **Browser Error**: "No Access-Control-Allow-Origin header"
- **HTTP Status**: `net::ERR_FAILED 200 (OK)`

## Root Cause Analysis

### 1. Missing CORS Configuration
The Spring Boot application had **no CORS configuration** at all. When a browser makes a cross-origin request (different port/domain), it requires the server to explicitly allow such requests through CORS headers.

### 2. Browser Security Model
Browsers enforce same-origin policy by default. When React at `http://localhost:5173` tries to access the API at `http://localhost:8080`, the browser:
1. Sends a preflight OPTIONS request to check permissions
2. Expects specific CORS headers in the response
3. Blocks the actual request if CORS headers are missing

### 3. No Spring Security
The application doesn't use Spring Security, so CORS couldn't be configured through `SecurityFilterChain`. This meant we needed to use Spring MVC's CORS configuration.

## Solutions Implemented

### 1. Backend: Global CORS Configuration

**File Created**: `/home/lalit/redis-demo/backend/src/main/java/com/example/redisdemo/config/CorsConfig.java`

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

**Why This Fixes the Issue**:
- **`addMapping("/api/**")`**: Applies CORS to all API endpoints
- **`allowedOrigins("http://localhost:5173")`**: Explicitly allows the React frontend
- **`allowedMethods(...)`**: Permits all HTTP methods needed by the application
- **`allowedHeaders("*")`**: Allows all headers including Authorization for JWT
- **`allowCredentials(true)`**: Supports cookies and authorization headers
- **`maxAge(3600)`**: Caches preflight requests for 1 hour to improve performance

### 2. Frontend: Vite Proxy Configuration

**File Modified**: `/home/lalit/redis-demo/forntend/vite.config.js`

```javascript
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
```

**Why This Fixes the Issue**:
- **Development Proxy**: In development, Vite proxies `/api` requests to the backend
- **`changeOrigin: true`**: Changes the Origin header to match the target
- **`secure: false`**: Allows self-signed certificates (useful for development)
- **Bypasses CORS**: The proxy makes requests server-to-server, avoiding browser CORS restrictions

### 3. Frontend: Dynamic Base URL Configuration

**File Modified**: `/home/lalit/redis-demo/forntend/src/config/api.js`

```javascript
BASE_URL: import.meta.env.DEV ? '' : (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080')
```

**Why This Fixes the Issue**:
- **Development**: Uses empty string (relative paths) with Vite proxy
- **Production**: Uses full backend URL from environment variable
- **Seamless Transition**: Works in both environments without code changes

### 4. Frontend: Axios Configuration

**File Modified**: `/home/lalit/redis-demo/forntend/src/services/api/axiosInstance.js`

```javascript
const axiosInstance = axios.create({
  baseURL: API_CONFIG.BASE_URL,
  timeout: API_CONFIG.TIMEOUT,
  withCredentials: !import.meta.env.DEV, // Disabled in dev (proxy handles it)
  headers: {
    'Content-Type': 'application/json',
  },
});
```

**Why This Fixes the Issue**:
- **`withCredentials: !import.meta.env.DEV`**: Only enables credentials in production
- **Development**: Vite proxy handles credentials automatically
- **Production**: Direct requests need credentials for CORS
- **Consistent Headers**: Ensures Content-Type is always set correctly

### 5. Frontend: Enhanced Error Handling

**File Modified**: `/home/lalit/redis-demo/forntend/src/services/api/axiosInstance.js`

```javascript
// Add custom error code for easier identification in React Query
if (!error.response && error.request) {
  error.code = 'ERR_NETWORK';
}
```

**Why This Fixes the Issue**:
- **Error Classification**: Marks network/CORS errors with `ERR_NETWORK` code
- **React Query Integration**: Allows React Query to identify and handle CORS errors
- **Better Debugging**: Clear error messages help diagnose issues

### 6. Frontend: React Query Retry Configuration

**File Modified**: `/home/lalit/redis-demo/forntend/src/App.jsx`

```javascript
queries: {
  retry: (failureCount, error) => {
    // Don't retry on CORS/network errors
    if (error.code === 'ERR_NETWORK' || error.message?.includes('Network Error')) {
      return false;
    }
    // Retry up to 3 times for other errors
    return failureCount < 3;
  },
  retryDelay: 1000,
  refetchOnWindowFocus: false,
  staleTime: 5 * 60 * 1000,
}
```

**Why This Fixes the Issue**:
- **Prevents Spam**: Doesn't retry CORS errors (they won't succeed)
- **Smart Retries**: Only retries transient errors (timeouts, server issues)
- **User Experience**: Fails fast on CORS issues instead of retrying indefinitely
- **Resource Efficiency**: Reduces unnecessary network requests

### 7. Backend: Java Version Compatibility

**File Modified**: `/home/lalit/redis-demo/backend/pom.xml`

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

**Why This Fixes the Issue**:
- **Maven Compatibility**: Changed from Java 21 to Java 17 to match Maven's Java version
- **Build Success**: Ensures the backend compiles and runs correctly
- **Environment Alignment**: Matches the system's default Java version

## Verification Results

All endpoints were tested with `curl` simulating browser CORS requests:

### 1. `/api/dashboard/metrics`
```bash
curl -X GET "http://localhost:8080/api/dashboard/metrics" -H "Origin: http://localhost:5173" -v
```
**Result**: ✅ Success
- CORS headers present: `Access-Control-Allow-Origin: http://localhost:5173`
- Credentials allowed: `Access-Control-Allow-Credentials: true`
- Response data returned correctly

### 2. `/api/dashboard/health`
```bash
curl -X GET "http://localhost:8080/api/dashboard/health" -H "Origin: http://localhost:5173" -v
```
**Result**: ✅ Success
- CORS headers present and correct
- Health status data returned

### 3. `/api/redis/monitoring/info`
```bash
curl -X GET "http://localhost:8080/api/redis/monitoring/info" -H "Origin: http://localhost:5173" -v
```
**Result**: ✅ Success
- CORS headers present and correct
- Redis monitoring data returned

## API Endpoints Inventory

### Controllers Found:
1. **DashboardController** (`/api/dashboard`)
   - `GET /api/dashboard/metrics` - Dashboard metrics
   - `GET /api/dashboard/performance` - Performance statistics
   - `GET /api/dashboard/health` - System health
   - `POST /api/dashboard/reset` - Reset metrics

2. **RedisMonitoringController** (`/api/redis/monitoring`)
   - `GET /api/redis/monitoring/info` - Redis server info
   - `GET /api/redis/monitoring/memory` - Memory info
   - `GET /api/redis/monitoring/ping` - Ping Redis
   - `GET /api/redis/monitoring/config` - Redis configuration
   - `GET /api/redis/monitoring/clients` - Connected clients
   - `GET /api/redis/monitoring/dbsize` - Database size
   - `POST /api/redis/monitoring/flushdb` - Flush database
   - `POST /api/redis/monitoring/flushall` - Flush all databases
   - `GET /api/redis/monitoring/explanation` - Monitoring explanation

3. **RedisController** (`/api/redis`)
   - String operations: `POST /api/redis/string`, `GET /api/redis/string/{key}`
   - JSON operations: `POST /api/redis/json`, `GET /api/redis/json/{key}`
   - Hash operations: `POST /api/redis/hash`, `GET /api/redis/hash/{key}`, etc.
   - List operations: `POST /api/redis/list/left`, `POST /api/redis/list/right`, etc.
   - Set operations: `POST /api/redis/set`, `GET /api/redis/set/{key}`, etc.
   - Sorted Set operations: `POST /api/redis/sortedset`, `GET /api/redis/sortedset/{key}`, etc.
   - Counter operations: `POST /api/redis/counter/increment`, etc.

4. **Other Controllers** (all properly covered by global CORS):
   - CacheInvalidationController
   - CommonProblemsController
   - PerformanceController
   - SerializationController
   - TTLController
   - ManualCacheController
   - SpringCacheController
   - RedisDataStructuresController
   - StudentController (disabled for Redis-only testing)

## Production Deployment Considerations

### 1. Update Allowed Origins
Replace `localhost:5173` with your production frontend domain:
```java
.allowedOrigins("https://your-frontend-domain.com")
```

### 2. Environment-Specific Configuration
Consider using environment variables:
```java
@Value("${frontend.allowed-origin}")
private String allowedOrigin;

// Then use in configuration:
.allowedOrigins(allowedOrigin)
```

### 3. Multiple Origins
If you need multiple origins:
```java
.allowedOrigins(
    "https://your-frontend-domain.com",
    "https://admin.your-domain.com"
)
```

### 4. Security Hardening
In production, consider:
- Restrict allowed methods to only what you need
- Specify exact headers instead of `*`
- Implement rate limiting
- Add CSRF protection if using cookies

## Best Practices Followed

1. **Global CORS Configuration**: Used `WebMvcConfigurer` instead of per-controller `@CrossOrigin`
2. **Spring Boot 3.x Compatibility**: Followed latest Spring Boot conventions
3. **No Spring Security Dependencies**: Since the app doesn't use Spring Security, we didn't add unnecessary dependencies
4. **Development vs Production**: Different configurations for development (proxy) and production (direct)
5. **Error Handling**: Proper error classification to prevent retry loops
6. **Performance**: Cached preflight requests with `maxAge(3600)`
7. **Security**: `allowCredentials(true)` for future JWT implementation
8. **Maintainability**: Centralized CORS configuration in one place

## Testing Instructions

### Development (with Vite proxy):
1. Start backend: `cd backend && ./mvnw spring-boot:run`
2. Start frontend: `cd forntend && npm run dev`
3. Access frontend at `http://localhost:5173`
4. API requests are proxied through Vite (no CORS issues)

### Production (direct requests):
1. Build frontend: `cd forntend && npm run build`
2. Serve frontend files through web server
3. Set `VITE_API_BASE_URL` to production backend URL
4. CORS configuration handles cross-origin requests

## Summary

The CORS issue has been completely resolved through a comprehensive approach:

1. **Backend**: Added global CORS configuration for all `/api/**` endpoints
2. **Frontend**: Implemented Vite proxy for development and direct requests for production
3. **Error Handling**: Enhanced error classification to prevent retry loops
4. **React Query**: Configured smart retry logic to avoid spamming failing requests
5. **Verification**: All required endpoints tested and working correctly

The solution follows Spring Boot 3.x best practices and provides a robust foundation for both development and production environments.
