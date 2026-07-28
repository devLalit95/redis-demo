package com.example.redisdemo.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis Operations Service
 * 
 * This service demonstrates all basic Redis operations and data structures.
 * This is Phase 2 of the project - Redis Introduction.
 * 
 * WHY this service exists:
 * - Teaches Redis basics through practical implementation
 * - Demonstrates all Redis data structures
 * - Shows how to use RedisTemplate for Redis operations
 * - Provides a foundation for understanding caching concepts
 * - Each method teaches one Redis concept
 * 
 * WHEN to use this service:
 * - Phase 2: Learning Redis basics
 * - Phase 3: Understanding how manual caching works internally
 * - All phases: Understanding Redis operations for debugging
 * 
 * PRODUCTION USE CASES:
 * - Session storage
 * - Temporary data storage
 * - Rate limiting
 * - Leaderboards
 * - Pub/Sub messaging
 * 
 * REDIS DATA STRUCTURES COVERED:
 * - Strings: Basic key-value storage
 * - Hashes: Field-value pairs within a key
 * - Lists: Ordered collections of strings
 * - Sets: Unordered unique collections
 * - Sorted Sets: Ordered unique collections with scores
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisOperationsService {

    private final RedisTemplate<String, Object> redisTemplate;

    // ==================== STRING OPERATIONS ====================

    /**
     * Store a string value in Redis.
     * 
     * WHY this method exists:
     * - Teaches basic string storage in Redis
     * - Most fundamental Redis operation
     * - Foundation for understanding caching
     * 
     * WHEN to use strings:
     * - Simple key-value storage
     * - Counters
     * - Session tokens
     * - Configuration values
     * 
     * PRODUCTION USE CASES:
     * - Session storage
     * - API rate limiting counters
     * - Feature flags
     * - Temporary locks
     * 
     * @param key The key to store
     * @param value The string value to store
     */
    public void setString(String key, String value) {
        log.info("Redis SET: key={}, value={}", key, value);
        redisTemplate.opsForValue().set(key, value);
        log.info("String stored successfully");
    }

    /**
     * Get a string value from Redis.
     * 
     * WHY this method exists:
     * - Teaches basic string retrieval
     * - Most common Redis operation
     * - Core operation for caching
     * 
     * WHEN to use this method:
     * - Retrieving cached values
     * - Getting session data
     * - Reading configuration
     * 
     * @param key The key to retrieve
     * @return The string value, or null if not found
     */
    public String getString(String key) {
        log.info("Redis GET: key={}", key);
        Object value = redisTemplate.opsForValue().get(key);
        log.info("Retrieved value: {}", value);
        return value != null ? value.toString() : null;
    }

    /**
     * Store a JSON object in Redis as a string.
     * 
     * WHY this method exists:
     * - Teaches how to store complex objects in Redis
     * - Shows serialization concepts
     * - Foundation for caching entities
     * 
     * WHEN to use JSON storage:
     * - Caching complex objects
     * - Storing API responses
     * - Session data with multiple fields
     * 
     * PRODUCTION USE CASES:
     * - Caching API responses
     * - Storing user sessions
     * - Configuration objects
     * 
     * @param key The key to store
     * @param value The object to store (will be serialized as JSON)
     */
    public void setJson(String key, Object value) {
        log.info("Redis SET JSON: key={}, value={}", key, value);
        redisTemplate.opsForValue().set(key, value);
        log.info("JSON object stored successfully");
    }

    /**
     * Get a JSON object from Redis.
     * 
     * WHY this method exists:
     * - Teaches object deserialization
     * - Shows how to retrieve complex objects
     * - Core operation for entity caching
     * 
     * WHEN to use this method:
     * - Retrieving cached entities
     * - Getting session objects
     * - Reading configuration objects
     * 
     * @param key The key to retrieve
     * @param clazz The class type to deserialize to
     * @return The deserialized object, or null if not found
     */
    public <T> T getJson(String key, Class<T> clazz) {
        log.info("Redis GET JSON: key={}, class={}", key, clazz.getSimpleName());
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null && clazz.isInstance(value)) {
            log.info("JSON object retrieved successfully");
            return clazz.cast(value);
        }
        log.info("JSON object not found or type mismatch");
        return null;
    }

    // ==================== HASH OPERATIONS ====================

    /**
     * Store a field-value pair in a Redis hash.
     * 
     * WHY this method exists:
     * - Teaches hash data structure
     * - Shows how to store related fields together
     * - More efficient than multiple string keys
     * 
     * WHEN to use hashes:
     * - Storing objects with multiple fields
     * - User profiles
     * - Product details
     * - Session data
     * 
     * PRODUCTION USE CASES:
     * - User profile storage
     * - Shopping cart items
     * - Product catalog
     * 
     * @param key The hash key
     * @param field The field name
     * @param value The field value
     */
    public void setHashField(String key, String field, Object value) {
        log.info("Redis HSET: key={}, field={}, value={}", key, field, value);
        redisTemplate.opsForHash().put(key, field, value);
        log.info("Hash field stored successfully");
    }

    /**
     * Get a field value from a Redis hash.
     * 
     * WHY this method exists:
     * - Teaches hash field retrieval
     * - Shows how to get specific fields
     * - More efficient than retrieving entire object
     * 
     * WHEN to use this method:
     * - Getting specific user fields
     * - Retrieving product attributes
     * - Reading session fields
     * 
     * @param key The hash key
     * @param field The field name
     * @return The field value, or null if not found
     */
    public Object getHashField(String key, String field) {
        log.info("Redis HGET: key={}, field={}", key, field);
        Object value = redisTemplate.opsForHash().get(key, field);
        log.info("Retrieved hash field value: {}", value);
        return value;
    }

    /**
     * Get all fields and values from a Redis hash.
     * 
     * WHY this method exists:
     * - Teaches complete hash retrieval
     * - Shows how to get entire object
     * - Useful for object reconstruction
     * 
     * WHEN to use this method:
     * - Retrieving complete user profiles
     * - Getting full product details
     * - Reading entire session
     * 
     * @param key The hash key
     * @return Map of all field-value pairs
     */
    public Map<Object, Object> getAllHashFields(String key) {
        log.info("Redis HGETALL: key={}", key);
        Map<Object, Object> fields = redisTemplate.opsForHash().entries(key);
        log.info("Retrieved {} hash fields", fields.size());
        return fields;
    }

    /**
     * Delete a field from a Redis hash.
     * 
     * WHY this method exists:
     * - Teaches hash field deletion
     * - Shows how to remove specific fields
     * - Useful for partial updates
     * 
     * WHEN to use this method:
     * - Removing user attributes
     * - Deleting product fields
     * - Clearing session fields
     * 
     * @param key The hash key
     * @param field The field to delete
     */
    public void deleteHashField(String key, String field) {
        log.info("Redis HDEL: key={}, field={}", key, field);
        redisTemplate.opsForHash().delete(key, field);
        log.info("Hash field deleted successfully");
    }

    // ==================== LIST OPERATIONS ====================

    /**
     * Add an element to the left (head) of a Redis list.
     * 
     * WHY this method exists:
     * - Teaches list operations
     * - Shows how to add to head of list
     * - Useful for stacks and queues
     * 
     * WHEN to use LPUSH:
     * - Implementing stacks
     * - Recent activity feeds
     * - Log storage
     * 
     * PRODUCTION USE CASES:
     * - Activity feeds
     * - Recent logs
     * - Message queues
     * 
     * @param key The list key
     * @param value The value to add
     */
    public void leftPush(String key, Object value) {
        log.info("Redis LPUSH: key={}, value={}", key, value);
        redisTemplate.opsForList().leftPush(key, value);
        log.info("Element added to left of list");
    }

    /**
     * Add an element to the right (tail) of a Redis list.
     * 
     * WHY this method exists:
     * - Teaches list operations
     * - Shows how to add to tail of list
     * - Useful for queues
     * 
     * WHEN to use RPUSH:
     * - Implementing queues
     * - Task lists
     * - Event streams
     * 
     * PRODUCTION USE CASES:
     * - Task queues
     * - Event streams
     * - Message queues
     * 
     * @param key The list key
     * @param value The value to add
     */
    public void rightPush(String key, Object value) {
        log.info("Redis RPUSH: key={}, value={}", key, value);
        redisTemplate.opsForList().rightPush(key, value);
        log.info("Element added to right of list");
    }

    /**
     * Get all elements from a Redis list.
     * 
     * WHY this method exists:
     * - Teaches list retrieval
     * - Shows how to get entire list
     * - Useful for batch processing
     * 
     * WHEN to use this method:
     * - Retrieving activity feeds
     * - Getting task lists
     * - Reading event streams
     * 
     * @param key The list key
     * @return List of all elements
     */
    public List<Object> getList(String key) {
        log.info("Redis LRANGE: key={}", key);
        List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
        log.info("Retrieved {} elements from list", list != null ? list.size() : 0);
        return list;
    }

    /**
     * Remove and return the first element from a Redis list.
     * 
     * WHY this method exists:
     * - Teaches list pop operation
     * - Shows how to implement queue
     * - Useful for task processing
     * 
     * WHEN to use LPOP:
     * - Processing task queues
     * - Implementing FIFO queues
     * - Processing messages
     * 
     * PRODUCTION USE CASES:
     * - Task processing
     * - Message queues
     * - Job scheduling
     * 
     * @param key The list key
     * @return The popped element, or null if list is empty
     */
    public Object leftPop(String key) {
        log.info("Redis LPOP: key={}", key);
        Object value = redisTemplate.opsForList().leftPop(key);
        log.info("Popped element from left: {}", value);
        return value;
    }

    /**
     * Remove and return the last element from a Redis list.
     * 
     * WHY this method exists:
     * - Teaches list pop operation
     * - Shows how to implement stack
     * - Useful for LIFO processing
     * 
     * WHEN to use RPOP:
     * - Implementing stacks
     * - LIFO processing
     * - Recent item processing
     * 
     * PRODUCTION USE CASES:
     * - Stack operations
     * - Recent item processing
     * - Undo operations
     * 
     * @param key The list key
     * @return The popped element, or null if list is empty
     */
    public Object rightPop(String key) {
        log.info("Redis RPOP: key={}", key);
        Object value = redisTemplate.opsForList().rightPop(key);
        log.info("Popped element from right: {}", value);
        return value;
    }

    // ==================== SET OPERATIONS ====================

    /**
     * Add an element to a Redis set.
     * 
     * WHY this method exists:
     * - Teaches set data structure
     * - Shows how to store unique values
     * - Automatic deduplication
     * 
     * WHEN to use sets:
     * - Storing unique values
     * - Tags and categories
     * - User groups
     * - Likes/favorites
     * 
     * PRODUCTION USE CASES:
     * - User groups
     * - Tags
     * - Likes/favorites
     * - Unique visitors
     * 
     * @param key The set key
     * @param value The value to add
     */
    public void addToSet(String key, Object value) {
        log.info("Redis SADD: key={}, value={}", key, value);
        redisTemplate.opsForSet().add(key, value);
        log.info("Element added to set");
    }

    /**
     * Get all members of a Redis set.
     * 
     * WHY this method exists:
     * - Teaches set retrieval
     * - Shows how to get all unique values
     * - Useful for group operations
     * 
     * WHEN to use this method:
     * - Getting group members
     * - Retrieving tags
     * - Getting unique values
     * 
     * @param key The set key
     * @return Set of all members
     */
    public Set<Object> getSetMembers(String key) {
        log.info("Redis SMEMBERS: key={}", key);
        Set<Object> members = redisTemplate.opsForSet().members(key);
        log.info("Retrieved {} set members", members != null ? members.size() : 0);
        return members;
    }

    /**
     * Check if an element is a member of a Redis set.
     * 
     * WHY this method exists:
     * - Teaches set membership test
     * - Shows how to check existence
     * - Useful for validation
     * 
     * WHEN to use this method:
     * - Checking group membership
     * - Validating tags
     * - Checking duplicates
     * 
     * @param key The set key
     * @param value The value to check
     * @return true if member exists, false otherwise
     */
    public boolean isSetMember(String key, Object value) {
        log.info("Redis SISMEMBER: key={}, value={}", key, value);
        boolean isMember = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        log.info("Is member: {}", isMember);
        return isMember;
    }

    /**
     * Remove an element from a Redis set.
     * 
     * WHY this method exists:
     * - Teaches set removal
     * - Shows how to remove members
     * - Useful for group management
     * 
     * WHEN to use this method:
     * - Removing group members
     * - Deleting tags
     * - Managing favorites
     * 
     * @param key The set key
     * @param value The value to remove
     */
    public void removeFromSet(String key, Object value) {
        log.info("Redis SREM: key={}, value={}", key, value);
        redisTemplate.opsForSet().remove(key, value);
        log.info("Element removed from set");
    }

    // ==================== SORTED SET OPERATIONS ====================

    /**
     * Add an element to a Redis sorted set with a score.
     * 
     * WHY this method exists:
     * - Teaches sorted set data structure
     * - Shows how to store ordered data
     * - Automatic sorting by score
     * 
     * WHEN to use sorted sets:
     * - Leaderboards
     * - Rankings
     * - Priority queues
     * - Time-series data
     * 
     * PRODUCTION USE CASES:
     * - Game leaderboards
     * - Product rankings
     * - Priority queues
     * - Time-based events
     * 
     * @param key The sorted set key
     * @param value The value to add
     * @param score The score for ordering
     */
    public void addToSortedSet(String key, Object value, double score) {
        log.info("Redis ZADD: key={}, value={}, score={}", key, value, score);
        redisTemplate.opsForZSet().add(key, value, score);
        log.info("Element added to sorted set");
    }

    /**
     * Get all elements from a Redis sorted set, ordered by score.
     * 
     * WHY this method exists:
     * - Teaches sorted set retrieval
     * - Shows how to get ordered data
     * - Useful for rankings
     * 
     * WHEN to use this method:
     * - Getting leaderboards
     * - Retrieving rankings
     * - Getting top N items
     * 
     * @param key The sorted set key
     * @return Set of elements ordered by score (ascending)
     */
    public Set<Object> getSortedSet(String key) {
        log.info("Redis ZRANGE: key={}", key);
        Set<Object> members = redisTemplate.opsForZSet().range(key, 0, -1);
        log.info("Retrieved {} sorted set members", members != null ? members.size() : 0);
        return members;
    }

    /**
     * Get elements from a Redis sorted set in reverse order (highest score first).
     * 
     * WHY this method exists:
     * - Teaches reverse sorted set retrieval
     * - Shows how to get descending order
     * - Useful for top rankings
     * 
     * WHEN to use this method:
     * - Getting top leaderboards
     * - Retrieving highest ranked items
     * - Getting top N by score
     * 
     * @param key The sorted set key
     * @return Set of elements ordered by score (descending)
     */
    public Set<Object> getSortedSetReverse(String key) {
        log.info("Redis ZREVRANGE: key={}", key);
        Set<Object> members = redisTemplate.opsForZSet().reverseRange(key, 0, -1);
        log.info("Retrieved {} sorted set members in reverse", members != null ? members.size() : 0);
        return members;
    }

    /**
     * Get the rank of an element in a Redis sorted set.
     * 
     * WHY this method exists:
     * - Teaches rank retrieval
     * - Shows how to get position
     * - Useful for leaderboard positions
     * 
     * WHEN to use this method:
     * - Getting user rank
     * - Finding item position
     * - Determining ranking
     * 
     * @param key The sorted set key
     * @param value The value to rank
     * @return The rank (0-based), or null if not found
     */
    public Long getSortedSetRank(String key, Object value) {
        log.info("Redis ZRANK: key={}, value={}", key, value);
        Long rank = redisTemplate.opsForZSet().rank(key, value);
        log.info("Rank: {}", rank);
        return rank;
    }

    // ==================== COUNTER OPERATIONS ====================

    /**
     * Increment a counter in Redis.
     * 
     * WHY this method exists:
     * - Teaches atomic increment operations
     * - Shows thread-safe counting
     * - Foundation for rate limiting
     * 
     * WHEN to use increment:
     * - Counters
     * - Rate limiting
     * - ID generation
     * - Statistics
     * 
     * PRODUCTION USE CASES:
     * - API rate limiting
     * - View counters
     * - ID generation
     * - Statistics tracking
     * 
     * @param key The counter key
     * @return The incremented value
     */
    public long increment(String key) {
        log.info("Redis INCR: key={}", key);
        long value = redisTemplate.opsForValue().increment(key);
        log.info("Incremented value: {}", value);
        return value;
    }

    /**
     * Increment a counter by a specific amount in Redis.
     * 
     * WHY this method exists:
     * - Teaches atomic increment by amount
     * - Shows flexible counting
     * - Useful for weighted counting
     * 
     * WHEN to use incrementBy:
     * - Adding points
     * - Weighted counters
     * - Batch increments
     * 
     * @param key The counter key
     * @param delta The amount to increment by
     * @return The incremented value
     */
    public long incrementBy(String key, long delta) {
        log.info("Redis INCRBY: key={}, delta={}", key, delta);
        long value = redisTemplate.opsForValue().increment(key, delta);
        log.info("Incremented value: {}", value);
        return value;
    }

    /**
     * Decrement a counter in Redis.
     * 
     * WHY this method exists:
     * - Teaches atomic decrement operations
     * - Shows thread-safe counting
     * - Useful for inventory management
     * 
     * WHEN to use decrement:
     * - Inventory counting
     * - Resource counting
     * - Quota management
     * 
     * PRODUCTION USE CASES:
     * - Inventory management
     * - Resource counting
     * - Quota tracking
     * 
     * @param key The counter key
     * @return The decremented value
     */
    public long decrement(String key) {
        log.info("Redis DECR: key={}", key);
        long value = redisTemplate.opsForValue().decrement(key);
        log.info("Decremented value: {}", value);
        return value;
    }

    // ==================== KEY OPERATIONS ====================

    /**
     * Set a time-to-live (TTL) for a key in Redis.
     * 
     * WHY this method exists:
     * - Teaches TTL concepts
     * - Shows automatic expiration
     * - Foundation for cache invalidation
     * 
     * WHEN to use TTL:
     * - Cache expiration
     * - Session timeout
     * - Temporary data
     * - Rate limiting windows
     * 
     * PRODUCTION USE CASES:
     * - Cache invalidation
     * - Session management
     * - Rate limiting
     * - Temporary locks
     * 
     * @param key The key to set TTL on
     * @param timeout The timeout value
     * @param unit The time unit
     */
    public void setExpire(String key, long timeout, TimeUnit unit) {
        log.info("Redis EXPIRE: key={}, timeout={}, unit={}", key, timeout, unit);
        redisTemplate.expire(key, timeout, unit);
        log.info("TTL set successfully");
    }

    /**
     * Get the remaining time-to-live (TTL) of a key.
     * 
     * WHY this method exists:
     * - Teaches TTL monitoring
     * - Shows how to check expiration
     * - Useful for cache monitoring
     * 
     * WHEN to use this method:
     * - Monitoring cache expiration
     * - Checking session validity
     * - Debugging TTL issues
     * 
     * @param key The key to check
     * @return Remaining TTL in seconds, -2 if key doesn't exist, -1 if key has no expiration
     */
    public long getTtl(String key) {
        log.info("Redis TTL: key={}", key);
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        log.info("TTL: {} seconds", ttl);
        return ttl != null ? ttl : -2;
    }

    /**
     * Delete a key from Redis.
     * 
     * WHY this method exists:
     * - Teaches key deletion
     * - Shows how to remove data
     * - Foundation for cache invalidation
     * 
     * WHEN to use delete:
     * - Cache invalidation
     * - Data cleanup
     * - Session removal
     * 
     * PRODUCTION USE CASES:
     * - Cache invalidation
     * - Session cleanup
     * - Data removal
     * 
     * @param key The key to delete
     * @return true if key was deleted, false otherwise
     */
    public boolean deleteKey(String key) {
        log.info("Redis DEL: key={}", key);
        Boolean deleted = redisTemplate.delete(key);
        log.info("Key deleted: {}", deleted);
        return Boolean.TRUE.equals(deleted);
    }

    /**
     * Check if a key exists in Redis.
     * 
     * WHY this method exists:
     * - Teaches key existence check
     * - Shows how to verify data
     * - Useful for cache hit detection
     * 
     * WHEN to use this method:
     * - Checking cache existence
     * - Validating data
     * - Conditional operations
     * 
     * @param key The key to check
     * @return true if key exists, false otherwise
     */
    public boolean keyExists(String key) {
        log.info("Redis EXISTS: key={}", key);
        Boolean exists = redisTemplate.hasKey(key);
        log.info("Key exists: {}", exists);
        return Boolean.TRUE.equals(exists);
    }

    /**
     * Rename a key in Redis.
     * 
     * WHY this method exists:
     * - Teaches key renaming
     * - Shows how to move data
     * - Useful for key management
     * 
     * WHEN to use rename:
     * - Key migration
     * - Data reorganization
     * - Key naming updates
     * 
     * @param oldKey The current key name
     * @param newKey The new key name
     */
    public void renameKey(String oldKey, String newKey) {
        log.info("Redis RENAME: oldKey={}, newKey={}", oldKey, newKey);
        redisTemplate.rename(oldKey, newKey);
        log.info("Key renamed successfully");
    }

    /**
     * Get all keys matching a pattern in Redis.
     * 
     * WHY this method exists:
     * - Teaches pattern matching
     * - Shows how to search keys
     * - Useful for key discovery
     * 
     * WHEN to use this method:
     * - Finding cache keys
     * - Key discovery
     * - Bulk operations
     * 
     * CAUTION: Use with care in production with large datasets
     * 
     * @param pattern The key pattern (e.g., "user:*")
     * @return Set of matching keys
     */
    public Set<String> getKeysByPattern(String pattern) {
        log.info("Redis KEYS: pattern={}", pattern);
        Set<String> keys = redisTemplate.keys(pattern);
        log.info("Found {} keys matching pattern", keys != null ? keys.size() : 0);
        return keys;
    }

    /**
     * Get the data type of a key.
     * 
     * WHY this method exists:
     * - Teaches type checking
     * - Shows how to identify data types
     * - Useful for debugging
     * 
     * WHEN to use this method:
     * - Debugging Redis data
     * - Type validation
     * - Data inspection
     * 
     * @param key The key to check
     * @return The data type as a string
     */
    public String getKeyType(String key) {
        log.info("Redis TYPE: key={}", key);
        try {
            String type = redisTemplate.type(key).code();
            log.info("Key type: {}", type);
            return type;
        } catch (Exception e) {
            log.info("Key type: none (key doesn't exist)");
            return "none";
        }
    }
}
