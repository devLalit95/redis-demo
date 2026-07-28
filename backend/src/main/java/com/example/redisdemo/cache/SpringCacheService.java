package com.example.redisdemo.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Spring Cache Service
 * 
 * This service demonstrates Spring's declarative caching annotations.
 * This is Phase 4 of the project - Spring Cache Annotations.
 * 
 * NOTE: StudentRepository dependency removed for Redis-only testing
 * 
 * WHY this service exists:
 * - Teaches Spring Cache annotations through practical implementation
 * - Shows how @Cacheable, @CachePut, @CacheEvict work
 * - Demonstrates different caching strategies
 * - Compares with manual caching implementation
 * 
 * WHEN to use this service:
 * - Phase 4: Learning Spring Cache annotations
 * - Production applications
 * - When you want declarative caching
 * 
 * PRODUCTION USE CASES:
 * - Production applications
 * - Standard caching approach
 * - Reduced code complexity
 * - Maintainable caching logic
 */
// @Service  // Disabled for Redis-only testing
@RequiredArgsConstructor
@Slf4j
public class SpringCacheService {

    // Removed StudentRepository dependency for Redis-only testing
    private final RedisTemplate<String, Object> redisTemplate;

    // ==================== @Cacheable EXAMPLES ====================

    /**
     * Get student by ID with @Cacheable.
     * 
     * DEMONSTRATES:
     * - Basic @Cacheable usage
     * - Default key generation
     * - Cache hit/miss behavior
     * 
     * @param id The student ID
     * @return The student DTO
     */
    @Cacheable(cacheNames = "students", key = "#id")
    public Object getStudentById(Long id) {
        log.info("Spring Cache: Fetching student ID: {}", id);
        log.info("🔄 This method executes only on cache miss");
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Return a placeholder student object
        return Map.of(
            "id", id,
            "name", "Test Student " + id,
            "email", "test" + id + "@example.com",
            "message", "Database fetch simulated (Redis-only testing)"
        );
    }

    /**
     * Get student by ID with condition.
     * 
     * DEMONSTRATES:
     * - @Cacheable with condition parameter
     * - Conditional caching based on method parameters
     * - Cache only when ID > 10
     * 
     * @param id The student ID
     * @return The student DTO
     */
    @Cacheable(cacheNames = "students", key = "#id", condition = "#id > 10")
    public Object getStudentByIdWithCondition(Long id) {
        log.info("Spring Cache: Fetching student ID: {} with condition (id > 10)", id);
        log.info("🔄 This method executes only on cache miss AND when condition is true");
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        return Map.of(
            "id", id,
            "name", "Test Student " + id,
            "message", "Conditional caching (id > 10)"
        );
    }

    /**
     * Get student by ID with unless.
     * 
     * DEMONSTRATES:
     * - @Cacheable with unless parameter
     * - Prevent caching based on result
     * - Don't cache when CGPA < 6.0
     * 
     * @param id The student ID
     * @return The student DTO
     */
    @Cacheable(cacheNames = "students", key = "#id", unless = "#result.cgpa.compareTo(new BigDecimal('6.0')) < 0")
    public Object getStudentByIdWithUnless(Long id) {
        log.info("Spring Cache: Fetching student ID: {} with unless (CGPA < 6.0)", id);
        log.info("🔄 This method executes only on cache miss");
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Create a student with low CGPA for testing
        return Map.of(
            "id", id,
            "name", "Test Student " + id,
            "cgpa", new BigDecimal("5.5"),
            "message", "Unless caching (CGPA < 6.0 won't be cached)"
        );
    }

    // ==================== @CachePut EXAMPLES ====================

