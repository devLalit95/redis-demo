package com.example.redisdemo.simulation;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Simulation Service
 * 
 * This service provides simulation APIs for testing and learning.
 * 
 * WHY this service exists:
 * - Simulates various performance scenarios
 * - Demonstrates cache behavior under different conditions
 * - Provides controlled testing environment
 * - Essential for learning Redis concepts
 * 
 * WHEN to use this service:
 * - Performance testing
 * - Learning cache behavior
 * - Demonstrating Redis concepts
 * - Load testing scenarios
 * 
 * PRODUCTION USE CASES:
 * - Performance testing
 * - Load testing
 * - Cache behavior validation
 * - Development and testing
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SimulationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StudentRepository studentRepository;
    private final CacheStatisticsService cacheStatisticsService;

    /**
     * Simulate slow database.
     * 
     * DEMONSTRATES:
     * - Impact of slow database on performance
     * - Benefits of caching in such scenarios
     * - Performance degradation
     * 
     * @param delayMs Delay in milliseconds to simulate slow database
     * @param id Student ID
     * @return API response with simulated slow database fetch
     */
    @Transactional(readOnly = true)
    public ApiResponse<StudentDTO> simulateSlowDatabase(Long delayMs, Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("🎮 SIMULATION: Simulating slow database ({}ms delay) for student ID: {}", delayMs, id);
        
        try {
            // Simulate slow database
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long dbStartTime = System.currentTimeMillis();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        long dbTime = System.currentTimeMillis() - dbStartTime + delayMs;
        
        StudentDTO studentDTO = mapToDTO(student);
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheMiss("SIMULATION", executionTime);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SIMULATION")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🎮 SIMULATION: Slow database fetch completed in {}ms (including {}ms delay)", 
                executionTime, delayMs);
        
        return ApiResponse.success(studentDTO, metadata, 
                "Simulated slow database fetch (" + delayMs + "ms delay)");
    }

    /**
     * Simulate fast Redis.
     * 
     * DEMONSTRATES:
     * - Speed of Redis operations
     * - Performance benefits of caching
     * - Cache hit scenario
     * 
     * @param id Student ID
     * @return API response with fast Redis fetch
     */
    public ApiResponse<StudentDTO> simulateFastRedis(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("🎮 SIMULATION: Simulating fast Redis for student ID: {}", id);
        
        String cacheKey = "simulation:fast:student:" + id;
        
        // First, try to get from cache
        long redisStartTime = System.currentTimeMillis();
        Object cachedStudent = redisTemplate.opsForValue().get(cacheKey);
        long redisTime = System.currentTimeMillis() - redisStartTime;
        
        if (cachedStudent != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            cacheStatisticsService.recordCacheHit("SIMULATION", executionTime);
            
            ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                    .executionTime(executionTime + " ms")
                    .databaseTime("0 ms")
                    .redisTime(redisTime + " ms")
                    .cacheHit(true)
                    .cacheMiss(false)
                    .cacheType("SIMULATION")
                    .dataSource("REDIS")
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            log.info("🎮 SIMULATION: Fast Redis cache hit in {}ms", executionTime);
            
            return ApiResponse.success((StudentDTO) cachedStudent, metadata, 
                    "Fast Redis cache hit");
        }
        
        // Cache miss - fetch from database and cache
        log.info("🎮 SIMULATION: Cache miss, fetching from database");
        
        long dbStartTime = System.currentTimeMillis();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        long dbTime = System.currentTimeMillis() - dbStartTime;
        
        StudentDTO studentDTO = mapToDTO(student);
        
        // Store in cache
        long redisWriteStartTime = System.currentTimeMillis();
        redisTemplate.opsForValue().set(cacheKey, studentDTO, 5, TimeUnit.MINUTES);
        long redisWriteTime = System.currentTimeMillis() - redisWriteStartTime;
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheMiss("SIMULATION", executionTime);
        cacheStatisticsService.recordRedisWrite("SIMULATION");
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime((redisTime + redisWriteTime) + " ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SIMULATION")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🎮 SIMULATION: Fast Redis cache miss and set in {}ms", executionTime);
        
        return ApiResponse.success(studentDTO, metadata, 
                "Fast Redis cache miss and set");
    }

    /**
     * Simulate cache miss.
     * 
     * DEMONSTRATES:
     * - Cache miss scenario
     * - Fallback to database
     * - Performance impact
     * 
     * @param id Student ID
     * @return API response demonstrating cache miss
     */
    @Transactional(readOnly = true)
    public ApiResponse<StudentDTO> simulateCacheMiss(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("🎮 SIMULATION: Simulating cache miss for student ID: {}", id);
        
        // Clear cache to ensure miss
        String cacheKey = "simulation:miss:student:" + id;
        redisTemplate.delete(cacheKey);
        
        // Fetch from database
        long dbStartTime = System.currentTimeMillis();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        long dbTime = System.currentTimeMillis() - dbStartTime;
        
        StudentDTO studentDTO = mapToDTO(student);
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheMiss("SIMULATION", executionTime);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SIMULATION")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🎮 SIMULATION: Cache miss demonstrated in {}ms", executionTime);
        
        return ApiResponse.success(studentDTO, metadata, "Cache miss simulated");
    }

    /**
     * Simulate cache hit.
     * 
     * DEMONSTRATES:
     * - Cache hit scenario
     * - Performance benefits
     * - Speed improvement
     * 
     * @param id Student ID
     * @return API response demonstrating cache hit
     */
    public ApiResponse<StudentDTO> simulateCacheHit(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("🎮 SIMULATION: Simulating cache hit for student ID: {}", id);
        
        // Ensure data is in cache
        String cacheKey = "simulation:hit:student:" + id;
        
        // Check if already cached
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        
        if (cached == null) {
            // Cache miss - fetch and cache
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
            StudentDTO studentDTO = mapToDTO(student);
            redisTemplate.opsForValue().set(cacheKey, studentDTO, 5, TimeUnit.MINUTES);
            cached = studentDTO;
        }
        
        // Now demonstrate cache hit
        long redisStartTime = System.currentTimeMillis();
        StudentDTO studentDTO = (StudentDTO) redisTemplate.opsForValue().get(cacheKey);
        long redisTime = System.currentTimeMillis() - redisStartTime;
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheHit("SIMULATION", executionTime);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(redisTime + " ms")
                .cacheHit(true)
                .cacheMiss(false)
                .cacheType("SIMULATION")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🎮 SIMULATION: Cache hit demonstrated in {}ms", executionTime);
        
        return ApiResponse.success(studentDTO, metadata, "Cache hit simulated");
    }

    /**
     * Simulate TTL expiration.
     * 
     * DEMONSTRATES:
     * - TTL behavior
     * - Key expiration
     * - Cache invalidation
     * 
     * @param id Student ID
     * @param ttlSeconds TTL in seconds
     * @return API response demonstrating TTL expiration
     */
    @Transactional(readOnly = true)
    public ApiResponse<StudentDTO> simulateTTLExpiration(Long id, Long ttlSeconds) {
        long startTime = System.currentTimeMillis();
        
        log.info("🎮 SIMULATION: Simulating TTL expiration for student ID: {} (TTL: {}s)", 
                id, ttlSeconds);
        
        String cacheKey = "simulation:ttl:student:" + id;
        
        // Set data with short TTL
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        StudentDTO studentDTO = mapToDTO(student);
        
        redisTemplate.opsForValue().set(cacheKey, studentDTO, ttlSeconds, TimeUnit.SECONDS);
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(executionTime + " ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("SIMULATION")
                .dataSource("REDIS")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🎮 SIMULATION: Data cached with TTL of {} seconds", ttlSeconds);
        
        return ApiResponse.success(studentDTO, metadata, 
                "Data cached with TTL: " + ttlSeconds + " seconds (will expire automatically)");
    }

    /**
     * Simulate database failure.
     * 
     * DEMONSTRATES:
     * - Graceful degradation
     * - Cache as fallback
     * - Error handling
     * 
     * @param id Student ID
     * @return API response with cached data or error
     */
    public ApiResponse<StudentDTO> simulateDatabaseFailure(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("🎮 SIMULATION: Simulating database failure for student ID: {}", id);
        
        String cacheKey = "simulation:failure:student:" + id;
        
        // Try to get from cache (fallback)
        long redisStartTime = System.currentTimeMillis();
        Object cachedStudent = redisTemplate.opsForValue().get(cacheKey);
        long redisTime = System.currentTimeMillis() - redisStartTime;
        
        if (cachedStudent != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            cacheStatisticsService.recordCacheHit("SIMULATION", executionTime);
            
            ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                    .executionTime(executionTime + " ms")
                    .databaseTime("0 ms")
                    .redisTime(redisTime + " ms")
                    .cacheHit(true)
                    .cacheMiss(false)
                    .cacheType("SIMULATION")
                    .dataSource("REDIS")
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            log.info("🎮 SIMULATION: Database failure - served from cache in {}ms", executionTime);
            
            return ApiResponse.success((StudentDTO) cachedStudent, metadata, 
                    "Database failure - served from cache (graceful degradation)");
        }
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime(redisTime + " ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SIMULATION")
                .dataSource("NONE")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.warn("🎮 SIMULATION: Database failure - no cache available");
        
        return ApiResponse.error("Database failure and no cache available");
    }

    /**
     * Simulate Redis down.
     * 
     * DEMONSTRATES:
     * - Redis unavailability
     * - Fallback to database
     * - System resilience
     * 
     * @param id Student ID
     * @return API response with database data or error
     */
    @Transactional(readOnly = true)
    public ApiResponse<StudentDTO> simulateRedisDown(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("🎮 SIMULATION: Simulating Redis down for student ID: {}", id);
        
        // Directly fetch from database (simulating Redis down)
        long dbStartTime = System.currentTimeMillis();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        long dbTime = System.currentTimeMillis() - dbStartTime;
        
        StudentDTO studentDTO = mapToDTO(student);
        long executionTime = System.currentTimeMillis() - startTime;
        
        cacheStatisticsService.recordCacheMiss("SIMULATION", executionTime);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("SIMULATION")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🎮 SIMULATION: Redis down - served from database in {}ms", executionTime);
        
        return ApiResponse.success(studentDTO, metadata, 
                "Redis down - served from database (fallback)");
    }

    /**
     * Load test comparison.
     * 
     * DEMONSTRATES:
     * - Performance comparison under load
     * - Cache vs no-cache performance
     * - Load testing scenarios
     * 
     * @param requestCount Number of requests to simulate
     * @param mode Fetch mode (NO_CACHE, MANUAL, SPRING)
     * @return API response with load test results
     */
    public ApiResponse<Map<String, Object>> loadTestComparison(int requestCount, String mode) {
        long startTime = System.currentTimeMillis();
        
        log.info("🎮 SIMULATION: Load test - {} requests in {} mode", requestCount, mode);
        
        List<Long> responseTimes = new ArrayList<>();
        int cacheHits = 0;
        int cacheMisses = 0;
        
        // Get random student IDs for testing
        List<Long> studentIds = studentRepository.findAll().stream()
                .limit(Math.min(10, requestCount))
                .map(Student::getId)
                .collect(Collectors.toList());
        
        if (studentIds.isEmpty()) {
            return ApiResponse.error("No students found for load testing");
        }
        
        for (int i = 0; i < requestCount; i++) {
            Long id = studentIds.get(i % studentIds.size());
            long requestStart = System.currentTimeMillis();
            
            try {
                switch (mode.toUpperCase()) {
                    case "NO_CACHE":
                        studentRepository.findById(id).orElse(null);
                        cacheMisses++;
                        break;
                    case "MANUAL":
                        String manualKey = "manual:student:" + id;
                        Object cached = redisTemplate.opsForValue().get(manualKey);
                        if (cached != null) {
                            cacheHits++;
                        } else {
                            studentRepository.findById(id).orElse(null);
                            cacheMisses++;
                        }
                        break;
                    case "SPRING":
                        // Simulate Spring Cache behavior
                        String springKey = "students::" + id;
                        Object springCached = redisTemplate.opsForValue().get(springKey);
                        if (springCached != null) {
                            cacheHits++;
                        } else {
                            studentRepository.findById(id).orElse(null);
                            cacheMisses++;
                        }
                        break;
                    default:
                        studentRepository.findById(id).orElse(null);
                        cacheMisses++;
                }
            } catch (Exception e) {
                log.error("Error during load test", e);
            }
            
            responseTimes.add(System.currentTimeMillis() - requestStart);
        }
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        // Calculate statistics
        double avgResponseTime = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        long minResponseTime = responseTimes.stream().mapToLong(Long::longValue).min().orElse(0);
        long maxResponseTime = responseTimes.stream().mapToLong(Long::longValue).max().orElse(0);
        double hitRatio = requestCount > 0 ? (double) cacheHits / requestCount * 100 : 0;
        
        Map<String, Object> results = new HashMap<>();
        results.put("requestCount", requestCount);
        results.put("mode", mode);
        results.put("avgResponseTime", String.format("%.2fms", avgResponseTime));
        results.put("minResponseTime", minResponseTime + "ms");
        results.put("maxResponseTime", maxResponseTime + "ms");
        results.put("cacheHits", cacheHits);
        results.put("cacheMisses", cacheMisses);
        results.put("hitRatio", String.format("%.2f%%", hitRatio));
        results.put("totalTime", executionTime + "ms");
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("SIMULATION")
                .dataSource("MIXED")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🎮 SIMULATION: Load test completed - Avg: {}ms, Hit Ratio: {}%", 
                String.format("%.2f", avgResponseTime), String.format("%.2f", hitRatio));
        
        return ApiResponse.success(results, metadata, "Load test completed for " + mode + " mode");
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
}