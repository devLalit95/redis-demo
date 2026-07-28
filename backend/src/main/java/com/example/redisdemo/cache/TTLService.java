package com.example.redisdemo.cache;

import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.entity.Student;
import com.example.redisdemo.exception.ResourceNotFoundException;
import com.example.redisdemo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * TTL (Time To Live) Service
 * 
 * This service demonstrates different TTL strategies for cached data.
 * This is Phase 5 of the project - TTL Implementation.
 * 
 * WHY this service exists:
 * - Teaches TTL concepts through practical implementation
 * - Shows how to set different expiration times
 * - Demonstrates automatic cache expiration
 * - Shows TTL monitoring and management
 * 
 * WHEN to use this service:
 * - Phase 5: Learning TTL concepts
 * - Understanding cache expiration
 * - Implementing time-based cache invalidation
 * 
 * PRODUCTION USE CASES:
 * - Session management (short TTL)
 * - Configuration data (long TTL)
 * - Real-time data (very short TTL)
 * - Static data (very long TTL)
 * 
 * TTL STRATEGIES COVERED:
 * - 30 seconds: Frequently changing data
 * - 1 minute: Moderately dynamic data
 * - 5 minutes: Standard caching
 * - Custom TTL: Application-specific needs
 * 
 * KEY CONCEPTS:
 * - TTL: Time To Live - how long data stays in cache
 * - Automatic expiration: Redis removes expired keys
 * - Cache refresh: Data is reloaded after expiration
 * - TTL monitoring: Check remaining time
 */
// @Service  // Disabled for Redis-only testing
@RequiredArgsConstructor
@Slf4j
public class TTLService {

    // Removed StudentRepository dependency for Redis-only testing
    private final RedisTemplate<String, Object> redisTemplate;

    // ==================== SHORT TTL (30 SECONDS) ====================

    /**
     * Get student with 30-second TTL.
     * 
     * WHY 30-second TTL:
     * - For frequently changing data
     * - Near real-time requirements
     * - High cache turnover
     * 
     * WHEN to use 30-second TTL:
     * - Real-time analytics
     * - Frequently updated user data
     * - Session data
     * - Rate limiting counters
     * 
     * PRODUCTION USE CASES:
     * - User activity feeds
     * - Real-time counters
     * - Session data
     * - Temporary locks
     * 
     * @param id The student ID
     * @return The student DTO
     */
    @Cacheable(cacheNames = "students-30s", key = "#id")
    public StudentDTO getStudentWith30SecondTTL(Long id) {
        log.info("🕐 TTL 30s: Fetching student ID: {}", id);
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Create a placeholder student DTO
        StudentDTO dto = StudentDTO.builder()
                .id(id)
                .name("Test Student " + id)
                .email("test" + id + "@example.com")
                .message("TTL 30s (Redis-only testing)")
                .build();
        
        // Set TTL manually to 30 seconds
        String cacheKey = "students-30s::" + id;
        redisTemplate.expire(cacheKey, 30, TimeUnit.SECONDS);
        
        log.info("✅ Student cached with 30-second TTL");
        log.info("Cache Key: {}", cacheKey);
        
        return dto;
    }

    /**
     * Get TTL remaining for 30-second cache.
     * 
     * WHY this method exists:
     * - Demonstrates TTL monitoring
     * - Shows how to check remaining time
     * - Useful for debugging TTL issues
     * 
     * @param id The student ID
     * @return Remaining TTL in seconds
     */
    public long get30SecondTTLRemaining(Long id) {
        String cacheKey = "students-30s::" + id;
        Long ttl = redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
        
        log.info("🕐 TTL 30s: Remaining TTL for ID {}: {} seconds", id, ttl);
        
        return ttl != null ? ttl : -2; // -2 means key doesn't exist
    }

    // ==================== MEDIUM TTL (1 MINUTE) ====================

