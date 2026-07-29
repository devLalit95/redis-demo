/**
 * Metrics API
 * All metrics-related API operations
 */

import axiosInstance from './axiosInstance';
import { API_ENDPOINTS } from '../../constants';

/**
 * Metrics API object containing all metrics operations
 */
export const metricsApi = {
  /**
   * Get dashboard metrics
   * @returns {Promise} Axios response with dashboard metrics
   */
  getDashboard: () => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.DASHBOARD);
  },

  /**
   * Get Redis metrics
   * @param {Object} params - Query parameters (time range, type, etc.)
   * @returns {Promise} Axios response with Redis metrics
   */
  getRedis: (params = {}) => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.REDIS, { params });
  },

  /**
   * Get cache metrics
   * @param {Object} params - Query parameters (time range, type, etc.)
   * @returns {Promise} Axios response with cache metrics
   */
  getCache: (params = {}) => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.CACHE, { params });
  },

  /**
   * Get API metrics
   * @param {Object} params - Query parameters (time range, type, etc.)
   * @returns {Promise} Axios response with API metrics
   */
  getApi: (params = {}) => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.API, { params });
  },

  /**
   * Get cache hit ratio
   * @param {string} timeRange - Time range (hour, day, week, month)
   * @returns {Promise} Axios response with cache hit ratio
   */
  getCacheHitRatio: (timeRange = 'hour') => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.CACHE, {
      params: { metric: 'hit-ratio', timeRange },
    });
  },

  /**
   * Get memory usage metrics
   * @param {string} timeRange - Time range (hour, day, week, month)
   * @returns {Promise} Axios response with memory usage metrics
   */
  getMemoryUsage: (timeRange = 'hour') => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.REDIS, {
      params: { metric: 'memory-usage', timeRange },
    });
  },

  /**
   * Get response time metrics
   * @param {string} timeRange - Time range (hour, day, week, month)
   * @returns {Promise} Axios response with response time metrics
   */
  getResponseTime: (timeRange = 'hour') => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.API, {
      params: { metric: 'response-time', timeRange },
    });
  },

  /**
   * Get operation count metrics
   * @param {string} timeRange - Time range (hour, day, week, month)
   * @returns {Promise} Axios response with operation count metrics
   */
  getOperationCount: (timeRange = 'hour') => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.REDIS, {
      params: { metric: 'operation-count', timeRange },
    });
  },

  /**
   * Get error rate metrics
   * @param {string} timeRange - Time range (hour, day, week, month)
   * @returns {Promise} Axios response with error rate metrics
   */
  getErrorRate: (timeRange = 'hour') => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.API, {
      params: { metric: 'error-rate', timeRange },
    });
  },

  /**
   * Get TTL distribution metrics
   * @returns {Promise} Axios response with TTL distribution
   */
  getTTLDistribution: () => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.CACHE, {
      params: { metric: 'ttl-distribution' },
    });
  },

  /**
   * Get key type distribution
   * @returns {Promise} Axios response with key type distribution
   */
  getKeyTypeDistribution: () => {
    return axiosInstance.get(API_ENDPOINTS.METRICS.REDIS, {
      params: { metric: 'key-type-distribution' },
    });
  },
};

export default metricsApi;