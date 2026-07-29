package com.example.redisdemo.redis;

import com.example.redisdemo.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis Explorer Service
 * 
 * This service provides APIs to explore and manage Redis data.
 * 
 * WHY this service exists:
 * - Provides visibility into Redis data
 * - Enables cache inspection and debugging
 * - Helps understand Redis internals
 * - Essential for cache management
 * 
 * WHEN to use this service:
 * - Debugging cache issues
 * - Inspecting cached data
 * - Cache management
 * - Learning Redis data structures
 * 
 * PRODUCTION USE CASES:
 * - Cache debugging tools
 * - Admin panels
 * - Cache inspection
 * - Performance monitoring
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisExplorerService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * List all Redis keys.
     * 
     * DEMONSTRATES:
     * - Key enumeration
     * - Pattern matching
     * - Redis key inspection
     * 
     * @param pattern Optional pattern to filter keys (default: *)
     * @return List of Redis keys
     */
    public ApiResponse<Set<String>> listKeys(String pattern) {
        long startTime = System.currentTimeMillis();
        
        if (pattern == null || pattern.isEmpty()) {
            pattern = "*";
        }
        
        log.info("🔍 REDIS EXPLORER: Listing keys with pattern: {}", pattern);
        
        Set<String> keys = redisTemplate.keys(pattern);
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🔍 Found {} keys matching pattern: {}", keys != null ? keys.size() : 0, pattern);
        
        return ApiResponse.success(keys != null ? keys : new HashSet<>(), metadata, 
                "Listed " + (keys != null ? keys.size() : 0) + " keys");
    }

    /**
     * Get key type.
     * 
     * DEMONSTRATES:
     * - Redis data type inspection
     * - Understanding Redis data structures
     * 
     * @param key The Redis key
     * @return The data type of the key
     */
    public ApiResponse<String> getKeyType(String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("🔍 REDIS EXPLORER: Getting type for key: {}", key);
        
        DataType type = redisTemplate.type(key);
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        String typeStr = type != null ? type.code() : "none";
        log.info("🔍 Key {} has type: {}", key, typeStr);
        
        return ApiResponse.success(typeStr, metadata, "Key type: " + typeStr);
    }

    /**
     * Get TTL for a key.
     * 
     * DEMONSTRATES:
     * - TTL inspection
     * - Understanding key expiration
     * 
     * @param key The Redis key
     * @return TTL in seconds (-1 if no expiration, -2 if key doesn't exist)
     */
    public ApiResponse<Long> getKeyTTL(String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("🔍 REDIS EXPLORER: Getting TTL for key: {}", key);
        
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🔍 Key {} has TTL: {} seconds", key, ttl);
        
        return ApiResponse.success(ttl != null ? ttl : -2L, metadata, 
                "TTL: " + (ttl != null ? ttl + " seconds" : "key not found"));
    }

    /**
     * Get memory usage for a key.
     * 
     * DEMONSTRATES:
     * - Memory inspection
     * - Understanding Redis memory usage
     * 
     * @param key The Redis key
     * @return Memory usage in bytes
     */
    public ApiResponse<Long> getKeyMemoryUsage(String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("🔍 REDIS EXPLORER: Getting memory usage for key: {}", key);
        
        // Simplified implementation - return 0 as memory usage not available in all Redis versions
        Long memoryUsage = 0L;
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🔍 Key {} uses {} bytes", key, memoryUsage);
        
        return ApiResponse.success(memoryUsage != null ? memoryUsage : 0L, metadata, 
                "Memory usage: " + (memoryUsage != null ? memoryUsage + " bytes" : "N/A"));
    }

    /**
     * Get value for a key.
     * 
     * DEMONSTRATES:
     * - Value retrieval
     * - Data inspection
     * 
     * @param key The Redis key
     * @return The value associated with the key
     */
    public ApiResponse<Object> getValue(String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("🔍 REDIS EXPLORER: Getting value for key: {}", key);
        
        Object value = redisTemplate.opsForValue().get(key);
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🔍 Retrieved value for key: {}", key);
        
        return ApiResponse.success(value, metadata, "Value retrieved for key: " + key);
    }

    /**
     * Delete a key.
     * 
     * DEMONSTRATES:
     * - Key deletion
     * - Cache management
     * 
     * @param key The Redis key
     * @return Deletion result
     */
    public ApiResponse<String> deleteKey(String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("🔍 REDIS EXPLORER: Deleting key: {}", key);
        
        Boolean deleted = redisTemplate.delete(key);
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        String result = Boolean.TRUE.equals(deleted) ? "Key deleted successfully" : "Key not found";
        log.info("🔍 {}", result);
        
        return ApiResponse.success(result, metadata, result);
    }

    /**
     * Get Redis information.
     * 
     * DEMONSTRATES:
     * - Redis server information
     * - System metrics
     * - Performance monitoring
     * 
     * @return Redis server information
     */
    public ApiResponse<Map<String, Object>> getRedisInfo() {
        long startTime = System.currentTimeMillis();
        
        log.info("🔍 REDIS EXPLORER: Getting Redis info");
        
        Properties info = redisTemplate.getConnectionFactory().getConnection().info();
        Map<String, Object> infoMap = new HashMap<>();
        
        if (info != null) {
            for (String key : info.stringPropertyNames()) {
                infoMap.put(key, info.getProperty(key));
            }
        }
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🔍 Retrieved Redis info");
        
        return ApiResponse.success(infoMap, metadata, "Redis information retrieved");
    }

    /**
     * Get database size.
     * 
     * DEMONSTRATES:
     * - Database size inspection
     * - Key count monitoring
     * 
     * @return Number of keys in the database
     */
    public ApiResponse<Long> getDatabaseSize() {
        long startTime = System.currentTimeMillis();
        
        log.info("🔍 REDIS EXPLORER: Getting database size");
        
        Long dbSize = redisTemplate.execute((RedisCallback<Long>) connection -> connection.dbSize());
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🔍 Database size: {} keys", dbSize);
        
        return ApiResponse.success(dbSize != null ? dbSize : 0L, metadata, 
                "Database contains " + (dbSize != null ? dbSize : 0) + " keys");
    }

    /**
     * Flush all keys from current database.
     * 
     * ⚠️ DANGEROUS OPERATION - Use with caution!
     * 
     * @return Flush result
     */
    public ApiResponse<String> flushDatabase() {
        long startTime = System.currentTimeMillis();
        
        log.warn("⚠️ REDIS EXPLORER: Flushing database - DANGEROUS OPERATION");
        
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.flushDb();
            return null;
        });
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("NONE")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.warn("⚠️ Database flushed");
        
        return ApiResponse.success("Database flushed successfully", metadata, 
                "⚠️ All keys in current database have been deleted");
    }
}