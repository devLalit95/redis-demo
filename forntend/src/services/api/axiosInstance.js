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
      // Request made but no response received
      console.error('[API Network Error] No response received:', error.message);
    } else {
      // Error in request configuration
      console.error('[API Request Config Error]:', error.message);
    }
    
    return Promise.reject(error);
  }
);

export default axiosInstance;