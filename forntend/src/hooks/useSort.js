/**
 * useSort Hook
 * Custom hook for sorting functionality
 */

import { useState, useCallback, useMemo } from 'react';
import { SORT_DIRECTION } from '../constants';

/**
 * Custom hook for sorting
 * @param {Object} options - Sort options
 * @returns {Object} - Sort state and functions
 */
export function useSort(options = {}) {
  const {
    initialField = null,
    initialDirection = SORT_DIRECTION.ASC,
    onSort,
  } = options;

  const [field, setField] = useState(initialField);
  const [direction, setDirection] = useState(initialDirection);

  const handleSort = useCallback((newField) => {
    if (field === newField) {
      // Toggle direction if same field
      setDirection((prev) =>
        prev === SORT_DIRECTION.ASC ? SORT_DIRECTION.DESC : SORT_DIRECTION.ASC
      );
    } else {
      // New field, set to ascending
      setField(newField);
      setDirection(SORT_DIRECTION.ASC);
    }
  }, [field]);

  const setSort = useCallback((newField, newDirection) => {
    setField(newField);
    setDirection(newDirection);
  }, []);

  const toggleDirection = useCallback(() => {
    setDirection((prev) =>
      prev === SORT_DIRECTION.ASC ? SORT_DIRECTION.DESC : SORT_DIRECTION.ASC
    );
  }, []);

  const resetSort = useCallback(() => {
    setField(initialField);
    setDirection(initialDirection);
  }, [initialField, initialDirection]);

  const sortInfo = useMemo(() => {
    return {
      field,
      direction,
      isSorted: field !== null,
    };
  }, [field, direction]);

  // Trigger sort callback when sort changes
  if (onSort && field !== null) {
    onSort(sortInfo);
  }

  return {
    field,
    direction,
    sortInfo,
    handleSort,
    setSort,
    toggleDirection,
    resetSort,
  };
}

/**
 * useSortData Hook
 * Custom hook for sorting data arrays
 */
export function useSortData(data, options = {}) {
  const {
    initialField = null,
    initialDirection = SORT_DIRECTION.ASC,
  } = options;

  const { field, direction, handleSort, resetSort } = useSort({
    initialField,
    initialDirection,
  });

  const sortedData = useMemo(() => {
    if (!field || !data || !Array.isArray(data)) {
      return data;
    }

    return [...data].sort((a, b) => {
      const aValue = a[field];
      const bValue = b[field];

      if (aValue === bValue) return 0;

      let comparison = 0;
      
      if (typeof aValue === 'number' && typeof bValue === 'number') {
        comparison = aValue - bValue;
      } else {
        comparison = String(aValue).localeCompare(String(bValue));
      }

      return direction === SORT_DIRECTION.ASC ? comparison : -comparison;
    });
  }, [data, field, direction]);

  return {
    sortedData,
    field,
    direction,
    handleSort,
    resetSort,
  };
}

export default useSort;