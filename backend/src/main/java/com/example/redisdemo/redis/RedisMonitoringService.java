package com.example.redisdemo.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Redis Monitoring Service
 * 
 * This service provides Redis monitoring and administrative operations.
 * This is Phase 9 of the project - Redis Monitoring.
 * 
 * WHY this service exists:
 * - Teaches Redis monitoring commands
 * - Shows how to monitor Redis health
 * - Demonstrates administrative operations
 * - Provides insights into Redis internals
 * 
 * WHEN to use this service:
 * - Phase 9: Learning Redis monitoring
 * - Production monitoring
 * - Debugging Redis issues
 * - Performance analysis
 * 
 * PRODUCTION USE CASES:
 * - Redis health monitoring
 * - Performance analysis
 * - Memory usage tracking
 * - Client connection monitoring
 * 
 * REDIS MONITORING COMMANDS COVERED:
 * - INFO: Server information and statistics
 * - MEMORY: Memory usage information
 * - PING: Connection check
 * - CONFIG: Configuration management
 * - CLIENT LIST: Connected clients
 * - DBSIZE: Database size
 * - FLUSHDB: Clear current database
 * - FLUSHALL: Clear all databases
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisMonitoringService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Get Redis server information.
     * 
     * WHY this method exists:
     * - Shows Redis server information
     * - Provides server statistics
     * - Essential for monitoring
     * 
     * WHEN to use this method:
     * - Server health checks
     * - Performance monitoring
     * - Capacity planning
     * 
     * PRODUCTION USE CASES:
     * - Monitoring dashboards
     * - Health checks
     * - Performance analysis
     * 
     * @return Redis server information
     */
    public Map<String, Object> getRedisInfo() {
        log.info("Redis Monitoring: INFO command");
        
        try {
            Properties info = redisTemplate.getConnectionFactory().getConnection().info();
            Map<String, Object> infoMap = new HashMap<>();
            
            for (String key : info.stringPropertyNames()) {
                infoMap.put(key, info.getProperty(key));
            }
            
            log.info("Redis INFO retrieved successfully");
            return infoMap;
            
        } catch (Exception e) {
            log.error("Error getting Redis INFO", e);
            throw new RuntimeException("Failed to get Redis INFO", e);
        }
    }

    /**
     * Get Redis memory usage information.
     * 
     * WHY this method exists:
     * - Shows memory usage statistics
     * - Helps identify memory issues
     * - Essential for capacity planning
     * 
     * WHEN to use this method:
     * - Memory monitoring
     * - Capacity planning
     * - Memory leak detection
     * 
     * PRODUCTION USE CASES:
     * - Memory monitoring
     * - Alerting on high memory usage
     * - Capacity planning
     * 
     * @return Redis memory information
     */
    public Map<String, Object> getRedisMemoryInfo() {
        log.info("Redis Monitoring: MEMORY command");
        
        try {
            Properties memoryInfo = redisTemplate.getConnectionFactory().getConnection().info("memory");
            Map<String, Object> memoryMap = new HashMap<>();
            
            for (String key : memoryInfo.stringPropertyNames()) {
                memoryMap.put(key, memoryInfo.getProperty(key));
            }
            
            log.info("Redis MEMORY info retrieved successfully");
            return memoryMap;
            
        } catch (Exception e) {
            log.error("Error getting Redis MEMORY info", e);
            throw new RuntimeException("Failed to get Redis MEMORY info", e);
        }
    }

    /**
     * Ping Redis server.
     * 
     * WHY this method exists:
     * - Checks Redis connectivity
     * - Simple health check
     * - Connection validation
     * 
     * WHEN to use this method:
     * - Health checks
     * - Connection testing
     * - Availability monitoring
     * 
     * PRODUCTION USE CASES:
     * - Health check endpoints
     * - Connection monitoring
     * - Availability alerts
     * 
     * @return PONG if connected, error message otherwise
     */
    public String pingRedis() {
        log.info("Redis Monitoring: PING command");
        
        try {
            String pong = redisTemplate.getConnectionFactory().getConnection().ping();
            log.info("Redis PING result: {}", pong);
            return pong;
            
        } catch (Exception e) {
            log.error("Redis PING failed", e);
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Get Redis configuration.
     * 
     * WHY this method exists:
     * - Shows Redis configuration
     * - Helps understand Redis setup
     * - Useful for debugging
     * 
     * WHEN to use this method:
     * - Configuration auditing
     * - Debugging setup issues
     * - Performance tuning
     * 
     * PRODUCTION USE CASES:
     * - Configuration monitoring
     * - Setup validation
     * - Performance optimization
     * 
     * @param pattern Configuration pattern (e.g., "*")
     * @return Redis configuration
     */
    public Map<String, String> getRedisConfig(String pattern) {
        log.info("Redis Monitoring: CONFIG GET {}", pattern);
        
        try {
            Properties config = redisTemplate.getConnectionFactory().getConnection().getConfig(pattern);
            Map<String, String> configMap = new HashMap<>();
            
            for (String key : config.stringPropertyNames()) {
                configMap.put(key, config.getProperty(key));
            }
            
            log.info("Redis CONFIG retrieved successfully");
            return configMap;
            
        } catch (Exception e) {
            log.error("Error getting Redis CONFIG", e);
            throw new RuntimeException("Failed to get Redis CONFIG", e);
        }
    }

    /**
     * Get connected clients list.
     * 
     * WHY this method exists:
     * - Shows connected clients
     * - Helps monitor connections
     * - Useful for debugging
     * 
     * WHEN to use this method:
     * - Connection monitoring
     * - Debugging connection issues
     * - Capacity planning
     * 
     * PRODUCTION USE CASES:
     * - Connection monitoring
     * - Security auditing
     * - Capacity planning
     * 
     * @return List of connected clients
     */
    public List<Object> getClientList() {
        log.info("Redis Monitoring: CLIENT LIST command");
        
        try {
            var clients = redisTemplate.getConnectionFactory().getConnection().getClientList();
            log.info("Retrieved {} connected clients", clients.size());
            return new java.util.ArrayList<>(clients);
            
        } catch (Exception e) {
            log.error("Error getting CLIENT LIST", e);
            throw new RuntimeException("Failed to get CLIENT LIST", e);
        }
    }

    /**
     * Get database size (number of keys).
     * 
     * WHY this method exists:
     * - Shows number of keys in database
     - Helps monitor cache size
     * - Useful for capacity planning
     * 
     * WHEN to use this method:
     * - Cache size monitoring
     * - Capacity planning
     * - Cache cleanup decisions
     * 
     * PRODUCTION USE CASES:
     * - Cache size monitoring
     * - Capacity planning
     * - Alerting on cache size
     * 
     * @return Number of keys in current database
     */
    public long getDatabaseSize() {
        log.info("Redis Monitoring: DBSIZE command");
        
        try {
            Long dbSize = redisTemplate.getConnectionFactory().getConnection().dbSize();
            log.info("Database size: {} keys", dbSize);
            return dbSize != null ? dbSize : 0;
            
        } catch (Exception e) {
            log.error("Error getting DBSIZE", e);
            throw new RuntimeException("Failed to get DBSIZE", e);
        }
    }

    /**
     * Flush current database (remove all keys).
     * 
     * WHY this method exists:
     * - Clears current database
     * - Useful for testing
     * - Cache reset functionality
     * 
     * WHEN to use this method:
     * - Development/testing
     * - Cache reset
     * - Data cleanup
     * 
     * PRODUCTION USE CASES:
     * - Development/testing only
     * - Emergency cache cleanup
     * - Maintenance operations
     * 
     * ⚠️ WARNING: Deletes all data in current database
     */
    public void flushDatabase() {
        log.info("Redis Monitoring: FLUSHDB command");
        log.warn("⚠️ Flushing current database - all keys will be deleted");
        
        try {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
            log.info("Database flushed successfully");
            
        } catch (Exception e) {
            log.error("Error flushing database", e);
            throw new RuntimeException("Failed to flush database", e);
        }
    }

    /**
     * Flush all databases (remove all keys from all databases).
     * 
     * WHY this method exists:
     * - Clears all Redis databases
     * - Complete Redis reset
     * - Useful for testing
     * 
     * WHEN to use this method:
     * - Development/testing only
     * - Complete Redis reset
     * - Maintenance operations
     * 
     * PRODUCTION USE CASES:
     * - Development/testing only
     * - Emergency cleanup
     * - Major maintenance
     * 
     * ⚠️ WARNING: Deletes all data from all databases
     */
    public void flushAllDatabases() {
        log.info("Redis Monitoring: FLUSHALL command");
        log.warn("⚠️ Flushing all databases - all keys will be deleted");
        
        try {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
            log.info("All databases flushed successfully");
            
        } catch (Exception e) {
            log.error("Error flushing all databases", e);
            throw new RuntimeException("Failed to flush all databases", e);
        }
    }

    /**
     * Get Redis monitoring explanation.
     * 
     * @return Monitoring explanation
     */
    public String getMonitoringExplanation() {
        return """
            Redis Monitoring Concepts:
            
            WHY MONITOR REDIS:
            - Ensure Redis is healthy and responsive
            - Track memory usage and capacity
            - Monitor client connections
            - Analyze performance metrics
            - Detect issues before they become critical
            
            MONITORING COMMANDS:
            
            1. INFO:
            - Server information and statistics
            - Shows version, uptime, memory usage
            - Provides client and performance stats
            - Essential for health monitoring
            
            2. MEMORY:
            - Memory usage information
            - Shows memory allocation and fragmentation
            - Helps identify memory leaks
            - Critical for capacity planning
            
            3. PING:
            - Simple connection check
            - Returns PONG if connected
            - Basic health check
            - First check in troubleshooting
            
            4. CONFIG:
            - Redis configuration settings
            - Shows runtime configuration
            - Helps understand Redis setup
            - Useful for debugging and tuning
            
            5. CLIENT LIST:
            - Connected clients information
            - Shows client details and connections
            - Helps monitor connection usage
            - Useful for security and capacity planning
            
            6. DBSIZE:
            - Number of keys in database
            - Shows cache size
            - Helps monitor cache growth
            - Useful for capacity planning
            
            7. FLUSHDB:
            - Delete all keys in current database
            - Use with caution in production
            - Useful for testing and cache reset
            - ⚠️ Destructive operation
            
            8. FLUSHALL:
            - Delete all keys in all databases
            - Most destructive Redis command
            - Use extreme caution
            - ⚠️ NEVER use in production
            
            PRODUCTION MONITORING:
            - Set up regular INFO checks
            - Monitor memory usage trends
            - Alert on high memory usage
            - Track client connections
            - Monitor cache hit ratios
            - Set up automated alerts
            
            REDIS METRICS TO WATCH:
            - Memory usage (used_memory)
            - Cache hit ratio
            - Connected clients
            - Command execution rate
            - Key eviction rate
            - Fragmentation ratio
            - Response times
            """;
    }
}
