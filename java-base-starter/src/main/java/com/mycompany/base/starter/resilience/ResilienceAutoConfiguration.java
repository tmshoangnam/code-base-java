package com.mycompany.base.starter.resilience;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for resilience patterns.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(ResilienceProperties.class)
@ConditionalOnProperty(prefix = "my-base.resilience", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResilienceAutoConfiguration {
    
    // Auto-configuration beans will be added here as needed
}
