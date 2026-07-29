package com.example.redisdemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Student Update DTO
 * 
 * This DTO is used specifically for updating existing student records.
 * All fields are optional to allow partial updates.
 * 
 * WHY this DTO exists:
 * - Allows partial updates (only update fields that are provided)
 * - Different validation rules than create DTO (all fields optional)
 * - Enables PATCH operations for updating specific fields
 * - Prevents accidental overwrites of missing fields
 * 
 * WHEN to use this DTO:
 * - In PUT/PATCH requests to update existing students
 * - When editing student information
 * 
 * PRODUCTION USE CASES:
 * - Profile update forms
 * - Admin panel student editing
 * - Bulk student update operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentUpdateDTO {

    /**
     * Roll number of the student.
     * Optional field - if provided, must be between 2-20 characters.
     */
    @Size(min = 2, max = 20, message = "Roll number must be between 2 and 20 characters")
    private String rollNumber;

    /**
     * Full name of the student.
     * Optional field - if provided, must be between 2-100 characters.
     */
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    /**
     * Email address of the student.
     * Optional field - if provided, must be a valid email format.
     */
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * Phone number.
     * Optional field - if provided, must be between 10-15 characters.
     */
    @Size(min = 10, max = 15, message = "Phone must be between 10 and 15 characters")
    private String phone;

    /**
     * Course name.
     * Optional field - if provided, must be between 2-50 characters.
     */
    @Size(min = 2, max = 50, message = "Course must be between 2 and 50 characters")
    private String course;

    /**
     * Branch or specialization.
     * Optional field - if provided, must be between 2-50 characters.
     */
    @Size(min = 2, max = 50, message = "Branch must be between 2 and 50 characters")
    private String branch;

    /**
     * Current semester.
     * Optional field - if provided, must be between 1-8.
     */
    private Integer semester;

    /**
     * Cumulative Grade Point Average.
     * Optional field - if provided, must be between 0.0 and 10.0.
     */
    private BigDecimal cgpa;

    /**
     * City where the student resides.
     * Optional field - if provided, must be between 2-50 characters.
     */
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    /**
     * Full address of the student.
     * Optional field - if provided, must be between 2-200 characters.
     */
    @Size(min = 2, max = 200, message = "Address must be between 2 and 200 characters")
    private String address;

    /**
     * Status of the student.
     * Optional field.
     */
    private String status;
}
