package com.example.redisdemo.controller;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.simulation.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Simulation Controller
 * 
 * This controller provides endpoints for simulating various scenarios.
 */
@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Simulation", description = "Simulate various performance scenarios")
public class SimulationController {

    private final SimulationService simulationService;

    @Operation(summary = "Simulate slow database", description = "Simulate slow database with custom delay")
    @GetMapping("/slow-database/{id}")
    public ApiResponse<StudentDTO> simulateSlowDatabase(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1000") Long delayMs) {
        return simulationService.simulateSlowDatabase(delayMs, id);
    }

    @Operation(summary = "Simulate fast Redis", description = "Simulate fast Redis cache hit")
    @GetMapping("/fast-redis/{id}")
    public ApiResponse<StudentDTO> simulateFastRedis(@PathVariable Long id) {
        return simulationService.simulateFastRedis(id);
    }

    @Operation(summary = "Simulate cache miss", description = "Simulate cache miss scenario")
    @GetMapping("/cache-miss/{id}")
    public ApiResponse<StudentDTO> simulateCacheMiss(@PathVariable Long id) {
        return simulationService.simulateCacheMiss(id);
    }

    @Operation(summary = "Simulate cache hit", description = "Simulate cache hit scenario")
    @GetMapping("/cache-hit/{id}")
    public ApiResponse<StudentDTO> simulateCacheHit(@PathVariable Long id) {
        return simulationService.simulateCacheHit(id);
    }

    @Operation(summary = "Simulate TTL expiration", description = "Simulate TTL expiration with custom TTL")
    @GetMapping("/ttl-expiration/{id}")
    public ApiResponse<StudentDTO> simulateTTLExpiration(
            @PathVariable Long id,
            @RequestParam(defaultValue = "30") Long ttlSeconds) {
        return simulationService.simulateTTLExpiration(id, ttlSeconds);
    }

    @Operation(summary = "Simulate database failure", description = "Simulate database failure with cache fallback")
    @GetMapping("/database-failure/{id}")
    public ApiResponse<StudentDTO> simulateDatabaseFailure(@PathVariable Long id) {
        return simulationService.simulateDatabaseFailure(id);
    }

    @Operation(summary = "Simulate Redis down", description = "Simulate Redis unavailability with database fallback")
    @GetMapping("/redis-down/{id}")
    public ApiResponse<StudentDTO> simulateRedisDown(@PathVariable Long id) {
        return simulationService.simulateRedisDown(id);
    }

    @Operation(summary = "Load test comparison", description = "Perform load test comparison between modes")
    @GetMapping("/load-test")
    public ApiResponse<Map<String, Object>> loadTestComparison(
            @RequestParam(defaultValue = "100") int requestCount,
            @RequestParam(defaultValue = "NO_CACHE") String mode) {
        return simulationService.loadTestComparison(requestCount, mode);
    }
}