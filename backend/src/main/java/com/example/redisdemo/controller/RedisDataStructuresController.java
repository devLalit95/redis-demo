package com.example.redisdemo.controller;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.redis.RedisDataStructuresService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis Data Structures Controller
 * 
 * This controller provides HTTP endpoints for Redis data structures demos.
 * This is Phase 10 of the project - Redis Data Structures Demo.
 */
@RestController
@RequestMapping("/api/redis/data-structures")
@RequiredArgsConstructor
@Slf4j
public class RedisDataStructuresController {

    private final RedisDataStructuresService redisDataStructuresService;

    /**
     * String demo operations.
     */
    @PostMapping("/string")
    public ResponseEntity<ApiResponse<Map<String, Object>>> stringDemo(
            @RequestParam String key,
            @RequestParam String value) {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: POST /api/redis/data-structures/string");
        
        Map<String, Object> result = redisDataStructuresService.stringDemo(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(result, responseTime));
    }

    /**
     * Get all string keys.
     */
    @GetMapping("/string/keys")
    public ResponseEntity<ApiResponse<Set<String>>> getStringKeys() {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/string/keys");
        
        Set<String> keys = redisDataStructuresService.getStringKeys();
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(keys, responseTime));
    }

    /**
     * Hash demo operations.
     */
    @PostMapping("/hash")
    public ResponseEntity<ApiResponse<Map<String, Object>>> hashDemo(
            @RequestParam String key,
            @RequestParam String field,
            @RequestParam String value) {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: POST /api/redis/data-structures/hash");
        
        Map<String, Object> result = redisDataStructuresService.hashDemo(key, field, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(result, responseTime));
    }

    /**
     * Get all hash keys.
     */
    @GetMapping("/hash/keys")
    public ResponseEntity<ApiResponse<Set<String>>> getHashKeys() {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/hash/keys");
        
        Set<String> keys = redisDataStructuresService.getHashKeys();
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(keys, responseTime));
    }

    /**
     * Get hash fields.
     */
    @GetMapping("/hash/{key}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> getHashFields(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/hash/{}", key);
        
        Map<Object, Object> fields = redisDataStructuresService.getHashFields(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(fields, responseTime));
    }

    /**
     * List demo operations.
     */
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listDemo(
            @RequestParam String key,
            @RequestParam String value) {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: POST /api/redis/data-structures/list");
        
        Map<String, Object> result = redisDataStructuresService.listDemo(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(result, responseTime));
    }

    /**
     * Get all list keys.
     */
    @GetMapping("/list/keys")
    public ResponseEntity<ApiResponse<Set<String>>> getListKeys() {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/list/keys");
        
        Set<String> keys = redisDataStructuresService.getListKeys();
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(keys, responseTime));
    }

    /**
     * Get list elements.
     */
    @GetMapping("/list/{key}")
    public ResponseEntity<ApiResponse<List<Object>>> getListElements(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/list/{}", key);
        
        List<Object> elements = redisDataStructuresService.getListElements(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(elements, responseTime));
    }

    /**
     * Set demo operations.
     */
    @PostMapping("/set")
    public ResponseEntity<ApiResponse<Map<String, Object>>> setDemo(
            @RequestParam String key,
            @RequestParam String value) {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: POST /api/redis/data-structures/set");
        
        Map<String, Object> result = redisDataStructuresService.setDemo(key, value);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(result, responseTime));
    }

    /**
     * Get all set keys.
     */
    @GetMapping("/set/keys")
    public ResponseEntity<ApiResponse<Set<String>>> getSetKeys() {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/set/keys");
        
        Set<String> keys = redisDataStructuresService.getSetKeys();
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(keys, responseTime));
    }

    /**
     * Get set members.
     */
    @GetMapping("/set/{key}")
    public ResponseEntity<ApiResponse<Set<Object>>> getSetMembers(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/set/{}", key);
        
        Set<Object> members = redisDataStructuresService.getSetMembers(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(members, responseTime));
    }

    /**
     * Sorted Set demo operations.
     */
    @PostMapping("/sortedset")
    public ResponseEntity<ApiResponse<Map<String, Object>>> sortedSetDemo(
            @RequestParam String key,
            @RequestParam String value,
            @RequestParam double score) {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: POST /api/redis/data-structures/sortedset");
        
        Map<String, Object> result = redisDataStructuresService.sortedSetDemo(key, value, score);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(result, responseTime));
    }

    /**
     * Get all sorted set keys.
     */
    @GetMapping("/sortedset/keys")
    public ResponseEntity<ApiResponse<Set<String>>> getSortedSetKeys() {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/sortedset/keys");
        
        Set<String> keys = redisDataStructuresService.getSortedSetKeys();
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(keys, responseTime));
    }

    /**
     * Get sorted set members.
     */
    @GetMapping("/sortedset/{key}")
    public ResponseEntity<ApiResponse<Set<Object>>> getSortedSetMembers(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/sortedset/{}", key);
        
        Set<Object> members = redisDataStructuresService.getSortedSetMembers(key);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(members, responseTime));
    }

    /**
     * Get data structures explanation.
     */
    @GetMapping("/explanation")
    public ResponseEntity<ApiResponse<String>> getDataStructuresExplanation() {
        long startTime = System.currentTimeMillis();
        
        log.info("DATA STRUCTURES: GET /api/redis/data-structures/explanation");
        
        String explanation = redisDataStructuresService.getDataStructuresExplanation();
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(explanation, responseTime));
    }
}
