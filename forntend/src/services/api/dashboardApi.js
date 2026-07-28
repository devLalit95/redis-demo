/**
 * Dashboard API Service
 * Handles all dashboard metrics API calls
 */

import axiosInstance from './axiosInstance';

/**
 * Get dashboard metrics
 */
export const getDashboardMetrics = async () => {
  const response = await axiosInstance.get('/api/dashboard/metrics');
  return response.data;
};

/**
 * Get performance statistics
 */
export const getPerformanceStats = async () => {
  const response = await axiosInstance.get('/api/dashboard/performance');
  return response.data;
};

/**
 * Get system health status
 */
export const getSystemHealth = async () => {
  const response = await axiosInstance.get('/api/dashboard/health');
  return response.data;
};

/**
 * Reset dashboard metrics
 */
export const resetMetrics = async () => {
  const response = await axiosInstance.post('/api/dashboard/reset');
  return response.data;
};

export default {
  getDashboardMetrics,
  getPerformanceStats,
  getSystemHealth,
  resetMetrics,
};