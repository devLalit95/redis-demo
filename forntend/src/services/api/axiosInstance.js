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
        case 400:
          error.message = data?.message || 'Bad request. Please check your input.';
          break;
        case 401:
          error.message = 'Unauthorized access. Please authenticate.';
          break;
        case 403:
          error.message = 'Access forbidden. You don\'t have permission.';
          break;
        case 404:
          error.message = 'Resource not found. Please check the endpoint.';
          break;
        case 500:
          error.message = 'Server error. Please try again later.';
          break;
        case 503:
          error.message = 'Service unavailable. Please try again later.';
          break;
        default:
          error.message = data?.message || error.message || 'An unexpected error occurred.';
      }
      
      error.userMessage = error.message;
    } else if (error.request) {
      // Request made but no response received - likely CORS or network error
      console.error('[API Network Error] No response received:', error.message);
      error.message = 'Network error. Please check your connection.';
      error.userMessage = 'Network error. Please check your connection.';
      error.code = 'ERR_NETWORK';
    } else {
      // Error in request configuration
      console.error('[API Request Config Error]:', error.message);
      error.message = 'Request configuration error.';
      error.userMessage = 'Request configuration error.';
    }
    
    return Promise.reject(error);
  }
);

export default axiosInstance;