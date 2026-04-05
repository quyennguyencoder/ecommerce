package com.nguyenquyen.ecommerce.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;

/**
 * Test configuration to disable Flyway migrations during tests.
 * This prevents database migration conflicts in test environment.
 */
@TestConfiguration
public class TestFlywayConfig {

    @Bean
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer() {
        return configuration -> configuration.baselineOnMigrate(false);
    }
}
