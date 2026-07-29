package com.example.redisdemo.controller;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.serialization.SerializationDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Serialization Demo Controller
 * 
 * This controller provides endpoints for serialization demonstrations.
 */
@RestController
@RequestMapping("/api/serialization")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Serialization Demo", description = "Demonstrate different serialization strategies")
public class SerializationDemoController {

    private final SerializationDemoService serializationDemoService;

    @Operation(summary = "Demonstrate JDK serialization", description = "Demonstrate JDK serialization for a student")
    @GetMapping("/jdk/{id}")
    public ApiResponse<Map<String, Object>> demonstrateJDKSerialization(@PathVariable Long id) {
        return serializationDemoService.demonstrateJDKSerialization(id);
    }

    @Operation(summary = "Demonstrate JSON serialization", description = "Demonstrate JSON serialization for a student")
    @GetMapping("/json/{id}")
    public ApiResponse<Map<String, Object>> demonstrateJSONSerialization(@PathVariable Long id) {
        return serializationDemoService.demonstrateJSONSerialization(id);
    }

    @Operation(summary = "Compare serialization methods", description = "Compare JDK and JSON serialization methods")
    @GetMapping("/compare/{id}")
    public ApiResponse<Map<String, Object>> compareSerializationMethods(@PathVariable Long id) {
        return serializationDemoService.compareSerializationMethods(id);
    }
}