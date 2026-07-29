package com.example.redisdemo.loadtest;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.entity.Student;
import com.example.redisdemo.metrics.CacheStatisticsService;
import com.example.redisdemo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Load Test Service
 * 
 * This service provides load testing capabilities for performance comparison.
 * 
 * WHY this service exists:
 * - Performance testing under load
 * - Comparison of caching strategies
 * - Stress testing
 * - Performance benchmarking
 * 
 * WHEN to use this service:
 * - Performance testing
 * - Load testing
 * - Cache validation
 * - Performance optimization
 * 
 * PRODUCTION USE CASES:
 * - Performance testing
 * - Load testing
 * - Capacity planning
 * - Performance monitoring
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoadTestService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StudentRepository studentRepository;
    private final CacheStatisticsService cacheStatisticsService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * Perform load test for no-cache mode.
     * 
     * @param requestCount Number of requests to simulate
     * @return Load test results
     */
    @Transactional(readOnly = true)
    public ApiResponse<Map<String, Object>> loadTestNoCache(int requestCount) {
        log.info("🧪 LOAD TEST: Testing NO_CACHE mode with {} requests", requestCount);
        
        return performLoadTest(requestCount, "NO_CACHE", () -> {
            List<Long> studentIds = getRandomStudentIds(10);
            Long id = studentIds.get(ThreadLocalRandom.current().nextInt(studentIds.size()));
            
            long startTime = System.currentTimeMillis();
            studentRepository.findById(id).orElse(null);
            return System.currentTimeMillis() - startTime;
        });
    }

    /**
     * Perform load test for manual cache mode.
     * 
     * @param requestCount Number of requests to simulate
     * @return Load test results
     */
    public ApiResponse<Map<String, Object>> loadTestManualCache(int requestCount) {
        log.info("🧪 LOAD TEST: Testing MANUAL_CACHE mode with {} requests", requestCount);
        
        return performLoadTest(requestCount, "MANUAL", () -> {
            List<Long> studentIds = getRandomStudentIds(10);
            Long id = studentIds.get(ThreadLocalRandom.current().nextInt(studentIds.size()));
            
            long startTime = System.currentTimeMillis();
            String cacheKey = "manual:student:" + id;
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            
            if (cached != null) {
                return System.currentTimeMillis() - startTime; // Cache hit
            } else {
                // Cache miss - fetch from database
                studentRepository.findById(id).orElse(null);
                redisTemplate.opsForValue().set(cacheKey, new StudentDTO(), 5, TimeUnit.MINUTES);
                return System.currentTimeMillis() - startTime; // Cache miss
            }
        });
    }

    /**
     * Perform load test for Spring Cache mode.
     * 
     * @param requestCount Number of requests to simulate
     * @return Load test results
     */
    public ApiResponse<Map<String, Object>> loadTestSpringCache(int requestCount) {
        log.info("🧪 LOAD TEST: Testing SPRING_CACHE mode with {} requests", requestCount);
        
        return performLoadTest(requestCount, "SPRING", () -> {
            List<Long> studentIds = getRandomStudentIds(10);
            Long id = studentIds.get(ThreadLocalRandom.current().nextInt(studentIds.size()));
            
            long startTime = System.currentTimeMillis();
            String cacheKey = "students::" + id;
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            
            if (cached != null) {
                return System.currentTimeMillis() - startTime; // Cache hit
            } else {
                // Cache miss - fetch from database
                studentRepository.findById(id).orElse(null);
                redisTemplate.opsForValue().set(cacheKey, new StudentDTO(), 5, TimeUnit.MINUTES);
                return System.currentTimeMillis() - startTime; // Cache miss
            }
        });
    }

    /**
     * Perform comprehensive load test comparison.
     * 
     * @param requestCount Number of requests per mode
     * @return Comparison results
     */
    public ApiResponse<Map<String, Object>> loadTestComparison(int requestCount) {
        log.info("🧪 LOAD TEST: Comprehensive comparison with {} requests per mode", requestCount);
        
        long startTime = System.currentTimeMillis();
        
        // Clear cache before test
        clearTestCache();
        
        // Test each mode
        ApiResponse<Map<String, Object>> noCacheResult = loadTestNoCache(requestCount);
        clearTestCache();
        
        ApiResponse<Map<String, Object>> manualCacheResult = loadTestManualCache(requestCount);
        clearTestCache();
        
        ApiResponse<Map<String, Object>> springCacheResult = loadTestSpringCache(requestCount);
        clearTestCache();
        
        long totalTime = System.currentTimeMillis() - startTime;
        
        // Compile comparison results
        Map<String, Object> comparison = new HashMap<>();
        comparison.put("requestCount", requestCount);
        comparison.put("noCache", noCacheResult.getData());
        comparison.put("manualCache", manualCacheResult.getData());
        comparison.put("springCache", springCacheResult.getData());
        comparison.put("totalTestTime", totalTime + "ms");
        
        // Calculate performance improvement
        double noCacheAvg = extractAverageTime(noCacheResult.getData());
        double manualAvg = extractAverageTime(manualCacheResult.getData());
        double springAvg = extractAverageTime(springCacheResult.getData());
        
        if (noCacheAvg > 0) {
            comparison.put("manualImprovement", String.format("%.2f%%", 
                    ((noCacheAvg - manualAvg) / noCacheAvg) * 100));
            comparison.put("springImprovement", String.format("%.2f%%", 
                    ((noCacheAvg - springAvg) / noCacheAvg) * 100));
        }
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(totalTime + " ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("LOAD_TEST")
                .dataSource("MIXED")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🧪 LOAD TEST: Comparison completed in {}ms", totalTime);
        
        return ApiResponse.success(comparison, metadata, "Load test comparison completed");
    }

    /**
     * Perform concurrent load test.
     * 
     * @param requestCount Number of requests
     * @param threadCount Number of concurrent threads
     * @return Concurrent load test results
     */
    public ApiResponse<Map<String, Object>> concurrentLoadTest(int requestCount, int threadCount) {
        log.info("🧪 LOAD TEST: Concurrent test - {} requests, {} threads", requestCount, threadCount);
        
        long startTime = System.currentTimeMillis();
        
        List<Long> studentIds = getRandomStudentIds(10);
        List<Future<Long>> futures = new ArrayList<>();
        
        for (int i = 0; i < requestCount; i++) {
            final int requestNum = i;
            Future<Long> future = executorService.submit(() -> {
                Long id = studentIds.get(requestNum % studentIds.size());
                long requestStart = System.currentTimeMillis();
                
                studentRepository.findById(id).orElse(null);
                
                return System.currentTimeMillis() - requestStart;
            });
            futures.add(future);
        }
        
        // Collect results
        List<Long> responseTimes = new ArrayList<>();
        for (Future<Long> future : futures) {
            try {
                responseTimes.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error in concurrent load test", e);
            }
        }
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        // Calculate statistics
        double avgResponseTime = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        long minResponseTime = responseTimes.stream().mapToLong(Long::longValue).min().orElse(0);
        long maxResponseTime = responseTimes.stream().mapToLong(Long::longValue).max().orElse(0);
        
        Map<String, Object> results = new HashMap<>();
        results.put("requestCount", requestCount);
        results.put("threadCount", threadCount);
        results.put("avgResponseTime", String.format("%.2fms", avgResponseTime));
        results.put("minResponseTime", minResponseTime + "ms");
        results.put("maxResponseTime", maxResponseTime + "ms");
        results.put("totalTime", executionTime + "ms");
        results.put("throughput", String.format("%.2f req/s", (double) requestCount / (executionTime / 1000.0)));
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("LOAD_TEST")
                .dataSource("MYSQL")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("🧪 LOAD TEST: Concurrent test completed - Throughput: {} req/s", 
                String.format("%.2f", (double) requestCount / (executionTime / 1000.0)));
        
        return ApiResponse.success(results, metadata, "Concurrent load test completed");
    }

    /**
     * Perform load test with actual implementation.
     * 
     * @param requestCount Number of requests
     * @param mode Test mode
     * @param requestLogic Request logic to execute
     * @return Load test results
     */
    private ApiResponse<Map<String, Object>> performLoadTest(int requestCount, String mode, 
            Callable<Long> requestLogic) {
        long startTime = System.currentTimeMillis();
        List<Long> responseTimes = new ArrayList<>();
        
        for (int i = 0; i < requestCount; i++) {
            try {
                long responseTime = requestLogic.call();
                responseTimes.add(responseTime);
            } catch (Exception e) {
                log.error("Error during load test request", e);
            }
        }
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        // Calculate statistics
        double avgResponseTime = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        long minResponseTime = responseTimes.stream().mapToLong(Long::longValue).min().orElse(0);
        long maxResponseTime = responseTimes.stream().mapToLong(Long::longValue).max().orElse(0);
        double medianResponseTime = calculateMedian(responseTimes);
        
        Map<String, Object> results = new HashMap<>();
        results.put("requestCount", requestCount);
        results.put("mode", mode);
        results.put("avgResponseTime", String.format("%.2fms", avgResponseTime));
        results.put("minResponseTime", minResponseTime + "ms");
        results.put("maxResponseTime", maxResponseTime + "ms");
        results.put("medianResponseTime", String.format("%.2fms", medianResponseTime));
        results.put("totalTime", executionTime + "ms");
        results.put("throughput", String.format("%.2f req/s", (double) requestCount / (executionTime / 1000.0)));
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("LOAD_TEST")
                .dataSource("MIXED")
                .timestamp(System.currentTimeMillis())
                .build();
        
        return ApiResponse.success(results, metadata, "Load test completed for " + mode);
    }

    /**
     * Get random student IDs for testing.
     * 
     * @param count Number of IDs to return
     * @return List of student IDs
     */
    private List<Long> getRandomStudentIds(int count) {
        return studentRepository.findAll().stream()
                .limit(count)
                .map(Student::getId)
                .collect(Collectors.toList());
    }

    /**
     * Clear test cache.
     */
    private void clearTestCache() {
        Set<String> keys = redisTemplate.keys("manual:student:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        
        Set<String> springKeys = redisTemplate.keys("students::*");
        if (springKeys != null && !springKeys.isEmpty()) {
            redisTemplate.delete(springKeys);
        }
    }

    /**
     * Calculate median of response times.
     * 
     * @param responseTimes List of response times
     * @return Median value
     */
    private double calculateMedian(List<Long> responseTimes) {
        List<Long> sorted = new ArrayList<>(responseTimes);
        Collections.sort(sorted);
        
        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
        } else {
            return sorted.get(size / 2);
        }
    }

    /**
     * Extract average time from results map.
     * 
     * @param results Results map
     * @return Average time as double
     */
    private double extractAverageTime(Map<String, Object> results) {
        Object avgTime = results.get("avgResponseTime");
        if (avgTime instanceof String) {
            return Double.parseDouble(((String) avgTime).replace("ms", ""));
        }
        return 0.0;
    }
}