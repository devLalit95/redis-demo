/**
 * Redis Monitoring API Service
 * Handles all Redis monitoring API calls
 */

import axiosInstance from './axiosInstance';

/**
 * Get Redis server information
 */
export const getRedisInfo = async () => {
  const response = await axiosInstance.get('/api/redis/monitoring/info');
  return response.data;
};

/**
 * Get Redis memory information
 */
export const getRedisMemoryInfo = async () => {
  const response = await axiosInstance.get('/api/redis/monitoring/memory');
  return response.data;
};

/**
 * Ping Redis server
 */
export const pingRedis = async () => {
  const response = await axiosInstance.get('/api/redis/monitoring/ping');
  return response.data;
};

/**
 * Get Redis configuration
 */
export const getRedisConfig = async (pattern = '*') => {
  const response = await axiosInstance.get('/api/redis/monitoring/config', { params: { pattern } });
  return response.data;
};

/**
 * Get connected clients list
 */
export const getClientList = async () => {
  const response = await axiosInstance.get('/api/redis/monitoring/clients');
  return response.data;
};

/**
 * Get database size
 */
export const getDatabaseSize = async () => {
  const response = await axiosInstance.get('/api/redis/monitoring/dbsize');
  return response.data;
};

/**
 * Flush current database (WARNING: destructive operation)
 */
export const flushDatabase = async () => {
  const response = await axiosInstance.post('/api/redis/monitoring/flushdb');
  return response.data;
};

/**
 * Flush all databases (WARNING: destructive operation)
 */
export const flushAllDatabases = async () => {
  const response = await axiosInstance.post('/api/redis/monitoring/flushall');
  return response.data;
};

/**
 * Get monitoring explanation
 */
export const getMonitoringExplanation = async () => {
  const response = await axiosInstance.get('/api/redis/monitoring/explanation');
  return response.data;
};

export default {
  getRedisInfo,
  getRedisMemoryInfo,
  pingRedis,
  getRedisConfig,
  getClientList,
  getDatabaseSize,
  flushDatabase,
  flushAllDatabases,
  getMonitoringExplanation,
};