    /**
     * Get student with 1-minute TTL.
     * 
     * WHY 1-minute TTL:
     * - For moderately dynamic data
     * - Balance between freshness and performance
     * - Standard session timeout
     * 
     * WHEN to use 1-minute TTL:
     * - User profile data
     * - Moderately changing data
     * - API response caching
     * - Shopping cart data
     * 
     * PRODUCTION USE CASES:
     * - User profiles
     * - Product information
     * - Shopping cart
     * - API responses
     * 
     * @param id The student ID
     * @return The student DTO
     */
    @Cacheable(cacheNames = "students-1m", key = "#id")
    public StudentDTO getStudentWith1MinuteTTL(Long id) {
        log.info("🕐 TTL 1m: Fetching student ID: {}", id);
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Create a placeholder student DTO
        StudentDTO dto = StudentDTO.builder()
                .id(id)
                .name("Test Student " + id)
                .email("test" + id + "@example.com")
                .message("TTL 1m (Redis-only testing)")
                .build();
        
        // Set TTL manually to 1 minute
        String cacheKey = "students-1m::" + id;
        redisTemplate.expire(cacheKey, 1, TimeUnit.MINUTES);
        
        log.info("✅ Student cached with 1-minute TTL");
        log.info("Cache Key: {}", cacheKey);
        
        return dto;
    }

    /**
     * Get TTL remaining for 1-minute cache.
     * 
     * @param id The student ID
     * @return Remaining TTL in seconds
     */
    public long get1MinuteTTLRemaining(Long id) {
        String cacheKey = "students-1m::" + id;
        Long ttl = redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
        
        log.info("🕐 TTL 1m: Remaining TTL for ID {}: {} seconds", id, ttl);
        
        return ttl != null ? ttl : -2;
    }

    // ==================== STANDARD TTL (5 MINUTES) ====================

    /**
     * Get student with 5-minute TTL.
     * 
     * WHY 5-minute TTL:
     * - For relatively static data
     * - Good balance for most use cases
     * - Default cache duration
     * 
     * WHEN to use 5-minute TTL:
     * - Configuration data
     * - Reference data
     * - Historical data
     * - Standard caching
     * 
     * PRODUCTION USE CASES:
     * - Configuration values
     * - Reference data
     * - Historical statistics
     * - Standard application cache
     * 
     * @param id The student ID
     * @return The student DTO
     */
    @Cacheable(cacheNames = "students-5m", key = "#id")
    public StudentDTO getStudentWith5MinuteTTL(Long id) {
        log.info("🕐 TTL 5m: Fetching student ID: {}", id);
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Create a placeholder student DTO
        StudentDTO dto = StudentDTO.builder()
                .id(id)
                .name("Test Student " + id)
                .email("test" + id + "@example.com")
                .message("TTL 5m (Redis-only testing)")
                .build();
        
        // Set TTL manually to 5 minutes
        String cacheKey = "students-5m::" + id;
        redisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
        
        log.info("✅ Student cached with 5-minute TTL");
        log.info("Cache Key: {}", cacheKey);
        
        return dto;
    }

    /**
     * Get TTL remaining for 5-minute cache.
     * 
     * @param id The student ID
     * @return Remaining TTL in seconds
     */
    public long get5MinuteTTLRemaining(Long id) {
        String cacheKey = "students-5m::" + id;
        Long ttl = redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
        
        log.info("🕐 TTL 5m: Remaining TTL for ID {}: {} seconds", id, ttl);
        
        return ttl != null ? ttl : -2;
    }

    // ==================== CUSTOM TTL ====================

    /**
     * Get student with custom TTL.
     * 
     * WHY custom TTL:
     * - Application-specific requirements
     * - Business rule-based expiration
     * - Flexible cache management
     * 
     * WHEN to use custom TTL:
     * - Business-specific requirements
     * - Different data types need different TTL
     * - User-specific cache policies
     * 
     * PRODUCTION USE CASES:
     * - Premium users get longer TTL
     * - Trial users get shorter TTL
     * - Time-sensitive promotions
     * - Scheduled content expiration
     * 
     * @param id The student ID
     * @param ttlSeconds Custom TTL in seconds
     * @return The student DTO
     */
    @Cacheable(cacheNames = "students-custom", key = "#id")
    public StudentDTO getStudentWithCustomTTL(Long id, long ttlSeconds) {
        log.info("🕐 TTL Custom ({}s): Fetching student ID: {}", ttlSeconds, id);
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Create a placeholder student DTO
        StudentDTO dto = StudentDTO.builder()
                .id(id)
                .name("Test Student " + id)
                .email("test" + id + "@example.com")
                .message("TTL custom " + ttlSeconds + "s (Redis-only testing)")
                .build();
        
        // Set custom TTL
        String cacheKey = "students-custom::" + id;
        redisTemplate.expire(cacheKey, ttlSeconds, TimeUnit.SECONDS);
        
        log.info("✅ Student cached with custom TTL: {} seconds", ttlSeconds);
        log.info("Cache Key: {}", cacheKey);
        
        return dto;
    }

