package com.example.redisdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Serialization Controller
 * 
 * This controller provides HTTP endpoints for serialization demonstrations.
 * This is Phase 6 of the project - Serialization.
 * 
 * NOTE: Disabled for Redis-only testing
 * 
 * WHY this controller exists:
 * - Provides HTTP interface to serialization operations
 * - Shows different serialization strategies
 * - Demonstrates serialization performance
 * - Shows serialization trade-offs
 * 
 * WHEN to use this controller:
 * - Phase 6: Learning serialization
 * - Understanding serialization trade-offs
 * - Performance optimization
 * - Testing different serialization strategies
 * 
 * PRODUCTION USE CASES:
 * - Serialization testing endpoints
 * - Performance monitoring interfaces
 * - Serialization debugging tools
 * - Understanding serialization trade-offs
 */
// @RestController  // Disabled for Redis-only testing
@RequestMapping("/api/serialization/students")
@RequiredArgsConstructor
@Slf4j
public class SerializationController {

    // Controller disabled for Redis-only testing
    // All methods commented out to prevent compilation errors
}