/**
 * Student API
 * All student-related API operations
 */

import axiosInstance from './axiosInstance';
import { API_ENDPOINTS } from '../../constants';

/**
 * Student API object containing all student operations
 */
export const studentApi = {
  /**
   * Get all students
   * @param {Object} params - Query parameters (page, size, sort, etc.)
   * @returns {Promise} Axios response with student data
   */
  getAll: (params = {}) => {
    return axiosInstance.get(API_ENDPOINTS.STUDENT.GET_ALL, { params });
  },

  /**
   * Get student by ID
   * @param {number|string} id - Student ID
   * @returns {Promise} Axios response with student data
   */
  getById: (id) => {
    const url = API_ENDPOINTS.STUDENT.GET_BY_ID.replace(':id', id);
    return axiosInstance.get(url);
  },

  /**
   * Create new student
   * @param {Object} studentData - Student data to create
   * @returns {Promise} Axios response with created student
   */
  create: (studentData) => {
    return axiosInstance.post(API_ENDPOINTS.STUDENT.CREATE, studentData);
  },

  /**
   * Update existing student
   * @param {number|string} id - Student ID
   * @param {Object} studentData - Updated student data
   * @returns {Promise} Axios response with updated student
   */
  update: (id, studentData) => {
    const url = API_ENDPOINTS.STUDENT.UPDATE.replace(':id', id);
    return axiosInstance.put(url, studentData);
  },

  /**
   * Delete student by ID
   * @param {number|string} id - Student ID
   * @returns {Promise} Axios response
   */
  delete: (id) => {
    const url = API_ENDPOINTS.STUDENT.DELETE.replace(':id', id);
    return axiosInstance.delete(url);
  },

  /**
   * Bulk delete students
   * @param {Array<number|string>} ids - Array of student IDs to delete
   * @returns {Promise} Axios response
   */
  bulkDelete: (ids) => {
    return axiosInstance.post(API_ENDPOINTS.STUDENT.BULK_DELETE, { ids });
  },

  /**
   * Search students
   * @param {string} query - Search query
   * @param {Object} params - Additional query parameters
   * @returns {Promise} Axios response with search results
   */
  search: (query, params = {}) => {
    return axiosInstance.get(API_ENDPOINTS.STUDENT.GET_ALL, {
      params: { ...params, search: query }
    });
  },

  /**
   * Get students by filter
   * @param {Object} filters - Filter criteria
   * @param {Object} params - Additional query parameters
   * @returns {Promise} Axios response with filtered students
   */
  filter: (filters, params = {}) => {
    return axiosInstance.get(API_ENDPOINTS.STUDENT.GET_ALL, {
      params: { ...params, ...filters }
    });
  },
};

export default studentApi;