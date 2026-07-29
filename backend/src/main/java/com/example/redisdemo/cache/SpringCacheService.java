package com.example.redisdemo.cache;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.entity.Student;
import com.example.redisdemo.exception.ResourceNotFoundException;
import com.example.redisdemo.metrics.CacheStatisticsService;
import com.example.redisdemo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Spring Cache Service
 * 
 * This service demonstrates Spring's declarative caching annotations.
 * This is Phase 4 of the project - Spring Cache Annotations.
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
@Service
@RequiredArgsConstructor
@Slf4j
public class SpringCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StudentRepository studentRepository;
    private final CacheStatisticsService cacheStatisticsService;

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
     * @return API response with student data and performance metadata
     */
    @Cacheable(cacheNames = "students", key = "#id")
    @Transactional(readOnly = true)
    public ApiResponse<StudentDTO> getStudentById(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("🟢 SPRING CACHE: Fetching student ID: {}", id);
        log.info("🔄 This method executes only on cache miss");
        
        long dbStartTime = System.currentTimeMillis();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        long dbTime = System.currentTimeMillis() - dbStartTime;
        
        StudentDTO studentDTO = mapToDTO(student);
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheMiss("SPRING", executionTime);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SPRING")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🟢 SPRING CACHE: Student fetched in {}ms (Database: {}ms)", executionTime, dbTime);
        
        return ApiResponse.success(studentDTO, metadata, "Student fetched from database and cached with @Cacheable");
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
     * @return API response with student data and performance metadata
     */
    @Cacheable(cacheNames = "students", key = "#id", condition = "#id > 10")
    @Transactional(readOnly = true)
    public ApiResponse<StudentDTO> getStudentByIdWithCondition(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("🟢 SPRING CACHE: Fetching student ID: {} with condition (id > 10)", id);
        log.info("🔄 This method executes only on cache miss AND when condition is true");
        
        long dbStartTime = System.currentTimeMillis();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        long dbTime = System.currentTimeMillis() - dbStartTime;
        
        StudentDTO studentDTO = mapToDTO(student);
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheMiss("SPRING", executionTime);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SPRING")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🟢 SPRING CACHE: Student fetched in {}ms (Database: {}ms)", executionTime, dbTime);
        
        return ApiResponse.success(studentDTO, metadata, "Student fetched with conditional caching (id > 10)");
    }

    /**
     * Get all students with @Cacheable.
     * 
     * @return API response with all students and performance metadata
     */
    @Cacheable(cacheNames = "allStudents")
    @Transactional(readOnly = true)
    public ApiResponse<List<StudentDTO>> getAllStudents() {
        long startTime = System.currentTimeMillis();
        
        log.info("🟢 SPRING CACHE: Fetching all students");
        log.info("🔄 This method executes only on cache miss");
        
        long dbStartTime = System.currentTimeMillis();
        List<Student> students = studentRepository.findAll();
        long dbTime = System.currentTimeMillis() - dbStartTime;
        
        List<StudentDTO> studentDTOs = students.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheMiss("SPRING", executionTime);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SPRING")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🟢 SPRING CACHE: Fetched {} students in {}ms (Database: {}ms)", 
                studentDTOs.size(), executionTime, dbTime);
        
        return ApiResponse.success(studentDTOs, metadata, "All students fetched and cached with @Cacheable");
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
    @Transactional
    public ApiResponse<StudentDTO> updateStudent(Long id, StudentDTO studentDTO) {
        long startTime = System.currentTimeMillis();
        
        log.info("🟢 SPRING CACHE: Updating student ID: {}", id);
        log.info("🔄 This method ALWAYS executes (unlike @Cacheable)");
        log.info("✅ Result will be cached");
        
        long dbStartTime = System.currentTimeMillis();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        
        // Update student fields
        if (studentDTO.getName() != null) student.setName(studentDTO.getName());
        if (studentDTO.getEmail() != null) student.setEmail(studentDTO.getEmail());
        if (studentDTO.getPhone() != null) student.setPhone(studentDTO.getPhone());
        if (studentDTO.getCourse() != null) student.setCourse(studentDTO.getCourse());
        if (studentDTO.getBranch() != null) student.setBranch(studentDTO.getBranch());
        if (studentDTO.getSemester() != null) student.setSemester(studentDTO.getSemester());
        if (studentDTO.getCgpa() != null) student.setCgpa(studentDTO.getCgpa());
        if (studentDTO.getCity() != null) student.setCity(studentDTO.getCity());
        if (studentDTO.getAddress() != null) student.setAddress(studentDTO.getAddress());
        if (studentDTO.getStatus() != null) student.setStatus(studentDTO.getStatus());
        
        Student updatedStudent = studentRepository.save(student);
        long dbTime = System.currentTimeMillis() - dbStartTime;
        
        StudentDTO updatedDTO = mapToDTO(updatedStudent);
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheMiss("SPRING", executionTime);
        cacheStatisticsService.recordDatabaseWrite("SPRING");
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SPRING")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🟢 SPRING CACHE: Student updated in {}ms (Database: {}ms)", executionTime, dbTime);
        
        return ApiResponse.success(updatedDTO, metadata, "Student updated and cache refreshed with @CachePut");
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
     * @return API response with eviction result
     */
    @CacheEvict(cacheNames = "students", key = "#id")
    public ApiResponse<String> evictStudentCache(Long id) {
        log.info("🟢 SPRING CACHE: Evicting student cache for ID: {}", id);
        log.info("✅ Cache entry will be removed");
        
        cacheStatisticsService.recordEviction("SPRING");
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime("0 ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("SPRING")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        return ApiResponse.success("Cache evicted successfully", metadata, "Student cache evicted with @CacheEvict");
    }

    /**
     * Delete all student cache.
     * 
     * DEMONSTRATES:
     * - @CacheEvict with allEntries
     * - Bulk cache eviction
     * 
     * @return API response with eviction result
     */
    @CacheEvict(cacheNames = "students", allEntries = true)
    public ApiResponse<String> evictAllStudentCache() {
        log.info("🟢 SPRING CACHE: Evicting all student cache");
        log.warn("⚠️ ALL cache entries will be removed");
        
        cacheStatisticsService.recordEviction("SPRING");
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime("0 ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("SPRING")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        return ApiResponse.success("All cache evicted successfully", metadata, "All student cache evicted with @CacheEvict");
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
     * @return API response with student data and performance metadata
     */
    @Caching(
        cacheable = @Cacheable(cacheNames = "students", key = "#id"),
        evict = @CacheEvict(cacheNames = "allStudents", allEntries = true)
    )
    @Transactional(readOnly = true)
    public ApiResponse<StudentDTO> getStudentWithComplexCaching(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("🟢 SPRING CACHE: Complex caching for student ID: {}", id);
        log.info("🔄 Using @Caching with multiple annotations");
        
        long dbStartTime = System.currentTimeMillis();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        long dbTime = System.currentTimeMillis() - dbStartTime;
        
        StudentDTO studentDTO = mapToDTO(student);
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheMiss("SPRING", executionTime);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SPRING")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🟢 SPRING CACHE: Student fetched in {}ms (Database: {}ms)", executionTime, dbTime);
        
        return ApiResponse.success(studentDTO, metadata, "Student fetched with complex caching (@Caching)");
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