/**
 * Redis API Service
 * Handles all Redis operations API calls
 */

import axiosInstance from './axiosInstance';

/**
 * String Operations
 */
export const redisStringApi = {
  /**
   * Store a string value in Redis
   */
  setString: async (key, value) => {
    const response = await axiosInstance.post('/api/redis/string', { key, value });
    return response.data;
  },
  
  /**
   * Get a string value from Redis
   */
  getString: async (key) => {
    const response = await axiosInstance.get(`/api/redis/string/${key}`);
    return response.data;
  },
  
  /**
   * Store a JSON object in Redis
   */
  setJson: async (key, value) => {
    const response = await axiosInstance.post('/api/redis/json', { key, value });
    return response.data;
  },
  
  /**
   * Get a JSON object from Redis
   */
  getJson: async (key) => {
    const response = await axiosInstance.get(`/api/redis/json/${key}`);
    return response.data;
  },
};

/**
 * Hash Operations
 */
export const redisHashApi = {
  /**
   * Store a field in a Redis hash
   */
  setHashField: async (key, field, value) => {
    const response = await axiosInstance.post('/api/redis/hash', { key, field, value });
    return response.data;
  },
  
  /**
   * Get a field from a Redis hash
   */
  getHashField: async (key, field) => {
    const response = await axiosInstance.get(`/api/redis/hash/${key}/${field}`);
    return response.data;
  },
  
  /**
   * Get all fields from a Redis hash
   */
  getAllHashFields: async (key) => {
    const response = await axiosInstance.get(`/api/redis/hash/${key}`);
    return response.data;
  },
  
  /**
   * Delete a field from a Redis hash
   */
  deleteHashField: async (key, field) => {
    const response = await axiosInstance.delete(`/api/redis/hash/${key}/${field}`);
    return response.data;
  },
};

/**
 * List Operations
 */
export const redisListApi = {
  /**
   * Add element to left of Redis list
   */
  leftPush: async (key, value) => {
    const response = await axiosInstance.post('/api/redis/list/left', { key, value });
    return response.data;
  },
  
  /**
   * Add element to right of Redis list
   */
  rightPush: async (key, value) => {
    const response = await axiosInstance.post('/api/redis/list/right', { key, value });
    return response.data;
  },
  
  /**
   * Get all elements from Redis list
   */
  getList: async (key) => {
    const response = await axiosInstance.get(`/api/redis/list/${key}`);
    return response.data;
  },
  
  /**
   * Pop element from left of Redis list
   */
  leftPop: async (key) => {
    const response = await axiosInstance.delete(`/api/redis/list/left/${key}`);
    return response.data;
  },
  
  /**
   * Pop element from right of Redis list
   */
  rightPop: async (key) => {
    const response = await axiosInstance.delete(`/api/redis/list/right/${key}`);
    return response.data;
  },
};

/**
 * Set Operations
 */
export const redisSetApi = {
  /**
   * Add element to Redis set
   */
  addToSet: async (key, value) => {
    const response = await axiosInstance.post('/api/redis/set', { key, value });
    return response.data;
  },
  
  /**
   * Get all members of Redis set
   */
  getSetMembers: async (key) => {
    const response = await axiosInstance.get(`/api/redis/set/${key}`);
    return response.data;
  },
  
  /**
   * Check if element is in Redis set
   */
  isSetMember: async (key, value) => {
    const response = await axiosInstance.get(`/api/redis/set/${key}/${value}`);
    return response.data;
  },
  
  /**
   * Remove element from Redis set
   */
  removeFromSet: async (key, value) => {
    const response = await axiosInstance.delete(`/api/redis/set/${key}/${value}`);
    return response.data;
  },
};

/**
 * Sorted Set Operations
 */
export const redisSortedSetApi = {
  /**
   * Add element to Redis sorted set with score
   */
  addToSortedSet: async (key, value, score) => {
    const response = await axiosInstance.post('/api/redis/sortedset', { key, value, score });
    return response.data;
  },
  
  /**
   * Get all elements from Redis sorted set (ascending)
   */
  getSortedSet: async (key) => {
    const response = await axiosInstance.get(`/api/redis/sortedset/${key}`);
    return response.data;
  },
  
  /**
   * Get all elements from Redis sorted set (descending)
   */
  getSortedSetReverse: async (key) => {
    const response = await axiosInstance.get(`/api/redis/sortedset/${key}/reverse`);
    return response.data;
  },
  
  /**
   * Get rank of element in Redis sorted set
   */
  getSortedSetRank: async (key, value) => {
    const response = await axiosInstance.get(`/api/redis/sortedset/${key}/${value}/rank`);
    return response.data;
  },
};

/**
 * Counter Operations
 */
export const redisCounterApi = {
  /**
   * Increment a counter in Redis
   */
  increment: async (key) => {
    const response = await axiosInstance.post('/api/redis/counter/increment', { key });
    return response.data;
  },
  
  /**
   * Increment counter by specific amount
   */
  incrementBy: async (key, delta) => {
    const response = await axiosInstance.post('/api/redis/counter/incrementby', { key, delta });
    return response.data;
  },
  
  /**
   * Decrement a counter in Redis
   */
  decrement: async (key) => {
    const response = await axiosInstance.post('/api/redis/counter/decrement', { key });
    return response.data;
  },
};

// Export all APIs as a single object
export default {
  string: redisStringApi,
  hash: redisHashApi,
  list: redisListApi,
  set: redisSetApi,
  sortedSet: redisSortedSetApi,
  counter: redisCounterApi,
};