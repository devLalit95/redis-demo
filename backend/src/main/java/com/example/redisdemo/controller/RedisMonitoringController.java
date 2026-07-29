package com.example.redisdemo.controller;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.redis.RedisMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Redis Monitoring Controller
 * 
 * This controller provides HTTP endpoints for Redis monitoring operations.
 * This is Phase 9 of the project - Redis Monitoring.
 */
@RestController
@RequestMapping("/api/redis/monitoring")
@RequiredArgsConstructor
@Slf4j
public class RedisMonitoringController {

    private final RedisMonitoringService redisMonitoringService;

    /**
     * Get Redis server information.
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRedisInfo() {
        long startTime = System.currentTimeMillis();
        
        log.info("REDIS MONITORING: GET /api/redis/monitoring/info");
        
        try {
            Map<String, Object> info = redisMonitoringService.getRedisInfo();
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return ResponseEntity.ok(ApiResponse.success(info, responseTime));
        } catch (Exception e) {
            log.error("Error getting Redis info", e);
            long responseTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get Redis info: " + e.getMessage(), responseTime));
        }
    }

    /**
     * Get Redis memory information.
     */
    @GetMapping("/memory")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRedisMemoryInfo() {
        long startTime = System.currentTimeMillis();
        
        log.info("REDIS MONITORING: GET /api/redis/monitoring/memory");
        
        try {
            Map<String, Object> memoryInfo = redisMonitoringService.getRedisMemoryInfo();
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return ResponseEntity.ok(ApiResponse.success(memoryInfo, responseTime));
        } catch (Exception e) {
            log.error("Error getting Redis memory info", e);
            long responseTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get Redis memory info: " + e.getMessage(), responseTime));
        }
    }

    /**
     * Ping Redis server.
     */
    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> pingRedis() {
        long startTime = System.currentTimeMillis();
        
        log.info("REDIS MONITORING: GET /api/redis/monitoring/ping");
        
        try {
            String pong = redisMonitoringService.pingRedis();
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return ResponseEntity.ok(ApiResponse.success(pong, responseTime));
        } catch (Exception e) {
            log.error("Error pinging Redis", e);
            long responseTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to ping Redis: " + e.getMessage(), responseTime));
        }
    }

    /**
     * Get Redis configuration.
     */
    @GetMapping("/config")
    public ResponseEntity<ApiResponse<Map<String, String>>> getRedisConfig(
            @RequestParam(defaultValue = "*") String pattern) {
        long startTime = System.currentTimeMillis();
        
        log.info("REDIS MONITORING: GET /api/redis/monitoring/config?pattern={}", pattern);
        
        try {
            Map<String, String> config = redisMonitoringService.getRedisConfig(pattern);
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return ResponseEntity.ok(ApiResponse.success(config, responseTime));
        } catch (Exception e) {
            log.error("Error getting Redis config", e);
            long responseTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get Redis config: " + e.getMessage(), responseTime));
        }
    }

    /**
     * Get connected clients list.
     */
    @GetMapping("/clients")
    public ResponseEntity<ApiResponse<List<Object>>> getClientList() {
        long startTime = System.currentTimeMillis();
        
        log.info("REDIS MONITORING: GET /api/redis/monitoring/clients");
        
        try {
            List<Object> clients = redisMonitoringService.getClientList();
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return ResponseEntity.ok(ApiResponse.success(clients, responseTime));
        } catch (Exception e) {
            log.error("Error getting client list", e);
            long responseTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get client list: " + e.getMessage(), responseTime));
        }
    }

    /**
     * Get database size.
     */
    @GetMapping("/dbsize")
    public ResponseEntity<ApiResponse<Long>> getDatabaseSize() {
        long startTime = System.currentTimeMillis();
        
        log.info("REDIS MONITORING: GET /api/redis/monitoring/dbsize");
        
        try {
            long dbSize = redisMonitoringService.getDatabaseSize();
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return ResponseEntity.ok(ApiResponse.success(dbSize, responseTime));
        } catch (Exception e) {
            log.error("Error getting database size", e);
            long responseTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get database size: " + e.getMessage(), responseTime));
        }
    }

    /**
     * Flush current database.
     * ⚠️ WARNING: Deletes all keys in current database
     */
    @PostMapping("/flushdb")
    public ResponseEntity<ApiResponse<String>> flushDatabase() {
        long startTime = System.currentTimeMillis();
        
        log.warn("REDIS MONITORING: POST /api/redis/monitoring/flushdb");
        log.warn("⚠️ FLUSHDB called - all keys in current database will be deleted");
        
        try {
            redisMonitoringService.flushDatabase();
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return ResponseEntity.ok(ApiResponse.success("Database flushed successfully", responseTime));
        } catch (Exception e) {
            log.error("Error flushing database", e);
            long responseTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to flush database: " + e.getMessage(), responseTime));
        }
    }

    /**
     * Flush all databases.
     * ⚠️ WARNING: Deletes all keys from all databases
     */
    @PostMapping("/flushall")
    public ResponseEntity<ApiResponse<String>> flushAllDatabases() {
        long startTime = System.currentTimeMillis();
        
        log.warn("REDIS MONITORING: POST /api/redis/monitoring/flushall");
        log.warn("⚠️ FLUSHALL called - all keys in all databases will be deleted");
        
        try {
            redisMonitoringService.flushAllDatabases();
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return ResponseEntity.ok(ApiResponse.success("All databases flushed successfully", responseTime));
        } catch (Exception e) {
            log.error("Error flushing all databases", e);
            long responseTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to flush all databases: " + e.getMessage(), responseTime));
        }
    }

    /**
     * Get monitoring explanation.
     */
    @GetMapping("/explanation")
    public ResponseEntity<ApiResponse<String>> getMonitoringExplanation() {
        long startTime = System.currentTimeMillis();
        
        log.info("REDIS MONITORING: GET /api/redis/monitoring/explanation");
        
        try {
            String explanation = redisMonitoringService.getMonitoringExplanation();
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return ResponseEntity.ok(ApiResponse.success(explanation, responseTime));
        } catch (Exception e) {
            log.error("Error getting monitoring explanation", e);
            long responseTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get monitoring explanation: " + e.getMessage(), responseTime));
        }
    }
}
