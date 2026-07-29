/**
 * Redis API
 * All Redis-related API operations
 */

import axiosInstance from './axiosInstance';
import { API_ENDPOINTS } from '../../constants';

/**
 * Redis API object containing all Redis operations
 */
export const redisApi = {
  // Key operations
  key: {
    /**
     * Get all Redis keys matching pattern
     * @param {string} pattern - Key pattern (default: '*')
     * @returns {Promise} Axios response with keys
     */
    getKeysByPattern: (pattern = '*') => {
      return axiosInstance.get(API_ENDPOINTS.REDIS.KEYS, { params: { pattern } });
    },

    /**
     * Get Redis key value
     * @param {string} key - Redis key
     * @returns {Promise} Axios response with key value
     */
    getValue: (key) => {
      const url = API_ENDPOINTS.REDIS.GET.replace(':key', key);
      return axiosInstance.get(url);
    },

    /**
     * Get key type
     * @param {string} key - Redis key
     * @returns {Promise} Axios response with key type
     */
    getKeyType: (key) => {
      return axiosInstance.get(`/api/redis-explorer/keys/${key}/type`);
    },

    /**
     * Delete Redis key
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    deleteKey: (key) => {
      const url = API_ENDPOINTS.REDIS.DELETE.replace(':key', key);
      return axiosInstance.delete(url);
    },
  },

  // String operations namespace
  string: {
    /**
     * Store a string value in Redis
     * @param {string} key - Redis key
     * @param {string} value - String value
     * @returns {Promise} Axios response
     */
    setString: (key, value) => {
      return axiosInstance.post('/api/cache-playground/string', { key, value });
    },

    /**
     * Get a string value from Redis
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    getString: (key) => {
      return axiosInstance.get(`/api/cache-playground/string/${key}`);
    },
  },

  // Hash operations namespace
  hash: {
    /**
     * Store a field in a Redis hash
     * @param {string} key - Redis key
     * @param {string} field - Hash field
     * @param {string} value - Field value
     * @returns {Promise} Axios response
     */
    setHashField: (key, field, value) => {
      return axiosInstance.post('/api/cache-playground/hash', { key, field, value });
    },

    /**
     * Get a field from a Redis hash
     * @param {string} key - Redis key
     * @param {string} field - Hash field
     * @returns {Promise} Axios response
     */
    getHashField: (key, field) => {
      return axiosInstance.get(`/api/cache-playground/hash/${key}/${field}`);
    },

    /**
     * Get all fields from a Redis hash
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    getAllHashFields: (key) => {
      return axiosInstance.get(`/api/cache-playground/hash/${key}`);
    },

    /**
     * Delete a field from a Redis hash
     * @param {string} key - Redis key
     * @param {string} field - Hash field
     * @returns {Promise} Axios response
     */
    deleteHashField: (key, field) => {
      return axiosInstance.delete(`/api/cache-playground/hash/${key}/${field}`);
    },
  },

  // List operations namespace
  list: {
    /**
     * Add element to left of Redis list
     * @param {string} key - Redis key
     * @param {string} value - List value
     * @returns {Promise} Axios response
     */
    leftPush: (key, value) => {
      return axiosInstance.post('/api/cache-playground/list/left', { key, value });
    },

    /**
     * Add element to right of Redis list
     * @param {string} key - Redis key
     * @param {string} value - List value
     * @returns {Promise} Axios response
     */
    rightPush: (key, value) => {
      return axiosInstance.post('/api/cache-playground/list/right', { key, value });
    },

    /**
     * Get all elements from Redis list
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    getList: (key) => {
      return axiosInstance.get(`/api/cache-playground/list/${key}`);
    },

    /**
     * Pop element from left of Redis list
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    leftPop: (key) => {
      return axiosInstance.delete(`/api/cache-playground/list/left/${key}`);
    },

    /**
     * Pop element from right of Redis list
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    rightPop: (key) => {
      return axiosInstance.delete(`/api/cache-playground/list/right/${key}`);
    },
  },

  // Set operations namespace
  set: {
    /**
     * Add element to Redis set
     * @param {string} key - Redis key
     * @param {string} value - Set value
     * @returns {Promise} Axios response
     */
    addToSet: (key, value) => {
      return axiosInstance.post('/api/cache-playground/set', { key, value });
    },

    /**
     * Get all members of Redis set
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    getSetMembers: (key) => {
      return axiosInstance.get(`/api/cache-playground/set/${key}`);
    },

    /**
     * Check if element is in Redis set
     * @param {string} key - Redis key
     * @param {string} value - Set value
     * @returns {Promise} Axios response
     */
    isSetMember: (key, value) => {
      return axiosInstance.get(`/api/cache-playground/set/${key}/${value}`);
    },

    /**
     * Remove element from Redis set
     * @param {string} key - Redis key
     * @param {string} value - Set value
     * @returns {Promise} Axios response
     */
    removeFromSet: (key, value) => {
      return axiosInstance.delete(`/api/cache-playground/set/${key}/${value}`);
    },
  },

  // Sorted set operations namespace
  sortedSet: {
    /**
     * Add element to Redis sorted set with score
     * @param {string} key - Redis key
     * @param {string} value - Set value
     * @param {number} score - Score
     * @returns {Promise} Axios response
     */
    addToSortedSet: (key, value, score) => {
      return axiosInstance.post('/api/cache-playground/sortedset', { key, value, score });
    },

    /**
     * Get all elements from Redis sorted set (ascending)
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    getSortedSet: (key) => {
      return axiosInstance.get(`/api/cache-playground/sortedset/${key}`);
    },

    /**
     * Get all elements from Redis sorted set (descending)
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    getSortedSetReverse: (key) => {
      return axiosInstance.get(`/api/cache-playground/sortedset/${key}/reverse`);
    },

    /**
     * Get rank of element in Redis sorted set
     * @param {string} key - Redis key
     * @param {string} value - Set value
     * @returns {Promise} Axios response
     */
    getSortedSetRank: (key, value) => {
      return axiosInstance.get(`/api/cache-playground/sortedset/${key}/${value}/rank`);
    },
  },

  // Counter operations namespace
  counter: {
    /**
     * Increment a counter in Redis
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    increment: (key) => {
      return axiosInstance.post('/api/cache-playground/counter/increment', { key });
    },

    /**
     * Increment counter by specific amount
     * @param {string} key - Redis key
     * @param {number} delta - Increment amount
     * @returns {Promise} Axios response
     */
    incrementBy: (key, delta) => {
      return axiosInstance.post('/api/cache-playground/counter/incrementby', { key, delta });
    },

    /**
     * Decrement a counter in Redis
     * @param {string} key - Redis key
     * @returns {Promise} Axios response
     */
    decrement: (key) => {
      return axiosInstance.post('/api/cache-playground/counter/decrement', { key });
    },
  },

  /**
   * Get Redis server information
   * @returns {Promise} Axios response with Redis info
   */
  getInfo: () => {
    return axiosInstance.get(API_ENDPOINTS.REDIS.INFO);
  },

  /**
   * Set Redis key value (direct)
   * @param {string} key - Redis key
   * @param {string} value - String value
   * @returns {Promise} Axios response
   */
  set: (key, value) => {
    return axiosInstance.post(`/api/cache-playground/string`, { key, value });
  },

  /**
   * Get Redis key value (direct)
   * @param {string} key - Redis key
   * @returns {Promise} Axios response
   */
  get: (key) => {
    return axiosInstance.get(`/api/cache-playground/string/${key}`);
  },

  /**
   * Delete Redis key
   * @param {string} key - Redis key
   * @returns {Promise} Axios response
   */
  delete: (key) => {
    const url = API_ENDPOINTS.REDIS.DELETE.replace(':key', key);
    return axiosInstance.delete(url);
  },

  /**
   * Flush current database
   * @returns {Promise} Axios response
   */
  flushDb: () => {
    return axiosInstance.post(API_ENDPOINTS.REDIS.FLUSHDB);
  },

  /**
   * Flush all databases
   * @returns {Promise} Axios response
   */
  flushAll: () => {
    return axiosInstance.post(API_ENDPOINTS.REDIS.FLUSHALL);
  },

  // Backward compatibility - keep old function names
  setString: (key, value) => {
    return axiosInstance.post('/api/cache-playground/string', { key, value });
  },

  getString: (key) => {
    return axiosInstance.get(`/api/cache-playground/string/${key}`);
  },

  setHashField: (key, field, value) => {
    return axiosInstance.post('/api/cache-playground/hash', { key, field, value });
  },

  getHashField: (key, field) => {
    return axiosInstance.get(`/api/cache-playground/hash/${key}/${field}`);
  },

  getAllHashFields: (key) => {
    return axiosInstance.get(`/api/cache-playground/hash/${key}`);
  },

  deleteHashField: (key, field) => {
    return axiosInstance.delete(`/api/cache-playground/hash/${key}/${field}`);
  },

  leftPush: (key, value) => {
    return axiosInstance.post('/api/cache-playground/list/left', { key, value });
  },

  rightPush: (key, value) => {
    return axiosInstance.post('/api/cache-playground/list/right', { key, value });
  },

  getList: (key) => {
    return axiosInstance.get(`/api/cache-playground/list/${key}`);
  },

  leftPop: (key) => {
    return axiosInstance.delete(`/api/cache-playground/list/left/${key}`);
  },

  rightPop: (key) => {
    return axiosInstance.delete(`/api/cache-playground/list/right/${key}`);
  },

  addToSet: (key, value) => {
    return axiosInstance.post('/api/cache-playground/set', { key, value });
  },

  getSetMembers: (key) => {
    return axiosInstance.get(`/api/cache-playground/set/${key}`);
  },

  isSetMember: (key, value) => {
    return axiosInstance.get(`/api/cache-playground/set/${key}/${value}`);
  },

  removeFromSet: (key, value) => {
    return axiosInstance.delete(`/api/cache-playground/set/${key}/${value}`);
  },

  addToSortedSet: (key, value, score) => {
    return axiosInstance.post('/api/cache-playground/sortedset', { key, value, score });
  },

  getSortedSet: (key) => {
    return axiosInstance.get(`/api/cache-playground/sortedset/${key}`);
  },

  getSortedSetReverse: (key) => {
    return axiosInstance.get(`/api/cache-playground/sortedset/${key}/reverse`);
  },

  getSortedSetRank: (key, value) => {
    return axiosInstance.get(`/api/cache-playground/sortedset/${key}/${value}/rank`);
  },

  increment: (key) => {
    return axiosInstance.post('/api/cache-playground/counter/increment', { key });
  },

  incrementBy: (key, delta) => {
    return axiosInstance.post('/api/cache-playground/counter/incrementby', { key, delta });
  },

  decrement: (key) => {
    return axiosInstance.post('/api/cache-playground/counter/decrement', { key });
  },
};

export default redisApi;