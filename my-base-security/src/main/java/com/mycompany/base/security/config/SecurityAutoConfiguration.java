package com.mycompany.base.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for security module.
 * Enables SecurityProperties and provides default security configuration.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {
    
    // This class enables the SecurityProperties bean
    // Additional security configuration beans can be added here
}
