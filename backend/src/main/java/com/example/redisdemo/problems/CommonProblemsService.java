package com.example.redisdemo.problems;

import com.example.redisdemo.dto.StudentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Common Problems Simulation Service
 * 
 * This service demonstrates common Redis caching problems and their solutions.
 * This is Phase 12 of the project - Common Problems & Solutions.
 * 
 * WHY this service exists:
 * - Teaches common caching problems
 * - Shows how to identify issues
     * - Demonstrates solutions
     * - Helps avoid production issues
 * 
 * WHEN to use this service:
 * - Phase 12: Learning caching problems
     * - Understanding cache issues
     * - Learning debugging techniques
     * - Production troubleshooting
 * 
 * PRODUCTION USE CASES:
     * - Training and education
     * - Problem identification
     * - Solution validation
     * - System design decisions
 * 
 * PROBLEMS COVERED:
     * - Cache Stampede: Thundering herd problem
     * - Cache Penetration: Invalid keys flooding cache
     * - Cache Breakdown: Hot key expiration
     * - Cache Avalanche: Massive key expiration
     * - Memory Issues: Out of memory scenarios
     * - Performance Issues: Slow operations
 */
// @Service  // Disabled for Redis-only testing
@RequiredArgsConstructor
@Slf4j
public class CommonProblemsService {

    // Removed StudentRepository dependency for Redis-only testing
    private final RedisTemplate<String, Object> redisTemplate;

    // ==================== CACHE STAMPEDE ====================

