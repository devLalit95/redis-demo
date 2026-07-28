package com.example.redisdemo.cache;

import com.example.redisdemo.dto.StudentDTO;
import com.example.redisdemo.entity.Student;
import com.example.redisdemo.exception.ResourceNotFoundException;
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
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Serialization Service
 * 
 * This service demonstrates different serialization strategies for Redis caching.
 * This is Phase 6 of the project - Serialization Comparison.
 * 
 * WHY this service exists:
 * - Teaches serialization concepts through practical implementation
 * - Shows JDK vs JSON serialization differences
 * - Demonstrates serialization performance and size differences
 * - Helps choose right serialization strategy
 * 
 * WHEN to use this service:
 * - Phase 6: Learning serialization concepts
 * - Understanding serialization trade-offs
 * - Choosing serialization strategy for production
 * 
 * PRODUCTION USE CASES:
 * - Performance optimization
 * - Memory optimization
 * - Cross-platform compatibility
 * - Debugging serialization issues
 * 
 * SERIALIZATION STRATEGIES COVERED:
 * - JDK Serialization: Java-specific, binary format
 * - Jackson JSON: Human-readable, cross-platform
 * - GenericJackson2JsonRedisSerializer: Type-safe JSON
 * 
 * KEY CONCEPTS:
 * - Serialization: Converting objects to bytes for storage
 * - Deserialization: Converting bytes back to objects
 * - Performance: Speed of serialization/deserialization
 * - Size: Memory footprint of serialized data
 * - Portability: Cross-language compatibility
 */
// @Service  // Disabled for Redis-only testing
@RequiredArgsConstructor
@Slf4j
public class SerializationService {

    // Removed StudentRepository dependency for Redis-only testing
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // ==================== JDK SERIALIZATION ====================

    /**
     * Store student using JDK serialization.
     * 
     * WHY JDK serialization:
     * - Built-in Java serialization
     * - Fast for Java-to-Java communication
     * - Preserves object graph
     * 
     * WHEN to use JDK serialization:
     * - Java-only applications
     * - Performance-critical operations
     * - Complex object graphs
     * 
     * PRODUCTION USE CASES:
     * - Internal Java services
     * - Session replication
     * - RMI communication
     * 
     * DRAWBACKS:
     * - Not human-readable
     * - Java-specific (not portable)
     * - Version compatibility issues
     * - Security concerns
     * 
     * @param id The student ID
     * @return The student DTO
     */
    public StudentDTO storeWithJDKSerialization(Long id) {
        log.info("🔧 JDK Serialization: Storing student ID: {}", id);
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Create a placeholder student DTO
        StudentDTO dto = StudentDTO.builder()
                .id(id)
                .name("Test Student " + id)
                .email("test" + id + "@example.com")
                .message("JDK Serialization (Redis-only testing)")
                .build();
        
        try {
            // Serialize using JDK serialization
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(dto);
            oos.close();
            
            byte[] serializedData = bos.toByteArray();
            String base64Data = Base64.getEncoder().encodeToString(serializedData);
            
            // Store in Redis
            String cacheKey = "jdk:student:" + id;
            redisTemplate.opsForValue().set(cacheKey, base64Data, 5, TimeUnit.MINUTES);
            
            log.info("✅ Student stored with JDK serialization");
            log.info("Serialized size: {} bytes", serializedData.length);
            log.info("Cache Key: {}", cacheKey);
            
            return dto;
            
        } catch (Exception e) {
            log.error("JDK serialization failed", e);
            throw new RuntimeException("JDK serialization failed", e);
        }
    }

    /**
     * Retrieve student using JDK deserialization.
     * 
     * @param id The student ID
     * @return The student DTO
     */
    @SuppressWarnings("unchecked")
    public StudentDTO retrieveWithJDKDeserialization(Long id) {
        log.info("🔧 JDK Deserialization: Retrieving student ID: {}", id);
        
        String cacheKey = "jdk:student:" + id;
        String base64Data = (String) redisTemplate.opsForValue().get(cacheKey);
        
        if (base64Data == null) {
            log.info("❌ Student not found in cache");
            return null;
        }
        
        try {
            // Deserialize using JDK deserialization
            byte[] serializedData = Base64.getDecoder().decode(base64Data);
            ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
            ObjectInputStream ois = new ObjectInputStream(bis);
            StudentDTO dto = (StudentDTO) ois.readObject();
            ois.close();
            
            log.info("✅ Student retrieved with JDK deserialization");
            log.info("Cache Key: {}", cacheKey);
            
            return dto;
            
        } catch (Exception e) {
            log.error("JDK deserialization failed", e);
            throw new RuntimeException("JDK deserialization failed", e);
        }
    }

    // ==================== JSON SERIALIZATION ====================

