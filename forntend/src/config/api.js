/**
 * API Configuration
 * Centralized API configuration and base URL setup
 */

const API_CONFIG = {
  // Base URL - use empty string for Vite proxy in development, or full URL for production
  // In development with Vite proxy, use relative path. In production, use full backend URL.
  BASE_URL: import.meta.env.DEV ? '' : (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'),
  
  // API timeout in milliseconds
  TIMEOUT: 30000,
  
  // Retry configuration (reduced to prevent spam on CORS errors)
  RETRY_COUNT: 2,
  RETRY_DELAY: 1000,
  
  // API endpoints
  ENDPOINTS: {
    // Redis operations
    REDIS: {
      STRING: '/api/redis/string',
      JSON: '/api/redis/json',
      HASH: '/api/redis/hash',
      LIST: '/api/redis/list',
      SET: '/api/redis/set',
      SORTED_SET: '/api/redis/sortedset',
      COUNTER: '/api/redis/counter',
    },
    
    // Cache invalidation
    CACHE_INVALIDATION: '/api/cache-invalidation/students',
    
    // Dashboard
    DASHBOARD: '/api/dashboard',
    
    // Redis data structures
    DATA_STRUCTURES: '/api/redis/data-structures',
    
    // Redis monitoring
    MONITORING: '/api/redis/monitoring',
  },
};

export default API_CONFIG;