    /**
     * Simulate cache stampede problem.
     * 
     * WHY this method exists:
     * - Demonstrates thundering herd problem
     * - Shows concurrent cache misses
     * - High database load simulation
     * 
     * WHEN to use this method:
     * - Understanding cache stampede
     * - Learning mitigation strategies
     * - Testing system resilience
     * 
     * PRODUCTION USE CASES:
     * - System testing
     * - Performance testing
     * - Mitigation validation
     * 
     * PROBLEM: Many requests hit cache simultaneously when it expires
     * SOLUTION: Use lock or early expiration
     */
    public Map<String, Object> simulateCacheStampede(Long id, int requestCount) {
        log.info("Cache Stampede Simulation: student ID={}, requests={}", id, requestCount);
        log.warn("⚠️ Simulating cache stampede - watch database load");
        
        Map<String, Object> result = new HashMap<>();
        AtomicInteger dbCallCount = new AtomicInteger(0);
        AtomicInteger cacheHitCount = new AtomicInteger(0);
        AtomicInteger cacheMissCount = new AtomicInteger(0);
        
        // Clear cache first
        String cacheKey = "student:" + id;
        redisTemplate.delete(cacheKey);
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(requestCount);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < requestCount; i++) {
            executor.submit(() -> {
                try {
                    // Check cache
                    Object cached = redisTemplate.opsForValue().get(cacheKey);
                    
                    if (cached != null) {
                        cacheHitCount.incrementAndGet();
                    } else {
                        cacheMissCount.incrementAndGet();
                        dbCallCount.incrementAndGet();
                        
                        // Simulate database query with placeholder data
                        log.info("⚠️ Database operations disabled for Redis-only testing");
                        StudentDTO studentDTO = StudentDTO.builder()
                                .id(id)
                                .name("Test Student " + id)
                                .email("test" + id + "@example.com")
                                .message("Cache stampede simulation (Redis-only testing)")
                                .build();
                        
                        // Store in cache
                        redisTemplate.opsForValue().set(cacheKey, studentDTO, 5, TimeUnit.MINUTES);
                    }
                } catch (Exception e) {
                    log.error("Error in cache stampede simulation", e);
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Cache stampede simulation interrupted", e);
        }
        
        executor.shutdown();
        
        long duration = System.currentTimeMillis() - startTime;
        
        result.put("totalRequests", requestCount);
        result.put("databaseCalls", dbCallCount.get());
        result.put("cacheHits", cacheHitCount.get());
        result.put("cacheMisses", cacheMissCount.get());
        result.put("duration", duration);
        result.put("problem", "Cache Stampede - Multiple requests hitting database simultaneously");
        result.put("solution", "Use locking, early expiration, or request coalescing");
        
        log.warn("Cache Stampede Results: {} DB calls for {} requests", dbCallCount.get(), requestCount);
        
        return result;
    }

    // ==================== CACHE PENETRATION ====================

    /**
     * Simulate cache penetration problem.
     * 
     * WHY this method exists:
     * - Demonstrates invalid key flooding
     * - Shows cache pollution
     * - High database load on invalid keys
     * 
     * WHEN to use this method:
     * - Understanding cache penetration
     * - Learning mitigation strategies
     * - Testing system resilience
     * 
     * PRODUCTION USE CASES:
     * - System testing
     * - Security testing
     * - Mitigation validation
     * 
     * PROBLEM: Requests for non-existent keys bypass cache
     * SOLUTION: Cache null values or use bloom filters
     */
    public Map<String, Object> simulateCachePenetration(int invalidKeyCount) {
        log.info("Cache Penetration Simulation: invalid keys={}", invalidKeyCount);
        log.warn("⚠️ Simulating cache penetration - watch database load");
        
        Map<String, Object> result = new HashMap<>();
        AtomicInteger dbCallCount = new AtomicInteger(0);
        AtomicInteger cacheHitCount = new AtomicInteger(0);
        AtomicInteger cacheMissCount = new AtomicInteger(0);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < invalidKeyCount; i++) {
            String invalidKey = "invalid:student:" + i;
            
            // Check cache
            Object cached = redisTemplate.opsForValue().get(invalidKey);
            
            if (cached != null) {
                cacheHitCount.incrementAndGet();
            } else {
                cacheMissCount.incrementAndGet();
                dbCallCount.incrementAndGet();
                
                // Try to fetch from database (will fail) - disabled for Redis-only testing
                log.info("⚠️ Database operations disabled for Redis-only testing");
                
                // Without mitigation, we don't cache the miss
            }
        }
        
        long duration = System.currentTimeMillis() - startTime;
        
        result.put("totalRequests", invalidKeyCount);
        result.put("databaseCalls", dbCallCount.get());
        result.put("cacheHits", cacheHitCount.get());
        result.put("cacheMisses", cacheMissCount.get());
        result.put("duration", duration);
        result.put("problem", "Cache Penetration - Invalid keys bypass cache");
        result.put("solution", "Cache null values or use bloom filters");
        
        log.warn("Cache Penetration Results: {} DB calls for {} invalid requests", dbCallCount.get(), invalidKeyCount);
        
        return result;
    }

    // ==================== CACHE BREAKDOWN ====================

    /**
     * Simulate cache breakdown problem.
     * 
     * WHY this method exists:
     * - Demonstrates hot key expiration
     * - Shows database load spikes
     * - Complex query degradation
     * 
     * WHEN to use this method:
     * - Understanding cache breakdown
     * - Learning mitigation strategies
     * - Testing system resilience
     * 
     * PRODUCTION USE CASES:
     * - System testing
     * - Performance testing
     * - Mitigation validation
     * 
     * PROBLEM: Hot key expiration causes database load spike
     * SOLUTION: Use mutex or logical expiration
     */
    public Map<String, Object> simulateCacheBreakdown(Long id, int requestCount) {
        log.info("Cache Breakdown Simulation: student ID={}, requests={}", id, requestCount);
        log.warn("⚠️ Simulating cache breakdown - watch database load");
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        Map<String, Object> result = new HashMap<>();
        AtomicInteger dbCallCount = new AtomicInteger(0);
        AtomicInteger cacheHitCount = new AtomicInteger(0);
        AtomicInteger cacheMissCount = new AtomicInteger(0);
        
        // First, populate cache with placeholder data
        StudentDTO studentDTO = StudentDTO.builder()
                .id(id)
                .name("Test Student " + id)
                .email("test" + id + "@example.com")
                .message("Cache breakdown simulation (Redis-only testing)")
                .build();
        String cacheKey = "hot:student:" + id;
        redisTemplate.opsForValue().set(cacheKey, studentDTO, 1, TimeUnit.SECONDS); // Short TTL
        
        // Wait for cache to expire
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            log.error("Sleep interrupted", e);
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(requestCount);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < requestCount; i++) {
            executor.submit(() -> {
                try {
                    // Check cache
                    Object cached = redisTemplate.opsForValue().get(cacheKey);
                    
                    if (cached != null) {
                        cacheHitCount.incrementAndGet();
                    } else {
                        cacheMissCount.incrementAndGet();
                        dbCallCount.incrementAndGet();
                        
                        // Fetch from database with placeholder data
                        log.info("⚠️ Database operations disabled for Redis-only testing");
                        StudentDTO freshStudentDTO = StudentDTO.builder()
                                .id(id)
                                .name("Test Student " + id)
                                .email("test" + id + "@example.com")
                                .message("Cache breakdown simulation (Redis-only testing)")
                                .build();
                        
                        // Store in cache
                        redisTemplate.opsForValue().set(cacheKey, freshStudentDTO, 1, TimeUnit.SECONDS);
                    }
                } catch (Exception e) {
                    log.error("Error in cache breakdown simulation", e);
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Cache breakdown simulation interrupted", e);
        }
        
        executor.shutdown();
        
        long duration = System.currentTimeMillis() - startTime;
        
        result.put("totalRequests", requestCount);
        result.put("databaseCalls", dbCallCount.get());
        result.put("cacheHits", cacheHitCount.get());
        result.put("cacheMisses", cacheMissCount.get());
        result.put("duration", duration);
        result.put("problem", "Cache Breakdown - Hot key expiration causes DB load spike");
        result.put("solution", "Use mutex lock or logical expiration");
        
        log.warn("Cache Breakdown Results: {} DB calls for {} requests", dbCallCount.get(), requestCount);
        
        return result;
    }

    // ==================== CACHE AVALANCHE ====================

    /**
     * Simulate cache avalanche problem.
     * 
     * WHY this method exists:
     * - Demonstrates massive key expiration
     * - Shows database overload
     * - System failure simulation
     * 
     * WHEN to use this method:
     * - Understanding cache avalanche
     * - Learning mitigation strategies
     * - Testing system resilience
     * 
     * PRODUCTION USE CASES:
     * - System testing
     * - Disaster recovery testing
     * - Mitigation validation
     * 
     * PROBLEM: Many keys expire simultaneously
     * SOLUTION: Randomize TTL, use multi-level cache
     */
    public Map<String, Object> simulateCacheAvalanche(int keyCount, int requestCount) {
        log.info("Cache Avalanche Simulation: keys={}, requests={}", keyCount, requestCount);
        log.warn("⚠️ Simulating cache avalanche - watch database load");
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        Map<String, Object> result = new HashMap<>();
        AtomicInteger dbCallCount = new AtomicInteger(0);
        AtomicInteger cacheHitCount = new AtomicInteger(0);
        AtomicInteger cacheMissCount = new AtomicInteger(0);
        
        // Populate cache with same TTL using placeholder data
        List<String> cacheKeys = new ArrayList<>();
        for (int i = 0; i < keyCount; i++) {
            StudentDTO studentDTO = StudentDTO.builder()
                    .id(1L)
                    .name("Test Student")
                    .email("test@example.com")
                    .message("Cache avalanche simulation (Redis-only testing)")
                    .build();
            String cacheKey = "avalanche:student:" + i;
            redisTemplate.opsForValue().set(cacheKey, studentDTO, 1, TimeUnit.SECONDS); // Same TTL
            cacheKeys.add(cacheKey);
        }
        
        // Wait for cache to expire
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            log.error("Sleep interrupted", e);
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(requestCount);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < requestCount; i++) {
            final int index = i % keyCount;
            executor.submit(() -> {
                try {
                    String cacheKey = cacheKeys.get(index);
                    
                    // Check cache
                    Object cached = redisTemplate.opsForValue().get(cacheKey);
                    
                    if (cached != null) {
                        cacheHitCount.incrementAndGet();
                    } else {
                        cacheMissCount.incrementAndGet();
                        dbCallCount.incrementAndGet();
                        
                        // Fetch from database with placeholder data
                        log.info("⚠️ Database operations disabled for Redis-only testing");
                        StudentDTO studentDTO = StudentDTO.builder()
                                .id(1L)
                                .name("Test Student")
                                .email("test@example.com")
                                .message("Cache avalanche simulation (Redis-only testing)")
                                .build();
                        
                        // Store in cache
                        redisTemplate.opsForValue().set(cacheKey, studentDTO, 1, TimeUnit.SECONDS);
                    }
                } catch (Exception e) {
                    log.error("Error in cache avalanche simulation", e);
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Cache avalanche simulation interrupted", e);
        }
        
        executor.shutdown();
        
        long duration = System.currentTimeMillis() - startTime;
        
        result.put("totalRequests", requestCount);
        result.put("keysExpired", keyCount);
        result.put("databaseCalls", dbCallCount.get());
        result.put("cacheHits", cacheHitCount.get());
        result.put("cacheMisses", cacheMissCount.get());
        result.put("duration", duration);
        result.put("problem", "Cache Avalanche - Massive key expiration overloads database");
        result.put("solution", "Randomize TTL, use multi-level cache");
        
        log.warn("Cache Avalanche Results: {} DB calls for {} requests, {} keys expired", 
            dbCallCount.get(), requestCount, keyCount);
        
        return result;
    }

    /**
     * Get common problems explanation.
     * 
     * @return Explanation of common problems and solutions
     */
    public String getCommonProblemsExplanation() {
        return """
            Common Redis Caching Problems & Solutions:
            
            1. CACHE STAMPEDE (Thundering Herd):
            PROBLEM: Many requests hit cache simultaneously when it expires
            SCENARIO: Hot key expires, thousands of requests miss cache
            IMPACT: Database overload, slow response times
            SOLUTIONS:
            - Use mutex lock (only one request rebuilds cache)
            - Use early expiration (rebuild before expiration)
            - Use request coalescing (merge similar requests)
            - Use multi-level cache (L1, L2, L3)
            
            2. CACHE PENETRATION:
            PROBLEM: Requests for non-existent keys bypass cache
            SCENARIO: Attackers request invalid keys, cache never stores them
            IMPACT: Database overload, cache pollution
            SOLUTIONS:
            - Cache null values (with short TTL)
            - Use bloom filters (quick invalid key check)
            - Use request validation (reject invalid requests)
            - Use rate limiting (limit attack attempts)
            
            3. CACHE BREAKDOWN:
            PROBLEM: Hot key expiration causes database load spike
            SCENARIO: Frequently accessed key expires, complex query runs
            IMPACT: Database load spike, slow response times
            SOLUTIONS:
            - Use mutex lock (prevent concurrent rebuilds)
            - Use logical expiration (rebuild in background)
            - Use cache warming (pre-populate critical keys)
            - Use backup cache (secondary cache layer)
            
            4. CACHE AVALANCHE:
            PROBLEM: Many keys expire simultaneously
            SCENARIO: Same TTL set on many keys, all expire at once
            IMPACT: Database overload, system failure
            SOLUTIONS:
            - Randomize TTL (add random jitter)
            - Use multi-level cache (spread load)
            - Use cache warming (prevent expiration)
            - Use staggered expiration (avoid simultaneous expiry)
            
            5. MEMORY ISSUES:
            PROBLEM: Redis runs out of memory
            SCENARIO: Too much data cached, memory limit reached
            IMPACT: Redis evicts keys, performance degradation
            SOLUTIONS:
            - Use appropriate TTL (expire old data)
            - Use memory-efficient serialization
            - Use Redis eviction policies
            - Monitor memory usage closely
            
            6. PERFORMANCE ISSUES:
            PROBLEM: Slow Redis operations
            SCENARIO: Large keys, complex operations, network latency
            IMPACT: Slow response times, poor user experience
            SOLUTIONS:
            - Use appropriate data structures
            - Optimize key design
            - Use pipelining (batch operations)
            - Use clustering (distribute load)
            
            PREVENTION STRATEGIES:
            - Monitor cache hit ratio
            - Monitor database load
            - Monitor Redis memory usage
            - Set up alerts for anomalies
            - Test with realistic load
            - Implement circuit breakers
            - Use retry with backoff
            """;
    }
}
