package com.example.redisdemo.service;

import com.example.redisdemo.dto.StudentCreateDTO;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.dto.StudentUpdateDTO;
import com.example.redisdemo.entity.Student;
import com.example.redisdemo.exception.ResourceNotFoundException;
import com.example.redisdemo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Student Service
 * 
 * This service handles all business logic for student operations.
 * In Phase 1, this uses direct database access without any caching.
 * This establishes the baseline performance for comparison with cached versions.
 * 
 * WHY this service exists:
 * - Encapsulates business logic separate from controller and repository
 * - Provides a single place for student-related operations
 * - Enables transaction management
 * - Will be used to demonstrate performance differences with/without caching
 * - Serves as the foundation for adding caching in later phases
 * 
 * WHEN to use this service:
 * - In Phase 1: Direct database operations (baseline)
 * - In Phase 3: Will be enhanced with manual caching
 * - In Phase 4: Will be enhanced with Spring Cache annotations
 * - In all phases: Core business logic layer
 * 
 * PRODUCTION USE CASES:
 * - Student management operations
 * - Business rule enforcement
 * - Transaction management
 * - Data validation
 * 
 * DESIGN PATTERNS USED:
 * - Service Layer Pattern: Separates business logic from presentation
 * - Dependency Injection: Constructor injection for dependencies
 * - Transaction Management: @Transactional for data consistency
 */
