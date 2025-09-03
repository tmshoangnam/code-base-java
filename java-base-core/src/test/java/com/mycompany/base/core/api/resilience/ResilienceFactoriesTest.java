package com.mycompany.base.core.api.resilience;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResilienceFactoriesTest {

    @Test
    void testDefaultCircuitBreakerRegistry() {
        CircuitBreakerRegistry registry = ResilienceFactories.defaultCircuitBreakerRegistry();
        
        assertThat(registry).isNotNull();
        assertThat(registry.getDefaultConfig()).isNotNull();
    }

    @Test
    void testDefaultRetryRegistry() {
        RetryRegistry registry = ResilienceFactories.defaultRetryRegistry();
        
        assertThat(registry).isNotNull();
        assertThat(registry.getDefaultConfig()).isNotNull();
    }

    @Test
    void testDefaultTimeLimiterRegistry() {
        TimeLimiterRegistry registry = ResilienceFactories.defaultTimeLimiterRegistry();
        
        assertThat(registry).isNotNull();
        assertThat(registry.getDefaultConfig()).isNotNull();
    }

    @Test
    void testDefaultBulkheadRegistry() {
        BulkheadRegistry registry = ResilienceFactories.defaultBulkheadRegistry();
        
        assertThat(registry).isNotNull();
        assertThat(registry.getDefaultConfig()).isNotNull();
    }

    @Test
    void testCircuitBreakerInstanceCreation() {
        CircuitBreakerRegistry registry = ResilienceFactories.defaultCircuitBreakerRegistry();
        
        var circuitBreaker = registry.circuitBreaker("test-service");
        
        assertThat(circuitBreaker).isNotNull();
        assertThat(circuitBreaker.getName()).isEqualTo("test-service");
    }

    @Test
    void testRetryInstanceCreation() {
        RetryRegistry registry = ResilienceFactories.defaultRetryRegistry();
        
        var retry = registry.retry("test-retry");
        
        assertThat(retry).isNotNull();
        assertThat(retry.getName()).isEqualTo("test-retry");
    }
}
