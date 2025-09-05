package io.github.base.core.resilience;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;

/**
 * Factory class for creating Resilience4j registries with default configurations.
 *
 * <p>This class provides static factory methods to create registry instances
 * for different resilience patterns. It follows the principle of providing
 * sensible defaults while allowing customization through configuration.
 *
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Application code should not instantiate CircuitBreaker or Retry directly</li>
 *   <li>Always fetch instances from a registry to simplify lifecycle management</li>
 *   <li>Use named instances for different services or operations</li>
 *   <li>Configure registries through external configuration when possible</li>
 * </ul>
 *
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * CircuitBreaker cb = ResilienceFactories.defaultCircuitBreakerRegistry()
 *                                        .circuitBreaker("payment-service");
 *
 * Runnable decorated = CircuitBreaker.decorateRunnable(cb, () -> {
 *     // External API call
 * });
 * decorated.run();
 * }</pre>
 *
 * @since 1.0.0
 * @author java-base-core
 */
public final class ResilienceFactories {

    private ResilienceFactories() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates a CircuitBreakerRegistry with default configuration.
     *
     * <p>The default configuration includes:
     * <ul>
     *   <li>Failure rate threshold: 50%</li>
     *   <li>Wait duration in open state: 60 seconds</li>
     *   <li>Sliding window size: 100 calls</li>
     *   <li>Minimum number of calls: 10</li>
     * </ul>
     *
     * @return a new CircuitBreakerRegistry instance
     */
    public static CircuitBreakerRegistry defaultCircuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }

    /**
     * Creates a RetryRegistry with default configuration.
     *
     * <p>The default configuration includes:
     * <ul>
     *   <li>Max attempts: 3</li>
     *   <li>Wait duration: 500ms</li>
     *   <li>Retry on all exceptions</li>
     * </ul>
     *
     * @return a new RetryRegistry instance
     */
    public static RetryRegistry defaultRetryRegistry() {
        return RetryRegistry.ofDefaults();
    }

    /**
     * Creates a TimeLimiterRegistry with default configuration.
     *
     * <p>The default configuration includes:
     * <ul>
     *   <li>Timeout duration: 1 second</li>
     *   <li>Cancel running future: true</li>
     * </ul>
     *
     * @return a new TimeLimiterRegistry instance
     */
    public static TimeLimiterRegistry defaultTimeLimiterRegistry() {
        return TimeLimiterRegistry.ofDefaults();
    }

    /**
     * Creates a BulkheadRegistry with default configuration.
     *
     * <p>The default configuration includes:
     * <ul>
     *   <li>Max concurrent calls: 25</li>
     *   <li>Max wait duration: 0ms (fail fast)</li>
     * </ul>
     *
     * @return a new BulkheadRegistry instance
     */
    public static BulkheadRegistry defaultBulkheadRegistry() {
        return BulkheadRegistry.ofDefaults();
    }
}
