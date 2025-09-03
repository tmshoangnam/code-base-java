/**
 * Resilience pattern utilities and factories package.
 * 
 * <p>This package provides factory methods and utilities for creating
 * and managing Resilience4j instances for circuit breaker, retry,
 * timeout, and bulkhead patterns.
 * 
 * <p><strong>Key Components:</strong>
 * <ul>
 *   <li>{@code ResilienceFactories} - Factory methods for creating registries</li>
 * </ul>
 * 
 * <p><strong>Supported Patterns:</strong>
 * <ul>
 *   <li>Circuit Breaker - Prevents cascading failures</li>
 *   <li>Retry - Automatic retry with configurable strategies</li>
 *   <li>Time Limiter - Timeout protection for operations</li>
 *   <li>Bulkhead - Resource isolation and concurrency control</li>
 * </ul>
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Framework-agnostic - no Spring dependencies</li>
 *   <li>Sensible defaults with customization support</li>
 *   <li>Registry-based instance management</li>
 *   <li>Thread-safe operations</li>
 * </ul>
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Always fetch instances from registries</li>
 *   <li>Use named instances for different services</li>
 *   <li>Configure through external configuration when possible</li>
 *   <li>Don't instantiate patterns directly</li>
 * </ul>
 * 
 * <p><strong>Example:</strong>
 * <pre>{@code
 * // Create circuit breaker
 * CircuitBreaker cb = ResilienceFactories.defaultCircuitBreakerRegistry()
 *                                        .circuitBreaker("payment-service");
 * 
 * // Decorate operation
 * Runnable decorated = CircuitBreaker.decorateRunnable(cb, () -> {
 *     // External API call
 * });
 * decorated.run();
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
package com.mycompany.base.core.api.resilience;
