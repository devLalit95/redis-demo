package com.example.redisdemo.controller;

import com.example.redisdemo.cache.CacheInvalidationService;
import com.example.redisdemo.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Cache Invalidation Controller
 * 
 * This controller provides HTTP endpoints for cache invalidation demonstrations.
 * This is Phase 11 of the project - Cache Invalidation.
 */
@RestController
@RequestMapping("/api/cache-invalidation/students")
@RequiredArgsConstructor
@Slf4j
public class CacheInvalidationController {

    private final CacheInvalidationService cacheInvalidationService;

    /**
     * Evict student cache by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> evictStudentCache(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: DELETE /api/cache-invalidation/students/{}", id);
        
        cacheInvalidationService.evictStudentCache(id);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Student cache evicted successfully", responseTime));
    }

    /**
     * Evict student cache by email.
     */
    @DeleteMapping("/email/{email}")
    public ResponseEntity<ApiResponse<String>> evictStudentCacheByEmail(@PathVariable String email) {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: DELETE /api/cache-invalidation/students/email/{}", email);
        
        cacheInvalidationService.evictStudentCacheByEmail(email);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Student cache evicted successfully", responseTime));
    }

    /**
     * Evict all student cache.
     */
    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<String>> evictAllStudentCache() {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: DELETE /api/cache-invalidation/students/all");
        
        cacheInvalidationService.evictAllStudentCache();
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("All student cache evicted successfully", responseTime));
    }

    /**
     * Update student with cache evict.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateStudentWithEvict(
            @PathVariable Long id,
            @RequestBody Object studentDTO) {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: PUT /api/cache-invalidation/students/{}", id);
        
        Object updatedStudent = cacheInvalidationService.updateStudentWithEvict(id, studentDTO);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(updatedStudent, responseTime, "Student updated and cache evicted"));
    }

    /**
     * Delete student with cache evict.
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<String>> deleteStudentWithEvict(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: DELETE /api/cache-invalidation/students/{}/delete", id);
        
        cacheInvalidationService.deleteStudentWithEvict(id);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success("Student deleted and cache evicted", responseTime));
    }

    /**
     * Refresh student cache.
     */
    @PostMapping("/{id}/refresh")
    public ResponseEntity<ApiResponse<Object>> refreshStudentCache(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: POST /api/cache-invalidation/students/{}/refresh", id);
        
        Object studentDTO = cacheInvalidationService.refreshStudentCache(id);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(studentDTO, responseTime, "Student cache refreshed"));
    }

    /**
     * Lazy load student into cache.
     */
    @GetMapping("/{id}/lazy")
    public ResponseEntity<ApiResponse<Object>> lazyLoadStudent(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: GET /api/cache-invalidation/students/{}/lazy", id);
        
        Object studentDTO = cacheInvalidationService.lazyLoadStudent(id);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(studentDTO, responseTime));
    }

    /**
     * Write through caching for student.
     */
    @PostMapping("/{id}/write-through")
    public ResponseEntity<ApiResponse<Object>> writeThroughStudent(
            @PathVariable Long id,
            @RequestBody Object studentDTO) {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: POST /api/cache-invalidation/students/{}/write-through", id);
        
        Object updatedStudent = cacheInvalidationService.writeThroughStudent(id, studentDTO);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(updatedStudent, responseTime, "Student written through cache"));
    }

    /**
     * Cache aside pattern for student.
     */
    @GetMapping("/{id}/cache-aside")
    public ResponseEntity<ApiResponse<Object>> cacheAsideStudent(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: GET /api/cache-invalidation/students/{}/cache-aside", id);
        
        Object studentDTO = cacheInvalidationService.cacheAsideStudent(id);
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(studentDTO, responseTime));
    }

    /**
     * Get cache invalidation explanation.
     */
    @GetMapping("/explanation")
    public ResponseEntity<ApiResponse<String>> getCacheInvalidationExplanation() {
        long startTime = System.currentTimeMillis();
        
        log.info("CACHE INVALIDATION: GET /api/cache-invalidation/students/explanation");
        
        String explanation = cacheInvalidationService.getCacheInvalidationExplanation();
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        return ResponseEntity.ok(ApiResponse.success(explanation, responseTime));
    }
}
