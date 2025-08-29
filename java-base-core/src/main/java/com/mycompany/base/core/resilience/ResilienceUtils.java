package com.mycompany.base.core.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * Utility class for resilience operations using Resilience4j.
 * Provides common resilience patterns: circuit breaker, retry, bulkhead, time limiter.
 * 
 * @author mycompany
 * @since 1.0.0
 */
public final class ResilienceUtils {
    
    private ResilienceUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Creates a circuit breaker with default configuration.
     * 
     * @param name the circuit breaker name
     * @return configured circuit breaker
     */
    public static CircuitBreaker createCircuitBreaker(String name) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .slidingWindowSize(100)
                .build();
        
        return CircuitBreaker.of(name, config);
    }
    
    /**
     * Creates a circuit breaker with custom configuration.
     * 
     * @param name the circuit breaker name
     * @param failureRateThreshold failure rate threshold percentage
     * @param waitDurationInOpenState wait duration in open state
     * @param slidingWindowSize sliding window size
     * @param minimumNumberOfCalls minimum number of calls
     * @param slowCallDurationThreshold slow call duration threshold
     * @param slowCallRateThreshold slow call rate threshold
     * @param automaticTransitionFromOpenToHalfOpenEnabled automatic transition flag
     * @return configured circuit breaker
     */
    public static CircuitBreaker createCircuitBreaker(String name, int failureRateThreshold, 
                                                    Duration waitDurationInOpenState, int slidingWindowSize,
                                                    int minimumNumberOfCalls, Duration slowCallDurationThreshold,
                                                    int slowCallRateThreshold, boolean automaticTransitionFromOpenToHalfOpenEnabled) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(failureRateThreshold)
                .waitDurationInOpenState(waitDurationInOpenState)
                .slidingWindowSize(slidingWindowSize)
                .minimumNumberOfCalls(minimumNumberOfCalls)
                .slowCallDurationThreshold(slowCallDurationThreshold)
                .slowCallRateThreshold(slowCallRateThreshold)
                .automaticTransitionFromOpenToHalfOpenEnabled(automaticTransitionFromOpenToHalfOpenEnabled)
                .build();
        
        return CircuitBreaker.of(name, config);
    }
    
    /**
     * Creates a retry with default configuration.
     * 
     * @param name the retry name
     * @return configured retry
     */
    public static Retry createRetry(String name) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(100))
                .retryExceptions(Exception.class)
                .build();
        
        return Retry.of(name, config);
    }
    
    /**
     * Creates a retry with custom configuration properties.
     * 
     * @param name the retry name
     * @param properties the configuration properties
     * @return configured retry
     */
    public static Retry createRetry(String name, ResilienceProperties properties) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(properties.getRetry().getMaxAttempts())
                .waitDuration(properties.getRetry().getWaitDuration())
                .retryExceptions(Exception.class)
                .build();
        
        return Retry.of(name, config);
    }
    
    /**
     * Creates a bulkhead with default configuration.
     * 
     * @param name the bulkhead name
     * @return configured bulkhead
     */
    public static Bulkhead createBulkhead(String name) {
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(10)
                .maxWaitDuration(Duration.ofMillis(100))
                .build();
        
        return Bulkhead.of(name, config);
    }
    
    /**
     * Creates a bulkhead with custom configuration properties.
     * 
     * @param name the bulkhead name
     * @param properties the configuration properties
     * @return configured bulkhead
     */
    public static Bulkhead createBulkhead(String name, ResilienceProperties properties) {
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(properties.getBulkhead().getMaxConcurrentCalls())
                .maxWaitDuration(properties.getBulkhead().getMaxWaitDuration())
                .build();
        
        return Bulkhead.of(name, config);
    }
    
    /**
     * Creates a time limiter with default configuration.
     * 
     * @param name the time limiter name
     * @return configured time limiter
     */
    public static TimeLimiter createTimeLimiter(String name) {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(5))
                .cancelRunningFuture(true)
                .build();
        
        return TimeLimiter.of(name, config);
    }
    
    /**
     * Creates a time limiter with custom configuration properties.
     * 
     * @param name the time limiter name
     * @param properties the configuration properties
     * @return configured time limiter
     */
    public static TimeLimiter createTimeLimiter(String name, ResilienceProperties properties) {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(properties.getTimeLimiter().getTimeoutDuration())
                .cancelRunningFuture(properties.getTimeLimiter().isCancelRunningFuture())
                .build();
        
        return TimeLimiter.of(name, config);
    }
    
    /**
     * Executes a supplier with circuit breaker protection.
     * 
     * @param circuitBreaker the circuit breaker to use
     * @param supplier the supplier to execute
     * @param <T> the return type
     * @return the result
     */
    public static <T> T executeWithCircuitBreaker(CircuitBreaker circuitBreaker, Supplier<T> supplier) {
        return circuitBreaker.executeSupplier(supplier);
    }
    
    /**
     * Executes a supplier with retry protection.
     * 
     * @param retry the retry to use
     * @param supplier the supplier to execute
     * @param <T> the return type
     * @return the result
     */
    public static <T> T executeWithRetry(Retry retry, Supplier<T> supplier) {
        return retry.executeSupplier(supplier);
    }
    
    /**
     * Executes a supplier with bulkhead protection.
     * 
     * @param bulkhead the bulkhead to use
     * @param supplier the supplier to execute
     * @param <T> the return type
     * @return the result
     */
    public static <T> T executeWithBulkhead(Bulkhead bulkhead, Supplier<T> supplier) {
        return bulkhead.executeSupplier(supplier);
    }
    
    /**
     * Executes a supplier with time limiter protection.
     * 
     * @param timeLimiter the time limiter to use
     * @param supplier the supplier to execute
     * @param <T> the return type
     * @return the result
     */
    public static <T> T executeWithTimeLimiter(TimeLimiter timeLimiter, Supplier<T> supplier) {
        Supplier<T> decorated = TimeLimiter.decorateSupplier(timeLimiter, supplier);
        return decorated.get();
    }
    
    /**
     * Executes a supplier with multiple resilience patterns.
     * 
     * @param circuitBreaker the circuit breaker to use
     * @param retry the retry to use
     * @param bulkhead the bulkhead to use
     * @param timeLimiter the time limiter to use
     * @param supplier the supplier to execute
     * @param <T> the return type
     * @return the result
     */
    public static <T> T executeWithResilience(CircuitBreaker circuitBreaker, 
                                            Retry retry, 
                                            Bulkhead bulkhead, 
                                            TimeLimiter timeLimiter, 
                                            Supplier<T> supplier) {
        Supplier<T> decorated = circuitBreaker
                .decorateSupplier(supplier);
        decorated = Retry.decorateSupplier(retry, decorated);
        decorated = Bulkhead.decorateSupplier(bulkhead, decorated);
        decorated = TimeLimiter.decorateSupplier(timeLimiter, decorated);
        return decorated.get();
    }
}
