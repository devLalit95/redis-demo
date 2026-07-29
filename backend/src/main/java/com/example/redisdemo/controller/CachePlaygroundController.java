package com.example.redisdemo.controller;

import com.example.redisdemo.cache.ManualCacheService;
import com.example.redisdemo.cache.SpringCacheService;
import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.fetch.NoCacheService;
import com.example.redisdemo.metrics.CacheStatisticsService;
import com.example.redisdemo.redis.RedisOperationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Cache Playground Controller
 * 
 * This controller provides endpoints for comparing different caching strategies.
 * 
 * WHY this controller exists:
 * - Provides unified API for testing all three fetch modes
 * - Enables easy performance comparison
 * - Demonstrates cache hit/miss scenarios
 * - Shows performance differences between strategies
 * 
 * WHEN to use this controller:
 * - Performance testing and comparison
 * - Learning cache behavior
 * - Demonstrating Redis benefits
 * - Cache optimization analysis
 * 
 * PRODUCTION USE CASES:
 * - Performance monitoring endpoints
 * - Cache testing utilities
 * - Load testing scenarios
 * - Performance benchmarking
 */
@RestController
@RequestMapping("/api/cache-playground")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cache Playground", description = "Compare different caching strategies")
public class CachePlaygroundController {

    private final NoCacheService noCacheService;
    private final ManualCacheService manualCacheService;
    private final SpringCacheService springCacheService;
    private final CacheStatisticsService cacheStatisticsService;
    private final RedisOperationsService redisOperationsService;

    // ==================== WITHOUT CACHE ENDPOINTS ====================

    /**
     * Get student by ID without cache.
     * 
     * DEMONSTRATES:
     * - Direct database access
     * - Baseline performance measurement
     * - No caching benefits
     * 
     * PERFORMANCE: 10-100ms (always hits database)
     */
    @Operation(summary = "Get student without cache", description = "Fetches student directly from database without any caching")
    @GetMapping("/no-cache/{id}")
    public ApiResponse<StudentDTO> getStudentWithoutCache(@PathVariable Long id) {
        log.info("🔴 API: Fetching student without cache - ID: {}", id);
        return noCacheService.getStudentById(id);
    }

    /**
     * Get all students without cache.
     * 
     * PERFORMANCE: 50-200ms (always hits database)
     */
    @Operation(summary = "Get all students without cache", description = "Fetches all students directly from database without any caching")
    @GetMapping("/no-cache/all")
    public ApiResponse<List<StudentDTO>> getAllStudentsWithoutCache() {
        log.info("🔴 API: Fetching all students without cache");
        return noCacheService.getAllStudents();
    }

    /**
     * Get students by course without cache.
     * 
     * PERFORMANCE: 20-100ms (always hits database)
     */
    @Operation(summary = "Get students by course without cache", description = "Fetches students by course directly from database without any caching")
    @GetMapping("/no-cache/course/{course}")
    public ApiResponse<List<StudentDTO>> getStudentsByCourseWithoutCache(@PathVariable String course) {
        log.info("🔴 API: Fetching students by course without cache - Course: {}", course);
        return noCacheService.getStudentsByCourse(course);
    }

    // ==================== MANUAL CACHE ENDPOINTS ====================

    /**
     * Get student by ID with manual cache.
     * 
     * DEMONSTRATES:
     * - Cache-aside pattern
     * - Manual cache implementation
     * - Cache hit/miss behavior
     * 
     * PERFORMANCE:
     * - Cache Hit: 1-2ms (Redis)
     * - Cache Miss: 10-50ms (Database + Redis)
     */
    @Operation(summary = "Get student with manual cache", description = "Fetches student using manual cache-aside pattern")
    @GetMapping("/manual-cache/{id}")
    public ApiResponse<StudentDTO> getStudentWithManualCache(@PathVariable Long id) {
        log.info("🔵 API: Fetching student with manual cache - ID: {}", id);
        return manualCacheService.getStudentById(id);
    }

    /**
     * Get all students with manual cache.
     * 
     * PERFORMANCE:
     * - Cache Hit: 1-5ms (Redis)
     * - Cache Miss: 50-200ms (Database + Redis)
     */
    @Operation(summary = "Get all students with manual cache", description = "Fetches all students using manual cache-aside pattern")
    @GetMapping("/manual-cache/all")
    public ApiResponse<List<StudentDTO>> getAllStudentsWithManualCache() {
        log.info("🔵 API: Fetching all students with manual cache");
        return manualCacheService.getAllStudents();
    }

