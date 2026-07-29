package com.example.redisdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Student Entity
 * 
 * This entity represents a student in the system.
 * It's used to demonstrate Redis caching concepts through practical implementation.
 * 
 * WHY this entity exists:
 * - Serves as the primary data model for learning Redis concepts
 * - Provides a realistic entity with various data types (String, Decimal, Integer, DateTime)
 * - Will be used to demonstrate caching strategies, serialization, and performance comparisons
 * 
 * WHEN to use this entity:
 * - In Phase 1: Basic CRUD operations without Redis (baseline performance)
 * - In Phase 3: Manual caching demonstrations
 * - In Phase 4: Spring Cache annotations (@Cacheable, @CachePut, @CacheEvict)
 * - In Phase 6: Serialization comparisons (JDK vs JSON)
 * - In Phase 7: Performance comparisons
 */
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    /**
     * Primary key for the student record.
     * Uses AUTO generation strategy for MySQL database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Roll number of the student.
     * Unique identifier within the institution.
     */
    @Column(name = "roll_number", unique = true, length = 20)
    private String rollNumber;

    /**
     * Full name of the student.
     * Cannot be null or empty.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Email address of the student.
     * Must be unique for each student.
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Phone number of the student.
     */
    @Column(name = "phone", length = 15)
    private String phone;

    /**
     * Course name (e.g., Computer Science, Mechanical Engineering).
     */
    @Column(name = "course", length = 50)
    private String course;

    /**
     * Branch or specialization (e.g., AI, ML, Civil).
     */
    @Column(name = "branch", length = 50)
    private String branch;

    /**
     * Current semester (1-8).
     */
    @Column(name = "semester")
    private Integer semester;

    /**
     * Cumulative Grade Point Average (0.0-10.0).
     * Uses BigDecimal to store decimal values accurately.
     */
    @Column(name = "cgpa", precision = 3, scale = 2)
    private BigDecimal cgpa;

    /**
     * City where the student resides.
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * Full address of the student.
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * Status of the student (ACTIVE, INACTIVE, GRADUATED, SUSPENDED).
     */
    @Column(name = "status", length = 20)
    private String status;

    /**
     * Timestamp when the student record was created.
     * Automatically set on creation.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the student record was last updated.
     * Automatically updated on any modification.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Lifecycle callback - automatically sets createdAt before persisting.
     * This is called by JPA before the entity is saved to the database.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle callback - automatically updates updatedAt before updating.
     * This is called by JPA before the entity is updated in the database.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
