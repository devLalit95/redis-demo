/**
 * Configuration API
 * All configuration-related API operations
 */

import axiosInstance from './axiosInstance';
import { API_ENDPOINTS } from '../../constants';

/**
 * Configuration API object containing all configuration operations
 */
export const configApi = {
  /**
   * Get application configuration
   * @returns {Promise} Axios response with configuration
   */
  get: () => {
    return axiosInstance.get(API_ENDPOINTS.CONFIG.GET);
  },

  /**
   * Update application configuration
   * @param {Object} configData - Configuration data to update
   * @returns {Promise} Axios response with updated configuration
   */
  update: (configData) => {
    return axiosInstance.put(API_ENDPOINTS.CONFIG.UPDATE, configData);
  },

  /**
   * Reset application configuration to defaults
   * @returns {Promise} Axios response with default configuration
   */
  reset: () => {
    return axiosInstance.post(API_ENDPOINTS.CONFIG.RESET);
  },

  /**
   * Get Redis configuration
   * @returns {Promise} Axios response with Redis configuration
   */
  getRedisConfig: () => {
    return axiosInstance.get('/api/config/redis');
  },

  /**
   * Update Redis configuration
   * @param {Object} redisConfig - Redis configuration data
   * @returns {Promise} Axios response with updated Redis configuration
   */
  updateRedisConfig: (redisConfig) => {
    return axiosInstance.put('/api/config/redis', redisConfig);
  },

  /**
   * Get cache configuration
   * @returns {Promise} Axios response with cache configuration
   */
  getCacheConfig: () => {
    return axiosInstance.get('/api/config/cache');
  },

  /**
   * Update cache configuration
   * @param {Object} cacheConfig - Cache configuration data
   * @returns {Promise} Axios response with updated cache configuration
   */
  updateCacheConfig: (cacheConfig) => {
    return axiosInstance.put('/api/config/cache', cacheConfig);
  },

  /**
   * Get API configuration
   * @returns {Promise} Axios response with API configuration
   */
  getApiConfig: () => {
    return axiosInstance.get('/api/config/api');
  },

  /**
   * Update API configuration
   * @param {Object} apiConfig - API configuration data
   * @returns {Promise} Axios response with updated API configuration
   */
  updateApiConfig: (apiConfig) => {
    return axiosInstance.put('/api/config/api', apiConfig);
  },

  /**
   * Get feature flags
   * @returns {Promise} Axios response with feature flags
   */
  getFeatureFlags: () => {
    return axiosInstance.get('/api/config/features');
  },

  /**
   * Update feature flags
   * @param {Object} features - Feature flags data
   * @returns {Promise} Axios response with updated feature flags
   */
  updateFeatureFlags: (features) => {
    return axiosInstance.put('/api/config/features', features);
  },
};

export default configApi;