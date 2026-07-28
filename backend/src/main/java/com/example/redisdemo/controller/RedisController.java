package com.example.redisdemo.controller;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.redis.RedisOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis Controller
 * 
 * This controller provides REST APIs for all Redis operations.
 * This is Phase 2 of the project - Redis Introduction.
 * Each endpoint demonstrates one Redis concept.
 * 
 * WHY this controller exists:
 * - Provides HTTP interface to Redis operations
 * - Enables learning Redis through practical API calls
 * - Shows real-world usage of Redis data structures
 * - Each endpoint teaches one specific Redis concept
 * 
 * WHEN to use this controller:
 * - Phase 2: Learning Redis basics
 * - Phase 3: Understanding manual caching internals
 * - All phases: Debugging and monitoring Redis operations
 * 
 * PRODUCTION USE CASES:
 * - Redis administration endpoints
 * - Cache management APIs
 * - Debugging tools
 * - Monitoring interfaces
 * 
 * LEARNING APPROACH:
 * - Each endpoint demonstrates one Redis command
 * - Detailed logging shows what happens internally
 * - Response time measurement shows performance
 * - Error handling provides clear feedback
 */
@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
@Slf4j
public class RedisController {

    private final RedisOperationsService redisOperationsService;

    // ==================== STRING OPERATIONS ====================

    /**
     * Store a string value in Redis.
     * 
     * Teaches: Basic SET command
     * 
     * Usage: POST /api/redis/string
     * Body: {"key": "mykey", "value": "myvalue"}
     */
    @PostMapping("/string")
    public ResponseEntity<ApiResponse<String>> setString(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        
        String key = request.get("key");
        String value = request.get("value");
        
        log.info("POST /api/redis/string - SET key={}, value={}", key, value);
        
        redisOperationsService.setString(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("String stored successfully", responseTime));
    }

    /**
     * Get a string value from Redis.
     * 
     * Teaches: Basic GET command
     * 
     * Usage: GET /api/redis/string/{key}
     */
    @GetMapping("/string/{key}")
    public ResponseEntity<ApiResponse<String>> getString(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/string/{} - GET", key);
        
        String value = redisOperationsService.getString(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(value, responseTime));
    }

    /**
     * Store a JSON object in Redis.
     * 
     * Teaches: Storing complex objects as JSON
     * 
     * Usage: POST /api/redis/json
     * Body: {"key": "user:1", "value": {"name": "John", "age": 30}}
     */
    @PostMapping("/json")
    public ResponseEntity<ApiResponse<String>> setJson(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        Object value = request.get("value");
        
        log.info("POST /api/redis/json - SET JSON key={}", key);
        
        redisOperationsService.setJson(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("JSON stored successfully", responseTime));
    }

