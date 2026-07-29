/**
 * Performance API
 * All performance-related API operations
 */

import axiosInstance from './axiosInstance';
import { API_ENDPOINTS } from '../../constants';

/**
 * Performance API object containing all performance operations
 */
export const performanceApi = {
  /**
   * Run performance benchmark
   * @param {Object} benchmarkData - Benchmark configuration
   * @returns {Promise} Axios response with benchmark results
   */
  benchmark: (benchmarkData) => {
    return axiosInstance.post(API_ENDPOINTS.PERFORMANCE.BENCHMARK, benchmarkData);
  },

  /**
   * Compare performance between different strategies
   * @param {Object} compareData - Comparison configuration
   * @returns {Promise} Axios response with comparison results
   */
  compare: (compareData) => {
    return axiosInstance.post(API_ENDPOINTS.PERFORMANCE.COMPARE, compareData);
  },

  /**
   * Get performance metrics
   * @param {Object} params - Query parameters (time range, type, etc.)
   * @returns {Promise} Axios response with performance metrics
   */
  getMetrics: (params = {}) => {
    return axiosInstance.get(API_ENDPOINTS.PERFORMANCE.METRICS, { params });
  },

  /**
   * Get performance history
   * @param {Object} params - Query parameters (time range, limit, etc.)
   * @returns {Promise} Axios response with performance history
   */
  getHistory: (params = {}) => {
    return axiosInstance.get(API_ENDPOINTS.PERFORMANCE.HISTORY, { params });
  },

  /**
   * Benchmark cache performance
   * @param {number} iterations - Number of iterations
   * @param {string} operation - Operation type
   * @returns {Promise} Axios response with cache benchmark results
   */
  benchmarkCache: (iterations, operation = 'read') => {
    return axiosInstance.post(API_ENDPOINTS.PERFORMANCE.BENCHMARK, {
      type: 'cache',
      iterations,
      operation,
    });
  },

  /**
   * Benchmark database performance
   * @param {number} iterations - Number of iterations
   * @param {string} operation - Operation type
   * @returns {Promise} Axios response with database benchmark results
   */
  benchmarkDatabase: (iterations, operation = 'read') => {
    return axiosInstance.post(API_ENDPOINTS.PERFORMANCE.BENCHMARK, {
      type: 'database',
      iterations,
      operation,
    });
  },

  /**
   * Compare cache vs database performance
   * @param {number} iterations - Number of iterations
   * @returns {Promise} Axios response with comparison results
   */
  compareCacheVsDatabase: (iterations) => {
    return axiosInstance.post(API_ENDPOINTS.PERFORMANCE.COMPARE, {
      type: 'cache-vs-database',
      iterations,
    });
  },

  /**
   * Get average response time metrics
   * @param {string} timeRange - Time range (hour, day, week, month)
   * @returns {Promise} Axios response with average response times
   */
  getAverageResponseTime: (timeRange = 'hour') => {
    return axiosInstance.get(API_ENDPOINTS.PERFORMANCE.METRICS, {
      params: { metric: 'avg-response-time', timeRange },
    });
  },

  /**
   * Get fastest/slowest operations
   * @param {number} limit - Number of results to return
   * @returns {Promise} Axios response with fastest/slowest operations
   */
  getExtremes: (limit = 10) => {
    return axiosInstance.get(API_ENDPOINTS.PERFORMANCE.METRICS, {
      params: { metric: 'extremes', limit },
    });
  },
};

export default performanceApi;