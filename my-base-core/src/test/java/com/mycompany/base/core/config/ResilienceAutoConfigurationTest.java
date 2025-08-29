package com.mycompany.base.core.config;

import com.mycompany.base.core.resilience.ResilienceProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ResilienceAutoConfiguration}.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ResilienceAutoConfiguration.class)
class ResilienceAutoConfigurationTest {
    
    @Autowired(required = false)
    private ResilienceProperties resilienceProperties;
    
    @Test
    void contextLoads_ShouldNotThrowException() {
        // When & Then - context should load without exception
        assertThat(true).isTrue();
    }
    
    @Test
    void resilienceProperties_ShouldBeAutowired() {
        // When & Then
        assertThat(resilienceProperties).isNotNull();
    }
    
    @Test
    void resilienceProperties_ShouldHaveDefaultValues() {
        // Given
        assertThat(resilienceProperties).isNotNull();
        
        // When & Then
        assertThat(resilienceProperties.getCircuitBreaker().getFailureRateThreshold()).isEqualTo(50);
        assertThat(resilienceProperties.getRetry().getMaxAttempts()).isEqualTo(3);
        assertThat(resilienceProperties.getBulkhead().getMaxConcurrentCalls()).isEqualTo(10);
        assertThat(resilienceProperties.getTimeLimiter().getTimeoutDuration().toSeconds()).isEqualTo(5);
    }
}
