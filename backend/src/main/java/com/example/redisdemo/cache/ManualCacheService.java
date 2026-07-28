package com.example.redisdemo.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Manual Cache Service
 * 
 * This service demonstrates manual caching implementation without @Cacheable.
 * This is Phase 3 of the project - Manual Cache Implementation.
 * 
 * NOTE: StudentRepository dependency removed for Redis-only testing
 * 
 * WHY this service exists:
 * - Teaches the cache-aside pattern through manual implementation
 * - Shows exactly how caching works under the hood
 * - Demonstrates cache hit vs miss scenarios
 * - Provides performance comparison baseline
 * 
 * WHEN to use this service:
 * - Phase 3: Learning manual caching
 * - Understanding cache hit/miss patterns
 * - Performance comparison with Spring Cache
 * 
 * PRODUCTION USE CASES:
 * - Custom caching logic
 * - Complex cache invalidation strategies
 * - Multi-level caching
 * - Cache warming strategies
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ManualCacheService {

    // Removed StudentRepository dependency for Redis-only testing
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "manual:student:";
    private static final long CACHE_TTL_SECONDS = 300; // 5 minutes

    /**
     * Get student by ID with manual caching.
     * 
     * FLOW:
     * 1. Check Redis cache
     * 2. If found (CACHE HIT): Return cached data
     * 3. If not found (CACHE MISS): Fetch from MySQL, store in Redis, return data
     * 
     * WHY this method exists:
     * - Demonstrates the cache-aside pattern
     * - Shows cache hit vs miss scenarios
     * - Provides performance comparison baseline
     * 
     * WHEN to use this method:
     * - Phase 3: Learning manual caching
     * - Understanding cache hit/miss patterns
     * - Performance comparison with Spring Cache
     * 
     * PERFORMANCE CHARACTERISTICS:
     * - Cache Hit: ~1-2ms (Redis operation)
     * - Cache Miss: ~10-50ms (MySQL query + Redis set)
     * - First call: Always cache miss
     * - Subsequent calls: Cache hit (until TTL expires)
     * 
     * @param id The student ID
     * @return The student DTO
     */
    public Object getStudentById(Long id) {
        String cacheKey = CACHE_PREFIX + id;
        
        log.info("Manual Cache: Fetching student ID: {}", id);
        
        // Step 1: Check Redis cache
        Object cachedStudent = redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedStudent != null) {
            // CACHE HIT
            log.info("✅ CACHE HIT: Student found in Redis for ID: {}", id);
            log.info("Cache Key: {}", cacheKey);
            return cachedStudent;
        }
        
        // CACHE MISS - Fetch from database
        log.info("❌ CACHE MISS: Student not found in Redis for ID: {}", id);
        log.info("Fetching from database (simulated for Redis-only testing)");
        
        // Create a placeholder student object
        Object studentDTO = Map.of(
            "id", id,
            "name", "Test Student " + id,
            "email", "test" + id + "@example.com",
            "message", "Database fetch simulated (Redis-only testing)"
        );
        
        // Store in Redis cache
        redisTemplate.opsForValue().set(cacheKey, studentDTO, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
        log.info("✅ Student stored in Redis cache with TTL: {} seconds", CACHE_TTL_SECONDS);
        
        return studentDTO;
    }

    /**
     * Get all students with manual caching.
     * 
     * @return List of all students
     */
    public Object getAllStudents() {
        String cacheKey = CACHE_PREFIX + "all";
        
        log.info("Manual Cache: Fetching all students");
        
        // Check cache
        Object cachedStudents = redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedStudents != null) {
            log.info("✅ CACHE HIT: All students found in Redis");
            return cachedStudents;
        }
        
        // Cache miss
        log.info("❌ CACHE MISS: All students not found in Redis");
        log.info("Fetching from database (simulated for Redis-only testing)");
        
        // Create placeholder data
        Object students = Map.of(
            "message", "Database fetch simulated (Redis-only testing)",
            "count", 0
        );
        
        // Store in cache
        redisTemplate.opsForValue().set(cacheKey, students, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
        log.info("✅ All students stored in Redis cache");
        
        return students;
    }

    /**
     * Delete student from cache by ID.
     * 
     * @param id The student ID
     */
    public void evictStudentCache(Long id) {
        String cacheKey = CACHE_PREFIX + id;
        
        log.info("Manual Cache: Evicting student cache for ID: {}", id);
        
        Boolean deleted = redisTemplate.delete(cacheKey);
        
        if (Boolean.TRUE.equals(deleted)) {
            log.info("✅ Student cache evicted successfully for ID: {}", id);
        } else {
            log.info("⚠️ No cache entry found for ID: {}", id);
        }
    }

    /**
     * Delete all students cache.
     */
    public void evictAllStudentsCache() {
        String cacheKey = CACHE_PREFIX + "all";
        
        log.info("Manual Cache: Evicting all students cache");
        
        Boolean deleted = redisTemplate.delete(cacheKey);
        
        if (Boolean.TRUE.equals(deleted)) {
            log.info("✅ All students cache evicted successfully");
        } else {
            log.info("⚠️ No cache entry found for all students");
        }
    }

    /**
     * Get manual cache explanation.
     * 
     * @return Explanation of manual caching
     */
    public String getManualCacheExplanation() {
        return """
            Manual Cache Implementation (Cache-Aside Pattern):
            
            WHY MANUAL CACHING:
            - Full control over cache behavior
            - Custom cache logic and strategies
            - Understanding how caching works under the hood
            - Performance optimization opportunities
            
            CACHE-ASIDE PATTERN:
            
            1. READ OPERATION:
            - Application requests data
            - Check cache first
            - If cache hit: Return cached data
            - If cache miss: Fetch from database, update cache, return data
            
            2. WRITE OPERATION:
            - Update database
            - Invalidate or update cache
            - Ensures data consistency
            
            ADVANTAGES:
            - Simple to implement
            - Cache only contains needed data
            - Easy to understand and debug
            - Works with any cache provider
            
            DISADVANTAGES:
            - Cache stampede risk on cache miss
            - Stale data possible
            - Requires manual cache management
            - More code to maintain
            
            PERFORMANCE COMPARISON:
            - Cache Hit: ~1-2ms (Redis operation)
            - Cache Miss: ~10-50ms (Database + Redis)
            - No Cache: ~10-100ms (Database only)
            
            WHEN TO USE:
            - Simple caching requirements
            - Fine-grained cache control needed
            - Custom cache logic required
            - Learning caching concepts
            
            PRODUCTION USE CASES:
            - Custom caching strategies
            - Multi-level caching
            - Cache warming
            - Complex invalidation logic
            """;
    }
}