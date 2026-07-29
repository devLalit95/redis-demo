package com.example.redisdemo.metrics;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Cache Statistics Service
 * 
 * This service maintains cache statistics for monitoring and analysis.
 * 
 * WHY this service exists:
 * - Tracks cache hit/miss ratios
 * - Monitors cache performance
 * - Provides data for dashboard analytics
 * - Helps identify cache optimization opportunities
 * 
 * WHEN to use this service:
 * - Monitoring cache effectiveness
 * - Analyzing cache performance
 * - Dashboard metrics
 * - Performance optimization
 * 
 * PRODUCTION USE CASES:
 * - Real-time cache monitoring
 * - Performance dashboards
 * - Cache optimization decisions
 * - Capacity planning
 * 
 * STATISTICS TRACKED:
 * - Total requests
 * - Cache hits
 * - Cache misses
 * - Hit ratio
 * - Miss ratio
 * - Average response time
 * - Redis reads/writes
 * - Database reads/writes
 * - Evictions
 * - Expired keys
 */
@Service
@Slf4j
public class CacheStatisticsService {

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private final AtomicLong redisReads = new AtomicLong(0);
    private final AtomicLong redisWrites = new AtomicLong(0);
    private final AtomicLong databaseReads = new AtomicLong(0);
    private final AtomicLong databaseWrites = new AtomicLong(0);
    private final AtomicLong evictions = new AtomicLong(0);
    private final AtomicLong expiredKeys = new AtomicLong(0);
    
    // Track response times for average calculation
    private final AtomicLong totalResponseTime = new AtomicLong(0);
    
    // Track statistics by cache type
    private final ConcurrentHashMap<String, CacheTypeStats> cacheTypeStats = new ConcurrentHashMap<>();

    /**
     * Record a cache hit.
     * 
     * @param cacheType The type of cache (MANUAL, SPRING, NONE)
     * @param responseTime The response time in milliseconds
     */
    public void recordCacheHit(String cacheType, long responseTime) {
        totalRequests.incrementAndGet();
        cacheHits.incrementAndGet();
        redisReads.incrementAndGet();
        totalResponseTime.addAndGet(responseTime);
        
        getCacheTypeStats(cacheType).recordHit(responseTime);
        
        log.debug("Cache Hit recorded for type: {} | Response Time: {}ms", cacheType, responseTime);
    }

    /**
     * Record a cache miss.
     * 
     * @param cacheType The type of cache (MANUAL, SPRING, NONE)
     * @param responseTime The response time in milliseconds
     */
    public void recordCacheMiss(String cacheType, long responseTime) {
        totalRequests.incrementAndGet();
        cacheMisses.incrementAndGet();
        redisReads.incrementAndGet();
        databaseReads.incrementAndGet();
        totalResponseTime.addAndGet(responseTime);
        
        getCacheTypeStats(cacheType).recordMiss(responseTime);
        
        log.debug("Cache Miss recorded for type: {} | Response Time: {}ms", cacheType, responseTime);
    }

    /**
     * Record a Redis write operation.
     * 
     * @param cacheType The type of cache (MANUAL, SPRING, NONE)
     */
    public void recordRedisWrite(String cacheType) {
        redisWrites.incrementAndGet();
        getCacheTypeStats(cacheType).recordWrite();
        
        log.debug("Redis Write recorded for type: {}", cacheType);
    }

    /**
     * Record a database write operation.
     * 
     * @param cacheType The type of cache (MANUAL, SPRING, NONE)
     */
    public void recordDatabaseWrite(String cacheType) {
        databaseWrites.incrementAndGet();
        getCacheTypeStats(cacheType).recordDatabaseWrite();
        
        log.debug("Database Write recorded for type: {}", cacheType);
    }

    /**
     * Record a cache eviction.
     * 
     * @param cacheType The type of cache (MANUAL, SPRING, NONE)
     */
    public void recordEviction(String cacheType) {
        evictions.incrementAndGet();
        getCacheTypeStats(cacheType).recordEviction();
        
        log.debug("Eviction recorded for type: {}", cacheType);
    }

    /**
     * Record an expired key.
     * 
     * @param cacheType The type of cache (MANUAL, SPRING, NONE)
     */
    public void recordExpiredKey(String cacheType) {
        expiredKeys.incrementAndGet();
        getCacheTypeStats(cacheType).recordExpiredKey();
        
        log.debug("Expired Key recorded for type: {}", cacheType);
    }

