package com.example.redisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main Spring Boot Application Class
 * 
 * This is the entry point for the Redis Learning Project application.
 * 
 * WHY @EnableCaching is now enabled:
 * - Phase 4: Spring Cache annotations become active
 * - Required for @Cacheable, @CachePut, @CacheEvict annotations to work
 * - Enables Spring's declarative caching support
 * 
 * WHEN caching is activated:
 * - Phase 4: Spring Cache annotations become active
 * - Phases 5-14: All caching features depend on this
 * 
 * WHY database auto-configuration is excluded:
 * - Redis-focused testing without MySQL dependency
 * - Allows testing Redis features without database setup
 * - Phase 8-11 APIs can work without database
 * 
 * ARCHITECTURE NOTES:
 * - Scans all components under com.example.redisdemo package
 * - Auto-configures Redis, Cache, Web, etc. (excludes DataSource and JPA)
 * - Loads configuration from application.properties
 */
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@EnableCaching  // Enabled for Phase 4 (Spring Cache)
public class RedisdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisdemoApplication.class, args);
	}

}
