package com.example.redisdemo.metrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Dashboard Service
 * 
 * This service provides metrics and statistics for the dashboard.
 * This is Phase 8 of the project - Dashboard Backend APIs.
 * 
 * WHY this service exists:
 * - Provides real-time metrics for dashboard
 * - Shows cache effectiveness
 * - Displays system health
 * - Demonstrates monitoring capabilities
 * 
 * WHEN to use this service:
 * - Phase 8: Dashboard implementation
 * - Real-time monitoring
 * - System health checks
 * - Performance tracking
 * 
 * PRODUCTION USE CASES:
 * - Admin dashboards
 * - Monitoring systems
 * - Performance analytics
 * - Health check systems
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    // Removed StudentRepository dependency for Redis-only testing
    private final RedisTemplate<String, Object> redisTemplate;

    // Performance tracking
    private long totalResponseTime = 0;
    private long requestCount = 0;
    private long fastestRequest = Long.MAX_VALUE;
    private long slowestRequest = 0;
    private int databaseCallCount = 0;
    private int redisCallCount = 0;
    private int cacheHitCount = 0;
    private int cacheMissCount = 0;

    /**
     * Get dashboard metrics.
     * 
     * WHY this method exists:
     * - Provides comprehensive dashboard metrics
     * - Shows system health and performance
     * - Essential for monitoring
     * 
     * @return Dashboard metrics
     */
    public Map<String, Object> getDashboardMetrics() {
        log.info("Collecting dashboard metrics");
        
        Map<String, Object> metrics = new HashMap<>();
        
        // Student metrics - disabled for Redis-only testing
        metrics.put("totalStudents", 0);
        
        // Redis metrics
        long redisKeys = getRedisKeyCount();
        metrics.put("redisKeys", redisKeys);
        
        // Cache metrics
        metrics.put("cacheHits", cacheHitCount);
        metrics.put("cacheMisses", cacheMissCount);
        
        if (cacheHitCount + cacheMissCount > 0) {
            double hitRatio = (cacheHitCount * 100.0) / (cacheHitCount + cacheMissCount);
            metrics.put("cacheHitRatio", String.format("%.2f%%", hitRatio));
        } else {
            metrics.put("cacheHitRatio", "0.00%");
        }
        
        // Performance metrics
        metrics.put("totalRequests", requestCount);
        
        if (requestCount > 0) {
            long avgResponseTime = totalResponseTime / requestCount;
            metrics.put("averageResponseTime", avgResponseTime);
        } else {
            metrics.put("averageResponseTime", 0);
        }
        
        metrics.put("fastestRequest", fastestRequest == Long.MAX_VALUE ? 0 : fastestRequest);
        metrics.put("slowestRequest", slowestRequest);
        
        // Database and Redis call metrics
        metrics.put("databaseCalls", databaseCallCount);
        metrics.put("redisCalls", redisCallCount);
        
        log.info("Dashboard metrics collected: {}", metrics);
        
        return metrics;
    }

    /**
     * Get Redis key count.
     * 
     * @return Number of keys in Redis
     */
    private long getRedisKeyCount() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            return keys != null ? keys.size() : 0;
        } catch (Exception e) {
            log.error("Error getting Redis key count", e);
            return 0;
        }
    }

    /**
     * Record request metrics.
     * 
     * WHY this method exists:
     * - Tracks request performance
     * - Updates dashboard metrics
     * - Used by controllers
     * 
     * @param responseTime Request response time in ms
     * @param databaseCall Whether database was called
     * @param redisCall Whether Redis was called
     * @param cacheHit Whether cache hit occurred
     */
    public void recordRequest(long responseTime, boolean databaseCall, boolean redisCall, boolean cacheHit) {
        totalResponseTime += responseTime;
        requestCount++;
        
        if (responseTime < fastestRequest) {
            fastestRequest = responseTime;
        }
        if (responseTime > slowestRequest) {
            slowestRequest = responseTime;
        }
        
        if (databaseCall) {
            databaseCallCount++;
        }
        if (redisCall) {
            redisCallCount++;
        }
        if (cacheHit) {
            cacheHitCount++;
        } else {
            cacheMissCount++;
        }
        
        log.debug("Request recorded: {}ms, DB: {}, Redis: {}, CacheHit: {}", 
            responseTime, databaseCall, redisCall, cacheHit);
    }

    /**
     * Reset performance metrics.
     * 
     * WHY this method exists:
     * - Clear metrics for fresh start
     * - Used for testing
     * - Manual reset functionality
     */
    public void resetMetrics() {
        totalResponseTime = 0;
        requestCount = 0;
        fastestRequest = Long.MAX_VALUE;
        slowestRequest = 0;
        databaseCallCount = 0;
        redisCallCount = 0;
        cacheHitCount = 0;
        cacheMissCount = 0;
        
        log.info("Dashboard metrics reset");
    }

    /**
     * Get detailed performance statistics.
     * 
     * @return Detailed performance stats
     */
    public Map<String, Object> getPerformanceStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalRequests", requestCount);
        stats.put("totalResponseTime", totalResponseTime);
        
        if (requestCount > 0) {
            stats.put("averageResponseTime", totalResponseTime / requestCount);
        }
        
        stats.put("fastestRequest", fastestRequest == Long.MAX_VALUE ? 0 : fastestRequest);
        stats.put("slowestRequest", slowestRequest);
        stats.put("databaseCalls", databaseCallCount);
        stats.put("redisCalls", redisCallCount);
        stats.put("cacheHits", cacheHitCount);
        stats.put("cacheMisses", cacheMissCount);
        
        if (cacheHitCount + cacheMissCount > 0) {
            double hitRatio = (cacheHitCount * 100.0) / (cacheHitCount + cacheMissCount);
            stats.put("cacheHitRatio", String.format("%.2f%%", hitRatio));
        }
        
        return stats;
    }

    /**
     * Get system health status.
     * 
     * @return System health information
     */
    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        
        // Database health - disabled for Redis-only testing
        health.put("database", Map.of(
            "status", "DISABLED",
            "message", "Database disabled for Redis-only testing"
        ));
        
        // Redis health
        try {
            redisTemplate.getConnectionFactory().getConnection().ping();
            long keyCount = getRedisKeyCount();
            health.put("redis", Map.of(
                "status", "UP",
                "keyCount", keyCount
            ));
        } catch (Exception e) {
            health.put("redis", Map.of(
                "status", "DOWN",
                "error", e.getMessage()
            ));
        }
        
        return health;
    }
}
