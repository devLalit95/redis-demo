package com.example.redisdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring Cache Controller
 * 
 * This controller demonstrates Spring's declarative caching annotations.
 * This is Phase 4 of the project - Spring Cache Annotations.
 * 
 * NOTE: Disabled for Redis-only testing
 * 
 * WHY this controller exists:
 * - Provides HTTP interface to Spring Cache operations
 * - Shows how @Cacheable, @CachePut, @CacheEvict work in practice
 * - Demonstrates different caching strategies through API calls
 * - Compares Spring Cache with manual caching
 * 
 * WHEN to use this controller:
 * - Phase 4: Learning Spring Cache annotations
 * - Understanding declarative caching behavior
 * - Performance comparison with manual caching
 * - Debugging cache behavior
 * 
 * PRODUCTION USE CASES:
 * - Cache management endpoints
 * - Cache monitoring interfaces
 * - Cache debugging tools
 * 
 * LEARNING APPROACH:
 * - First call: Method executes, result cached
 * - Second call: Method skipped, cached result returned
 * - Update operations: Cache updated with @CachePut
 * - Delete operations: Cache evicted with @CacheEvict
 */
// @RestController  // Disabled for Redis-only testing
@RequestMapping("/api/spring-cache/students")
@RequiredArgsConstructor
@Slf4j
public class SpringCacheController {

    // Controller disabled for Redis-only testing
    // All methods commented out to prevent compilation errors
}