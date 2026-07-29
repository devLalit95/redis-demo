/**
 * Dashboard API
 * All dashboard-related API operations
 */

import axiosInstance from './axiosInstance';
import { API_ENDPOINTS } from '../../constants';

/**
 * Dashboard API object containing all dashboard operations
 */
export const dashboardApi = {
  /**
   * Get dashboard overview
   * @returns {Promise} Axios response with dashboard overview
   */
  getOverview: () => {
    return axiosInstance.get(API_ENDPOINTS.DASHBOARD.OVERVIEW);
  },

  /**
   * Get dashboard statistics
   * @returns {Promise} Axios response with dashboard statistics
   */
  getStatistics: () => {
    return axiosInstance.get(API_ENDPOINTS.DASHBOARD.STATISTICS);
  },

  /**
   * Get recent activity
   * @param {Object} params - Query parameters (limit, type, etc.)
   * @returns {Promise} Axios response with recent activity
   */
  getRecent: (params = {}) => {
    return axiosInstance.get(API_ENDPOINTS.DASHBOARD.RECENT, { params });
  },

  /**
   * Get dashboard metrics
   * @returns {Promise} Axios response with dashboard metrics
   */
  getMetrics: () => {
    return axiosInstance.get('/api/dashboard/metrics');
  },

  /**
   * Get performance statistics
   * @returns {Promise} Axios response with performance statistics
   */
  getPerformanceStats: () => {
    return axiosInstance.get('/api/dashboard/performance');
  },

  /**
   * Get system health status
   * @returns {Promise} Axios response with system health
   */
  getSystemHealth: () => {
    return axiosInstance.get('/api/dashboard/health');
  },

  /**
   * Reset dashboard metrics
   * @returns {Promise} Axios response
   */
  resetMetrics: () => {
    return axiosInstance.post('/api/dashboard/reset');
  },
};

export default dashboardApi;