    /**
     * Get a JSON object from Redis.
     * 
     * Teaches: Retrieving and deserializing JSON objects
     * 
     * Usage: GET /api/redis/json/{key}
     */
    @GetMapping("/json/{key}")
    public ResponseEntity<ApiResponse<Object>> getJson(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/json/{} - GET JSON", key);
        
        Object value = redisOperationsService.getJson(key, Object.class);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(value, responseTime));
    }

    // ==================== HASH OPERATIONS ====================

    /**
     * Store a field in a Redis hash.
     * 
     * Teaches: HSET command for storing field-value pairs
     * 
     * Usage: POST /api/redis/hash
     * Body: {"key": "user:1", "field": "name", "value": "John"}
     */
    @PostMapping("/hash")
    public ResponseEntity<ApiResponse<String>> setHashField(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        String field = (String) request.get("field");
        Object value = request.get("value");
        
        log.info("POST /api/redis/hash - HSET key={}, field={}", key, field);
        
        redisOperationsService.setHashField(key, field, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Hash field stored successfully", responseTime));
    }

    /**
     * Get a field from a Redis hash.
     * 
     * Teaches: HGET command for retrieving specific fields
     * 
     * Usage: GET /api/redis/hash/{key}/{field}
     */
    @GetMapping("/hash/{key}/{field}")
    public ResponseEntity<ApiResponse<Object>> getHashField(@PathVariable String key, @PathVariable String field) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/hash/{}/{} - HGET", key, field);
        
        Object value = redisOperationsService.getHashField(key, field);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(value, responseTime));
    }

    /**
     * Get all fields from a Redis hash.
     * 
     * Teaches: HGETALL command for retrieving complete objects
     * 
     * Usage: GET /api/redis/hash/{key}
     */
    @GetMapping("/hash/{key}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> getAllHashFields(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/hash/{} - HGETALL", key);
        
        Map<Object, Object> fields = redisOperationsService.getAllHashFields(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(fields, responseTime));
    }

    /**
     * Delete a field from a Redis hash.
     * 
     * Teaches: HDEL command for removing specific fields
     * 
     * Usage: DELETE /api/redis/hash/{key}/{field}
     */
    @DeleteMapping("/hash/{key}/{field}")
    public ResponseEntity<ApiResponse<String>> deleteHashField(@PathVariable String key, @PathVariable String field) {
        long startTime = System.currentTimeMillis();
        
        log.info("DELETE /api/redis/hash/{}/{} - HDEL", key, field);
        
        redisOperationsService.deleteHashField(key, field);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Hash field deleted successfully", responseTime));
    }

    // ==================== LIST OPERATIONS ====================

    /**
     * Add element to left of Redis list.
     * 
     * Teaches: LPUSH command for stack operations
     * 
     * Usage: POST /api/redis/list/left
     * Body: {"key": "activity", "value": "login"}
     */
    @PostMapping("/list/left")
    public ResponseEntity<ApiResponse<String>> leftPush(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        Object value = request.get("value");
        
        log.info("POST /api/redis/list/left - LPUSH key={}", key);
        
        redisOperationsService.leftPush(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Element added to left of list", responseTime));
    }

    /**
     * Add element to right of Redis list.
     * 
     * Teaches: RPUSH command for queue operations
     * 
     * Usage: POST /api/redis/list/right
     * Body: {"key": "queue", "value": "task1"}
     */
    @PostMapping("/list/right")
    public ResponseEntity<ApiResponse<String>> rightPush(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        Object value = request.get("value");
        
        log.info("POST /api/redis/list/right - RPUSH key={}", key);
        
        redisOperationsService.rightPush(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Element added to right of list", responseTime));
    }

    /**
     * Get all elements from Redis list.
     * 
     * Teaches: LRANGE command for retrieving list contents
     * 
     * Usage: GET /api/redis/list/{key}
     */
    @GetMapping("/list/{key}")
    public ResponseEntity<ApiResponse<List<Object>>> getList(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/list/{} - LRANGE", key);
        
        List<Object> list = redisOperationsService.getList(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(list, responseTime));
    }

    /**
     * Pop element from left of Redis list.
     * 
     * Teaches: LPOP command for queue processing
     * 
     * Usage: DELETE /api/redis/list/left/{key}
     */
    @DeleteMapping("/list/left/{key}")
    public ResponseEntity<ApiResponse<Object>> leftPop(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("DELETE /api/redis/list/left/{} - LPOP", key);
        
        Object value = redisOperationsService.leftPop(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(value, responseTime));
    }

    /**
     * Pop element from right of Redis list.
     * 
     * Teaches: RPOP command for stack processing
     * 
     * Usage: DELETE /api/redis/list/right/{key}
     */
    @DeleteMapping("/list/right/{key}")
    public ResponseEntity<ApiResponse<Object>> rightPop(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("DELETE /api/redis/list/right/{} - RPOP", key);
        
        Object value = redisOperationsService.rightPop(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(value, responseTime));
    }

    // ==================== SET OPERATIONS ====================

    /**
     * Add element to Redis set.
     * 
     * Teaches: SADD command for unique collections
     * 
     * Usage: POST /api/redis/set
     * Body: {"key": "tags", "value": "java"}
     */
    @PostMapping("/set")
    public ResponseEntity<ApiResponse<String>> addToSet(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        Object value = request.get("value");
        
        log.info("POST /api/redis/set - SADD key={}", key);
        
        redisOperationsService.addToSet(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Element added to set", responseTime));
    }

    /**
     * Get all members of Redis set.
     * 
     * Teaches: SMEMBERS command for retrieving unique values
     * 
     * Usage: GET /api/redis/set/{key}
     */
    @GetMapping("/set/{key}")
    public ResponseEntity<ApiResponse<Set<Object>>> getSetMembers(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/set/{} - SMEMBERS", key);
        
        Set<Object> members = redisOperationsService.getSetMembers(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(members, responseTime));
    }

    /**
     * Check if element is in Redis set.
     * 
     * Teaches: SISMEMBER command for membership testing
     * 
     * Usage: GET /api/redis/set/{key}/{value}
     */
    @GetMapping("/set/{key}/{value}")
    public ResponseEntity<ApiResponse<Boolean>> isSetMember(@PathVariable String key, @PathVariable String value) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/set/{}/{} - SISMEMBER", key, value);
        
        boolean isMember = redisOperationsService.isSetMember(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(isMember, responseTime));
    }

    /**
     * Remove element from Redis set.
     * 
     * Teaches: SREM command for removing members
     * 
     * Usage: DELETE /api/redis/set/{key}/{value}
     */
    @DeleteMapping("/set/{key}/{value}")
    public ResponseEntity<ApiResponse<String>> removeFromSet(@PathVariable String key, @PathVariable String value) {
        long startTime = System.currentTimeMillis();
        
        log.info("DELETE /api/redis/set/{}/{} - SREM", key, value);
        
        redisOperationsService.removeFromSet(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Element removed from set", responseTime));
    }

    // ==================== SORTED SET OPERATIONS ====================

    /**
     * Add element to Redis sorted set with score.
     * 
     * Teaches: ZADD command for ordered collections
     * 
     * Usage: POST /api/redis/sortedset
     * Body: {"key": "leaderboard", "value": "player1", "score": 100}
     */
    @PostMapping("/sortedset")
    public ResponseEntity<ApiResponse<String>> addToSortedSet(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        Object value = request.get("value");
        double score = ((Number) request.get("score")).doubleValue();
        
        log.info("POST /api/redis/sortedset - ZADD key={}, score={}", key, score);
        
        redisOperationsService.addToSortedSet(key, value, score);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Element added to sorted set", responseTime));
    }

    /**
     * Get all elements from Redis sorted set (ascending).
     * 
     * Teaches: ZRANGE command for ordered retrieval
     * 
     * Usage: GET /api/redis/sortedset/{key}
     */
    @GetMapping("/sortedset/{key}")
    public ResponseEntity<ApiResponse<Set<Object>>> getSortedSet(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/sortedset/{} - ZRANGE", key);
        
        Set<Object> members = redisOperationsService.getSortedSet(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(members, responseTime));
    }

    /**
     * Get all elements from Redis sorted set (descending).
     * 
     * Teaches: ZREVRANGE command for reverse ordered retrieval
     * 
     * Usage: GET /api/redis/sortedset/{key}/reverse
     */
    @GetMapping("/sortedset/{key}/reverse")
    public ResponseEntity<ApiResponse<Set<Object>>> getSortedSetReverse(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/sortedset/{}/reverse - ZREVRANGE", key);
        
        Set<Object> members = redisOperationsService.getSortedSetReverse(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(members, responseTime));
    }

    /**
     * Get rank of element in Redis sorted set.
     * 
     * Teaches: ZRANK command for position lookup
     * 
     * Usage: GET /api/redis/sortedset/{key}/{value}/rank
     */
    @GetMapping("/sortedset/{key}/{value}/rank")
    public ResponseEntity<ApiResponse<Long>> getSortedSetRank(@PathVariable String key, @PathVariable String value) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/sortedset/{}/rank - ZRANK", key);
        
        Long rank = redisOperationsService.getSortedSetRank(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(rank, responseTime));
    }

    // ==================== COUNTER OPERATIONS ====================

    /**
     * Increment a counter in Redis.
     * 
     * Teaches: INCR command for atomic counting
     * 
     * Usage: POST /api/redis/counter/increment
     * Body: {"key": "views"}
     */
    @PostMapping("/counter/increment")
    public ResponseEntity<ApiResponse<Long>> increment(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        
        String key = request.get("key");
        
        log.info("POST /api/redis/counter/increment - INCR key={}", key);
        
        long value = redisOperationsService.increment(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(value, responseTime));
    }

    /**
     * Increment counter by specific amount.
     * 
     * Teaches: INCRBY command for batch incrementing
     * 
     * Usage: POST /api/redis/counter/incrementby
     * Body: {"key": "points", "delta": 10}
     */
    @PostMapping("/counter/incrementby")
    public ResponseEntity<ApiResponse<Long>> incrementBy(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        long delta = ((Number) request.get("delta")).longValue();
        
        log.info("POST /api/redis/counter/incrementby - INCRBY key={}, delta={}", key, delta);
        
        long value = redisOperationsService.incrementBy(key, delta);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(value, responseTime));
    }

    /**
     * Decrement a counter in Redis.
     * 
     * Teaches: DECR command for atomic decrementing
     * 
     * Usage: POST /api/redis/counter/decrement
     * Body: {"key": "inventory"}
     */
    @PostMapping("/counter/decrement")
    public ResponseEntity<ApiResponse<Long>> decrement(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        
        String key = request.get("key");
        
        log.info("POST /api/redis/counter/decrement - DECR key={}", key);
        
        long value = redisOperationsService.decrement(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(value, responseTime));
    }

    // ==================== KEY OPERATIONS ====================

    /**
     * Set TTL for a key.
     * 
     * Teaches: EXPIRE command for automatic expiration
     * 
     * Usage: POST /api/redis/expire
     * Body: {"key": "session:123", "timeout": 30, "unit": "SECONDS"}
     */
    @PostMapping("/expire")
    public ResponseEntity<ApiResponse<String>> setExpire(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        
        String key = (String) request.get("key");
        long timeout = ((Number) request.get("timeout")).longValue();
        String unitStr = (String) request.get("unit");
        TimeUnit unit = TimeUnit.valueOf(unitStr.toUpperCase());
        
        log.info("POST /api/redis/expire - EXPIRE key={}, timeout={}, unit={}", key, timeout, unit);
        
        redisOperationsService.setExpire(key, timeout, unit);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("TTL set successfully", responseTime));
    }

    /**
     * Get TTL of a key.
     * 
     * Teaches: TTL command for checking expiration
     * 
     * Usage: GET /api/redis/ttl/{key}
     */
    @GetMapping("/ttl/{key}")
    public ResponseEntity<ApiResponse<Long>> getTtl(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/ttl/{} - TTL", key);
        
        long ttl = redisOperationsService.getTtl(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(ttl, responseTime));
    }

    /**
     * Delete a key from Redis.
     * 
     * Teaches: DEL command for key removal
     * 
     * Usage: DELETE /api/redis/key/{key}
     */
    @DeleteMapping("/key/{key}")
    public ResponseEntity<ApiResponse<Boolean>> deleteKey(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("DELETE /api/redis/key/{} - DEL", key);
        
        boolean deleted = redisOperationsService.deleteKey(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(deleted, responseTime));
    }

    /**
     * Check if key exists in Redis.
     * 
     * Teaches: EXISTS command for key validation
     * 
     * Usage: GET /api/redis/exists/{key}
     */
    @GetMapping("/exists/{key}")
    public ResponseEntity<ApiResponse<Boolean>> keyExists(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/exists/{} - EXISTS", key);
        
        boolean exists = redisOperationsService.keyExists(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(exists, responseTime));
    }

    /**
     * Rename a key in Redis.
     * 
     * Teaches: RENAME command for key management
     * 
     * Usage: PUT /api/redis/rename
     * Body: {"oldKey": "oldname", "newKey": "newname"}
     */
    @PutMapping("/rename")
    public ResponseEntity<ApiResponse<String>> renameKey(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        
        String oldKey = request.get("oldKey");
        String newKey = request.get("newKey");
        
        log.info("PUT /api/redis/rename - RENAME oldKey={}, newKey={}", oldKey, newKey);
        
        redisOperationsService.renameKey(oldKey, newKey);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Key renamed successfully", responseTime));
    }

    /**
     * Get keys matching pattern.
     * 
     * Teaches: KEYS command for pattern matching
     * 
     * Usage: GET /api/redis/keys/{pattern}
     */
    @GetMapping("/keys/{pattern}")
    public ResponseEntity<ApiResponse<Set<String>>> getKeysByPattern(@PathVariable String pattern) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/keys/{} - KEYS", pattern);
        
        Set<String> keys = redisOperationsService.getKeysByPattern(pattern);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(keys, responseTime));
    }

    /**
     * Get data type of a key.
     * 
     * Teaches: TYPE command for data type identification
     * 
     * Usage: GET /api/redis/type/{key}
     */
    @GetMapping("/type/{key}")
    public ResponseEntity<ApiResponse<String>> getKeyType(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("GET /api/redis/type/{} - TYPE", key);
        
        String type = redisOperationsService.getKeyType(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(type, responseTime));
    }
}
