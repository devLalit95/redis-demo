/**
 * useDebounce Hook
 * Custom hook for debouncing values
 */

import { useState, useEffect } from 'react';
import { DEBOUNCE_DELAYS } from '../constants';

/**
 * Custom hook for debouncing a value
 * @param {*} value - The value to debounce
 * @param {number} delay - Delay in milliseconds (default: 300ms)
 * @returns {*} - Debounced value
 */
export function useDebounce(value, delay = DEBOUNCE_DELAYS.SEARCH) {
  const [debouncedValue, setDebouncedValue] = useState(value);

  useEffect(() => {
    // Set debounced value to value after delay
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    // Cleanup timeout on value change or unmount
    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
}

/**
 * useDebouncedCallback Hook
 * Custom hook for debouncing a callback function
 * @param {Function} callback - The callback to debounce
 * @param {number} delay - Delay in milliseconds (default: 300ms)
 * @returns {Function} - Debounced callback
 */
export function useDebouncedCallback(callback, delay = DEBOUNCE_DELAYS.SEARCH) {
  const [timeoutId, setTimeoutId] = useState(null);

  const debouncedCallback = useCallback(
    (...args) => {
      if (timeoutId) {
        clearTimeout(timeoutId);
      }

      const newTimeoutId = setTimeout(() => {
        callback(...args);
      }, delay);

      setTimeoutId(newTimeoutId);
    },
    [callback, delay, timeoutId]
  );

  // Cleanup timeout on unmount
  useEffect(() => {
    return () => {
      if (timeoutId) {
        clearTimeout(timeoutId);
      }
    };
  }, [timeoutId]);

  return debouncedCallback;
}

/**
 * useThrottle Hook
 * Custom hook for throttling a callback function
 * @param {Function} callback - The callback to throttle
 * @param {number} delay - Delay in milliseconds (default: 300ms)
 * @returns {Function} - Throttled callback
 */
export function useThrottle(callback, delay = DEBOUNCE_DELAYS.SEARCH) {
  const [lastRun, setLastRun] = useState(0);

  const throttledCallback = useCallback(
    (...args) => {
      const now = Date.now();
      
      if (now - lastRun >= delay) {
        callback(...args);
        setLastRun(now);
      }
    },
    [callback, delay, lastRun]
  );

  return throttledCallback;
}

export default useDebounce;