/**
 * API Services Index
 * Centralized export of all API services
 */

export { default as axiosInstance } from './axiosInstance';
export { studentApi } from './studentApi';
export { cacheApi } from './cacheApi';
export { redisApi } from './redisApi';
export { performanceApi } from './performanceApi';
export { metricsApi } from './metricsApi';
export { dashboardApi } from './dashboardApi';
export { configApi } from './configApi';
export { healthApi } from './healthApi';

// Export monitoring API as well (already exists)
export { default as monitoringApi } from './monitoringApi';