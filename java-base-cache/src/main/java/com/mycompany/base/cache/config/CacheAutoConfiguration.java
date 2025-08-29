package com.mycompany.base.cache.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for cache module.
 * Enables CacheProperties and provides default cache configuration.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {
    
    // This class enables the CacheProperties bean
    // Additional cache configuration beans can be added here
}
