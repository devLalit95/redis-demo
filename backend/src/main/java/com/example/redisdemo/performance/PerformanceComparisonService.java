package com.example.redisdemo.performance;

import com.example.redisdemo.cache.ManualCacheService;
import com.example.redisdemo.cache.SpringCacheService;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Performance Comparison Service
 * 
 * This service compares performance between different caching strategies.
 * This is Phase 7 of the project - Performance Comparison.
 * 
 * WHY this service exists:
 * - Demonstrates performance differences between caching strategies
 * - Shows real-world impact of caching
 * - Provides data for optimization decisions
 * - Helps choose right caching strategy
 * 
 * WHEN to use this service:
 * - Phase 7: Learning performance comparison
 * - Understanding caching impact
 * - Performance optimization
 * - Capacity planning
 * 
 * PRODUCTION USE CASES:
 * - Performance monitoring
 * - Cache effectiveness analysis
 * - Performance optimization
 * - Capacity planning
 * 
 * COMPARISON METRICS:
 * - Response time
 * - Database query count
 * - Redis hit/miss ratio
 * - Memory usage
 * 
 * CACHING STRATEGIES COMPARED:
 * - No Cache: Direct database access
 * - Manual Cache: Cache-aside pattern
 * - Spring Cache: @Cacheable annotation
 */
// @Service  // Disabled for Redis-only testing
@RequiredArgsConstructor
@Slf4j
public class PerformanceComparisonService {

    // Removed StudentService dependency for Redis-only testing
    private final ManualCacheService manualCacheService; // Manual cache
    private final SpringCacheService springCacheService; // Spring Cache

    // Performance tracking
    private int databaseQueryCount = 0;
    private int redisHitCount = 0;
    private int redisMissCount = 0;

    /**
     * Compare performance for getting student by ID.
     * 
     * WHY this method exists:
     * - Shows performance difference between strategies
     * - Demonstrates caching impact
     * - Provides concrete performance data
     * 
     * METRICS COMPARED:
     * - Response time for each strategy
     * - Database queries
     * - Redis operations
     * 
     * @param id The student ID
     * @return Performance comparison results
     */
    public Map<String, Object> compareGetStudentById(Long id) {
        log.info("=".repeat(60));
        log.info("PERFORMANCE COMPARISON: compareGetStudentById({})", id);
        log.info("=".repeat(60));

        Map<String, Object> results = new HashMap<>();
        
        // Reset counters
        resetCounters();

        // 1. Manual Cache (First call - Cache Miss)
        log.info("1️⃣ Testing: Manual Cache (First call - Cache Miss)");
        long manualCacheMissStart = System.currentTimeMillis();
        Object manualCacheMissResult = manualCacheService.getStudentById(id);
        long manualCacheMissTime = System.currentTimeMillis() - manualCacheMissStart;
        int manualCacheMissDbQueries = databaseQueryCount;
        
        results.put("manualCacheMiss", Map.of(
            "responseTime", manualCacheMissTime,
            "databaseQueries", manualCacheMissDbQueries,
            "redisHits", redisHitCount,
            "redisMisses", redisMissCount,
            "data", manualCacheMissResult
        ));
        
        log.info("Manual Cache Miss - Response Time: {} ms, DB Queries: {}, Redis Misses: {}", 
            manualCacheMissTime, manualCacheMissDbQueries, redisMissCount);

        // Reset counters
        resetCounters();

        // 2. Manual Cache (Second call - Cache Hit)
        log.info("2️⃣ Testing: Manual Cache (Second call - Cache Hit)");
        long manualCacheHitStart = System.currentTimeMillis();
        Object manualCacheHitResult = manualCacheService.getStudentById(id);
        long manualCacheHitTime = System.currentTimeMillis() - manualCacheHitStart;
        int manualCacheHitDbQueries = databaseQueryCount;
        
        results.put("manualCacheHit", Map.of(
            "responseTime", manualCacheHitTime,
            "databaseQueries", manualCacheHitDbQueries,
            "redisHits", redisHitCount,
            "redisMisses", redisMissCount,
            "data", manualCacheHitResult
        ));
        
        log.info("Manual Cache Hit - Response Time: {} ms, DB Queries: {}, Redis Hits: {}", 
            manualCacheHitTime, manualCacheHitDbQueries, redisHitCount);

        // Reset counters
        resetCounters();

        // 3. Spring Cache (First call - Cache Miss)
        log.info("3️⃣ Testing: Spring Cache (First call - Cache Miss)");
        long springCacheMissStart = System.currentTimeMillis();
        Object springCacheMissResult = springCacheService.getStudentById(id);
        long springCacheMissTime = System.currentTimeMillis() - springCacheMissStart;
        int springCacheMissDbQueries = databaseQueryCount;
        
        results.put("springCacheMiss", Map.of(
            "responseTime", springCacheMissTime,
            "databaseQueries", springCacheMissDbQueries,
            "redisHits", redisHitCount,
            "redisMisses", redisMissCount,
            "data", springCacheMissResult
        ));
        
        log.info("Spring Cache Miss - Response Time: {} ms, DB Queries: {}, Redis Misses: {}", 
            springCacheMissTime, springCacheMissDbQueries, redisMissCount);

        // Reset counters
        resetCounters();

        // 4. Spring Cache (Second call - Cache Hit)
        log.info("4️⃣ Testing: Spring Cache (Second call - Cache Hit)");
        long springCacheHitStart = System.currentTimeMillis();
        Object springCacheHitResult = springCacheService.getStudentById(id);
        long springCacheHitTime = System.currentTimeMillis() - springCacheHitStart;
        int springCacheHitDbQueries = databaseQueryCount;
        
        results.put("springCacheHit", Map.of(
            "responseTime", springCacheHitTime,
            "databaseQueries", springCacheHitDbQueries,
            "redisHits", redisHitCount,
            "redisMisses", redisMissCount,
            "data", springCacheHitResult
        ));
        
        log.info("Spring Cache Hit - Response Time: {} ms, DB Queries: {}, Redis Hits: {}", 
            springCacheHitTime, springCacheHitDbQueries, redisHitCount);

        // Calculate performance improvements
        long improvement = manualCacheHitTime - springCacheHitTime;
        double improvementPercent = (improvement * 100.0) / manualCacheHitTime;

        results.put("summary", Map.of(
            "baselineManualCache", manualCacheHitTime,
            "springCacheImprovement", improvement,
            "springCacheImprovementPercent", String.format("%.2f%%", improvementPercent)
        ));

        log.info("=".repeat(60));
        log.info("PERFORMANCE SUMMARY:");
        log.info("Baseline (Manual Cache): {} ms", manualCacheHitTime);
        log.info("Spring Cache Improvement: {} ms ({})", improvement, String.format("%.2f%%", improvementPercent));
        log.info("=".repeat(60));

        return results;
    }

