package com.example.redisdemo.repository;

import com.example.redisdemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Student Repository
 * 
 * This interface handles all database operations for the Student entity.
 * It extends JpaRepository to provide CRUD operations out of the box.
 * 
 * WHY this repository exists:
 * - Provides a clean abstraction layer for database operations
 * - Separates data access logic from business logic
 * - Will be used to demonstrate the performance difference between database and cache operations
 * - Enables easy testing and mocking of database operations
 * 
 * WHEN to use this repository:
 * - In Phase 1: Direct database access for baseline performance measurement
 * - In Phase 3: Database access in cache-aside pattern (cache miss scenario)
 * - In Phase 7: Performance comparison between database and cache operations
 * 
 * PRODUCTION USE CASES:
 * - When implementing caching, you'll still need the repository as the fallback data source
 * - The repository is used when cache misses occur or when cache is invalidated
 * - Essential for write operations that need to update both cache and database
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find a student by email address.
     * 
     * WHY this method exists:
     * - Email is a unique identifier for students
     * - Common lookup pattern in real applications
     * - Will be used to demonstrate caching by email lookup
     * 
     * WHEN to use this method:
     * - User login scenarios
     * - Profile lookups by email
     * - Checking for duplicate emails during registration
     * 
     * @param email The email address to search for
     * @return Optional containing the student if found
     */
    Optional<Student> findByEmail(String email);

    /**
     * Find all students by course.
     * 
     * WHY this method exists:
     * - Demonstrates query generation from method names
     * - Will be used to demonstrate caching of query results
     * - Shows how to cache collection results
     * 
     * WHEN to use this method:
     * - Displaying all students in a particular course
     * - Analytics by course
     * - Course management features
     * 
     * @param course The course name to filter by
     * @return List of students in the specified course
     */
    List<Student> findByCourse(String course);

    /**
     * Find students by course and branch.
     * 
     * WHY this method exists:
     * - Demonstrates multi-parameter queries
     * - Shows more specific filtering for caching demonstrations
     * - Common real-world query pattern
     * 
     * WHEN to use this method:
     * - Filtering students by multiple criteria
     * - Department-specific student lists
     * 
     * @param course The course name
     * @param branch The branch/specialization
     * @return List of students matching both criteria
     */
    List<Student> findByCourseAndBranch(String course, String branch);

    /**
     * Find students with CGPA greater than or equal to a threshold.
     * 
     * WHY this method exists:
     * - Demonstrates comparison queries
     * - Will be used to demonstrate caching of filtered results
     * - Real-world use case: finding high-performing students
     * 
     * WHEN to use this method:
     * - Dean's list generation
     * - Scholarship eligibility
     * - Performance analytics
     * 
     * @param cgpa The minimum CGPA threshold
     * @return List of students with CGPA >= threshold
     */
    List<Student> findByCgpaGreaterThanEqual(Double cgpa);

    /**
     * Custom query to find students by city.
     * 
     * WHY this method exists:
     * - Demonstrates custom @Query annotation usage
     * - Shows how to write JPQL queries
     * - Will be used to demonstrate caching of custom query results
     * 
     * WHEN to use this method:
     * - Location-based student filtering
     * - Regional analytics
     * 
     * @param city The city name
     * @return List of students from the specified city
     */
    @Query("SELECT s FROM Student s WHERE s.city = :city")
    List<Student> findByCity(@Param("city") String city);

    /**
     * Check if a student exists by email.
     * 
     * WHY this method exists:
     * - More efficient than fetching entire student object
     * - Used for validation during registration
     * - Demonstrates existence check queries
     * 
     * WHEN to use this method:
     * - Email validation during registration
     * - Checking duplicates before updates
     * 
     * @param email The email address to check
     * @return true if student exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Count students by course.
     * 
     * WHY this method exists:
     * - Demonstrates aggregate queries
     * - Will be used to demonstrate caching of count results
     * - Real-world use case: course statistics
     * 
     * WHEN to use this method:
     * - Dashboard statistics
     * - Course capacity planning
     * 
     * @param course The course name
     * @return Number of students in the course
     */
    long countByCourse(String course);
}
