package com.mycompany.base.starter.resilience;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

/**
 * Configuration properties for resilience patterns.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "my-base.resilience")
public class ResilienceProperties {
    
    @NestedConfigurationProperty
    private CircuitBreaker circuitBreaker = new CircuitBreaker();
    
    @NestedConfigurationProperty
    private Retry retry = new Retry();
    
    @NestedConfigurationProperty
    private Bulkhead bulkhead = new Bulkhead();
    
    @NestedConfigurationProperty
    private TimeLimiter timeLimiter = new TimeLimiter();
    
    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }
    
    public void setCircuitBreaker(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }
    
    public Retry getRetry() {
        return retry;
    }
    
    public void setRetry(Retry retry) {
        this.retry = retry;
    }
    
    public Bulkhead getBulkhead() {
        return bulkhead;
    }
    
    public void setBulkhead(Bulkhead bulkhead) {
        this.bulkhead = bulkhead;
    }
    
    public TimeLimiter getTimeLimiter() {
        return timeLimiter;
    }
    
    public void setTimeLimiter(TimeLimiter timeLimiter) {
        this.timeLimiter = timeLimiter;
    }
    
    /**
     * Circuit Breaker configuration properties.
     */
    public static class CircuitBreaker {
        private int failureRateThreshold = 50;
        private Duration waitDurationInOpenState = Duration.ofSeconds(60);
        private int slidingWindowSize = 100;
        private int minimumNumberOfCalls = 10;
        private Duration slowCallDurationThreshold = Duration.ofSeconds(2);
        private int slowCallRateThreshold = 100;
        private boolean automaticTransitionFromOpenToHalfOpenEnabled = true;
        
        public int getFailureRateThreshold() {
            return failureRateThreshold;
        }
        
        public void setFailureRateThreshold(int failureRateThreshold) {
            this.failureRateThreshold = failureRateThreshold;
        }
        
        public Duration getWaitDurationInOpenState() {
            return waitDurationInOpenState;
        }
        
        public void setWaitDurationInOpenState(Duration waitDurationInOpenState) {
            this.waitDurationInOpenState = waitDurationInOpenState;
        }
        
        public int getSlidingWindowSize() {
            return slidingWindowSize;
        }
        
        public void setSlidingWindowSize(int slidingWindowSize) {
            this.slidingWindowSize = slidingWindowSize;
        }
        
        public int getMinimumNumberOfCalls() {
            return minimumNumberOfCalls;
        }
        
        public void setMinimumNumberOfCalls(int minimumNumberOfCalls) {
            this.minimumNumberOfCalls = minimumNumberOfCalls;
        }
        
        public Duration getSlowCallDurationThreshold() {
            return slowCallDurationThreshold;
        }
        
        public void setSlowCallDurationThreshold(Duration slowCallDurationThreshold) {
            this.slowCallDurationThreshold = slowCallDurationThreshold;
        }
        
        public int getSlowCallRateThreshold() {
            return slowCallRateThreshold;
        }
        
        public void setSlowCallRateThreshold(int slowCallRateThreshold) {
            this.slowCallRateThreshold = slowCallRateThreshold;
        }
        
        public boolean isAutomaticTransitionFromOpenToHalfOpenEnabled() {
            return automaticTransitionFromOpenToHalfOpenEnabled;
        }
        
        public void setAutomaticTransitionFromOpenToHalfOpenEnabled(boolean automaticTransitionFromOpenToHalfOpenEnabled) {
            this.automaticTransitionFromOpenToHalfOpenEnabled = automaticTransitionFromOpenToHalfOpenEnabled;
        }
    }
    
    /**
     * Retry configuration properties.
     */
    public static class Retry {
        private int maxAttempts = 3;
        private Duration waitDuration = Duration.ofMillis(100);
        
        public int getMaxAttempts() {
            return maxAttempts;
        }
        
        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }
        
        public Duration getWaitDuration() {
            return waitDuration;
        }
        
        public void setWaitDuration(Duration waitDuration) {
            this.waitDuration = waitDuration;
        }
    }
    
    /**
     * Bulkhead configuration properties.
     */
    public static class Bulkhead {
        private int maxConcurrentCalls = 10;
        private Duration maxWaitDuration = Duration.ofMillis(500);
        
        public int getMaxConcurrentCalls() {
            return maxConcurrentCalls;
        }
        
        public void setMaxConcurrentCalls(int maxConcurrentCalls) {
            this.maxConcurrentCalls = maxConcurrentCalls;
        }
        
        public Duration getMaxWaitDuration() {
            return maxWaitDuration;
        }
        
        public void setMaxWaitDuration(Duration maxWaitDuration) {
            this.maxWaitDuration = maxWaitDuration;
        }
    }
    
    /**
     * Time Limiter configuration properties.
     */
    public static class TimeLimiter {
        private Duration timeoutDuration = Duration.ofSeconds(5);
        private boolean cancelRunningFuture = true;
        
        public Duration getTimeoutDuration() {
            return timeoutDuration;
        }
        
        public void setTimeoutDuration(Duration timeoutDuration) {
            this.timeoutDuration = timeoutDuration;
        }
        
        public boolean isCancelRunningFuture() {
            return cancelRunningFuture;
        }
        
        public void setCancelRunningFuture(boolean cancelRunningFuture) {
            this.cancelRunningFuture = cancelRunningFuture;
        }
    }
}