// @Service  // Disabled for Redis-only testing
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * Create a new student.
     * 
     * WHY this method exists:
     * - Handles student creation with validation
     * - Measures baseline performance for create operations
     * - Will be compared with cached create operations in later phases
     * 
     * WHEN to use this method:
     * - Creating new student records
     * - Student registration
     * - Bulk student imports
     * 
     * PERFORMANCE CONSIDERATIONS:
     * - Phase 1: Direct database insert (baseline)
     * - Later phases: May include cache invalidation
     * - Create operations typically don't benefit from caching
     * 
     * @param createDTO The student creation data
     * @return The created student as DTO
     */
    @Transactional
    public StudentDTO createStudent(StudentCreateDTO createDTO) {
        log.info("Creating student with email: {}", createDTO.getEmail());
        
        // Check if email already exists
        if (studentRepository.existsByEmail(createDTO.getEmail())) {
            throw new IllegalArgumentException("Student with email " + createDTO.getEmail() + " already exists");
        }

        // Convert DTO to entity manually
        Student student = new Student();
        student.setName(createDTO.getName());
        student.setEmail(createDTO.getEmail());
        student.setCourse(createDTO.getCourse());
        student.setBranch(createDTO.getBranch());
        student.setSemester(createDTO.getSemester());
        student.setCgpa(createDTO.getCgpa());
        student.setCity(createDTO.getCity());
        student.setPhone(createDTO.getPhone());

        // Save to database
        Student savedStudent = studentRepository.save(student);

        log.info("Student created successfully with ID: {}", savedStudent.getId());

        // Convert entity to DTO manually
        return mapToDTO(savedStudent);
    }

    /**
     * Get a student by ID.
     * 
     * WHY this method exists:
     * - Retrieves single student by primary key
     * - Most common operation to benefit from caching
     * - Establishes baseline for read performance
     * 
     * WHEN to use this method:
     * - Viewing student details
     * - Profile pages
     * - Edit forms
     * 
     * PERFORMANCE CONSIDERATIONS:
     * - Phase 1: Direct database query (baseline)
     * - Phase 3: Will implement manual caching
     * - Phase 4: Will use @Cacheable annotation
     * - This is THE operation to cache for maximum benefit
     * 
     * @param id The student ID
     * @return The student as DTO
     * @throws ResourceNotFoundException if student not found
     */
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        log.info("Fetching student by ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        log.info("Student fetched successfully");

        return mapToDTO(student);
    }

    /**
     * Get a student by email.
     * 
     * WHY this method exists:
     * - Email is a natural unique identifier
     * - Common lookup pattern in applications
     * - Good candidate for caching by email
     * 
     * WHEN to use this method:
     * - User login scenarios
     * - Email-based lookups
     * - Checking user existence
     * 
     * PERFORMANCE CONSIDERATIONS:
     * - Phase 1: Direct database query with index (baseline)
     * - Later phases: Will be cached by email
     * - Email lookups are very common in production
     * 
     * @param email The student email
     * @return The student as DTO
     * @throws ResourceNotFoundException if student not found
     */
    @Transactional(readOnly = true)
    public StudentDTO getStudentByEmail(String email) {
        log.info("Fetching student by email: {}", email);
        
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));

        log.info("Student fetched successfully");

        return mapToDTO(student);
    }

    /**
     * Get all students.
     * 
     * WHY this method exists:
     * - Retrieves all students from database
     * - Expensive operation - good candidate for caching
     * - Establishes baseline for list retrieval performance
     * 
     * WHEN to use this method:
     * - Student listing pages
     * - Admin panels
     * - Bulk operations
     * 
     * PERFORMANCE CONSIDERATIONS:
     * - Phase 1: Direct database query (baseline)
     * - Later phases: Will be cached with short TTL
     * - List operations can be expensive with large datasets
     * - Consider pagination for production
     * 
     * @return List of all students as DTOs
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        log.info("Fetching all students");
        
        List<Student> students = studentRepository.findAll();

        log.info("Fetched {} students", students.size());

        return students.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get students by course.
     * 
     * WHY this method exists:
     * - Demonstrates filtered queries
     * - Good candidate for caching with course as key
     * - Common real-world query pattern
     * 
     * WHEN to use this method:
     * - Course-specific student lists
     * - Department views
     * - Course analytics
     * 
     * PERFORMANCE CONSIDERATIONS:
     * - Phase 1: Direct database query (baseline)
     * - Later phases: Will be cached by course name
     * - Filtered queries benefit from caching
     * 
     * @param course The course name
     * @return List of students in the course
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByCourse(String course) {
        log.info("Fetching students by course: {}", course);
        
        List<Student> students = studentRepository.findByCourse(course);

        log.info("Fetched {} students for course: {}", students.size(), course);

        return students.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update a student.
     * 
     * WHY this method exists:
     * - Handles student updates with validation
     * - Demonstrates update operations performance
     * - Will require cache invalidation in later phases
     * 
     * WHEN to use this method:
     * - Updating student information
     * - Profile updates
     * - Data corrections
     * 
     * PERFORMANCE CONSIDERATIONS:
     * - Phase 1: Direct database update (baseline)
     * - Later phases: Will include cache eviction
     * - Update operations need cache invalidation
     * - Consider write-through caching strategy
     * 
     * @param id The student ID
     * @param updateDTO The update data
     * @return The updated student as DTO
     * @throws ResourceNotFoundException if student not found
     */
    @Transactional
    public StudentDTO updateStudent(Long id, StudentUpdateDTO updateDTO) {
        log.info("Updating student with ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        // Check if email is being updated and if it already exists
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(student.getEmail())) {
            if (studentRepository.existsByEmail(updateDTO.getEmail())) {
                throw new IllegalArgumentException("Student with email " + updateDTO.getEmail() + " already exists");
            }
        }

        // Update only non-null fields
        if (updateDTO.getName() != null) {
            student.setName(updateDTO.getName());
        }
        if (updateDTO.getEmail() != null) {
            student.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getCourse() != null) {
            student.setCourse(updateDTO.getCourse());
        }
        if (updateDTO.getBranch() != null) {
            student.setBranch(updateDTO.getBranch());
        }
        if (updateDTO.getSemester() != null) {
            student.setSemester(updateDTO.getSemester());
        }
        if (updateDTO.getCgpa() != null) {
            student.setCgpa(updateDTO.getCgpa());
        }
        if (updateDTO.getCity() != null) {
            student.setCity(updateDTO.getCity());
        }
        if (updateDTO.getPhone() != null) {
            student.setPhone(updateDTO.getPhone());
        }

        // Save to database
        Student updatedStudent = studentRepository.save(student);

        log.info("Student updated successfully with ID: {}", updatedStudent.getId());

        return mapToDTO(updatedStudent);
    }

    /**
     * Delete a student.
     * 
     * WHY this method exists:
     * - Handles student deletion
     * - Demonstrates delete operations performance
     * - Will require cache invalidation in later phases
     * 
     * WHEN to use this method:
     * - Removing student records
     * - Account deletion
     * - Data cleanup
     * 
     * PERFORMANCE CONSIDERATIONS:
     * - Phase 1: Direct database delete (baseline)
     * - Later phases: Will include cache eviction
     * - Delete operations must invalidate cache
     * - Consider soft deletes for production
     * 
     * @param id The student ID
     * @throws ResourceNotFoundException if student not found
     */
    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        studentRepository.delete(student);
        
        log.info("Student deleted successfully with ID: {}", id);
    }

    /**
     * Get students with CGPA >= threshold.
     * 
     * WHY this method exists:
     * - Demonstrates comparison queries
     * - Good candidate for caching with composite key
     * - Real-world use case: finding high performers
     * 
     * WHEN to use this method:
     * - Dean's list generation
     * - Scholarship eligibility
     * - Performance analytics
     * 
     * PERFORMANCE CONSIDERATIONS:
     * - Phase 1: Direct database query (baseline)
     * - Later phases: Will be cached with threshold as part of key
     * - Range queries may need special caching strategy
     * 
     * @param cgpa The minimum CGPA threshold
     * @return List of students with CGPA >= threshold
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByCgpaGreaterThanEqual(Double cgpa) {
        log.info("Fetching students with CGPA >= {}", cgpa);
        
        List<Student> students = studentRepository.findByCgpaGreaterThanEqual(cgpa);

        log.info("Fetched {} students with CGPA >= {}", students.size(), cgpa);

        return students.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Count students by course.
     * 
     * WHY this method exists:
     * - Demonstrates aggregate queries
     * - Excellent candidate for caching
     * - Real-world use case: statistics
     * 
     * WHEN to use this method:
     * - Dashboard statistics
     * - Course capacity planning
     * - Analytics
     * 
     * PERFORMANCE CONSIDERATIONS:
     * - Phase 1: Direct database count query (baseline)
     * - Later phases: Will be cached with long TTL
     * - Count operations are perfect for caching
     * 
     * @param course The course name
     * @return Number of students in the course
     */
    @Transactional(readOnly = true)
    public long countStudentsByCourse(String course) {
        log.info("Counting students in course: {}", course);
        
        long count = studentRepository.countByCourse(course);

        log.info("Counted {} students in course: {}", count, course);

        return count;
    }

    /**
     * Manual mapping from Student entity to StudentDTO.
     * 
     * WHY this method exists:
     * - Phase 1: Manual mapping without MapStruct dependency
     * - Simple and straightforward for basic CRUD
     * - Easy to understand for learning purposes
     * 
     * WHEN to use this method:
     * - Phase 1: Basic CRUD operations
     * - Can be replaced with MapStruct in later phases if needed
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
