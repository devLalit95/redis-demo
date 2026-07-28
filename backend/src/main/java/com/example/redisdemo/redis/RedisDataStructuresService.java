package com.example.redisdemo.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Redis Data Structures Demo Service
 * 
 * This service provides demo APIs for each Redis data structure with visualization support.
 * This is Phase 10 of the project - Redis Data Structures Demo.
 * 
 * WHY this service exists:
 * - Teaches each Redis data structure in isolation
 * - Provides visual representation of data structures
 * - Shows CRUD operations for each structure
 * - Enables hands-on learning of Redis data types
 * 
 * WHEN to use this service:
 * - Phase 10: Learning Redis data structures
 * - Understanding data structure behavior
 * - Choosing right data structure for use case
 * - Debugging data structure usage
 * 
 * PRODUCTION USE CASES:
 * - Data structure selection guidance
 * - Debugging complex data structures
 * - Teaching Redis concepts
 * - API documentation examples
 * 
 * DATA STRUCTURES COVERED:
 * - Strings: Simple key-value pairs
 * - Hashes: Field-value pairs
 * - Lists: Ordered collections
 * - Sets: Unordered unique collections
 * - Sorted Sets: Ordered unique collections with scores
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisDataStructuresService {

    private final RedisTemplate<String, Object> redisTemplate;

    // ==================== STRINGS DEMO ====================

    /**
     * String demo: Create, Read, Update, Delete.
     * 
     * @param key The string key
     * @param value The string value
     * @return Operation result
     */
    public Map<String, Object> stringDemo(String key, String value) {
        log.info("String Demo: key={}, value={}", key, value);
        
        Map<String, Object> result = new HashMap<>();
        
        // Create
        redisTemplate.opsForValue().set(key, value);
        result.put("created", Map.of("key", key, "value", value));
        
        // Read
        String readValue = (String) redisTemplate.opsForValue().get(key);
        result.put("read", readValue);
        
        // Update
        String updatedValue = value + " (updated)";
        redisTemplate.opsForValue().set(key, updatedValue);
        result.put("updated", Map.of("key", key, "value", updatedValue));
        
        // Delete
        redisTemplate.delete(key);
        result.put("deleted", Map.of("key", key));
        
        log.info("String demo completed");
        return result;
    }

    /**
     * Get all string keys.
     * 
     * @return List of string keys
     */
    public Set<String> getStringKeys() {
        Set<String> keys = redisTemplate.keys("string:*");
        return keys != null ? keys : new HashSet<>();
    }

    // ==================== HASHES DEMO ====================

    /**
     * Hash demo: Create, Read, Update, Delete.
     * 
     * @param key The hash key
     * @param field The field name
     * @param value The field value
     * @return Operation result
     */
    public Map<String, Object> hashDemo(String key, String field, Object value) {
        log.info("Hash Demo: key={}, field={}, value={}", key, field, value);
        
        Map<String, Object> result = new HashMap<>();
        
        // Create
        redisTemplate.opsForHash().put(key, field, value);
        result.put("created", Map.of("key", key, "field", field, "value", value));
        
        // Read
        Object readValue = redisTemplate.opsForHash().get(key, field);
        result.put("read", readValue);
        
        // Update
        Object updatedValue = value + " (updated)";
        redisTemplate.opsForHash().put(key, field, updatedValue);
        result.put("updated", Map.of("key", key, "field", field, "value", updatedValue));
        
        // Delete field
        redisTemplate.opsForHash().delete(key, field);
        result.put("deleted", Map.of("key", key, "field", field));
        
        log.info("Hash demo completed");
        return result;
    }

    /**
     * Get all hash keys.
     * 
     * @return List of hash keys
     */
    public Set<String> getHashKeys() {
        Set<String> keys = redisTemplate.keys("hash:*");
        return keys != null ? keys : new HashSet<>();
    }

    /**
     * Get all fields for a hash.
     * 
     * @param key The hash key
     * @return Map of all fields
     */
    public Map<Object, Object> getHashFields(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // ==================== LISTS DEMO ====================

    /**
     * List demo: Create, Read, Update, Delete.
     * 
     * @param key The list key
     * @param value The value to add
     * @return Operation result
     */
    public Map<String, Object> listDemo(String key, Object value) {
        log.info("List Demo: key={}, value={}", key, value);
        
        Map<String, Object> result = new HashMap<>();
        
        // Create (add to left)
        redisTemplate.opsForList().leftPush(key, value);
        result.put("created", Map.of("key", key, "value", value, "position", "left"));
        
        // Read
        List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
        result.put("read", list);
        
        // Update (add another value)
        Object updatedValue = value + " (new)";
        redisTemplate.opsForList().rightPush(key, updatedValue);
        result.put("updated", Map.of("key", key, "value", updatedValue, "position", "right"));
        
        // Delete (pop from left)
        Object poppedValue = redisTemplate.opsForList().leftPop(key);
        result.put("deleted", Map.of("key", key, "poppedValue", poppedValue));
        
        log.info("List demo completed");
        return result;
    }

    /**
     * Get all list keys.
     * 
     * @return List of list keys
     */
    public Set<String> getListKeys() {
        Set<String> keys = redisTemplate.keys("list:*");
        return keys != null ? keys : new HashSet<>();
    }

    /**
     * Get list elements.
     * 
     * @param key The list key
     * @return List of elements
     */
    public List<Object> getListElements(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    // ==================== SETS DEMO ====================

    /**
     * Set demo: Create, Read, Update, Delete.
     * 
     * @param key The set key
     * @param value The value to add
     * @return Operation result
     */
    public Map<String, Object> setDemo(String key, Object value) {
        log.info("Set Demo: key={}, value={}", key, value);
        
        Map<String, Object> result = new HashMap<>();
        
        // Create
        redisTemplate.opsForSet().add(key, value);
        result.put("created", Map.of("key", key, "value", value));
        
        // Read
        Set<Object> members = redisTemplate.opsForSet().members(key);
        result.put("read", members);
        
        // Update (add another value)
        Object updatedValue = value + " (new)";
        redisTemplate.opsForSet().add(key, updatedValue);
        result.put("updated", Map.of("key", key, "value", updatedValue));
        
        // Delete
        redisTemplate.opsForSet().remove(key, value);
        result.put("deleted", Map.of("key", key, "value", value));
        
        log.info("Set demo completed");
        return result;
    }

    /**
     * Get all set keys.
     * 
     * @return List of set keys
     */
    public Set<String> getSetKeys() {
        Set<String> keys = redisTemplate.keys("set:*");
        return keys != null ? keys : new HashSet<>();
    }

    /**
     * Get set members.
     * 
     * @param key The set key
     * @return Set of members
     */
    public Set<Object> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // ==================== SORTED SETS DEMO ====================

    /**
     * Sorted Set demo: Create, Read, Update, Delete.
     * 
     * @param key The sorted set key
     * @param value The value to add
     * @param score The score
     * @return Operation result
     */
    public Map<String, Object> sortedSetDemo(String key, Object value, double score) {
        log.info("Sorted Set Demo: key={}, value={}, score={}", key, value, score);
        
        Map<String, Object> result = new HashMap<>();
        
        // Create
        redisTemplate.opsForZSet().add(key, value, score);
        result.put("created", Map.of("key", key, "value", value, "score", score));
        
        // Read
        Set<Object> members = redisTemplate.opsForZSet().range(key, 0, -1);
        result.put("read", members);
        
        // Update (update score)
        double updatedScore = score + 1.0;
        redisTemplate.opsForZSet().add(key, value, updatedScore);
        result.put("updated", Map.of("key", key, "value", value, "newScore", updatedScore));
        
        // Delete
        redisTemplate.opsForZSet().remove(key, value);
        result.put("deleted", Map.of("key", key, "value", value));
        
        log.info("Sorted Set demo completed");
        return result;
    }

    /**
     * Get all sorted set keys.
     * 
     * @return List of sorted set keys
     */
    public Set<String> getSortedSetKeys() {
        Set<String> keys = redisTemplate.keys("sortedset:*");
        return keys != null ? keys : new HashSet<>();
    }

    /**
     * Get sorted set members with scores.
     * 
     * @param key The sorted set key
     * @return Set of members
     */
    public Set<Object> getSortedSetMembers(String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }

    /**
     * Get sorted set members with scores (with scores).
     * 
     * @param key The sorted set key
     * @return Set of members with scores
     */
    public Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<Object>> getSortedSetMembersWithScores(String key) {
        return redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
    }

    /**
     * Get data structures explanation.
     * 
     * @return Explanation of all data structures
     */
    public String getDataStructuresExplanation() {
        return """
            Redis Data Structures:
            
            1. STRINGS:
            - Simple key-value pairs
            - Binary safe
            - Max size: 512MB
            - Use cases: Caching, counters, session storage
            
            2. HASHES:
            - Field-value pairs within a key
            - Maps to objects in other languages
            - Max fields: ~4 billion
            - Use cases: Object storage, user profiles
            
            3. LISTS:
            - Ordered collections of strings
            - Maintains insertion order
            - Max size: ~4 billion
            - Use cases: Queues, timelines, activity feeds
            
            4. SETS:
            - Unordered unique collections
            - No duplicates allowed
            - Max size: ~4 billion
            - Use cases: Tags, unique visitors, follows
            
            5. SORTED SETS:
            - Ordered unique collections with scores
            - Sorted by score
            - Max size: ~4 billion
            - Use cases: Leaderboards, rankings, priority queues
            
            CHOOSING THE RIGHT STRUCTURE:
            - Strings: Simple values, counters, caching
            - Hashes: Objects with multiple fields
            - Lists: Ordered data, queues, stacks
            - Sets: Unique collections, tags
            - Sorted Sets: Rankings, leaderboards, scores
            
            PERFORMANCE CONSIDERATIONS:
            - Strings: O(1) for all operations
            - Hashes: O(1) for field operations
            - Lists: O(1) for ends, O(N) for middle
            - Sets: O(1) for all operations
            - Sorted Sets: O(log(N)) for most operations
            """;
    }
}
