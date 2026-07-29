package com.example.redisdemo.fetch;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.entity.Student;
import com.example.redisdemo.exception.ResourceNotFoundException;
import com.example.redisdemo.metrics.CacheStatisticsService;
import com.example.redisdemo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * No Cache Service
 * 
 * This service demonstrates fetching data WITHOUT any Redis caching.
 * This serves as the baseline for performance comparison.
 * 
 * WHY this service exists:
 * - Provides baseline performance metrics (MySQL only)
 * - Demonstrates the performance impact of NOT using cache
 * - Shows how much time can be saved with Redis
 * - Essential for performance comparison analysis
 * 
 * WHEN to use this service:
 * - Performance baseline measurement
 * - Comparison with cached operations
 * - Understanding the cost of cache misses
 * - Learning why caching is important
 * 
 * PRODUCTION USE CASES:
 * - Real-time data that must always be fresh
 * - Financial transactions where consistency is critical
 * - When data changes frequently and staleness is unacceptable
 * - When cache infrastructure is down (fallback)
 * 
 * PERFORMANCE CHARACTERISTICS:
 * - Every request hits MySQL database
 * - No cache hits possible
 * - Response time: 10-100ms (depending on query complexity)
 * - Database load: High (every request)
 * 
 * USE CASES FOR NO CACHE:
 * - Real-time stock prices
 * - Bank account balances
 * - Inventory counts
 * - Session data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NoCacheService {

    private final StudentRepository studentRepository;
    private final CacheStatisticsService cacheStatisticsService;

    /**
     * Get student by ID without cache.
     * 
     * PERFORMANCE CHARACTERISTICS:
     * - Always queries MySQL
     * - Response time: 10-50ms
     * - Database load: 1 query per request
     * 
     * @param id The student ID
     * @return API response with student data and performance metadata
     */
    @Transactional(readOnly = true)
    public ApiResponse<StudentDTO> getStudentById(Long id) {
        long startTime = System.currentTimeMillis();
        long dbStartTime = System.currentTimeMillis();
        
        log.info("🔴 NO CACHE: Fetching student by ID: {}", id);
        
        try {
            // Always query database
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
            
            long dbTime = System.currentTimeMillis() - dbStartTime;
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Record statistics (cache miss since no cache)
            cacheStatisticsService.recordCacheMiss("NONE", executionTime);
            cacheStatisticsService.recordDatabaseWrite("NONE");
            
            StudentDTO studentDTO = mapToDTO(student);
            
            ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                    .executionTime(executionTime + " ms")
                    .databaseTime(dbTime + " ms")
                    .redisTime("0 ms")
                    .cacheHit(false)
                    .cacheMiss(true)
                    .cacheType("NONE")
                    .dataSource("MYSQL")
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            log.info("🔴 NO CACHE: Student fetched in {}ms (Database: {}ms)", executionTime, dbTime);
            
            return ApiResponse.success(studentDTO, metadata, "Student fetched without cache");
        } catch (ResourceNotFoundException e) {
            log.error("❌ Student not found: {}", e.getMessage());
            return ApiResponse.error("Student not found with ID: " + id);
        } catch (Exception e) {
            log.error("❌ ERROR in no cache getStudentById: {}", e.getMessage(), e);
            return ApiResponse.error("Error fetching student: " + e.getMessage());
        }
    }

    /**
     * Get all students without cache.
     * 
     * PERFORMANCE CHARACTERISTICS:
     * - Always queries MySQL
     * - Response time: 50-200ms (depending on data size)
     * - Database load: 1 query per request
     * 
     * @return API response with all students and performance metadata
     */
    @Transactional(readOnly = true)
    public ApiResponse<List<StudentDTO>> getAllStudents() {
        long startTime = System.currentTimeMillis();
        long dbStartTime = System.currentTimeMillis();
        
        log.info("🔴 NO CACHE: Fetching all students");
        
        try {
            List<Student> students = studentRepository.findAll();
            
            long dbTime = System.currentTimeMillis() - dbStartTime;
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Record statistics
            cacheStatisticsService.recordCacheMiss("NONE", executionTime);
            cacheStatisticsService.recordDatabaseWrite("NONE");
            
            List<StudentDTO> studentDTOs = students.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            
            ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                    .executionTime(executionTime + " ms")
                    .databaseTime(dbTime + " ms")
                    .redisTime("0 ms")
                    .cacheHit(false)
                    .cacheMiss(true)
                    .cacheType("NONE")
                    .dataSource("MYSQL")
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            log.info("🔴 NO CACHE: Fetched {} students in {}ms (Database: {}ms)", 
                    studentDTOs.size(), executionTime, dbTime);
            
            return ApiResponse.success(studentDTOs, metadata, "All students fetched without cache");
        } catch (Exception e) {
            log.error("❌ ERROR in no cache getAllStudents: {}", e.getMessage(), e);
            return ApiResponse.error("Error fetching students: " + e.getMessage());
        }
    }

    /**
     * Get students by course without cache.
     * 
     * @param course The course name
     * @return API response with students in the course and performance metadata
     */
    @Transactional(readOnly = true)
    public ApiResponse<List<StudentDTO>> getStudentsByCourse(String course) {
        long startTime = System.currentTimeMillis();
        long dbStartTime = System.currentTimeMillis();
        
        log.info("🔴 NO CACHE: Fetching students by course: {}", course);
        
        List<Student> students = studentRepository.findByCourse(course);
        
        long dbTime = System.currentTimeMillis() - dbStartTime;
        long executionTime = System.currentTimeMillis() - startTime;
        
        // Record statistics
        cacheStatisticsService.recordCacheMiss("NONE", executionTime);
        cacheStatisticsService.recordDatabaseWrite("NONE");
        
        List<StudentDTO> studentDTOs = students.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime(dbTime + " ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(true)
                .cacheType("NONE")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🔴 NO CACHE: Fetched {} students for course {} in {}ms (Database: {}ms)", 
                studentDTOs.size(), course, executionTime, dbTime);
        
        return ApiResponse.success(studentDTOs, metadata, "Students by course fetched without cache");
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