package com.example.redisdemo.controller;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.redis.RedisExplorerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * Redis Explorer Controller
 * 
 * This controller provides endpoints for exploring Redis data.
 */
@RestController
@RequestMapping("/api/redis-explorer")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Redis Explorer", description = "Explore and manage Redis data")
public class RedisExplorerController {

    private final RedisExplorerService redisExplorerService;

    @Operation(summary = "List Redis keys", description = "List all Redis keys with optional pattern matching")
    @GetMapping("/keys")
    public ApiResponse<Set<String>> listKeys(@RequestParam(required = false) String pattern) {
        return redisExplorerService.listKeys(pattern);
    }

    @Operation(summary = "Get key type", description = "Get the data type of a Redis key")
    @GetMapping("/keys/{key}/type")
    public ApiResponse<String> getKeyType(@PathVariable String key) {
        return redisExplorerService.getKeyType(key);
    }

    @Operation(summary = "Get key TTL", description = "Get the time-to-live (TTL) of a Redis key")
    @GetMapping("/keys/{key}/ttl")
    public ApiResponse<Long> getKeyTTL(@PathVariable String key) {
        return redisExplorerService.getKeyTTL(key);
    }

    @Operation(summary = "Get key memory usage", description = "Get the memory usage of a Redis key in bytes")
    @GetMapping("/keys/{key}/memory")
    public ApiResponse<Long> getKeyMemoryUsage(@PathVariable String key) {
        return redisExplorerService.getKeyMemoryUsage(key);
    }

    @Operation(summary = "Get key value", description = "Get the value of a Redis key")
    @GetMapping("/keys/{key}/value")
    public ApiResponse<Object> getValue(@PathVariable String key) {
        return redisExplorerService.getValue(key);
    }

    @Operation(summary = "Delete key", description = "Delete a Redis key")
    @DeleteMapping("/keys/{key}")
    public ApiResponse<String> deleteKey(@PathVariable String key) {
        return redisExplorerService.deleteKey(key);
    }

    @Operation(summary = "Get Redis info", description = "Get Redis server information")
    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getRedisInfo() {
        return redisExplorerService.getRedisInfo();
    }

    @Operation(summary = "Get database size", description = "Get the number of keys in the database")
    @GetMapping("/dbsize")
    public ApiResponse<Long> getDatabaseSize() {
        return redisExplorerService.getDatabaseSize();
    }

    @Operation(summary = "Flush database", description = "Flush all keys from current database (DANGEROUS)")
    @PostMapping("/flush")
    public ApiResponse<String> flushDatabase() {
        return redisExplorerService.flushDatabase();
    }
}