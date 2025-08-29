package com.mycompany.base.core.resilience;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ResilienceProperties}.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class ResiliencePropertiesTest {
    
    @Test
    void circuitBreaker_DefaultValues_ShouldBeCorrect() {
        // When
        ResilienceProperties properties = new ResilienceProperties();
        ResilienceProperties.CircuitBreaker cb = properties.getCircuitBreaker();
        
        // Then
        assertThat(cb.getFailureRateThreshold()).isEqualTo(50);
        assertThat(cb.getWaitDurationInOpenState()).isEqualTo(Duration.ofSeconds(60));
        assertThat(cb.getSlidingWindowSize()).isEqualTo(100);
        assertThat(cb.getMinimumNumberOfCalls()).isEqualTo(10);
        assertThat(cb.getSlowCallDurationThreshold()).isEqualTo(Duration.ofSeconds(2));
        assertThat(cb.getSlowCallRateThreshold()).isEqualTo(100);
        assertThat(cb.isAutomaticTransitionFromOpenToHalfOpenEnabled()).isTrue();
    }
    
    @Test
    void retry_DefaultValues_ShouldBeCorrect() {
        // When
        ResilienceProperties properties = new ResilienceProperties();
        ResilienceProperties.Retry retry = properties.getRetry();
        
        // Then
        assertThat(retry.getMaxAttempts()).isEqualTo(3);
        assertThat(retry.getWaitDuration()).isEqualTo(Duration.ofMillis(100));
        assertThat(retry.getIntervalFunction()).isEqualTo(Duration.ofMillis(100));
        assertThat(retry.isEnableExponentialBackoff()).isFalse();
        assertThat(retry.getMultiplier()).isEqualTo(2.0);
        assertThat(retry.getMaxWaitDuration()).isEqualTo(Duration.ofSeconds(10));
    }
    
    @Test
    void bulkhead_DefaultValues_ShouldBeCorrect() {
        // When
        ResilienceProperties properties = new ResilienceProperties();
        ResilienceProperties.Bulkhead bulkhead = properties.getBulkhead();
        
        // Then
        assertThat(bulkhead.getMaxConcurrentCalls()).isEqualTo(10);
        assertThat(bulkhead.getMaxWaitDuration()).isEqualTo(Duration.ofMillis(100));
        assertThat(bulkhead.getMaxConcurrentCallsPerThread()).isEqualTo(5);
    }
    
    @Test
    void timeLimiter_DefaultValues_ShouldBeCorrect() {
        // When
        ResilienceProperties properties = new ResilienceProperties();
        ResilienceProperties.TimeLimiter timeLimiter = properties.getTimeLimiter();
        
        // Then
        assertThat(timeLimiter.getTimeoutDuration()).isEqualTo(Duration.ofSeconds(5));
        assertThat(timeLimiter.isCancelRunningFuture()).isTrue();
    }
    
    @Test
    void circuitBreaker_Setters_ShouldUpdateValues() {
        // Given
        ResilienceProperties properties = new ResilienceProperties();
        ResilienceProperties.CircuitBreaker cb = properties.getCircuitBreaker();
        
        // When
        cb.setFailureRateThreshold(75);
        cb.setWaitDurationInOpenState(Duration.ofSeconds(120));
        cb.setSlidingWindowSize(200);
        cb.setMinimumNumberOfCalls(25);
        cb.setSlowCallDurationThreshold(Duration.ofSeconds(10));
        cb.setSlowCallRateThreshold(80);
        cb.setAutomaticTransitionFromOpenToHalfOpenEnabled(false);
        
        // Then
        assertThat(cb.getFailureRateThreshold()).isEqualTo(75);
        assertThat(cb.getWaitDurationInOpenState()).isEqualTo(Duration.ofSeconds(120));
        assertThat(cb.getSlidingWindowSize()).isEqualTo(200);
        assertThat(cb.getMinimumNumberOfCalls()).isEqualTo(25);
        assertThat(cb.getSlowCallDurationThreshold()).isEqualTo(Duration.ofSeconds(10));
        assertThat(cb.getSlowCallRateThreshold()).isEqualTo(80);
        assertThat(cb.isAutomaticTransitionFromOpenToHalfOpenEnabled()).isFalse();
    }
    
    @Test
    void retry_Setters_ShouldUpdateValues() {
        // Given
        ResilienceProperties properties = new ResilienceProperties();
        ResilienceProperties.Retry retry = properties.getRetry();
        
        // When
        retry.setMaxAttempts(5);
        retry.setWaitDuration(Duration.ofMillis(500));
        retry.setIntervalFunction(Duration.ofMillis(250));
        retry.setEnableExponentialBackoff(true);
        retry.setMultiplier(3.0);
        retry.setMaxWaitDuration(Duration.ofSeconds(30));
        
        // Then
        assertThat(retry.getMaxAttempts()).isEqualTo(5);
        assertThat(retry.getWaitDuration()).isEqualTo(Duration.ofMillis(500));
        assertThat(retry.getIntervalFunction()).isEqualTo(Duration.ofMillis(250));
        assertThat(retry.isEnableExponentialBackoff()).isTrue();
        assertThat(retry.getMultiplier()).isEqualTo(3.0);
        assertThat(retry.getMaxWaitDuration()).isEqualTo(Duration.ofSeconds(30));
    }
    
    @Test
    void bulkhead_Setters_ShouldUpdateValues() {
        // Given
        ResilienceProperties properties = new ResilienceProperties();
        ResilienceProperties.Bulkhead bulkhead = properties.getBulkhead();
        
        // When
        bulkhead.setMaxConcurrentCalls(25);
        bulkhead.setMaxWaitDuration(Duration.ofMillis(250));
        bulkhead.setMaxConcurrentCallsPerThread(10);
        
        // Then
        assertThat(bulkhead.getMaxConcurrentCalls()).isEqualTo(25);
        assertThat(bulkhead.getMaxWaitDuration()).isEqualTo(Duration.ofMillis(250));
        assertThat(bulkhead.getMaxConcurrentCallsPerThread()).isEqualTo(10);
    }
    
    @Test
    void timeLimiter_Setters_ShouldUpdateValues() {
        // Given
        ResilienceProperties properties = new ResilienceProperties();
        ResilienceProperties.TimeLimiter timeLimiter = properties.getTimeLimiter();
        
        // When
        timeLimiter.setTimeoutDuration(Duration.ofSeconds(15));
        timeLimiter.setCancelRunningFuture(false);
        
        // Then
        assertThat(timeLimiter.getTimeoutDuration()).isEqualTo(Duration.ofSeconds(15));
        assertThat(timeLimiter.isCancelRunningFuture()).isFalse();
    }
    
    @Test
    void properties_Setters_ShouldUpdateValues() {
        // Given
        ResilienceProperties properties = new ResilienceProperties();
        
        // When
        ResilienceProperties.CircuitBreaker newCb = new ResilienceProperties.CircuitBreaker();
        newCb.setFailureRateThreshold(100);
        properties.setCircuitBreaker(newCb);
        
        ResilienceProperties.Retry newRetry = new ResilienceProperties.Retry();
        newRetry.setMaxAttempts(10);
        properties.setRetry(newRetry);
        
        ResilienceProperties.Bulkhead newBulkhead = new ResilienceProperties.Bulkhead();
        newBulkhead.setMaxConcurrentCalls(50);
        properties.setBulkhead(newBulkhead);
        
        ResilienceProperties.TimeLimiter newTimeLimiter = new ResilienceProperties.TimeLimiter();
        newTimeLimiter.setTimeoutDuration(Duration.ofSeconds(30));
        properties.setTimeLimiter(newTimeLimiter);
        
        // Then
        assertThat(properties.getCircuitBreaker().getFailureRateThreshold()).isEqualTo(100);
        assertThat(properties.getRetry().getMaxAttempts()).isEqualTo(10);
        assertThat(properties.getBulkhead().getMaxConcurrentCalls()).isEqualTo(50);
        assertThat(properties.getTimeLimiter().getTimeoutDuration()).isEqualTo(Duration.ofSeconds(30));
    }
}
