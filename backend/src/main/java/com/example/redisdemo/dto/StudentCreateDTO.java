package com.example.redisdemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Student Create DTO
 * 
 * This DTO is used specifically for creating new student records.
 * It includes validation annotations to ensure data integrity.
 * 
 * WHY this DTO exists:
 * - Separate DTO for creation allows different validation rules than updates
     * - Doesn't include id and timestamps (auto-generated)
 * - Enables client-side validation before sending to server
 * - Provides clear contract for create operations
 * 
 * WHEN to use this DTO:
 * - In POST requests to create new students
 * - When validating user input for student registration
 * 
 * PRODUCTION USE CASES:
 * - User registration forms
 * - Admin panel student creation
 * - Bulk student import operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCreateDTO {

    /**
     * Roll number of the student.
     * Cannot be blank and must be between 2-20 characters.
     */
    @NotBlank(message = "Roll number is required")
    @Size(min = 2, max = 20, message = "Roll number must be between 2 and 20 characters")
    private String rollNumber;

    /**
     * Full name of the student.
     * Cannot be blank and must be between 2-100 characters.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    /**
     * Email address of the student.
     * Must be a valid email format and cannot be blank.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * Phone number.
     * Cannot be blank and must be between 10-15 characters.
     */
    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 15, message = "Phone must be between 10 and 15 characters")
    private String phone;

    /**
     * Course name.
     * Cannot be blank and must be between 2-50 characters.
     */
    @NotBlank(message = "Course is required")
    @Size(min = 2, max = 50, message = "Course must be between 2 and 50 characters")
    private String course;

    /**
     * Branch or specialization.
     * Cannot be blank and must be between 2-50 characters.
     */
    @NotBlank(message = "Branch is required")
    @Size(min = 2, max = 50, message = "Branch must be between 2 and 50 characters")
    private String branch;

    /**
     * Current semester.
     * Must be between 1-8.
     */
    @NotNull(message = "Semester is required")
    private Integer semester;

    /**
     * Cumulative Grade Point Average.
     * Must be between 0.0 and 10.0.
     */
    @NotNull(message = "CGPA is required")
    private BigDecimal cgpa;

    /**
     * City where the student resides.
     * Cannot be blank and must be between 2-50 characters.
     */
    @NotBlank(message = "City is required")
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
     * Default is ACTIVE.
     */
    private String status;
}
