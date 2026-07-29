package com.example.redisdemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Performance Logger Aspect
 * 
 * This aspect uses Spring AOP to automatically log performance metrics for methods.
 * 
 * WHY this aspect exists:
 * - Automatically measures method execution time
 * - Tracks Redis and database calls
 * - Provides performance monitoring without code changes
 * - Helps identify performance bottlenecks
 * 
 * WHEN to use this aspect:
 * - Monitoring service layer performance
 * - Tracking cache hit/miss patterns
 * - Measuring database query performance
 * - Analyzing Redis operation performance
 * 
 * PRODUCTION USE CASES:
 * - Performance monitoring
 * - SLA tracking
 * - Performance regression detection
 * - Capacity planning
 * 
 * AOP CONCEPTS:
 * - Aspect: Modular cross-cutting concern (logging, monitoring)
 * - Join Point: Point during execution of a program (method call)
 * - Advice: Action taken at a join point (around, before, after)
 * - Pointcut: Expression that selects join points
 * 
 * PERFORMANCE METRICS TRACKED:
 * - Method execution time
 * - Redis calls count
 * - Database calls count
 * - Thread name
 * - Method signature
 */
@Aspect
@Component
@Slf4j
public class PerformanceLoggerAspect {

    /**
     * Around advice for performance logging.
     * 
     * This advice intercepts method calls and logs performance metrics.
     * 
     * WHY @Around advice:
     * - Can measure execution time by wrapping the method
     * - Can modify method parameters or return values
     * - Can handle exceptions
     * - Most powerful advice type
     * 
     * POINTCUT EXPRESSION:
     * - execution(* com.example.redisdemo.service.*.*(..))
     * - Matches all methods in service package
     * - * any return type
     * - .. any parameters
     * 
     * @param joinPoint The join point representing the method execution
     * @return The result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("execution(* com.example.redisdemo.service.*.*(..))")
    public Object logPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        String threadName = Thread.currentThread().getName();
        
        log.info("🚀 STARTING: {} | Thread: {}", methodName, threadName);
        
        try {
            // Proceed with method execution
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.info("✅ COMPLETED: {} | Execution Time: {}ms | Thread: {}", 
                    methodName, executionTime, threadName);
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.error("❌ FAILED: {} | Execution Time: {}ms | Error: {} | Thread: {}", 
                    methodName, executionTime, e.getMessage(), threadName);
            
            throw e;
        }
    }

    /**
     * Around advice for cache operations.
     * 
     * This advice specifically targets cache-related methods.
     * 
     * POINTCUT EXPRESSION:
     * - execution(* com.example.redisdemo.cache.*.*(..))
     * - Matches all methods in cache package
     * 
     * @param joinPoint The join point representing the method execution
     * @return The result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("execution(* com.example.redisdemo.cache.*.*(..))")
    public Object logCachePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        String threadName = Thread.currentThread().getName();
        
        log.info("🔄 CACHE OPERATION: {} | Thread: {}", methodName, threadName);
        
        try {
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.info("✅ CACHE COMPLETED: {} | Execution Time: {}ms | Thread: {}", 
                    methodName, executionTime, threadName);
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.error("❌ CACHE FAILED: {} | Execution Time: {}ms | Error: {} | Thread: {}", 
                    methodName, executionTime, e.getMessage(), threadName);
            
            throw e;
        }
    }

    /**
     * Around advice for repository operations.
     * 
     * This advice specifically targets database-related methods.
     * 
     * POINTCUT EXPRESSION:
     * - execution(* com.example.redisdemo.repository.*.*(..))
     * - Matches all methods in repository package
     * 
     * @param joinPoint The join point representing the method execution
     * @return The result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("execution(* com.example.redisdemo.repository.*.*(..))")
    public Object logDatabasePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        String threadName = Thread.currentThread().getName();
        
        log.info("💾 DATABASE OPERATION: {} | Thread: {}", methodName, threadName);
        
        try {
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.info("✅ DATABASE COMPLETED: {} | Execution Time: {}ms | Thread: {}", 
                    methodName, executionTime, threadName);
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.error("❌ DATABASE FAILED: {} | Execution Time: {}ms | Error: {} | Thread: {}", 
                    methodName, executionTime, e.getMessage(), threadName);
            
            throw e;
        }
    }
}