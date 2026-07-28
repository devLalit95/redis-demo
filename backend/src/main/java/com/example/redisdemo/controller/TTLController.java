package com.example.redisdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TTL Controller
 * 
 * This controller provides HTTP endpoints for TTL (Time To Live) demonstrations.
 * This is Phase 5 of the project - TTL Management.
 * 
 * NOTE: Disabled for Redis-only testing
 * 
 * WHY this controller exists:
 * - Provides HTTP interface to TTL operations
 * - Shows cache expiration behavior
 * - Demonstrates TTL monitoring
 * - Shows different TTL strategies
 * 
 * WHEN to use this controller:
 * - Phase 5: Learning TTL management
 * - Understanding cache expiration
 * - Performance optimization
 * - Cache management
 * 
 * PRODUCTION USE CASES:
 * - Cache management endpoints
 * - TTL monitoring interfaces
 * - Cache debugging tools
 * - Performance optimization
 */
// @RestController  // Disabled for Redis-only testing
@RequestMapping("/api/ttl/students")
@RequiredArgsConstructor
@Slf4j
public class TTLController {

    // Controller disabled for Redis-only testing
    // All methods commented out to prevent compilation errors
}