package com.example.redisdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration
 * 
 * This class configures Cross-Origin Resource Sharing (CORS) for the application.
 * 
 * WHY this configuration exists:
 * - Allows React frontend (http://localhost:5173) to communicate with Spring Boot backend
 * - Enables browser security policies while permitting legitimate cross-origin requests
 * - Supports preflight OPTIONS requests
 * - Allows custom headers including Authorization for JWT
 * 
 * WHEN to use this configuration:
 * - Development: React frontend on different port/domain
 * - Production: Frontend and backend on different domains
 * - API access from web browsers
 * 
 * PRODUCTION USE CASES:
 * - Frontend-backend separation
 * - Multi-domain applications
 * - API gateway scenarios
 * 
 * SECURITY CONSIDERATIONS:
 * - In production, replace localhost:5173 with actual frontend domain
 * - Consider using environment variables for allowed origins
 * - Restrict allowed methods to only what your application needs
 * - For JWT authentication, ensure Authorization header is allowed
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configure CORS mappings.
     * 
     * WHY this method exists:
     * - Defines which origins can access the API
     * - Specifies allowed HTTP methods
     * - Sets allowed headers for requests
     * - Configures credential support for cookies/authorization
     * 
     * CONFIGURATION DETAILS:
     * - Allowed origins: http://localhost:5173 (React development server)
     * - Allowed methods: GET, POST, PUT, PATCH, DELETE, OPTIONS
     * - Allowed headers: * (all headers)
     * - Allow credentials: true (for cookies, authorization headers)
     * - Max age: 3600 seconds (1 hour for preflight cache)
     * 
     * PRODUCTION NOTES:
     * - Replace localhost:5173 with production frontend domain
     * - Consider using environment-specific configuration
     * - May need to add multiple origins for different environments
     * 
     * @param registry CORS registry to configure
     */
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
