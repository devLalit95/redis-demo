package com.example.redisdemo.controller;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.loadtest.LoadTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Load Test Controller
 * 
 * This controller provides endpoints for load testing.
 */
@RestController
@RequestMapping("/api/load-test")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Load Test", description = "Perform load testing and performance comparison")
public class LoadTestController {

    private final LoadTestService loadTestService;

    @Operation(summary = "Load test no-cache mode", description = "Perform load test for no-cache mode")
    @PostMapping("/no-cache")
    public ApiResponse<Map<String, Object>> loadTestNoCache(
            @RequestParam(defaultValue = "100") int requestCount) {
        return loadTestService.loadTestNoCache(requestCount);
    }

    @Operation(summary = "Load test manual cache mode", description = "Perform load test for manual cache mode")
    @PostMapping("/manual-cache")
    public ApiResponse<Map<String, Object>> loadTestManualCache(
            @RequestParam(defaultValue = "100") int requestCount) {
        return loadTestService.loadTestManualCache(requestCount);
    }

    @Operation(summary = "Load test Spring Cache mode", description = "Perform load test for Spring Cache mode")
    @PostMapping("/spring-cache")
    public ApiResponse<Map<String, Object>> loadTestSpringCache(
            @RequestParam(defaultValue = "100") int requestCount) {
        return loadTestService.loadTestSpringCache(requestCount);
    }

    @Operation(summary = "Load test comparison", description = "Perform comprehensive load test comparison")
    @PostMapping("/comparison")
    public ApiResponse<Map<String, Object>> loadTestComparison(
            @RequestParam(defaultValue = "100") int requestCount) {
        return loadTestService.loadTestComparison(requestCount);
    }

    @Operation(summary = "Concurrent load test", description = "Perform concurrent load test")
    @PostMapping("/concurrent")
    public ApiResponse<Map<String, Object>> concurrentLoadTest(
            @RequestParam(defaultValue = "100") int requestCount,
            @RequestParam(defaultValue = "5") int threadCount) {
        return loadTestService.concurrentLoadTest(requestCount, threadCount);
    }
}