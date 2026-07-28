package com.example.redisdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Manual Cache Controller
 * 
 * This controller provides HTTP endpoints for manual caching demonstrations.
 * This is Phase 3 of the project - Manual Cache Implementation.
 * 
 * NOTE: Disabled for Redis-only testing
 * 
 * WHY this controller exists:
 * - Provides HTTP interface to manual caching operations
 * - Shows cache-aside pattern through API calls
 * - Demonstrates cache hit/miss scenarios
 * - Compares with Spring Cache implementation
 * 
 * WHEN to use this controller:
 * - Phase 3: Learning manual caching
 * - Understanding cache hit/miss patterns
 * - Performance comparison with Spring Cache
 * 
 * PRODUCTION USE CASES:
 * - Cache management endpoints
 * - Cache monitoring interfaces
 * - Cache debugging tools
 * 
 * LEARNING APPROACH:
 * - First call: CACHE MISS (fetches from database, stores in cache)
 * - Second call: CACHE HIT (returns from cache)
 * - After TTL expires: CACHE MISS again
 * - Clear logs show exactly what's happening
 */
// @RestController  // Disabled for Redis-only testing
@RequestMapping("/api/manual-cache/students")
@RequiredArgsConstructor
@Slf4j
public class ManualCacheController {

    // Controller disabled for Redis-only testing
    // All methods commented out to prevent compilation errors
}