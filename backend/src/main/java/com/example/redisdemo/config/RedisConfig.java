package com.example.redisdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis Configuration
 * 
 * This class configures Redis connection and serialization settings.
 * 
 * WHY this configuration exists:
 * - Sets up RedisTemplate with proper serializers
 * - Configures JSON serialization for complex objects
 * - Enables String serialization for keys
 * - Provides a clean RedisTemplate bean for the application
 * 
 * WHEN to use this configuration:
 * - Phase 2: Basic Redis operations
 * - Phase 3: Manual caching
     * - All phases requiring Redis operations
 * 
 * PRODUCTION USE CASES:
 * - Custom serialization strategies
 * - Connection pool configuration
 * - Timeout settings
 * - SSL configuration
 * 
 * SERIALIZATION STRATEGY:
 * - Keys: String serialization (human-readable)
 * - Values: JSON serialization (portable, readable)
 * - This combination is production-standard
 * 
 * WHY JSON serialization:
 * - Portable across different languages
 * - Human-readable for debugging
 * - Supports complex nested objects
 * - Standard for modern applications
 */
@Configuration
public class RedisConfig {

    /**
     * Configure RedisTemplate with proper serializers.
     * 
     * WHY this method exists:
     * - Provides a configured RedisTemplate bean
     * - Sets up JSON serialization for values
     * - Sets up String serialization for keys
     * - Makes Redis operations type-safe and convenient
     * 
     * WHEN to use this RedisTemplate:
     * - Phase 2: Basic Redis operations (String, Hash, List, Set, Sorted Set)
     * - Phase 3: Manual caching operations
     * - Phase 6: Serialization demonstrations
     * - Custom Redis operations beyond Spring Cache
     * 
     * SERIALIZATION DETAILS:
     * - Key serializer: StringRedisSerializer (human-readable keys)
     * - Value serializer: GenericJackson2JsonRedisSerializer (JSON values)
     * - Hash key serializer: StringRedisSerializer
     * - Hash value serializer: GenericJackson2JsonRedisSerializer
     * 
     * WHY this serialization strategy:
     * - Keys as strings: Easy to debug, natural for Redis
     * - Values as JSON: Portable, readable, supports complex objects
     * - Standard practice in production applications
     * - Works well with Redis CLI monitoring
     * 
     * @param connectionFactory Redis connection factory
     * @return Configured RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use String serializer for keys
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Configure ObjectMapper with JavaTimeModule for LocalDateTime support
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Use JSON serializer for values with configured ObjectMapper
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
