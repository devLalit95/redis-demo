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
     * Success status of the request.
     */
    private Boolean success;

    /**
     * Message describing the result (success, error, etc.).
     * Useful for providing feedback to the frontend.
     */
    private String message;

    /**
     * Metadata containing performance and cache information.
     */
    private Metadata metadata;

    /**
     * Metadata inner class for performance tracking.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Metadata {
        /**
         * Total execution time in milliseconds.
         */
        private String executionTime;

        /**
         * Time spent on database operations in milliseconds.
         */
        private String databaseTime;

        /**
         * Time spent on Redis operations in milliseconds.
         */
        private String redisTime;

        /**
         * Whether the request was a cache hit.
         */
        private Boolean cacheHit;

        /**
         * Whether the request was a cache miss.
         */
        private Boolean cacheMiss;

        /**
         * Type of cache used (MANUAL, SPRING, NONE).
         */
        private String cacheType;

        /**
         * Data source (REDIS, MYSQL, MIXED).
         */
        private String dataSource;

        /**
         * Timestamp when the response was generated.
         */
        private Long timestamp;
    }

    /**
     * Convenience method to create a success response.
     * 
     * @param data The data to return
     * @param metadata The metadata containing performance information
     * @param <T> The type of data
     * @return ApiResponse with success status
     */
    public static <T> ApiResponse<T> success(T data, Metadata metadata) {
        return ApiResponse.<T>builder()
                .data(data)
                .success(true)
                .message("Success")
                .metadata(metadata)
                .build();
    }

    /**
     * Convenience method to create a success response with response time.
     * 
     * @param data The data to return
     * @param responseTime Response time in milliseconds
     * @param <T> The type of data
     * @return ApiResponse with success status
     */
    public static <T> ApiResponse<T> success(T data, long responseTime) {
        return ApiResponse.<T>builder()
                .data(data)
                .success(true)
                .message("Success")
                .metadata(Metadata.builder()
                    .executionTime(responseTime + " ms")
                    .databaseTime("0 ms")
                    .redisTime(responseTime + " ms")
                    .cacheHit(false)
                    .cacheMiss(false)
                    .cacheType("NONE")
                    .dataSource("REDIS")
                    .timestamp(System.currentTimeMillis())
                    .build())
                .build();
    }

    /**
     * Convenience method to create a success response with custom message.
     * 
     * @param data The data to return
     * @param metadata The metadata containing performance information
     * @param message Custom success message
     * @param <T> The type of data
     * @return ApiResponse with success status and custom message
     */
    public static <T> ApiResponse<T> success(T data, Metadata metadata, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .success(true)
                .message(message)
                .metadata(metadata)
                .build();
    }

    /**
     * Convenience method to create an error response.
     * 
     * @param message Error message
     * @param <T> The type of data (usually null for errors)
     * @return ApiResponse with error status
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .metadata(Metadata.builder()
                    .executionTime("0 ms")
                    .databaseTime("0 ms")
                    .redisTime("0 ms")
                    .cacheHit(false)
                    .cacheMiss(false)
                    .cacheType("NONE")
                    .dataSource("NONE")
                    .timestamp(System.currentTimeMillis())
                    .build())
                .build();
    }

    /**
     * Convenience method to create an error response with response time.
     * 
     * @param message Error message
     * @param responseTime Response time in milliseconds
     * @param <T> The type of data (usually null for errors)
     * @return ApiResponse with error status
     */
    public static <T> ApiResponse<T> error(String message, long responseTime) {
        return ApiResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .metadata(Metadata.builder()
                    .executionTime(responseTime + " ms")
                    .databaseTime("0 ms")
                    .redisTime("0 ms")
                    .cacheHit(false)
                    .cacheMiss(false)
                    .cacheType("NONE")
                    .dataSource("NONE")
                    .timestamp(System.currentTimeMillis())
                    .build())
                .build();
    }
}
