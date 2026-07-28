package com.example.redisdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Validation Error DTO
 * 
 * This DTO is used to return validation error details to the client.
 * 
 * WHY this DTO exists:
 * - Provides structured validation error information
     * - Maps field names to error messages for easy frontend display
 * - Works with global exception handler for consistent error responses
 * - Helps users understand what went wrong with their input
 * 
 * WHEN to use this DTO:
 * - When validation fails in @Valid annotated parameters
 * - In global exception handler for MethodArgumentNotValidException
 * - When returning detailed validation errors to clients
 * 
 * PRODUCTION USE CASES:
 * - Form validation feedback
 * - API error documentation
 * - User input correction guidance
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationError {

    /**
     * HTTP status code for validation errors (typically 400).
     */
    private Integer status;

    /**
     * Error message describing the validation failure.
     */
    private String message;

    /**
     * Map of field names to their corresponding error messages.
     * Key: field name (e.g., "email", "name")
     * Value: error message (e.g., "Email is required", "Name must be at least 2 characters")
     */
    private Map<String, String> errors;

    /**
     * Timestamp when the validation error occurred.
     */
    private Long timestamp;

    /**
     * Convenience method to create a validation error response.
     * 
     * @param errors Map of field names to error messages
     * @return ValidationError with default status and message
     */
    public static ValidationError of(Map<String, String> errors) {
        return ValidationError.builder()
                .status(400)
                .message("Validation failed")
                .errors(errors)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