    /**
     * Get current cache statistics.
     * 
     * @return CacheStatistics object with current metrics
     */
    public CacheStatistics getStatistics() {
        long totalReqs = totalRequests.get();
        long hits = cacheHits.get();
        long misses = cacheMisses.get();
        
        double hitRatio = totalReqs > 0 ? (double) hits / totalReqs * 100 : 0.0;
        double missRatio = totalReqs > 0 ? (double) misses / totalReqs * 100 : 0.0;
        double avgResponseTime = totalReqs > 0 ? (double) totalResponseTime.get() / totalReqs : 0.0;
        
        return CacheStatistics.builder()
                .totalRequests(totalReqs)
                .cacheHits(hits)
                .cacheMisses(misses)
                .hitRatio(String.format("%.2f%%", hitRatio))
                .missRatio(String.format("%.2f%%", missRatio))
                .averageResponseTime(String.format("%.2fms", avgResponseTime))
                .redisReads(redisReads.get())
                .redisWrites(redisWrites.get())
                .databaseReads(databaseReads.get())
                .databaseWrites(databaseWrites.get())
                .evictions(evictions.get())
                .expiredKeys(expiredKeys.get())
                .cacheTypeStats(cacheTypeStats)
                .build();
    }

    /**
     * Reset all statistics.
     */
    public void resetStatistics() {
        totalRequests.set(0);
        cacheHits.set(0);
        cacheMisses.set(0);
        redisReads.set(0);
        redisWrites.set(0);
        databaseReads.set(0);
        databaseWrites.set(0);
        evictions.set(0);
        expiredKeys.set(0);
        totalResponseTime.set(0);
        cacheTypeStats.clear();
        
        log.info("Cache statistics reset");
    }

    /**
     * Get statistics for a specific cache type.
     * 
     * @param cacheType The type of cache
     * @return CacheTypeStats for the specified cache type
     */
    public CacheTypeStats getCacheTypeStatistics(String cacheType) {
        return getCacheTypeStats(cacheType);
    }

    /**
     * Get or create cache type statistics.
     * 
     * @param cacheType The type of cache
     * @return CacheTypeStats for the specified cache type
     */
    private CacheTypeStats getCacheTypeStats(String cacheType) {
        return cacheTypeStats.computeIfAbsent(cacheType, k -> new CacheTypeStats());
    }

    /**
     * Cache Statistics Data Transfer Object.
     */
    @Data
    @lombok.Builder
    public static class CacheStatistics {
        private long totalRequests;
        private long cacheHits;
        private long cacheMisses;
        private String hitRatio;
        private String missRatio;
        private String averageResponseTime;
        private long redisReads;
        private long redisWrites;
        private long databaseReads;
        private long databaseWrites;
        private long evictions;
        private long expiredKeys;
        private ConcurrentHashMap<String, CacheTypeStats> cacheTypeStats;
    }

    /**
     * Cache Type Statistics inner class.
     */
    @Data
    public static class CacheTypeStats {
        private final AtomicLong hits = new AtomicLong(0);
        private final AtomicLong misses = new AtomicLong(0);
        private final AtomicLong writes = new AtomicLong(0);
        private final AtomicLong databaseWrites = new AtomicLong(0);
        private final AtomicLong evictions = new AtomicLong(0);
        private final AtomicLong expiredKeys = new AtomicLong(0);
        private final AtomicLong totalResponseTime = new AtomicLong(0);
        private final AtomicLong requestCount = new AtomicLong(0);

        public void recordHit(long responseTime) {
            hits.incrementAndGet();
            totalResponseTime.addAndGet(responseTime);
            requestCount.incrementAndGet();
        }

        public void recordMiss(long responseTime) {
            misses.incrementAndGet();
            totalResponseTime.addAndGet(responseTime);
            requestCount.incrementAndGet();
        }

        public void recordWrite() {
            writes.incrementAndGet();
        }

        public void recordDatabaseWrite() {
            databaseWrites.incrementAndGet();
        }

        public void recordEviction() {
            evictions.incrementAndGet();
        }

        public void recordExpiredKey() {
            expiredKeys.incrementAndGet();
        }

        public double getHitRatio() {
            long total = hits.get() + misses.get();
            return total > 0 ? (double) hits.get() / total * 100 : 0.0;
        }

        public double getAverageResponseTime() {
            long count = requestCount.get();
            return count > 0 ? (double) totalResponseTime.get() / count : 0.0;
        }
    }
}