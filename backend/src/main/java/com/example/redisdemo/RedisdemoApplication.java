package com.example.redisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot Application Class
 * 
 * This is the entry point for the Redis Learning Project application.
 * 
 * WHY @EnableCaching is enabled:
 * - Phase 4: Spring Cache annotations become active
 * - Required for @Cacheable, @CachePut, @CacheEvict annotations to work
 * - Enables Spring's declarative caching support
 * 
 * WHEN caching is activated:
 * - Phase 4: Spring Cache annotations become active
 * - Phases 5-14: All caching features depend on this
 * 
 * WHY @EnableJpaRepositories is enabled:
 * - Explicitly enables JPA repositories to avoid conflict with Redis repositories
 * - Ensures StudentRepository is recognized as a JPA repository
 * - Prevents Spring Data Redis from trying to treat JPA repositories as Redis repositories
 * 
 * ARCHITECTURE NOTES:
 * - Scans all components under com.example.redisdemo package
 * - Auto-configures Redis, Cache, Web, DataSource, JPA, etc.
 * - Supports both MySQL database and Redis caching
 * - Explicit repository configuration to avoid conflicts
 */
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = "com.example.redisdemo.repository")
public class RedisdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisdemoApplication.class, args);
	}

}
