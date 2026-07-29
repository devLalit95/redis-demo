package com.example.redisdemo.controller;

import com.example.redisdemo.dto.StudentCreateDTO;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.dto.StudentUpdateDTO;
import com.example.redisdemo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Student Controller
 * 
 * This controller provides HTTP endpoints for student CRUD operations.
 * This is the main REST API for the application.
 * 
 * WHY this controller exists:
 * - Provides REST API for student management
 * - Implements CRUD operations with caching
 * - Demonstrates Spring Boot REST patterns
 * - Shows proper API design principles
 * 
 * WHEN to use this controller:
 * - Main student management operations
 * - Testing caching with database operations
 * - Learning Spring Boot REST patterns
 * 
 * PRODUCTION USE CASES:
 * - Student management system
 * - REST API implementation
 * - Caching in production applications
 * - Standard CRUD operations
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Management", description = "Complete CRUD operations for students")
public class StudentController {

    private final StudentService studentService;

    /**
     * Create a new student.
     * 
     * @param createDTO The student creation data
     * @return Created student DTO
     */
    @Operation(summary = "Create a new student", description = "Create a new student record")
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentCreateDTO createDTO) {
        StudentDTO studentDTO = studentService.createStudent(createDTO);
        return ResponseEntity.ok(studentDTO);
    }

    /**
     * Get student by ID.
     * 
     * @param id The student ID
     * @return Student DTO
     */
    @Operation(summary = "Get student by ID", description = "Retrieve a student by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO studentDTO = studentService.getStudentById(id);
        return ResponseEntity.ok(studentDTO);
    }

    /**
     * Get all students.
     * 
     * @return List of all students
     */
    @Operation(summary = "Get all students", description = "Retrieve all students")
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * Update student.
     * 
     * @param id The student ID
     * @param updateDTO The update data
     * @return Updated student DTO
     */
    @Operation(summary = "Update student", description = "Update an existing student record")
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateDTO updateDTO) {
        StudentDTO studentDTO = studentService.updateStudent(id, updateDTO);
        return ResponseEntity.ok(studentDTO);
    }

    /**
     * Delete student.
     * 
     * @param id The student ID
     * @return No content
     */
    @Operation(summary = "Delete student", description = "Delete a student by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get students by course.
     * 
     * @param course The course name
     * @return List of students in the course
     */
    @Operation(summary = "Get students by course", description = "Retrieve students by course")
    @GetMapping("/course/{course}")
    public ResponseEntity<List<StudentDTO>> getStudentsByCourse(@PathVariable String course) {
        List<StudentDTO> students = studentService.getStudentsByCourse(course);
        return ResponseEntity.ok(students);
    }

    /**
     * Get students with pagination.
     * 
     * @param page Page number (0-based)
     * @param size Page size
     * @param sortBy Field to sort by
     * @param sortDirection Sort direction (ASC/DESC)
     * @return Page of students
     */
    @Operation(summary = "Get students with pagination", description = "Retrieve students with pagination and sorting")
    @GetMapping("/paginated")
    public ResponseEntity<Page<StudentDTO>> getStudentsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Page<StudentDTO> students = studentService.getStudentsWithPagination(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(students);
    }

    /**
     * Search students by name or email.
     * 
     * @param searchTerm The search term
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of matching students
     */
    @Operation(summary = "Search students", description = "Search students by name or email")
    @GetMapping("/search")
    public ResponseEntity<Page<StudentDTO>> searchStudents(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<StudentDTO> students = studentService.searchStudents(searchTerm, page, size);
        return ResponseEntity.ok(students);
    }

    /**
     * Get students by status.
     * 
     * @param status The student status
     * @return List of students with the specified status
     */
    @Operation(summary = "Get students by status", description = "Retrieve students by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<StudentDTO>> getStudentsByStatus(@PathVariable String status) {
        List<StudentDTO> students = studentService.getStudentsByStatus(status);
        return ResponseEntity.ok(students);
    }

    /**
     * Get student by roll number.
     * 
     * @param rollNumber The roll number
     * @return Student DTO
     */
    @Operation(summary = "Get student by roll number", description = "Retrieve a student by their roll number")
    @GetMapping("/roll-number/{rollNumber}")
    public ResponseEntity<StudentDTO> getStudentByRollNumber(@PathVariable String rollNumber) {
        StudentDTO studentDTO = studentService.getStudentByRollNumber(rollNumber);
        return ResponseEntity.ok(studentDTO);
    }

    /**
     * Bulk insert students.
     * 
     * @param createDTOs List of student creation DTOs
     * @return List of created students
     */
    @Operation(summary = "Bulk insert students", description = "Create multiple students at once")
    @PostMapping("/bulk")
    public ResponseEntity<List<StudentDTO>> bulkInsertStudents(
            @Valid @RequestBody List<StudentCreateDTO> createDTOs) {
        List<StudentDTO> students = studentService.bulkInsertStudents(createDTOs);
        return ResponseEntity.ok(students);
    }

    /**
     * Bulk delete students.
     * 
     * @param ids List of student IDs to delete
     * @return No content
     */
    @Operation(summary = "Bulk delete students", description = "Delete multiple students at once")
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDeleteStudents(@RequestBody List<Long> ids) {
        studentService.bulkDeleteStudents(ids);
        return ResponseEntity.noContent().build();
    }
}