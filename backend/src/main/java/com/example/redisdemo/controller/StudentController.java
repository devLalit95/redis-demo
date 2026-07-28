package com.example.redisdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Student Controller
 * 
 * This controller provides HTTP endpoints for student CRUD operations.
 * This is the main REST API for the application.
 * 
 * NOTE: Disabled for Redis-only testing
 * 
 * WHY this controller exists:
 * - Provides REST API for student management
 * - Implements CRUD operations with caching
 * - Demonstrates Spring Boot REST patterns
 * - Shows proper API design principles
 * 
 * WHEN to use this controller:
 * - Main student management operations
 * - Testing caching with database operations
 * - Learning Spring Boot REST patterns
 * 
 * PRODUCTION USE CASES:
 * - Student management system
 * - REST API implementation
 * - Caching in production applications
 * - Standard CRUD operations
 */
// @RestController  // Disabled for Redis-only testing
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    // Controller disabled for Redis-only testing
    // All methods commented out to prevent compilation errors
}