    /**
     * Get TTL remaining for custom cache.
     * 
     * @param id The student ID
     * @return Remaining TTL in seconds
     */
    public long getCustomTTLRemaining(Long id) {
        String cacheKey = "students-custom::" + id;
        Long ttl = redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
        
        log.info("🕐 TTL Custom: Remaining TTL for ID {}: {} seconds", id, ttl);
        
        return ttl != null ? ttl : -2;
    }

    // ==================== TTL MANAGEMENT ====================

    /**
     * Refresh TTL for a cached entry.
     * 
     * WHY refresh TTL:
     * - Extend cache lifetime
     * - Keep frequently accessed data fresh
     * - Implement sliding expiration
     * 
     * WHEN to refresh TTL:
     * - User continues session
     * - Data still relevant
     * - Prevent cache stampede
     * 
     * PRODUCTION USE CASES:
     * - Session refresh
     * - Keep alive mechanisms
     * - Sliding expiration
     * 
     * @param id The student ID
     * @param ttlSeconds New TTL in seconds
     * @return Previous TTL or -1 if key doesn't exist
     */
    public long refreshTTL(Long id, long ttlSeconds, String cacheName) {
        String cacheKey = cacheName + "::" + id;
        
        log.info("🔄 Refreshing TTL for key: {}", cacheKey);
        log.info("New TTL: {} seconds", ttlSeconds);
        
        Boolean exists = redisTemplate.hasKey(cacheKey);
        
        if (Boolean.TRUE.equals(exists)) {
            redisTemplate.expire(cacheKey, ttlSeconds, TimeUnit.SECONDS);
            log.info("✅ TTL refreshed successfully");
            return ttlSeconds;
        } else {
            log.info("⚠️ Key doesn't exist, cannot refresh TTL");
            return -1;
        }
    }

    /**
     * Get TTL explanation.
     * 
     * WHY this method exists:
     * - Educational purpose
     * - Explains TTL concepts
     * - Provides usage guidance
     * 
     * @return TTL explanation
     */
    public String getTTLExplanation() {
        return """
            TTL (Time To Live) Concepts:
            
            WHAT IS TTL:
            - Time To Live - how long data stays in cache
            - Automatic expiration after TTL expires
            - Redis removes expired keys automatically
            
            TTL STRATEGIES:
            - 30 seconds: Real-time data, frequently changing
            - 1 minute: Moderately dynamic data, sessions
            - 5 minutes: Standard caching, reference data
            - Custom: Application-specific requirements
            
            WHEN TO USE SHORT TTL:
            - Real-time requirements
            - Frequently updated data
            - Session management
            - Rate limiting
            
            WHEN TO USE LONG TTL:
            - Static reference data
            - Configuration values
            - Historical data
            - Rarely changing data
            
            TTL TRADE-OFFS:
            - Short TTL: Fresh data, more cache misses
            - Long TTL: Better performance, stale data risk
            - Balance based on data volatility and business needs
            """;
    }

    /**
     * Manual mapping from Student entity to StudentDTO.
     * 
     * @param student The entity to convert
     * @return The DTO representation
     */
    private StudentDTO mapToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setCourse(student.getCourse());
        dto.setBranch(student.getBranch());
        dto.setSemester(student.getSemester());
        dto.setCgpa(student.getCgpa());
        dto.setCity(student.getCity());
        dto.setPhone(student.getPhone());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        return dto;
    }
}
