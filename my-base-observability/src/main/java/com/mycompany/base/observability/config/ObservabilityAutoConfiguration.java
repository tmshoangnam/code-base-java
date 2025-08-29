package com.mycompany.base.observability.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for observability module.
 * Enables ObservabilityProperties and provides default monitoring configuration.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(ObservabilityProperties.class)
public class ObservabilityAutoConfiguration {
    
    // This class enables the ObservabilityProperties bean
    // Additional observability configuration beans can be added here
}
