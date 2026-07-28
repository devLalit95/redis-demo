/**
 * Cache Invalidation API Service
 * Handles all cache invalidation API calls
 */

import axiosInstance from './axiosInstance';

/**
 * Evict student cache by ID
 */
export const evictStudentCache = async (id) => {
  const response = await axiosInstance.delete(`/api/cache-invalidation/students/${id}`);
  return response.data;
};

/**
 * Evict student cache by email
 */
export const evictStudentCacheByEmail = async (email) => {
  const response = await axiosInstance.delete(`/api/cache-invalidation/students/email/${email}`);
  return response.data;
};

/**
 * Evict all student cache
 */
export const evictAllStudentCache = async () => {
  const response = await axiosInstance.delete('/api/cache-invalidation/students/all');
  return response.data;
};

/**
 * Update student with cache evict
 */
export const updateStudentWithEvict = async (id, studentDTO) => {
  const response = await axiosInstance.put(`/api/cache-invalidation/students/${id}`, studentDTO);
  return response.data;
};

/**
 * Delete student with cache evict
 */
export const deleteStudentWithEvict = async (id) => {
  const response = await axiosInstance.delete(`/api/cache-invalidation/students/${id}/delete`);
  return response.data;
};

/**
 * Refresh student cache
 */
export const refreshStudentCache = async (id) => {
  const response = await axiosInstance.post(`/api/cache-invalidation/students/${id}/refresh`);
  return response.data;
};

/**
 * Lazy load student into cache
 */
export const lazyLoadStudent = async (id) => {
  const response = await axiosInstance.get(`/api/cache-invalidation/students/${id}/lazy`);
  return response.data;
};

/**
 * Write through caching for student
 */
export const writeThroughStudent = async (id, studentDTO) => {
  const response = await axiosInstance.post(`/api/cache-invalidation/students/${id}/write-through`, studentDTO);
  return response.data;
};

/**
 * Cache aside pattern for student
 */
export const cacheAsideStudent = async (id) => {
  const response = await axiosInstance.get(`/api/cache-invalidation/students/${id}/cache-aside`);
  return response.data;
};

/**
 * Get cache invalidation explanation
 */
export const getCacheInvalidationExplanation = async () => {
  const response = await axiosInstance.get('/api/cache-invalidation/students/explanation');
  return response.data;
};

export default {
  evictStudentCache,
  evictStudentCacheByEmail,
  evictAllStudentCache,
  updateStudentWithEvict,
  deleteStudentWithEvict,
  refreshStudentCache,
  lazyLoadStudent,
  writeThroughStudent,
  cacheAsideStudent,
  getCacheInvalidationExplanation,
};