package com.example.redisdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Performance Controller
 * 
 * This controller provides HTTP endpoints for performance comparison demonstrations.
 * This is Phase 7 of the project - Performance Comparison.
 * 
 * NOTE: Disabled for Redis-only testing
 * 
 * WHY this controller exists:
 * - Provides HTTP interface to performance comparison operations
 * - Shows performance differences between caching strategies
 * - Demonstrates real-world caching impact
 * - Enables performance monitoring and analysis
 * 
 * WHEN to use this controller:
 * - Phase 7: Learning performance comparison
 * - Understanding caching impact
 * - Performance optimization
 * - Capacity planning
 */
// @RestController  // Disabled for Redis-only testing
@RequestMapping("/api/performance")
@RequiredArgsConstructor
@Slf4j
public class PerformanceController {

    // Controller disabled for Redis-only testing
    // All methods commented out to prevent compilation errors
}