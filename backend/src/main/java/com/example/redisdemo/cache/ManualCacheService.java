package com.example.redisdemo.cache;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.entity.Student;
import com.example.redisdemo.exception.ResourceNotFoundException;
import com.example.redisdemo.metrics.CacheStatisticsService;
import com.example.redisdemo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Manual Cache Service
 * 
 * This service demonstrates manual caching implementation without @Cacheable.
 * This is Phase 3 of the project - Manual Cache Implementation.
 * 
 * WHY this service exists:
 * - Teaches the cache-aside pattern through manual implementation
 * - Shows exactly how caching works under the hood
 * - Demonstrates cache hit vs miss scenarios
 * - Provides performance comparison baseline
 * 
 * WHEN to use this service:
 * - Phase 3: Learning manual caching
 * - Understanding cache hit/miss patterns
 * - Performance comparison with Spring Cache
 * 
 * PRODUCTION USE CASES:
 * - Custom caching logic
 * - Complex cache invalidation strategies
 * - Multi-level caching
 * - Cache warming strategies
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ManualCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StudentRepository studentRepository;
    private final CacheStatisticsService cacheStatisticsService;

    private static final String CACHE_PREFIX = "manual:student:";
    private static final long CACHE_TTL_SECONDS = 300; // 5 minutes

    /**
     * Get student by ID with manual caching.
     * 
     * FLOW:
     * 1. Check Redis cache
     * 2. If found (CACHE HIT): Return cached data
     * 3. If not found (CACHE MISS): Fetch from MySQL, store in Redis, return data
     * 
     * WHY this method exists:
     * - Demonstrates the cache-aside pattern
     * - Shows cache hit vs miss scenarios
     * - Provides performance comparison baseline
     * 
     * WHEN to use this method:
     * - Phase 3: Learning manual caching
     * - Understanding cache hit/miss patterns
     * - Performance comparison with Spring Cache
     * 
     * PERFORMANCE CHARACTERISTICS:
     * - Cache Hit: ~1-2ms (Redis operation)
     * - Cache Miss: ~10-50ms (MySQL query + Redis set)
     * - First call: Always cache miss
     * - Subsequent calls: Cache hit (until TTL expires)
     * 
     * @param id The student ID
     * @return API response with student data and performance metadata
     */
    @Transactional(readOnly = true)
    public ApiResponse<StudentDTO> getStudentById(Long id) {
        long startTime = System.currentTimeMillis();
        String cacheKey = CACHE_PREFIX + id;
        
        log.info("🔵 MANUAL CACHE: Fetching student ID: {}", id);
        
        try {
            // Step 1: Check Redis cache
            long redisStartTime = System.currentTimeMillis();
            Object cachedStudent = redisTemplate.opsForValue().get(cacheKey);
            long redisTime = System.currentTimeMillis() - redisStartTime;
            
            if (cachedStudent != null) {
                // CACHE HIT
                long executionTime = System.currentTimeMillis() - startTime;
                
                cacheStatisticsService.recordCacheHit("MANUAL", executionTime);
                
                ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                        .executionTime(executionTime + " ms")
                        .databaseTime("0 ms")
                        .redisTime(redisTime + " ms")
                        .cacheHit(true)
                        .cacheMiss(false)
                        .cacheType("MANUAL")
                        .dataSource("REDIS")
                        .timestamp(System.currentTimeMillis())
                        .build();
                
                log.info("✅ CACHE HIT: Student found in Redis for ID: {} in {}ms", id, executionTime);
                
                StudentDTO studentDTO = (StudentDTO) cachedStudent;
                return ApiResponse.success(studentDTO, metadata, "Student fetched from manual cache");
            }
            
            // CACHE MISS - Fetch from database
            log.info("❌ CACHE MISS: Student not found in Redis for ID: {}", id);
            
            long dbStartTime = System.currentTimeMillis();
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
            long dbTime = System.currentTimeMillis() - dbStartTime;
            
            StudentDTO studentDTO = mapToDTO(student);
            
            // Store in Redis cache
            long redisWriteStartTime = System.currentTimeMillis();
            redisTemplate.opsForValue().set(cacheKey, studentDTO, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
            long redisWriteTime = System.currentTimeMillis() - redisWriteStartTime;
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            cacheStatisticsService.recordCacheMiss("MANUAL", executionTime);
            cacheStatisticsService.recordRedisWrite("MANUAL");
            
            ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                    .executionTime(executionTime + " ms")
                    .databaseTime(dbTime + " ms")
                    .redisTime((redisTime + redisWriteTime) + " ms")
                    .cacheHit(false)
                    .cacheMiss(true)
                    .cacheType("MANUAL")
                    .dataSource("MYSQL")
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            log.info("✅ Student stored in Redis cache with TTL: {} seconds", CACHE_TTL_SECONDS);
            log.info("🔵 MANUAL CACHE: Student fetched in {}ms (DB: {}ms, Redis: {}ms)", 
                    executionTime, dbTime, redisTime + redisWriteTime);
            
            return ApiResponse.success(studentDTO, metadata, "Student fetched from database and cached");
        } catch (ResourceNotFoundException e) {
            log.error("❌ Student not found: {}", e.getMessage());
            return ApiResponse.error("Student not found with ID: " + id);
        } catch (Exception e) {
            log.error("❌ ERROR in manual cache getStudentById: {}", e.getMessage(), e);
            return ApiResponse.error("Error fetching student: " + e.getMessage());
        }
    }

    /**
     * Get all students with manual caching.
     * 
     * @return API response with all students and performance metadata
     */
    @Transactional(readOnly = true)
    public ApiResponse<List<StudentDTO>> getAllStudents() {
        long startTime = System.currentTimeMillis();
        String cacheKey = CACHE_PREFIX + "all";
        
        log.info("🔵 MANUAL CACHE: Fetching all students");
        
        try {
            // Check cache
            long redisStartTime = System.currentTimeMillis();
            Object cachedStudents = redisTemplate.opsForValue().get(cacheKey);
            long redisTime = System.currentTimeMillis() - redisStartTime;
            
            if (cachedStudents != null) {
                long executionTime = System.currentTimeMillis() - startTime;
                
                cacheStatisticsService.recordCacheHit("MANUAL", executionTime);
                
                ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                        .executionTime(executionTime + " ms")
                        .databaseTime("0 ms")
                        .redisTime(redisTime + " ms")
                        .cacheHit(true)
                        .cacheMiss(false)
                        .cacheType("MANUAL")
                        .dataSource("REDIS")
                        .timestamp(System.currentTimeMillis())
                        .build();
                
                log.info("✅ CACHE HIT: All students found in Redis in {}ms", executionTime);
                
                @SuppressWarnings("unchecked")
                List<StudentDTO> studentDTOs = (List<StudentDTO>) cachedStudents;
                return ApiResponse.success(studentDTOs, metadata, "All students fetched from manual cache");
            }
            
            // Cache miss
            log.info("❌ CACHE MISS: All students not found in Redis");
            
            long dbStartTime = System.currentTimeMillis();
            List<Student> students = studentRepository.findAll();
            long dbTime = System.currentTimeMillis() - dbStartTime;
            
            List<StudentDTO> studentDTOs = students.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            
            // Store in cache
            long redisWriteStartTime = System.currentTimeMillis();
            redisTemplate.opsForValue().set(cacheKey, studentDTOs, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
            long redisWriteTime = System.currentTimeMillis() - redisWriteStartTime;
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            cacheStatisticsService.recordCacheMiss("MANUAL", executionTime);
            cacheStatisticsService.recordRedisWrite("MANUAL");
            
            ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                    .executionTime(executionTime + " ms")
                    .databaseTime(dbTime + " ms")
                    .redisTime((redisTime + redisWriteTime) + " ms")
                    .cacheHit(false)
                    .cacheMiss(true)
                    .cacheType("MANUAL")
                    .dataSource("MYSQL")
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            log.info("✅ All students stored in Redis cache");
            log.info("🔵 MANUAL CACHE: Fetched {} students in {}ms (DB: {}ms, Redis: {}ms)", 
                    studentDTOs.size(), executionTime, dbTime, redisTime + redisWriteTime);
            
            return ApiResponse.success(studentDTOs, metadata, "All students fetched from database and cached");
        } catch (Exception e) {
            log.error("❌ ERROR in manual cache getAllStudents: {}", e.getMessage(), e);
            return ApiResponse.error("Error fetching students: " + e.getMessage());
        }
    }

    /**
     * Delete student from cache by ID.
     * 
     * @param id The student ID
     * @return API response with eviction result
     */
    public ApiResponse<String> evictStudentCache(Long id) {
        String cacheKey = CACHE_PREFIX + id;
        
        log.info("🔵 MANUAL CACHE: Evicting student cache for ID: {}", id);
        
        Boolean deleted = redisTemplate.delete(cacheKey);
        
        if (Boolean.TRUE.equals(deleted)) {
            cacheStatisticsService.recordEviction("MANUAL");
            log.info("✅ Student cache evicted successfully for ID: {}", id);
            return ApiResponse.success("Cache evicted successfully", 
                    ApiResponse.Metadata.builder()
                            .executionTime("0 ms")
                            .databaseTime("0 ms")
                            .redisTime("0 ms")
                            .cacheHit(false)
                            .cacheMiss(false)
                            .cacheType("MANUAL")
                            .dataSource("REDIS")
                            .timestamp(System.currentTimeMillis())
                            .build(), 
                    "Student cache evicted successfully");
        } else {
            log.info("⚠️ No cache entry found for ID: {}", id);
            return ApiResponse.success("No cache entry found", 
                    ApiResponse.Metadata.builder()
                            .executionTime("0 ms")
                            .databaseTime("0 ms")
                            .redisTime("0 ms")
                            .cacheHit(false)
                            .cacheMiss(false)
                            .cacheType("MANUAL")
                            .dataSource("REDIS")
                            .timestamp(System.currentTimeMillis())
                            .build(), 
                    "No cache entry found");
        }
    }

    /**
     * Delete all students cache.
     * 
     * @return API response with eviction result
     */
    public ApiResponse<String> evictAllStudentsCache() {
        String cacheKey = CACHE_PREFIX + "all";
        
        log.info("🔵 MANUAL CACHE: Evicting all students cache");
        
        Boolean deleted = redisTemplate.delete(cacheKey);
        
        if (Boolean.TRUE.equals(deleted)) {
            cacheStatisticsService.recordEviction("MANUAL");
            log.info("✅ All students cache evicted successfully");
            return ApiResponse.success("All students cache evicted successfully", 
                    ApiResponse.Metadata.builder()
                            .executionTime("0 ms")
                            .databaseTime("0 ms")
                            .redisTime("0 ms")
                            .cacheHit(false)
                            .cacheMiss(false)
                            .cacheType("MANUAL")
                            .dataSource("REDIS")
                            .timestamp(System.currentTimeMillis())
                            .build(), 
                    "All students cache evicted successfully");
        } else {
            log.info("⚠️ No cache entry found for all students");
            return ApiResponse.success("No cache entry found", 
                    ApiResponse.Metadata.builder()
                            .executionTime("0 ms")
                            .databaseTime("0 ms")
                            .redisTime("0 ms")
                            .cacheHit(false)
                            .cacheMiss(false)
                            .cacheType("MANUAL")
                            .dataSource("REDIS")
                            .timestamp(System.currentTimeMillis())
                            .build(), 
                    "No cache entry found");
        }
    }

    /**
     * Map Student entity to StudentDTO.
     * 
     * @param student The entity to convert
     * @return The DTO representation
     */
    private StudentDTO mapToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setRollNumber(student.getRollNumber());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setCourse(student.getCourse());
        dto.setBranch(student.getBranch());
        dto.setSemester(student.getSemester());
        dto.setCgpa(student.getCgpa());
        dto.setCity(student.getCity());
        dto.setAddress(student.getAddress());
        dto.setStatus(student.getStatus());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        return dto;
    }

    /**
     * Get manual cache explanation.
     * 
     * @return Explanation of manual caching
     */
    public String getManualCacheExplanation() {
        return """
            Manual Cache Implementation (Cache-Aside Pattern):
            
            WHY MANUAL CACHING:
            - Full control over cache behavior
            - Custom cache logic and strategies
            - Understanding how caching works under the hood
            - Performance optimization opportunities
            
            CACHE-ASIDE PATTERN:
            
            1. READ OPERATION:
            - Application requests data
            - Check cache first
            - If cache hit: Return cached data
            - If cache miss: Fetch from database, update cache, return data
            
            2. WRITE OPERATION:
            - Update database
            - Invalidate or update cache
            - Ensures data consistency
            
            ADVANTAGES:
            - Simple to implement
            - Cache only contains needed data
            - Easy to understand and debug
            - Works with any cache provider
            
            DISADVANTAGES:
            - Cache stampede risk on cache miss
            - Stale data possible
            - Requires manual cache management
            - More code to maintain
            
            PERFORMANCE COMPARISON:
            - Cache Hit: ~1-2ms (Redis operation)
            - Cache Miss: ~10-50ms (Database + Redis)
            - No Cache: ~10-100ms (Database only)
            
            WHEN TO USE:
            - Simple caching requirements
            - Fine-grained cache control needed
            - Custom cache logic required
            - Learning caching concepts
            
            PRODUCTION USE CASES:
            - Custom caching strategies
            - Multi-level caching
            - Cache warming
            - Complex invalidation logic
            """;
    }
}