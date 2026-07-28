package com.example.redisdemo.exception;

/**
 * Resource Not Found Exception
 * 
 * This exception is thrown when a requested resource is not found in the system.
 * 
 * WHY this exception exists:
 * - Provides specific exception for missing resources
 * - Differentiates from other types of errors
 * - Can be handled specifically in global exception handler
 * - Returns appropriate HTTP status code (404)
 * 
 * WHEN to use this exception:
 * - When a requested entity is not found by ID
 * - When a requested entity is not found by unique field
     * - When search returns no results and results are expected
 * 
 * PRODUCTION USE CASES:
 * - User not found during login
 * - Product not found in catalog
 * - Order not found for viewing
 * - Resource access with invalid ID
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor with error message.
     * 
     * @param message The error message describing what was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with error message and cause.
     * 
     * @param message The error message describing what was not found
     * @param cause The underlying cause of the exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
