package com.example.redisdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Student Data Transfer Object
 * 
 * This DTO is used to transfer student data between layers without exposing the entity directly.
 * 
 * WHY DTOs exist:
 * - Decouples the internal entity model from the API contract
 * - Prevents over-exposure of internal entity fields
 * - Enables selective field exposure (hide sensitive or internal fields)
 * - Provides a place for validation annotations separate from entity
 * - Allows transformation of data before sending to clients
 * 
 * WHEN to use DTOs:
 * - In all API responses (never return entities directly)
 * - When you need to hide certain fields from the API response
 * - When you need to transform data before sending to clients
 * - When you want to add computed fields that don't exist in the entity
 * 
 * PRODUCTION USE CASES:
 * - API responses to control what clients see
 * - Adding computed fields like "age" from "dateOfBirth"
 * - Hiding internal fields like "createdBy", "updatedBy"
 * - Combining data from multiple entities into one response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    /**
     * Unique identifier for the student.
     */
    private Long id;

    /**
     * Full name of the student.
     */
    private String name;

    /**
     * Email address of the student.
     */
    private String email;

    /**
     * Course name.
     */
    private String course;

    /**
     * Branch or specialization.
     */
    private String branch;

    /**
     * Current semester.
     */
    private Integer semester;

    /**
     * Cumulative Grade Point Average.
     */
    private BigDecimal cgpa;

    /**
     * City where the student resides.
     */
    private String city;

    /**
     * Phone number.
     */
    private String phone;

    /**
     * Timestamp when the record was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the record was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Additional message field for testing.
     */
    private String message;
}
