/**
 * Axios Instance Configuration
 * Configured axios instance with interceptors for API calls
 */

import axios from 'axios';
import API_CONFIG from '../../config/api';

/**
 * Create and configure axios instance
 */
const axiosInstance = axios.create({
  baseURL: API_CONFIG.BASE_URL,
  timeout: API_CONFIG.TIMEOUT,
  // Only use withCredentials in production or when not using proxy
  // In development with Vite proxy, credentials are handled by the proxy
  withCredentials: !import.meta.env.DEV,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * Request interceptor
 * Adds authentication, logging, and request transformation
 */
axiosInstance.interceptors.request.use(
  (config) => {
    // Add timestamp to prevent caching
    config.params = {
      ...config.params,
      _t: Date.now(),
    };
    
    // Log request in development
    if (import.meta.env.DEV) {
      console.log(`[API Request] ${config.method?.toUpperCase()} ${config.url}`, config.data || config.params);
    }
    
    return config;
  },
  (error) => {
    console.error('[API Request Error]', error);
    return Promise.reject(error);
  }
);

/**
 * Response interceptor
 * Handles response transformation, error handling, and logging
 */
axiosInstance.interceptors.response.use(
  (response) => {
    // Log response in development
    if (import.meta.env.DEV) {
      console.log(`[API Response] ${response.config.url}`, response.data);
    }
    
    return response;
  },
  (error) => {
    // Handle common error scenarios
    if (error.response) {
      // Server responded with error status
      const { status, data } = error.response;
      
      console.error(`[API Error] ${status}:`, data?.message || error.message);
      
      // Handle specific status codes
      switch (status) {
        case 401:
          // Unauthorized - handle token refresh or redirect
          console.error('Unauthorized access');
          break;
        case 403:
          // Forbidden
          console.error('Access forbidden');
          break;
        case 404:
          // Not found
          console.error('Resource not found');
          break;
        case 500:
          // Server error
          console.error('Server error');
          break;
        default:
          console.error('API error occurred');
      }
    } else if (error.request) {
      // Request made but no response received - likely CORS or network error
      console.error('[API Network Error] No response received:', error.message);
      console.error('This might be a CORS issue. Check backend CORS configuration.');
    } else {
      // Error in request configuration
      console.error('[API Request Config Error]:', error.message);
    }
    
    // Add custom error code for easier identification in React Query
    if (!error.response && error.request) {
      error.code = 'ERR_NETWORK';
    }
    
    return Promise.reject(error);
  }
);

export default axiosInstance;