    /**
     * Evict student cache manually.
     * 
     * DEMONSTRATES:
     * - Manual cache invalidation
     * - Cache eviction
     * - Cache management
     */
    @Operation(summary = "Evict student manual cache", description = "Removes student from manual cache")
    @DeleteMapping("/manual-cache/{id}")
    public ApiResponse<String> evictManualStudentCache(@PathVariable Long id) {
        log.info("🔵 API: Evicting manual cache for student - ID: {}", id);
        return manualCacheService.evictStudentCache(id);
    }

    /**
     * Evict all students cache manually.
     */
    @Operation(summary = "Evict all manual cache", description = "Removes all students from manual cache")
    @DeleteMapping("/manual-cache/all")
    public ApiResponse<String> evictAllManualCache() {
        log.info("🔵 API: Evicting all manual cache");
        return manualCacheService.evictAllStudentsCache();
    }

    // ==================== SPRING CACHE ENDPOINTS ====================

    /**
     * Get student by ID with Spring Cache.
     * 
     * DEMONSTRATES:
     * - @Cacheable annotation
     * - Declarative caching
     * - Spring Cache abstraction
     * 
     * PERFORMANCE:
     * - Cache Hit: 1-2ms (Redis)
     * - Cache Miss: 10-50ms (Database + Redis)
     */
    @Operation(summary = "Get student with Spring Cache", description = "Fetches student using @Cacheable annotation")
    @GetMapping("/spring-cache/{id}")
    public ApiResponse<StudentDTO> getStudentWithSpringCache(@PathVariable Long id) {
        log.info("🟢 API: Fetching student with Spring Cache - ID: {}", id);
        return springCacheService.getStudentById(id);
    }

    /**
     * Get student by ID with conditional caching.
     * 
     * DEMONSTRATES:
     * - @Cacheable with condition
     * - Conditional caching
     * - SpEL expressions
     */
    @Operation(summary = "Get student with conditional cache", description = "Fetches student with conditional caching (id > 10)")
    @GetMapping("/spring-cache/conditional/{id}")
    public ApiResponse<StudentDTO> getStudentWithConditionalCache(@PathVariable Long id) {
        log.info("🟢 API: Fetching student with conditional cache - ID: {}", id);
        return springCacheService.getStudentByIdWithCondition(id);
    }

    /**
     * Get all students with Spring Cache.
     * 
     * PERFORMANCE:
     * - Cache Hit: 1-5ms (Redis)
     * - Cache Miss: 50-200ms (Database + Redis)
     */
    @Operation(summary = "Get all students with Spring Cache", description = "Fetches all students using @Cacheable annotation")
    @GetMapping("/spring-cache/all")
    public ApiResponse<List<StudentDTO>> getAllStudentsWithSpringCache() {
        log.info("🟢 API: Fetching all students with Spring Cache");
        return springCacheService.getAllStudents();
    }

    /**
     * Evict student cache with Spring Cache.
     * 
     * DEMONSTRATES:
     * - @CacheEvict annotation
     * - Declarative cache invalidation
     */
    @Operation(summary = "Evict student Spring Cache", description = "Removes student from cache using @CacheEvict")
    @DeleteMapping("/spring-cache/{id}")
    public ApiResponse<String> evictSpringStudentCache(@PathVariable Long id) {
        log.info("🟢 API: Evicting Spring Cache for student - ID: {}", id);
        return springCacheService.evictStudentCache(id);
    }

    /**
     * Evict all students cache with Spring Cache.
     */
    @Operation(summary = "Evict all Spring Cache", description = "Removes all students from cache using @CacheEvict")
    @DeleteMapping("/spring-cache/all")
    public ApiResponse<String> evictAllSpringCache() {
        log.info("🟢 API: Evicting all Spring Cache");
        return springCacheService.evictAllStudentCache();
    }

    // ==================== CACHE STATISTICS ENDPOINTS ====================

