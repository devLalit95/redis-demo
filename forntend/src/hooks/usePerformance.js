/**
 * usePerformance Hook
 * Custom hook for performance monitoring and metrics
 */

import { useState, useEffect, useCallback, useRef } from 'react';
import { performanceApi } from '../services/api/performanceApi';
import { formatResponseTime, getResponseTimeColor, getResponseTimeStatus } from '../utils/formatResponseTime';

/**
 * Custom hook for performance monitoring
 * @param {Object} options - Performance options
 * @returns {Object} - Performance state and functions
 */
export function usePerformance(options = {}) {
  const {
    autoRefresh = false,
    refreshInterval = 10000,
    metricType = 'all',
  } = options;

  const [metrics, setMetrics] = useState(null);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchMetrics = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await performanceApi.getMetrics({ type: metricType });
      const data = response.data || response;
      setMetrics(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [metricType]);

  const fetchHistory = useCallback(async (params = {}) => {
    setLoading(true);
    setError(null);

    try {
      const response = await performanceApi.getHistory(params);
      const data = response.data || response;
      setHistory(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  const runBenchmark = useCallback(async (benchmarkData) => {
    setLoading(true);
    setError(null);

    try {
      const response = await performanceApi.benchmark(benchmarkData);
      const data = response.data || response;
      setMetrics(data);
      return data;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const runComparison = useCallback(async (compareData) => {
    setLoading(true);
    setError(null);

    try {
      const response = await performanceApi.compare(compareData);
      const data = response.data || response;
      setMetrics(data);
      return data;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  // Auto-refresh metrics
  useEffect(() => {
    if (autoRefresh) {
      const interval = setInterval(fetchMetrics, refreshInterval);
      return () => clearInterval(interval);
    }
  }, [autoRefresh, refreshInterval, fetchMetrics]);

  // Initial fetch
  useEffect(() => {
    fetchMetrics();
  }, [fetchMetrics]);

  const performanceInfo = {
    averageResponseTime: metrics?.averageResponseTime || 0,
    formattedResponseTime: formatResponseTime(metrics?.averageResponseTime || 0),
    responseTimeColor: getResponseTimeColor(metrics?.averageResponseTime || 0),
    responseTimeStatus: getResponseTimeStatus(metrics?.averageResponseTime || 0),
    fastestOperation: metrics?.fastest || null,
    slowestOperation: metrics?.slowest || null,
    totalOperations: metrics?.total || 0,
    errorRate: metrics?.errorRate || 0,
  };

  return {
    metrics,
    history,
    loading,
    error,
    performanceInfo,
    fetchMetrics,
    fetchHistory,
    runBenchmark,
    runComparison,
  };
}

/**
 * usePerformanceMonitor Hook
 * Custom hook for real-time performance monitoring
 */
export function usePerformanceMonitor(options = {}) {
  const {
    sampleRate = 1.0,
    slowThreshold = 1000,
    verySlowThreshold = 3000,
  } = options;

  const [operations, setOperations] = useState([]);
  const [slowOperations, setSlowOperations] = useState([]);
  const [verySlowOperations, setVerySlowOperations] = useState([]);
  const [isMonitoring, setIsMonitoring] = useState(false);
  const intervalRef = useRef(null);

  const startMonitoring = useCallback(() => {
    if (isMonitoring) return;
    setIsMonitoring(true);
  }, [isMonitoring]);

  const stopMonitoring = useCallback(() => {
    setIsMonitoring(false);
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
    }
  }, []);

  const recordOperation = useCallback((operation) => {
    if (Math.random() > sampleRate) return;

    const timestamp = Date.now();
    const operationRecord = {
      ...operation,
      timestamp,
      formattedTime: formatResponseTime(operation.duration),
      color: getResponseTimeColor(operation.duration),
      status: getResponseTimeStatus(operation.duration),
    };

    setOperations((prev) => [...prev, operationRecord]);

    if (operation.duration >= verySlowThreshold) {
      setVerySlowOperations((prev) => [...prev, operationRecord]);
    } else if (operation.duration >= slowThreshold) {
      setSlowOperations((prev) => [...prev, operationRecord]);
    }
  }, [sampleRate, slowThreshold, verySlowThreshold]);

  const clearOperations = useCallback(() => {
    setOperations([]);
    setSlowOperations([]);
    setVerySlowOperations([]);
  }, []);

  const getAverageResponseTime = useCallback(() => {
    if (operations.length === 0) return 0;
    const total = operations.reduce((sum, op) => sum + op.duration, 0);
    return total / operations.length;
  }, [operations]);

  const getMedianResponseTime = useCallback(() => {
    if (operations.length === 0) return 0;
    const sorted = [...operations].sort((a, b) => a.duration - b.duration);
    const mid = Math.floor(sorted.length / 2);
    return sorted.length % 2 === 0
      ? (sorted[mid - 1].duration + sorted[mid].duration) / 2
      : sorted[mid].duration;
  }, [operations]);

  const getPercentile = useCallback((percentile) => {
    if (operations.length === 0) return 0;
    const sorted = [...operations].sort((a, b) => a.duration - b.duration);
    const index = Math.ceil((percentile / 100) * sorted.length) - 1;
    return sorted[index]?.duration || 0;
  }, [operations]);

  const monitoringInfo = {
    isMonitoring,
    totalOperations: operations.length,
    slowOperationsCount: slowOperations.length,
    verySlowOperationsCount: verySlowOperations.length,
    averageResponseTime: getAverageResponseTime(),
    medianResponseTime: getMedianResponseTime(),
    p95ResponseTime: getPercentile(95),
    p99ResponseTime: getPercentile(99),
  };

  return {
    operations,
    slowOperations,
    verySlowOperations,
    isMonitoring,
    monitoringInfo,
    startMonitoring,
    stopMonitoring,
    recordOperation,
    clearOperations,
    getAverageResponseTime,
    getMedianResponseTime,
    getPercentile,
  };
}

export default usePerformance;