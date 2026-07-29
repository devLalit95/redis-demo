package com.example.redisdemo.serialization;

import com.example.redisdemo.dto.ApiResponse;
import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.entity.Student;
import com.example.redisdemo.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Serialization Demo Service
 * 
 * This service demonstrates different serialization strategies for Redis.
 * 
 * WHY this service exists:
 * - Shows different serialization approaches
 * - Compares serialization performance
 * - Demonstrates size differences
 * - Helps choose optimal serialization
 * 
 * WHEN to use this service:
 * - Serialization strategy selection
 * - Performance optimization
 * - Size optimization
 * - Learning serialization concepts
 * 
 * PRODUCTION USE CASES:
 * - Serialization strategy selection
 * - Performance optimization
 * - Cache size optimization
 * - Cross-language compatibility
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SerializationDemoService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    /**
     * Demonstrate JDK serialization.
     * 
     * DEMONSTRATES:
     * - Java built-in serialization
     * - Binary format
     * - Java-specific
     * 
     * @param id Student ID
     * @return Serialization results
     */
    @Transactional(readOnly = true)
    public ApiResponse<Map<String, Object>> demonstrateJDKSerialization(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("📦 SERIALIZATION: Demonstrating JDK serialization for student ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        StudentDTO studentDTO = mapToDTO(student);
        
        // JDK Serialization
        long serializeStart = System.currentTimeMillis();
        byte[] serialized = jdkSerialize(studentDTO);
        long serializeTime = System.currentTimeMillis() - serializeStart;
        
        // JDK Deserialization
        long deserializeStart = System.currentTimeMillis();
        StudentDTO deserialized = jdkDeserialize(serialized);
        long deserializeTime = System.currentTimeMillis() - deserializeStart;
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        Map<String, Object> results = new HashMap<>();
        results.put("serializationType", "JDK");
        results.put("objectSize", serialized.length + " bytes");
        results.put("serializeTime", serializeTime + " ms");
        results.put("deserializeTime", deserializeTime + " ms");
        results.put("totalTime", executionTime + " ms");
        results.put("portable", false);
        results.put("humanReadable", false);
        results.put("javaSpecific", true);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("SERIALIZATION")
                .dataSource("MEMORY")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("📦 JDK Serialization - Size: {} bytes, Serialize: {}ms, Deserialize: {}ms", 
                serialized.length, serializeTime, deserializeTime);
        
        return ApiResponse.success(results, metadata, "JDK serialization demonstrated");
    }

    /**
     * Demonstrate JSON serialization.
     * 
     * DEMONSTRATES:
     * - JSON format serialization
     * - Text-based format
     * - Language-agnostic
     * 
     * @param id Student ID
     * @return Serialization results
     */
    @Transactional(readOnly = true)
    public ApiResponse<Map<String, Object>> demonstrateJSONSerialization(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("📦 SERIALIZATION: Demonstrating JSON serialization for student ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        StudentDTO studentDTO = mapToDTO(student);
        
        // JSON Serialization
        long serializeStart = System.currentTimeMillis();
        String json;
        try {
            json = objectMapper.writeValueAsString(studentDTO);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
        long serializeTime = System.currentTimeMillis() - serializeStart;
        
        // JSON Deserialization
        long deserializeStart = System.currentTimeMillis();
        StudentDTO deserialized;
        try {
            deserialized = objectMapper.readValue(json, StudentDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
        long deserializeTime = System.currentTimeMillis() - deserializeStart;
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        Map<String, Object> results = new HashMap<>();
        results.put("serializationType", "JSON");
        results.put("objectSize", json.getBytes().length + " bytes");
        results.put("serializeTime", serializeTime + " ms");
        results.put("deserializeTime", deserializeTime + " ms");
        results.put("totalTime", executionTime + " ms");
        results.put("portable", true);
        results.put("humanReadable", true);
        results.put("javaSpecific", false);
        results.put("jsonString", json);
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("SERIALIZATION")
                .dataSource("MEMORY")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("📦 JSON Serialization - Size: {} bytes, Serialize: {}ms, Deserialize: {}ms", 
                json.getBytes().length, serializeTime, deserializeTime);
        
        return ApiResponse.success(results, metadata, "JSON serialization demonstrated");
    }

    /**
     * Compare serialization methods.
     * 
     * DEMONSTRATES:
     * - Performance comparison
     * - Size comparison
     * - Trade-offs analysis
     * 
     * @param id Student ID
     * @return Comparison results
     */
    @Transactional(readOnly = true)
    public ApiResponse<Map<String, Object>> compareSerializationMethods(Long id) {
        long startTime = System.currentTimeMillis();
        
        log.info("📦 SERIALIZATION: Comparing serialization methods for student ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        StudentDTO studentDTO = mapToDTO(student);
        
        // JDK Serialization
        long jdkSerializeStart = System.currentTimeMillis();
        byte[] jdkSerialized = jdkSerialize(studentDTO);
        long jdkSerializeTime = System.currentTimeMillis() - jdkSerializeStart;
        
        long jdkDeserializeStart = System.currentTimeMillis();
        jdkDeserialize(jdkSerialized);
        long jdkDeserializeTime = System.currentTimeMillis() - jdkDeserializeStart;
        
        // JSON Serialization
        long jsonSerializeStart = System.currentTimeMillis();
        String json;
        try {
            json = objectMapper.writeValueAsString(studentDTO);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
        long jsonSerializeTime = System.currentTimeMillis() - jsonSerializeStart;
        
        long jsonDeserializeStart = System.currentTimeMillis();
        try {
            objectMapper.readValue(json, StudentDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
        long jsonDeserializeTime = System.currentTimeMillis() - jsonDeserializeStart;
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        Map<String, Object> comparison = new HashMap<>();
        
        Map<String, Object> jdkResults = new HashMap<>();
        jdkResults.put("size", jdkSerialized.length + " bytes");
        jdkResults.put("serializeTime", jdkSerializeTime + " ms");
        jdkResults.put("deserializeTime", jdkDeserializeTime + " ms");
        jdkResults.put("totalTime", (jdkSerializeTime + jdkDeserializeTime) + " ms");
        jdkResults.put("portable", false);
        jdkResults.put("humanReadable", false);
        
        Map<String, Object> jsonResults = new HashMap<>();
        jsonResults.put("size", json.getBytes().length + " bytes");
        jsonResults.put("serializeTime", jsonSerializeTime + " ms");
        jsonResults.put("deserializeTime", jsonDeserializeTime + " ms");
        jsonResults.put("totalTime", (jsonSerializeTime + jsonDeserializeTime) + " ms");
        jsonResults.put("portable", true);
        jsonResults.put("humanReadable", true);
        
        comparison.put("jdk", jdkResults);
        comparison.put("json", jsonResults);
        
        // Calculate differences
        int sizeDiff = json.getBytes().length - jdkSerialized.length;
        double sizeDiffPercent = ((double) sizeDiff / jdkSerialized.length) * 100;
        
        comparison.put("sizeDifference", sizeDiff + " bytes");
        comparison.put("sizeDifferencePercent", String.format("%.2f%%", sizeDiffPercent));
        comparison.put("recommended", sizeDiff > 0 ? "JDK" : "JSON");
        
        ApiResponse.Metadata metadata = ApiResponse.Metadata.builder()
                .executionTime(executionTime + " ms")
                .databaseTime("0 ms")
                .redisTime("0 ms")
                .cacheHit(false)
                .cacheMiss(false)
                .cacheType("SERIALIZATION")
                .dataSource("MEMORY")
                .timestamp(System.currentTimeMillis())
                .build();
        
        log.info("📦 Serialization comparison completed - JDK: {} bytes, JSON: {} bytes", 
                jdkSerialized.length, json.getBytes().length);
        
        return ApiResponse.success(comparison, metadata, "Serialization methods compared");
    }

    /**
     * JDK serialization.
     * 
     * @param obj Object to serialize
     * @return Serialized bytes
     */
    private byte[] jdkSerialize(Object obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("JDK serialization failed", e);
        }
    }

    /**
     * JDK deserialization.
     * 
     * @param data Serialized bytes
     * @return Deserialized object
     */
    @SuppressWarnings("unchecked")
    private <T> T jdkDeserialize(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (T) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("JDK deserialization failed", e);
        }
    }

    /**
     * Map Student entity to StudentDTO.
     * 
     * @param student The entity to convert
     * @return The DTO representation
     */
    private StudentDTO mapToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setRollNumber(student.getRollNumber());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setCourse(student.getCourse());
        dto.setBranch(student.getBranch());
        dto.setSemester(student.getSemester());
        dto.setCgpa(student.getCgpa());
        dto.setCity(student.getCity());
        dto.setAddress(student.getAddress());
        dto.setStatus(student.getStatus());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        return dto;
    }
}