    /**
     * Get current cache statistics.
     * 
     * DEMONSTRATES:
     * - Cache hit/miss ratios
     * - Performance metrics
     * - Cache effectiveness
     */
    @Operation(summary = "Get cache statistics", description = "Returns current cache statistics and performance metrics")
    @GetMapping("/statistics")
    public ApiResponse<CacheStatisticsService.CacheStatistics> getCacheStatistics() {
        log.info("📊 API: Fetching cache statistics");
        CacheStatisticsService.CacheStatistics stats = cacheStatisticsService.getStatistics();
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime("0 ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("MEMORY")
                .timestamp(System.currentTimeMillis())
                .build();
        
        return ApiResponse.success(stats, metadata, "Cache statistics retrieved");
    }

    /**
     * Reset cache statistics.
     */
    @Operation(summary = "Reset cache statistics", description = "Resets all cache statistics to zero")
    @PostMapping("/statistics/reset")
    public ApiResponse<String> resetCacheStatistics() {
        log.info("📊 API: Resetting cache statistics");
        cacheStatisticsService.resetStatistics();
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime("0 ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("MEMORY")
                .timestamp(System.currentTimeMillis())
                .build();
        
        return ApiResponse.success("Statistics reset", metadata, "Cache statistics reset successfully");
    }

    /**
     * Get cache statistics by cache type.
     */
    @Operation(summary = "Get cache statistics by type", description = "Returns cache statistics for a specific cache type")
    @GetMapping("/statistics/{cacheType}")
    public ApiResponse<CacheStatisticsService.CacheTypeStats> getCacheStatisticsByType(@PathVariable String cacheType) {
        log.info("📊 API: Fetching cache statistics for type: {}", cacheType);
        CacheStatisticsService.CacheTypeStats stats = cacheStatisticsService.getCacheTypeStatistics(cacheType);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime("0 ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("MEMORY")
                .timestamp(System.currentTimeMillis())
                .build();
        
        return ApiResponse.success(stats, metadata, "Cache statistics for " + cacheType + " retrieved");
    }

    // ==================== REDIS OPERATIONS ENDPOINTS ====================

    /**
     * Store a string value in Redis.
     */
    @Operation(summary = "Set string value", description = "Store a string value in Redis")
    @PostMapping("/string")
    public ApiResponse<String> setString(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        
        String key = request.get("key");
        String value = request.get("value");
        
        log.info("POST /api/cache-playground/string - SET key={}, value={}", key, value);
        
        redisOperationsService.setString(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success("String stored successfully", responseTime);
    }

    /**
     * Get a string value from Redis.
     */
    @Operation(summary = "Get string value", description = "Get a string value from Redis")
    @GetMapping("/string/{key}")
    public ApiResponse<String> getString(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/cache-playground/string/{} - GET", key);
        
        String value = redisOperationsService.getString(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(value, responseTime);
    }

    /**
     * Store a field in a Redis hash.
     */
    @Operation(summary = "Set hash field", description = "Store a field in a Redis hash")
    @PostMapping("/hash")
    public ApiResponse<String> setHashField(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        String field = (String) request.get("field");
        Object value = request.get("value");
        
        log.info("POST /api/cache-playground/hash - HSET key={}, field={}", key, field);
        
        redisOperationsService.setHashField(key, field, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success("Hash field stored successfully", responseTime);
    }

    /**
     * Get all fields from a Redis hash.
     */
    @Operation(summary = "Get hash fields", description = "Get all fields from a Redis hash")
    @GetMapping("/hash/{key}")
    public ApiResponse<Map<Object, Object>> getAllHashFields(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/cache-playground/hash/{} - HGETALL", key);
        
        Map<Object, Object> fields = redisOperationsService.getAllHashFields(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(fields, responseTime);
    }

    /**
     * Add element to left of Redis list.
     */
    @Operation(summary = "Left push to list", description = "Add element to left of Redis list")
    @PostMapping("/list/left")
    public ApiResponse<String> leftPush(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        Object value = request.get("value");
        
        log.info("POST /api/cache-playground/list/left - LPUSH key={}", key);
        
        redisOperationsService.leftPush(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success("Element added to left of list", responseTime);
    }

    /**
     * Add element to right of Redis list.
     */
    @Operation(summary = "Right push to list", description = "Add element to right of Redis list")
    @PostMapping("/list/right")
    public ApiResponse<String> rightPush(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        Object value = request.get("value");
        
        log.info("POST /api/cache-playground/list/right - RPUSH key={}", key);
        
        redisOperationsService.rightPush(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success("Element added to right of list", responseTime);
    }

    /**
     * Get all elements from Redis list.
     */
    @Operation(summary = "Get list elements", description = "Get all elements from Redis list")
    @GetMapping("/list/{key}")
    public ApiResponse<List<Object>> getList(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/cache-playground/list/{} - LRANGE", key);
        
        List<Object> list = redisOperationsService.getList(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(list, responseTime);
    }

    /**
     * Pop element from left of Redis list.
     */
    @Operation(summary = "Left pop from list", description = "Pop element from left of Redis list")
    @DeleteMapping("/list/left/{key}")
    public ApiResponse<Object> leftPop(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("DELETE /api/cache-playground/list/left/{} - LPOP", key);
        
        Object value = redisOperationsService.leftPop(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(value, responseTime);
    }

    /**
     * Pop element from right of Redis list.
     */
    @Operation(summary = "Right pop from list", description = "Pop element from right of Redis list")
    @DeleteMapping("/list/right/{key}")
    public ApiResponse<Object> rightPop(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("DELETE /api/cache-playground/list/right/{} - RPOP", key);
        
        Object value = redisOperationsService.rightPop(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(value, responseTime);
    }

    /**
     * Add element to Redis set.
     */
    @Operation(summary = "Add to set", description = "Add element to Redis set")
    @PostMapping("/set")
    public ApiResponse<String> addToSet(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        Object value = request.get("value");
        
        log.info("POST /api/cache-playground/set - SADD key={}", key);
        
        redisOperationsService.addToSet(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success("Element added to set", responseTime);
    }

    /**
     * Get all members of Redis set.
     */
    @Operation(summary = "Get set members", description = "Get all members of Redis set")
    @GetMapping("/set/{key}")
    public ApiResponse<Set<Object>> getSetMembers(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/cache-playground/set/{} - SMEMBERS", key);
        
        Set<Object> members = redisOperationsService.getSetMembers(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(members, responseTime);
    }

    /**
     * Add element to Redis sorted set with score.
     */
    @Operation(summary = "Add to sorted set", description = "Add element to Redis sorted set with score")
    @PostMapping("/sortedset")
    public ApiResponse<String> addToSortedSet(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        Object value = request.get("value");
        double score = ((Number) request.get("score")).doubleValue();
        
        log.info("POST /api/cache-playground/sortedset - ZADD key={}, score={}", key, score);
        
        redisOperationsService.addToSortedSet(key, value, score);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success("Element added to sorted set", responseTime);
    }

    /**
     * Get all elements from Redis sorted set (ascending).
     */
    @Operation(summary = "Get sorted set", description = "Get all elements from Redis sorted set (ascending)")
    @GetMapping("/sortedset/{key}")
    public ApiResponse<Set<Object>> getSortedSet(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/cache-playground/sortedset/{} - ZRANGE", key);
        
        Set<Object> members = redisOperationsService.getSortedSet(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(members, responseTime);
    }

    /**
     * Get all elements from Redis sorted set (descending).
     */
    @Operation(summary = "Get sorted set reverse", description = "Get all elements from Redis sorted set (descending)")
    @GetMapping("/sortedset/{key}/reverse")
    public ApiResponse<Set<Object>> getSortedSetReverse(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/cache-playground/sortedset/{}/reverse - ZREVRANGE", key);
        
        Set<Object> members = redisOperationsService.getSortedSetReverse(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(members, responseTime);
    }

    /**
     * Increment a counter in Redis.
     */
    @Operation(summary = "Increment counter", description = "Increment a counter in Redis")
    @PostMapping("/counter/increment")
    public ApiResponse<Long> increment(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        
        String key = request.get("key");
        
        log.info("POST /api/cache-playground/counter/increment - INCR key={}", key);
        
        long value = redisOperationsService.increment(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(value, responseTime);
    }

    /**
     * Increment counter by specific amount.
     */
    @Operation(summary = "Increment counter by", description = "Increment counter by specific amount")
    @PostMapping("/counter/incrementby")
    public ApiResponse<Long> incrementBy(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        long delta = ((Number) request.get("delta")).longValue();
        
        log.info("POST /api/cache-playground/counter/incrementby - INCRBY key={}, delta={}", key, delta);
        
        long value = redisOperationsService.incrementBy(key, delta);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(value, responseTime);
    }

    /**
     * Decrement a counter in Redis.
     */
    @Operation(summary = "Decrement counter", description = "Decrement a counter in Redis")
    @PostMapping("/counter/decrement")
    public ApiResponse<Long> decrement(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        
        String key = request.get("key");
        
        log.info("POST /api/cache-playground/counter/decrement - DECR key={}", key);
        
        long value = redisOperationsService.decrement(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ApiResponse.success(value, responseTime);
    }
}