/**
 * Health API
 * All health check-related API operations
 */

import axiosInstance from './axiosInstance';
import { API_ENDPOINTS } from '../../constants';

/**
 * Health API object containing all health check operations
 */
export const healthApi = {
  /**
   * Get overall application health status
   * @returns {Promise} Axios response with health status
   */
  check: () => {
    return axiosInstance.get(API_ENDPOINTS.HEALTH.CHECK);
  },

  /**
   * Get deep health check with detailed diagnostics
   * @returns {Promise} Axios response with detailed health status
   */
  deepCheck: () => {
    return axiosInstance.get(API_ENDPOINTS.HEALTH.DEEP_CHECK);
  },

  /**
   * Get Redis health status
   * @returns {Promise} Axios response with Redis health status
   */
  checkRedis: () => {
    return axiosInstance.get(API_ENDPOINTS.HEALTH.REDIS);
  },

  /**
   * Get database health status
   * @returns {Promise} Axios response with database health status
   */
  checkDatabase: () => {
    return axiosInstance.get(API_ENDPOINTS.HEALTH.DATABASE);
  },

  /**
   * Get API health status
   * @returns {Promise} Axios response with API health status
   */
  checkApi: () => {
    return axiosInstance.get('/api/health/api');
  },

  /**
   * Get cache health status
   * @returns {Promise} Axios response with cache health status
   */
  checkCache: () => {
    return axiosInstance.get('/api/health/cache');
  },

  /**
   * Get system resource health (CPU, memory, disk)
   * @returns {Promise} Axios response with system resource health
   */
  checkSystemResources: () => {
    return axiosInstance.get('/api/health/system');
  },

  /**
   * Get connection pool health
   * @returns {Promise} Axios response with connection pool health
   */
  checkConnectionPool: () => {
    return axiosInstance.get('/api/health/connection-pool');
  },

  /**
   * Run comprehensive health check
   * @returns {Promise} Axios response with comprehensive health status
   */
  comprehensiveCheck: () => {
    return axiosInstance.get('/api/health/comprehensive');
  },
};

export default healthApi;