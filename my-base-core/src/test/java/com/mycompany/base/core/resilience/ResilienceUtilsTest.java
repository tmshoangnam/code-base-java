package com.mycompany.base.core.resilience;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link ResilienceUtils}.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class ResilienceUtilsTest {
    
    @Test
    void createCircuitBreaker_WithDefaultConfig_ShouldCreateInstance() {
        // When
        CircuitBreaker circuitBreaker = ResilienceUtils.createCircuitBreaker("test");
        
        // Then
        assertThat(circuitBreaker).isNotNull();
        assertThat(circuitBreaker.getName()).isEqualTo("test");
        assertThat(circuitBreaker.getCircuitBreakerConfig().getFailureRateThreshold()).isEqualTo(50);
        assertThat(circuitBreaker.getCircuitBreakerConfig().getWaitDurationInOpenState()).isEqualTo(Duration.ofSeconds(60));
        assertThat(circuitBreaker.getCircuitBreakerConfig().getSlidingWindowSize()).isEqualTo(100);
    }
    
    @Test
    void createCircuitBreaker_WithCustomConfig_ShouldApplyProperties() {
        // Given
        ResilienceProperties properties = new ResilienceProperties();
        properties.getCircuitBreaker().setFailureRateThreshold(75);
        properties.getCircuitBreaker().setWaitDurationInOpenState(Duration.ofSeconds(120));
        properties.getCircuitBreaker().setSlidingWindowSize(200);
        properties.getCircuitBreaker().setMinimumNumberOfCalls(20);
        properties.getCircuitBreaker().setSlowCallDurationThreshold(Duration.ofSeconds(5));
        properties.getCircuitBreaker().setSlowCallRateThreshold(80);
        properties.getCircuitBreaker().setAutomaticTransitionFromOpenToHalfOpenEnabled(false);
        
        // When
        CircuitBreaker circuitBreaker = ResilienceUtils.createCircuitBreaker("test", properties);
        
        // Then
        assertThat(circuitBreaker).isNotNull();
        assertThat(circuitBreaker.getName()).isEqualTo("test");
        assertThat(circuitBreaker.getCircuitBreakerConfig().getFailureRateThreshold()).isEqualTo(75);
        assertThat(circuitBreaker.getCircuitBreakerConfig().getWaitDurationInOpenState()).isEqualTo(Duration.ofSeconds(120));
        assertThat(circuitBreaker.getCircuitBreakerConfig().getSlidingWindowSize()).isEqualTo(200);
        assertThat(circuitBreaker.getCircuitBreakerConfig().getMinimumNumberOfCalls()).isEqualTo(20);
        assertThat(circuitBreaker.getCircuitBreakerConfig().getSlowCallDurationThreshold()).isEqualTo(Duration.ofSeconds(5));
        assertThat(circuitBreaker.getCircuitBreakerConfig().getSlowCallRateThreshold()).isEqualTo(80);
        assertThat(circuitBreaker.getCircuitBreakerConfig().isAutomaticTransitionFromOpenToHalfOpenEnabled()).isFalse();
    }
    
    @Test
    void createRetry_WithDefaultConfig_ShouldCreateInstance() {
        // When
        Retry retry = ResilienceUtils.createRetry("test");
        
        // Then
        assertThat(retry).isNotNull();
        assertThat(retry.getName()).isEqualTo("test");
        assertThat(retry.getRetryConfig().getMaxAttempts()).isEqualTo(3);
        assertThat(retry.getRetryConfig().getWaitDuration()).isEqualTo(Duration.ofMillis(100));
    }
    
    @Test
    void createRetry_WithCustomConfig_ShouldApplyProperties() {
        // Given
        ResilienceProperties properties = new ResilienceProperties();
        properties.getRetry().setMaxAttempts(5);
        properties.getRetry().setWaitDuration(Duration.ofMillis(500));
        properties.getRetry().setEnableExponentialBackoff(true);
        properties.getRetry().setMultiplier(3.0);
        properties.getRetry().setMaxWaitDuration(Duration.ofSeconds(30));
        
        // When
        Retry retry = ResilienceUtils.createRetry("test", properties);
        
        // Then
        assertThat(retry).isNotNull();
        assertThat(retry.getName()).isEqualTo("test");
        assertThat(retry.getRetryConfig().getMaxAttempts()).isEqualTo(5);
        assertThat(retry.getRetryConfig().getWaitDuration()).isEqualTo(Duration.ofMillis(500));
    }
    
    @Test
    void createBulkhead_WithDefaultConfig_ShouldCreateInstance() {
        // When
        Bulkhead bulkhead = ResilienceUtils.createBulkhead("test");
        
        // Then
        assertThat(bulkhead).isNotNull();
        assertThat(bulkhead.getName()).isEqualTo("test");
        assertThat(bulkhead.getBulkheadConfig().getMaxConcurrentCalls()).isEqualTo(10);
        assertThat(bulkhead.getBulkheadConfig().getMaxWaitDuration()).isEqualTo(Duration.ofMillis(100));
    }
    
    @Test
    void createBulkhead_WithCustomConfig_ShouldApplyProperties() {
        // Given
        ResilienceProperties properties = new ResilienceProperties();
        properties.getBulkhead().setMaxConcurrentCalls(25);
        properties.getBulkhead().setMaxWaitDuration(Duration.ofMillis(250));
        properties.getBulkhead().setMaxConcurrentCallsPerThread(10);
        
        // When
        Bulkhead bulkhead = ResilienceUtils.createBulkhead("test", properties);
        
        // Then
        assertThat(bulkhead).isNotNull();
        assertThat(bulkhead.getName()).isEqualTo("test");
        assertThat(bulkhead.getBulkheadConfig().getMaxConcurrentCalls()).isEqualTo(25);
        assertThat(bulkhead.getBulkheadConfig().getMaxWaitDuration()).isEqualTo(Duration.ofMillis(250));
    }
    
    @Test
    void createTimeLimiter_WithDefaultConfig_ShouldCreateInstance() {
        // When
        TimeLimiter timeLimiter = ResilienceUtils.createTimeLimiter("test");
        
        // Then
        assertThat(timeLimiter).isNotNull();
        assertThat(timeLimiter.getName()).isEqualTo("test");
        assertThat(timeLimiter.getTimeLimiterConfig().getTimeoutDuration()).isEqualTo(Duration.ofSeconds(5));
        assertThat(timeLimiter.getTimeLimiterConfig().shouldCancelRunningFuture()).isTrue();
    }
    
    @Test
    void createTimeLimiter_WithCustomConfig_ShouldApplyProperties() {
        // Given
        ResilienceProperties properties = new ResilienceProperties();
        properties.getTimeLimiter().setTimeoutDuration(Duration.ofSeconds(10));
        properties.getTimeLimiter().setCancelRunningFuture(false);
        
        // When
        TimeLimiter timeLimiter = ResilienceUtils.createTimeLimiter("test", properties);
        
        // Then
        assertThat(timeLimiter).isNotNull();
        assertThat(timeLimiter.getName()).isEqualTo("test");
        assertThat(timeLimiter.getTimeLimiterConfig().getTimeoutDuration()).isEqualTo(Duration.ofSeconds(10));
        assertThat(timeLimiter.getTimeLimiterConfig().shouldCancelRunningFuture()).isFalse();
    }
    
    @Test
    void executeWithCircuitBreaker_ShouldExecuteSupplier() {
        // Given
        CircuitBreaker circuitBreaker = ResilienceUtils.createCircuitBreaker("test");
        Supplier<String> supplier = () -> "success";
        
        // When
        String result = ResilienceUtils.executeWithCircuitBreaker(circuitBreaker, supplier);
        
        // Then
        assertThat(result).isEqualTo("success");
    }
    
    @Test
    void executeWithRetry_ShouldExecuteSupplier() {
        // Given
        Retry retry = ResilienceUtils.createRetry("test");
        Supplier<String> supplier = () -> "success";
        
        // When
        String result = ResilienceUtils.executeWithRetry(retry, supplier);
        
        // Then
        assertThat(result).isEqualTo("success");
    }
    
    @Test
    void executeWithBulkhead_ShouldExecuteSupplier() {
        // Given
        Bulkhead bulkhead = ResilienceUtils.createBulkhead("test");
        Supplier<String> supplier = () -> "success";
        
        // When
        String result = ResilienceUtils.executeWithBulkhead(bulkhead, supplier);
        
        // Then
        assertThat(result).isEqualTo("success");
    }
    
    @Test
    void executeWithTimeLimiter_ShouldExecuteSupplier() {
        // Given
        TimeLimiter timeLimiter = ResilienceUtils.createTimeLimiter("test");
        Supplier<String> supplier = () -> "success";
        
        // When
        String result = ResilienceUtils.executeWithTimeLimiter(timeLimiter, supplier);
        
        // Then
        assertThat(result).isEqualTo("success");
    }
    
    @Test
    void executeWithResilience_ShouldExecuteSupplier() {
        // Given
        CircuitBreaker circuitBreaker = ResilienceUtils.createCircuitBreaker("test");
        Retry retry = ResilienceUtils.createRetry("test");
        Bulkhead bulkhead = ResilienceUtils.createBulkhead("test");
        TimeLimiter timeLimiter = ResilienceUtils.createTimeLimiter("test");
        Supplier<String> supplier = () -> "success";
        
        // When
        String result = ResilienceUtils.executeWithResilience(
            circuitBreaker, retry, bulkhead, timeLimiter, supplier);
        
        // Then
        assertThat(result).isEqualTo("success");
    }
    
    @Test
    void executeWithCircuitBreaker_WhenSupplierThrowsException_ShouldPropagateException() {
        // Given
        CircuitBreaker circuitBreaker = ResilienceUtils.createCircuitBreaker("test");
        Supplier<String> supplier = () -> {
            throw new RuntimeException("test exception");
        };
        
        // When & Then
        assertThatThrownBy(() -> 
            ResilienceUtils.executeWithCircuitBreaker(circuitBreaker, supplier))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("test exception");
    }
    
    @Test
    void constructor_ShouldBePrivate() {
        // When & Then
        assertThatThrownBy(() -> {
            var constructor = ResilienceUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }).isInstanceOf(Exception.class);
    }
}
