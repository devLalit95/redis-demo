package com.example.redisdemo.exception;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.dto.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 * 
 * This class handles all exceptions globally across the application.
 * It provides consistent error responses and proper HTTP status codes.
 * 
 * WHY this handler exists:
 * - Centralizes exception handling logic
 * - Provides consistent error response structure
 * - Eliminates try-catch blocks in controllers
     * - Separates error handling from business logic
 * - Enables proper logging of errors
 * - Returns user-friendly error messages
 * 
 * WHEN to use this handler:
 * - All exceptions are automatically handled by this class
 * - No need for try-catch in controllers for standard errors
 * - Custom exceptions can be added here
 * 
 * PRODUCTION USE CASES:
 * - API error standardization
 * - Logging and monitoring
 * - User-friendly error messages
 * - Security (hiding internal errors)
 * 
 * DESIGN PATTERNS USED:
 * - Exception Handling Pattern: Centralized error handling
 * - RESTful Error Responses: Standardized error format
 * - Separation of Concerns: Error handling separate from business logic
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle Resource Not Found Exception.
     * 
     * WHY this handler exists:
     * - Returns 404 status code for missing resources
     * - Provides consistent error message
     * - Logs the error for debugging
     * 
     * WHEN this handler is triggered:
     * - Student not found by ID
     * - Student not found by email
     * - Any resource lookup fails
     * 
     * @param ex The ResourceNotFoundException
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handle validation errors from @Valid annotation.
     * 
     * WHY this handler exists:
     * - Returns 400 status code for validation failures
     * - Provides field-level error details
     * - Helps users correct their input
     * - Logs validation failures
     * 
     * WHEN this handler is triggered:
     * - @Valid annotation fails in controller parameters
     * - DTO validation constraints are violated
     * - Invalid input is provided by client
     * 
     * PRODUCTION USE CASES:
     * - Form validation feedback
     * - API input validation
     * - User input correction guidance
     * 
     * @param ex The MethodArgumentNotValidException
     * @return ResponseEntity with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation failed: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ValidationError response = ValidationError.of(errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle Illegal Argument Exception.
     * 
     * WHY this handler exists:
     * - Returns 400 status code for invalid arguments
     * - Handles business logic validation failures
     * - Provides clear error messages
     * 
     * WHEN this handler is triggered:
     * - Invalid business logic parameters
     * - Duplicate email during creation/update
     * - Invalid state transitions
     * 
     * @param ex The IllegalArgumentException
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle all other exceptions.
     * 
     * WHY this handler exists:
     * - Catches any unhandled exceptions
     * - Returns 500 status code for server errors
     * - Logs full stack trace for debugging
     * - Prevents exposing internal errors to clients
     * 
     * WHEN this handler is triggered:
     * - Any unhandled exception occurs
     * - Database connection failures
     * - Unexpected runtime errors
     * 
     * SECURITY CONSIDERATIONS:
     * - Don't expose internal error details to clients
     * - Log full error details for debugging
     * - Return generic error message to clients
     * 
     * @param ex The Exception
     * @return ResponseEntity with generic error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        
        ApiResponse<Void> response = ApiResponse.error("An unexpected error occurred. Please try again later.");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
