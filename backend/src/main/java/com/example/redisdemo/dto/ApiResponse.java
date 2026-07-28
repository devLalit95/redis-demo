package com.example.redisdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API Response Wrapper
 * 
 * This class wraps all API responses to include response time and standard structure.
 * 
 * WHY this wrapper exists:
 * - Provides consistent response structure across all APIs
 * - Includes response time measurement for performance monitoring
 * - Enables easy comparison between cached and non-cached operations
 * - Standard format for frontend to consume
 * - Separates metadata from actual data
 * 
 * WHEN to use this wrapper:
 * - In all API responses for consistency
 * - When measuring performance between different caching strategies
 * - When you need to provide metadata along with data
 * 
 * PRODUCTION USE CASES:
 * - Performance monitoring and SLA tracking
 * - A/B testing different caching strategies
 * - Frontend performance optimization
 * - Debugging slow API responses
 * 
 * @param <T> The type of data being returned
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    /**
     * The actual data being returned.
     * Can be a single object, list, or any other data type.
     */
    private T data;

    /**
     * Time taken to process the request in milliseconds.
     * This is crucial for demonstrating Redis performance benefits.
     * 
     * WHY include response time:
     * - Directly shows the performance difference between cached and non-cached operations
     * - Helps identify slow endpoints that need caching
     * - Provides data for performance charts and analytics
     * - Enables real-time performance monitoring
     * 
     * WHEN response time is important:
     * - Phase 1: Baseline MySQL performance measurement
     * - Phase 3: Manual cache performance comparison
     * - Phase 4: Spring Cache performance comparison
     * - Phase 7: Comprehensive performance comparison
     * - Phase 8: Dashboard performance metrics
     */
    private Long responseTime;

    /**
     * HTTP status code or custom status code.
     * Provides additional context about the response.
     */
    private Integer status;

    /**
     * Message describing the result (success, error, etc.).
     * Useful for providing feedback to the frontend.
     */
    private String message;

    /**
     * Timestamp when the response was generated.
     * Useful for debugging and tracking.
     */
    private Long timestamp;

    /**
     * Convenience method to create a success response.
     * 
     * @param data The data to return
     * @param responseTime The time taken in milliseconds
     * @param <T> The type of data
     * @return ApiResponse with success status
     */
    public static <T> ApiResponse<T> success(T data, Long responseTime) {
        return ApiResponse.<T>builder()
                .data(data)
                .responseTime(responseTime)
                .status(200)
                .message("Success")
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Convenience method to create a success response with custom message.
     * 
     * @param data The data to return
     * @param responseTime The time taken in milliseconds
     * @param message Custom success message
     * @param <T> The type of data
     * @return ApiResponse with success status and custom message
     */
    public static <T> ApiResponse<T> success(T data, Long responseTime, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .responseTime(responseTime)
                .status(200)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Convenience method to create an error response.
     * 
     * @param message Error message
     * @param status HTTP status code
     * @param <T> The type of data (usually null for errors)
     * @return ApiResponse with error status
     */
    public static <T> ApiResponse<T> error(String message, Integer status) {
        return ApiResponse.<T>builder()
                .data(null)
                .responseTime(0L)
                .status(status)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