    /**
     * Update student with @CachePut.
     * 
     * DEMONSTRATES:
     * - @CachePut for updating cache
     * - Method always executes
     * - Result is cached
     * 
     * @param id The student ID
     * @param studentDTO The student data
     * @return The updated student DTO
     */
    @CachePut(cacheNames = "students", key = "#id")
    public Object updateStudent(Long id, Object studentDTO) {
        log.info("Spring Cache: Updating student ID: {}", id);
        log.info("🔄 This method ALWAYS executes (unlike @Cacheable)");
        log.info("✅ Result will be cached");
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        return studentDTO;
    }

    // ==================== @CacheEvict EXAMPLES ====================

    /**
     * Delete student cache by ID.
     * 
     * DEMONSTRATES:
     * - @CacheEvict for cache removal
     * - Single entry eviction
     * 
     * @param id The student ID
     */
    @CacheEvict(cacheNames = "students", key = "#id")
    public void evictStudentCache(Long id) {
        log.info("Spring Cache: Evicting student cache for ID: {}", id);
        log.info("✅ Cache entry will be removed");
    }

    /**
     * Delete all student cache.
     * 
     * DEMONSTRATES:
     * - @CacheEvict with allEntries
     * - Bulk cache eviction
     * 
     */
    @CacheEvict(cacheNames = "students", allEntries = true)
    public void evictAllStudentCache() {
        log.info("Spring Cache: Evicting all student cache");
        log.warn("⚠️ ALL cache entries will be removed");
    }

    // ==================== @Caching EXAMPLES ====================

    /**
     * Complex caching with @Caching.
     * 
     * DEMONSTRATES:
     * - @Caching for multiple cache operations
     * - Combining @Cacheable and @CacheEvict
     * - Complex caching strategies
     * 
     * @param id The student ID
     * @return The student DTO
     */
    @Caching(
        cacheable = @Cacheable(cacheNames = "students", key = "#id"),
        evict = @CacheEvict(cacheNames = "allStudents", allEntries = true)
    )
    public Object getStudentWithComplexCaching(Long id) {
        log.info("Spring Cache: Complex caching for student ID: {}", id);
        log.info("🔄 Using @Caching with multiple annotations");
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        return Map.of(
            "id", id,
            "name", "Test Student " + id,
            "message", "Complex caching example"
        );
    }

    /**
     * Get Spring Cache explanation.
     * 
     * @return Explanation of Spring Cache annotations
     */
    public String getSpringCacheExplanation() {
        return """
            Spring Cache Annotations:
            
            WHY SPRING CACHE:
            - Declarative caching with annotations
            - Reduces boilerplate code
            - Production-ready approach
            - Easy to maintain and understand
            
            ANNOTATIONS:
            
            1. @Cacheable:
            - Caches method results
            - Skips execution if cache hit
            - Key generation customizable
            - Supports conditions and unless
            
            2. @CachePut:
            - Always executes method
            - Updates cache with result
            - Used for cache updates
            - Unlike @Cacheable, always runs
            
            3. @CacheEvict:
            - Removes entries from cache
            - Supports single or all entries
            - Used for cache invalidation
            - Can be triggered before/after method
            
            4. @Caching:
            - Combines multiple cache annotations
            - Complex caching strategies
            - Multiple cache operations
            - Fine-grained control
            
            KEY GENERATION:
            - Default: method parameters
            - Custom: SpEL expressions
            - Examples: #id, #user.id, #root.methodName
            
            CONDITIONAL CACHING:
            - condition: Caches when condition is true
            - unless: Doesn't cache when condition is true
            - SpEL expressions supported
            - Example: condition="#id > 10"
            
            CACHE CONFIGURATION:
            - TTL (Time To Live)
            - Cache names
            - Key prefixes
            - Serialization
            
            PRODUCTION USE CASES:
            - Standard Spring Boot applications
            - Microservices
            - Enterprise applications
            - High-performance systems
            
            ADVANTAGES:
            - Declarative approach
            - Minimal code changes
            - Consistent behavior
            - Easy to test
            
            DISADVANTAGES:
            - Less control than manual caching
            - Harder to debug complex scenarios
            - Annotation limitations
            - Learning curve for SpEL
            """;
    }
}