    /**
     * Store student using JSON serialization.
     * 
     * WHY JSON serialization:
     * - Human-readable format
     * - Cross-platform compatibility
     * - Language-agnostic
     * - Easy debugging
     * 
     * WHEN to use JSON serialization:
     * - Multi-language environments
     * - Web services
     * - Debugging and monitoring
     * - Cross-platform communication
     * 
     * PRODUCTION USE CASES:
     * - REST APIs
     * - Microservices
     * - Web applications
     * - Mobile applications
     * 
     * ADVANTAGES:
     * - Human-readable
     * - Cross-platform
     * - Language-agnostic
     * - Easy debugging
     * 
     * DRAWBACKS:
     * - Larger size than binary
     * - Slower than binary serialization
     * - Type information loss
     * 
     * @param id The student ID
     * @return The student DTO
     */
    public StudentDTO storeWithJSONSerialization(Long id) {
        log.info("🔧 JSON Serialization: Storing student ID: {}", id);
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Create a placeholder student DTO
        StudentDTO dto = StudentDTO.builder()
                .id(id)
                .name("Test Student " + id)
                .email("test" + id + "@example.com")
                .message("JSON Serialization (Redis-only testing)")
                .build();
        
        try {
            // Serialize using JSON
            String jsonData = objectMapper.writeValueAsString(dto);
            
            // Store in Redis
            String cacheKey = "json:student:" + id;
            redisTemplate.opsForValue().set(cacheKey, jsonData, 5, TimeUnit.MINUTES);
            
            log.info("✅ Student stored with JSON serialization");
            log.info("Serialized size: {} bytes", jsonData.getBytes().length);
            log.info("JSON: {}", jsonData);
            log.info("Cache Key: {}", cacheKey);
            
            return dto;
            
        } catch (Exception e) {
            log.error("JSON serialization failed", e);
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    /**
     * Retrieve student using JSON deserialization.
     * 
     * @param id The student ID
     * @return The student DTO
     */
    public StudentDTO retrieveWithJSONDeserialization(Long id) {
        log.info("🔧 JSON Deserialization: Retrieving student ID: {}", id);
        
        String cacheKey = "json:student:" + id;
        String jsonData = (String) redisTemplate.opsForValue().get(cacheKey);
        
        if (jsonData == null) {
            log.info("❌ Student not found in cache");
            return null;
        }
        
        try {
            // Deserialize using JSON
            StudentDTO dto = objectMapper.readValue(jsonData, StudentDTO.class);
            
            log.info("✅ Student retrieved with JSON deserialization");
            log.info("JSON: {}", jsonData);
            log.info("Cache Key: {}", cacheKey);
            
            return dto;
            
        } catch (Exception e) {
            log.error("JSON deserialization failed", e);
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }

    // ==================== REDIS TEMPLATE SERIALIZATION ====================

    /**
     * Store student using RedisTemplate with JSON serializer.
     * 
     * WHY RedisTemplate serialization:
     * - Spring's built-in serialization
     * - Configured at template level
     * - Automatic serialization/deserialization
     * - Type-safe operations
     * 
     * WHEN to use RedisTemplate serialization:
     * - Spring Boot applications
     * - Type-safe operations
     * - Automatic serialization management
     * 
     * PRODUCTION USE CASES:
     * - Spring Boot applications
     * - Type-safe caching
     * - Automatic serialization
     * 
     * @param id The student ID
     * @return The student DTO
     */
    public StudentDTO storeWithRedisTemplate(Long id) {
        log.info("🔧 RedisTemplate Serialization: Storing student ID: {}", id);
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Create a placeholder student DTO
        StudentDTO dto = StudentDTO.builder()
                .id(id)
                .name("Test Student " + id)
                .email("test" + id + "@example.com")
                .message("RedisTemplate Serialization (Redis-only testing)")
                .build();
        
        // Store using RedisTemplate (uses configured JSON serializer)
        String cacheKey = "template:student:" + id;
        redisTemplate.opsForValue().set(cacheKey, dto, 5, TimeUnit.MINUTES);
        
        log.info("✅ Student stored with RedisTemplate serialization");
        log.info("Cache Key: {}", cacheKey);
        
        return dto;
    }

    /**
     * Retrieve student using RedisTemplate.
     * 
     * @param id The student ID
     * @return The student DTO
     */
    public StudentDTO retrieveWithRedisTemplate(Long id) {
        log.info("🔧 RedisTemplate Deserialization: Retrieving student ID: {}", id);
        
        String cacheKey = "template:student:" + id;
        StudentDTO dto = (StudentDTO) redisTemplate.opsForValue().get(cacheKey);
        
        if (dto == null) {
            log.info("❌ Student not found in cache");
            return null;
        }
        
        log.info("✅ Student retrieved with RedisTemplate deserialization");
        log.info("Cache Key: {}", cacheKey);
        
        return dto;
    }

    // ==================== SERIALIZATION COMPARISON ====================

    /**
     * Compare serialization methods.
     * 
     * WHY this method exists:
     * - Demonstrates performance differences
     * - Shows size differences
     * - Helps choose right serialization
     * 
     * @param id The student ID
     * @return Comparison results
     */
    public String compareSerialization(Long id) {
        log.info("🔧 Comparing serialization methods for student ID: {}", id);
        log.info("⚠️ Database operations disabled for Redis-only testing");
        
        // Create a placeholder student DTO
        StudentDTO dto = StudentDTO.builder()
                .id(id)
                .name("Test Student " + id)
                .email("test" + id + "@example.com")
                .message("Serialization comparison (Redis-only testing)")
                .build();
        
        StringBuilder comparison = new StringBuilder();
        comparison.append("Serialization Comparison for Student ID: ").append(id).append("\n\n");
        
        // JDK Serialization
        long jdkStartTime = System.currentTimeMillis();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(dto);
            oos.close();
            byte[] jdkData = bos.toByteArray();
            long jdkTime = System.currentTimeMillis() - jdkStartTime;
            
            comparison.append("JDK Serialization:\n");
            comparison.append("  Size: ").append(jdkData.length).append(" bytes\n");
            comparison.append("  Time: ").append(jdkTime).append(" ms\n");
            comparison.append("  Format: Binary, Java-specific\n");
            comparison.append("  Pros: Fast, preserves object graph\n");
            comparison.append("  Cons: Not portable, version issues\n\n");
            
        } catch (Exception e) {
            comparison.append("JDK Serialization: Failed\n\n");
        }
        
        // JSON Serialization
        long jsonStartTime = System.currentTimeMillis();
        try {
            String jsonData = objectMapper.writeValueAsString(dto);
            byte[] jsonBytes = jsonData.getBytes();
            long jsonTime = System.currentTimeMillis() - jsonStartTime;
            
            comparison.append("JSON Serialization:\n");
            comparison.append("  Size: ").append(jsonBytes.length).append(" bytes\n");
            comparison.append("  Time: ").append(jsonTime).append(" ms\n");
            comparison.append("  Format: Text, cross-platform\n");
            comparison.append("  Pros: Portable, human-readable\n");
            comparison.append("  Cons: Larger size, slower\n\n");
            
        } catch (Exception e) {
            comparison.append("JSON Serialization: Failed\n\n");
        }
        
        // RedisTemplate Serialization
        long templateStartTime = System.currentTimeMillis();
        try {
            String cacheKey = "comparison:student:" + id;
            redisTemplate.opsForValue().set(cacheKey, dto, 1, TimeUnit.MINUTES);
            long templateTime = System.currentTimeMillis() - templateStartTime;
            
            comparison.append("RedisTemplate Serialization:\n");
            comparison.append("  Time: ").append(templateTime).append(" ms\n");
            comparison.append("  Format: JSON (configured)\n");
            comparison.append("  Pros: Automatic, type-safe\n");
            comparison.append("  Cons: Depends on configuration\n\n");
            
        } catch (Exception e) {
            comparison.append("RedisTemplate Serialization: Failed\n\n");
        }
        
        log.info("Serialization comparison completed");
        
        return comparison.toString();
    }

    /**
     * Get serialization explanation.
     * 
     * @return Serialization explanation
     */
    public String getSerializationExplanation() {
        return """
            Serialization Concepts:
            
            WHAT IS SERIALIZATION:
            - Converting objects to bytes for storage/transmission
            - Essential for caching and distributed systems
            - Reversible process (deserialization)
            
            SERIALIZATION TYPES:
            
            1. JDK SERIALIZATION:
            - Java's built-in binary serialization
            - Fast but Java-specific
            - Preserves complete object graph
            - Version compatibility issues
            - Not human-readable
            - Best for: Java-only systems, performance-critical
            
            2. JSON SERIALIZATION:
            - Text-based, human-readable format
            - Cross-platform compatible
            - Language-agnostic
            - Larger size than binary
            - Slower than binary
            - Best for: Web services, microservices, debugging
            
            3. REDIS TEMPLATE SERIALIZATION:
            - Spring's abstraction layer
            - Configurable serialization strategy
            - Type-safe operations
            - Automatic serialization management
            - Best for: Spring Boot applications, type safety
            
            TRADE-OFFS:
            - Performance vs Portability
            - Size vs Readability
            - Speed vs Compatibility
            - Complexity vs Maintainability
            
            PRODUCTION RECOMMENDATIONS:
            - Use JSON for web services and microservices
            - Use JDK for internal Java-only services
            - Use RedisTemplate for Spring Boot applications
            - Consider memory constraints for large datasets
            - Test serialization performance for your use case
            """;
    }

    /**
     * Manual mapping from Student entity to StudentDTO.
     * 
     * @param student The entity to convert
     * @return The DTO representation
     */
    private StudentDTO mapToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setCourse(student.getCourse());
        dto.setBranch(student.getBranch());
        dto.setSemester(student.getSemester());
        dto.setCgpa(student.getCgpa());
        dto.setCity(student.getCity());
        dto.setPhone(student.getPhone());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        return dto;
    }
}
