/**
 * Cache API
 * All cache-related API operations
 */

import axiosInstance from './axiosInstance';
import { API_ENDPOINTS } from '../../constants';

/**
 * Cache API object containing all cache operations
 */
export const cacheApi = {
  /**
   * Get cache value by key
   * @param {string} key - Cache key
   * @returns {Promise} Axios response with cache value
   */
  get: (key) => {
    const url = API_ENDPOINTS.CACHE.GET.replace(':key', key);
    return axiosInstance.get(url);
  },

  /**
   * Set cache value
   * @param {Object} cacheData - Cache data (key, value, ttl)
   * @returns {Promise} Axios response
   */
  set: (cacheData) => {
    return axiosInstance.post(API_ENDPOINTS.CACHE.SET, cacheData);
  },

  /**
   * Delete cache by key
   * @param {string} key - Cache key
   * @returns {Promise} Axios response
   */
  delete: (key) => {
    const url = API_ENDPOINTS.CACHE.DELETE.replace(':key', key);
    return axiosInstance.delete(url);
  },

  /**
   * Clear all cache
   * @returns {Promise} Axios response
   */
  clear: () => {
    return axiosInstance.post(API_ENDPOINTS.CACHE.CLEAR);
  },

  /**
   * Refresh cache
   * @param {string} key - Cache key to refresh
   * @returns {Promise} Axios response
   */
  refresh: (key) => {
    return axiosInstance.post(API_ENDPOINTS.CACHE.REFRESH, { key });
  },

  /**
   * Set cache expiration
   * @param {string} key - Cache key
   * @param {number} ttl - Time to live in seconds
   * @returns {Promise} Axios response
   */
  expire: (key, ttl) => {
    const url = API_ENDPOINTS.CACHE.EXPIRE.replace(':key', key);
    return axiosInstance.put(url, { ttl });
  },

  /**
   * Get cache statistics
   * @returns {Promise} Axios response with cache stats
   */
  getStats: () => {
    return axiosInstance.get(API_ENDPOINTS.CACHE.STATS);
  },

  // Cache invalidation specific operations
  /**
   * Evict student cache by ID
   * @param {number|string} id - Student ID
   * @returns {Promise} Axios response
   */
  evictStudentCache: (id) => {
    return axiosInstance.delete(`/api/cache-invalidation/students/${id}`);
  },

  /**
   * Evict student cache by email
   * @param {string} email - Student email
   * @returns {Promise} Axios response
   */
  evictStudentCacheByEmail: (email) => {
    return axiosInstance.delete(`/api/cache-invalidation/students/email/${email}`);
  },

  /**
   * Evict all student cache
   * @returns {Promise} Axios response
   */
  evictAllStudentCache: () => {
    return axiosInstance.delete('/api/cache-invalidation/students/all');
  },

  /**
   * Update student with cache evict
   * @param {number|string} id - Student ID
   * @param {Object} studentData - Student data
   * @returns {Promise} Axios response
   */
  updateStudentWithEvict: (id, studentData) => {
    return axiosInstance.put(`/api/cache-invalidation/students/${id}`, studentData);
  },

  /**
   * Delete student with cache evict
   * @param {number|string} id - Student ID
   * @returns {Promise} Axios response
   */
  deleteStudentWithEvict: (id) => {
    return axiosInstance.delete(`/api/cache-invalidation/students/${id}/delete`);
  },

  /**
   * Refresh student cache
   * @param {number|string} id - Student ID
   * @returns {Promise} Axios response
   */
  refreshStudentCache: (id) => {
    return axiosInstance.post(`/api/cache-invalidation/students/${id}/refresh`);
  },

  /**
   * Lazy load student into cache
   * @param {number|string} id - Student ID
   * @returns {Promise} Axios response
   */
  lazyLoadStudent: (id) => {
    return axiosInstance.get(`/api/cache-invalidation/students/${id}/lazy`);
  },

  /**
   * Write through caching for student
   * @param {number|string} id - Student ID
   * @param {Object} studentData - Student data
   * @returns {Promise} Axios response
   */
  writeThroughStudent: (id, studentData) => {
    return axiosInstance.post(`/api/cache-invalidation/students/${id}/write-through`, studentData);
  },

  /**
   * Cache aside pattern for student
   * @param {number|string} id - Student ID
   * @returns {Promise} Axios response
   */
  cacheAsideStudent: (id) => {
    return axiosInstance.get(`/api/cache-invalidation/students/${id}/cache-aside`);
  },

  /**
   * Get cache invalidation explanation
   * @returns {Promise} Axios response with explanation
   */
  getCacheInvalidationExplanation: () => {
    return axiosInstance.get('/api/cache-invalidation/students/explanation');
  },
};

export default cacheApi;