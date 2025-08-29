package com.mycompany.base.core.config;

import com.mycompany.base.core.resilience.ResilienceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for resilience patterns.
 * Enables configuration properties and provides default beans.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(ResilienceProperties.class)
public class ResilienceAutoConfiguration {
    
    // This class enables the configuration properties
    // Additional beans can be added here if needed in the future
    
}
