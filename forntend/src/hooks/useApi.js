/**
 * useApi Hook
 * Custom hook for making API calls with loading, error, and data states
 */

import { useState, useCallback } from 'react';
import toast from 'react-hot-toast';

/**
 * Custom hook for API calls with automatic error handling and loading states
 * @param {Function} apiFunction - The API function to call
 * @param {Object} options - Hook options
 * @returns {Object} - API state and execute function
 */
export function useApi(apiFunction, options = {}) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const {
    onSuccess,
    onError,
    successMessage,
    errorMessage,
    showToast = true,
  } = options;

  const execute = useCallback(
    async (...args) => {
      setLoading(true);
      setError(null);
      
      try {
        const response = await apiFunction(...args);
        const result = response.data || response;
        
        setData(result);
        
        if (onSuccess) {
          onSuccess(result);
        }
        
        if (showToast && successMessage) {
          toast.success(successMessage);
        }
        
        return result;
      } catch (err) {
        const error = err.response?.data?.message || err.message || 'An error occurred';
        setError(error);
        
        if (onError) {
          onError(error);
        }
        
        if (showToast) {
          toast.error(errorMessage || error);
        }
        
        throw err;
      } finally {
        setLoading(false);
      }
    },
    [apiFunction, onSuccess, onError, successMessage, errorMessage, showToast]
  );

  const reset = useCallback(() => {
    setData(null);
    setError(null);
    setLoading(false);
  }, []);

  return {
    data,
    loading,
    error,
    execute,
    reset,
  };
}

/**
 * useApiMutation Hook
 * Custom hook for API mutations (POST, PUT, DELETE)
 */
export function useApiMutation(apiFunction, options = {}) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const {
    onSuccess,
    onError,
    successMessage = 'Operation completed successfully',
    errorMessage = 'Operation failed',
    showToast = true,
    invalidateQueries,
  } = options;

  const mutate = useCallback(
    async (...args) => {
      setLoading(true);
      setError(null);
      
      try {
        const response = await apiFunction(...args);
        const result = response.data || response;
        
        setData(result);
        
        if (onSuccess) {
          onSuccess(result);
        }
        
        if (showToast) {
          toast.success(successMessage);
        }
        
        if (invalidateQueries) {
          // In a real app, this would use React Query's invalidateQueries
          console.log('Invalidating queries:', invalidateQueries);
        }
        
        return result;
      } catch (err) {
        const error = err.response?.data?.message || err.message || 'An error occurred';
        setError(error);
        
        if (onError) {
          onError(error);
        }
        
        if (showToast) {
          toast.error(errorMessage);
        }
        
        throw err;
      } finally {
        setLoading(false);
      }
    },
    [apiFunction, onSuccess, onError, successMessage, errorMessage, showToast, invalidateQueries]
  );

  const reset = useCallback(() => {
    setData(null);
    setError(null);
    setLoading(false);
  }, []);

  return {
    data,
    loading,
    error,
    mutate,
    reset,
  };
}

export default useApi;