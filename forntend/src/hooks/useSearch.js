/**
 * useSearch Hook
 * Custom hook for search functionality
 */

import { useState, useCallback, useMemo } from 'react';
import { useDebounce } from './useDebounce';

/**
 * Custom hook for search functionality
 * @param {Object} options - Search options
 * @returns {Object} - Search state and functions
 */
export function useSearch(options = {}) {
  const {
    initialQuery = '',
    debounceDelay = 300,
    onSearch,
  } = options;

  const [query, setQuery] = useState(initialQuery);
  const [isSearching, setIsSearching] = useState(false);
  const debouncedQuery = useDebounce(query, debounceDelay);

  const handleSearch = useCallback((newQuery) => {
    setQuery(newQuery);
    setIsSearching(true);
  }, []);

  const clearSearch = useCallback(() => {
    setQuery('');
    setIsSearching(false);
  }, []);

  const resetSearch = useCallback(() => {
    setQuery(initialQuery);
    setIsSearching(false);
  }, [initialQuery]);

  // Trigger search callback when debounced query changes
  if (onSearch && debouncedQuery !== query) {
    setIsSearching(false);
    onSearch(debouncedQuery);
  }

  return {
    query,
    debouncedQuery,
    isSearching,
    handleSearch,
    clearSearch,
    resetSearch,
  };
}

/**
 * useAdvancedSearch Hook
 * Custom hook for advanced search with filters
 */
export function useAdvancedSearch(options = {}) {
  const {
    initialQuery = '',
    initialFilters = {},
    debounceDelay = 300,
    onSearch,
  } = options;

  const [query, setQuery] = useState(initialQuery);
  const [filters, setFilters] = useState(initialFilters);
  const [isSearching, setIsSearching] = useState(false);
  const debouncedQuery = useDebounce(query, debounceDelay);

  const handleSearch = useCallback((newQuery) => {
    setQuery(newQuery);
    setIsSearching(true);
  }, []);

  const handleFilterChange = useCallback((filterName, filterValue) => {
    setFilters((prev) => ({
      ...prev,
      [filterName]: filterValue,
    }));
    setIsSearching(true);
  }, []);

  const handleMultipleFilters = useCallback((newFilters) => {
    setFilters(newFilters);
    setIsSearching(true);
  }, []);

  const clearSearch = useCallback(() => {
    setQuery('');
    setIsSearching(false);
  }, []);

  const clearFilters = useCallback(() => {
    setFilters(initialFilters);
    setIsSearching(true);
  }, [initialFilters]);

  const clearAll = useCallback(() => {
    setQuery('');
    setFilters(initialFilters);
    setIsSearching(false);
  }, [initialFilters]);

  const resetSearch = useCallback(() => {
    setQuery(initialQuery);
    setFilters(initialFilters);
    setIsSearching(false);
  }, [initialQuery, initialFilters]);

  const searchParams = useMemo(() => {
    return {
      query: debouncedQuery,
      ...filters,
    };
  }, [debouncedQuery, filters]);

  // Trigger search callback when debounced query or filters change
  if (onSearch && (debouncedQuery !== query || Object.keys(filters).length > 0)) {
    setIsSearching(false);
    onSearch(searchParams);
  }

  return {
    query,
    debouncedQuery,
    filters,
    searchParams,
    isSearching,
    handleSearch,
    handleFilterChange,
    handleMultipleFilters,
    clearSearch,
    clearFilters,
    clearAll,
    resetSearch,
  };
}

export default useSearch;