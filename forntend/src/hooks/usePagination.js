/**
 * usePagination Hook
 * Custom hook for pagination logic
 */

import { useState, useCallback, useMemo } from 'react';
import { PAGINATION } from '../constants';

/**
 * Custom hook for pagination
 * @param {Object} options - Pagination options
 * @returns {Object} - Pagination state and functions
 */
export function usePagination(options = {}) {
  const {
    initialPage = PAGINATION.DEFAULT_PAGE,
    initialPageSize = PAGINATION.DEFAULT_PAGE_SIZE,
    pageSizeOptions = PAGINATION.PAGE_SIZE_OPTIONS,
    totalItems = 0,
  } = options;

  const [page, setPage] = useState(initialPage);
  const [pageSize, setPageSize] = useState(initialPageSize);

  const totalPages = useMemo(() => {
    return Math.ceil(totalItems / pageSize);
  }, [totalItems, pageSize]);

  const hasNextPage = useMemo(() => {
    return page < totalPages;
  }, [page, totalPages]);

  const hasPreviousPage = useMemo(() => {
    return page > 1;
  }, [page]);

  const goToPage = useCallback((newPage) => {
    setPage(Math.max(1, Math.min(newPage, totalPages)));
  }, [totalPages]);

  const nextPage = useCallback(() => {
    if (hasNextPage) {
      setPage((prev) => prev + 1);
    }
  }, [hasNextPage]);

  const previousPage = useCallback(() => {
    if (hasPreviousPage) {
      setPage((prev) => prev - 1);
    }
  }, [hasPreviousPage]);

  const firstPage = useCallback(() => {
    setPage(1);
  }, []);

  const lastPage = useCallback(() => {
    setPage(totalPages);
  }, [totalPages]);

  const changePageSize = useCallback((newPageSize) => {
    setPageSize(newPageSize);
    setPage(1); // Reset to first page when changing page size
  }, []);

  const reset = useCallback(() => {
    setPage(initialPage);
    setPageSize(initialPageSize);
  }, [initialPage, initialPageSize]);

  const paginationInfo = useMemo(() => {
    const startIndex = (page - 1) * pageSize + 1;
    const endIndex = Math.min(page * pageSize, totalItems);
    
    return {
      page,
      pageSize,
      totalPages,
      totalItems,
      startIndex,
      endIndex,
      hasNextPage,
      hasPreviousPage,
    };
  }, [page, pageSize, totalPages, totalItems, hasNextPage, hasPreviousPage]);

  return {
    // State
    page,
    pageSize,
    pageSizeOptions,
    totalPages,
    
    // Computed
    hasNextPage,
    hasPreviousPage,
    paginationInfo,
    
    // Actions
    goToPage,
    nextPage,
    previousPage,
    firstPage,
    lastPage,
    changePageSize,
    reset,
  };
}

export default usePagination;