/**
 * useCacheStatus Hook
 * Custom hook for monitoring cache status
 */

import { useState, useEffect, useCallback } from 'react';
import { cacheApi } from '../services/api/cacheApi';
import { CACHE_STATUS, CACHE_STATUS_COLORS, CACHE_STATUS_LABELS } from '../constants';

/**
 * Custom hook for cache status monitoring
 * @param {Object} options - Cache status options
 * @returns {Object} - Cache status state and functions
 */
export function useCacheStatus(options = {}) {
  const {
    key,
    autoRefresh = false,
    refreshInterval = 5000,
  } = options;

  const [status, setStatus] = useState(CACHE_STATUS.PENDING);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchCacheStatus = useCallback(async () => {
    if (!key) return;

    setLoading(true);
    setError(null);

    try {
      const response = await cacheApi.get(key);
      const data = response.data || response;

      if (data && data.exists) {
        setStatus(CACHE_STATUS.HIT);
      } else {
        setStatus(CACHE_STATUS.MISS);
      }

      setStats(data);
    } catch (err) {
      setStatus(CACHE_STATUS.ERROR);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [key]);

  const fetchCacheStats = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await cacheApi.getStats();
      const data = response.data || response;
      setStats(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  const refreshCache = useCallback(async () => {
    if (!key) return;

    setLoading(true);
    setError(null);

    try {
      await cacheApi.refresh(key);
      await fetchCacheStatus();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [key, fetchCacheStatus]);

  const clearCache = useCallback(async () => {
    if (!key) return;

    setLoading(true);
    setError(null);

    try {
      await cacheApi.delete(key);
      setStatus(CACHE_STATUS.MISS);
      setStats(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [key]);

  // Auto-refresh cache status
  useEffect(() => {
    if (autoRefresh && key) {
      const interval = setInterval(fetchCacheStatus, refreshInterval);
      return () => clearInterval(interval);
    }
  }, [autoRefresh, key, refreshInterval, fetchCacheStatus]);

  // Initial fetch
  useEffect(() => {
    if (key) {
      fetchCacheStatus();
    }
  }, [key, fetchCacheStatus]);

  const statusInfo = {
    status,
    label: CACHE_STATUS_LABELS[status] || status,
    color: CACHE_STATUS_COLORS[status] || '#9ca3af',
  };

  return {
    status,
    stats,
    loading,
    error,
    statusInfo,
    fetchCacheStatus,
    fetchCacheStats,
    refreshCache,
    clearCache,
  };
}

/**
 * useCacheStats Hook
 * Custom hook for cache statistics
 */
export function useCacheStats(options = {}) {
  const {
    autoRefresh = false,
    refreshInterval = 10000,
  } = options;

  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchStats = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await cacheApi.getStats();
      const data = response.data || response;
      setStats(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  // Auto-refresh stats
  useEffect(() => {
    if (autoRefresh) {
      const interval = setInterval(fetchStats, refreshInterval);
      return () => clearInterval(interval);
    }
  }, [autoRefresh, refreshInterval, fetchStats]);

  // Initial fetch
  useEffect(() => {
    fetchStats();
  }, [fetchStats]);

  const cacheHitRatio = stats ? (stats.hits / (stats.hits + stats.misses)) * 100 : 0;

  return {
    stats,
    loading,
    error,
    cacheHitRatio,
    fetchStats,
  };
}

export default useCacheStatus;