    /**
     * Get performance statistics.
     * 
     * WHY this method exists:
     * - Shows current performance metrics
     * - Tracks cache effectiveness
     * - Provides performance insights
     * 
     * @return Performance statistics
     */
    public Map<String, Object> getPerformanceStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("databaseQueryCount", databaseQueryCount);
        stats.put("redisHitCount", redisHitCount);
        stats.put("redisMissCount", redisMissCount);
        
        if (redisHitCount + redisMissCount > 0) {
            double hitRatio = (redisHitCount * 100.0) / (redisHitCount + redisMissCount);
            stats.put("cacheHitRatio", String.format("%.2f%%", hitRatio));
        } else {
            stats.put("cacheHitRatio", "0.00%");
        }
        
        log.info("Performance Stats: {}", stats);
        
        return stats;
    }

    /**
     * Reset performance counters.
     * 
     * WHY this method exists:
     * - Clear counters for fresh comparison
     * - Start new performance test
     * - Reset for accurate measurements
     */
    public void resetCounters() {
        databaseQueryCount = 0;
        redisHitCount = 0;
        redisMissCount = 0;
        
        log.info("Performance counters reset");
    }

    /**
     * Increment database query counter.
     * 
     * WHY this method exists:
     * - Track database queries
     * - Measure database load
     * - Compare caching effectiveness
     */
    public void incrementDatabaseQueryCount() {
        databaseQueryCount++;
    }

    /**
     * Increment Redis hit counter.
     * 
     * WHY this method exists:
     * - Track cache hits
     * - Measure cache effectiveness
     * - Calculate hit ratio
     */
    public void incrementRedisHitCount() {
        redisHitCount++;
    }

    /**
     * Increment Redis miss counter.
     * 
     * WHY this method exists:
     * - Track cache misses
     * - Measure cache effectiveness
     * - Calculate hit ratio
     */
    public void incrementRedisMissCount() {
        redisMissCount++;
    }

    /**
     * Get performance explanation.
     * 
     * WHY this method exists:
     * - Educational purpose
     * - Explains performance concepts
     * - Provides optimization guidance
     * 
     * @return Performance explanation
     */
    public String getPerformanceExplanation() {
        return """
            Performance Comparison Concepts:
            
            WHY PERFORMANCE MATTERS:
            - User experience: Faster response times
            - Resource usage: Lower database load
            - Scalability: Handle more traffic
            - Cost: Reduced infrastructure costs
            
            PERFORMANCE METRICS:
            
            1. RESPONSE TIME:
            - Time taken to complete request
            - Measured in milliseconds
            - Critical for user experience
            - Target: < 100ms for web apps
            
            2. DATABASE QUERIES:
            - Number of database queries
            - Each query adds latency
            - Database is often bottleneck
            - Caching reduces query count
            
            3. CACHE HIT RATIO:
            - Percentage of requests served from cache
            - Higher is better
            - Target: > 80% for effective caching
            - Formula: Hits / (Hits + Misses)
            
            CACHING PERFORMANCE IMPACT:
            
            NO CACHE:
            - Every request hits database
            - High database load
            - Slow response times
            - Poor scalability
            
            MANUAL CACHE:
            - First request: Cache miss (database)
            - Subsequent requests: Cache hit (Redis)
            - Reduces database load
            - Faster response times
            
            SPRING CACHE:
            - Similar to manual cache
            - Declarative (annotations)
            - Easier to implement
            - Automatic cache management
            
            PERFORMANCE IMPROVEMENT:
            - Typical: 10-100x faster for cache hits
            - Database load: 80-95% reduction
            - Response time: 90-99% improvement
            - Scalability: 10-100x more capacity
            
            WHEN TO CACHE:
            - Read-heavy workloads
            - Expensive operations
            - Frequently accessed data
            - Data that doesn't change often
            
            WHEN NOT TO CACHE:
            - Write-heavy workloads
            - Real-time data requirements
            - Rarely accessed data
            - Very large datasets
            """;
    }
}
