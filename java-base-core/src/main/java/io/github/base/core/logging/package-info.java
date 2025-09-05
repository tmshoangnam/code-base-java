/**
 * Structured logging and correlation ID management package.
 *
 * <p>This package provides utilities for structured logging using MDC
 * (Mapped Diagnostic Context) and correlation ID generation for
 * distributed request tracing.
 *
 * <p><strong>Key Components:</strong>
 * <ul>
 *   <li>{@code StructuredLogger} - MDC-based structured logging utilities</li>
 *   <li>{@code CorrelationIdGenerator} - Unique correlation ID generation</li>
 * </ul>
 *
 * <p><strong>Features:</strong>
 * <ul>
 *   <li>Automatic MDC context management with proper cleanup</li>
 *   <li>Thread-safe correlation ID generation</li>
 *   <li>Support for business events, errors, and informational messages</li>
 *   <li>Human-readable correlation ID format with timestamps</li>
 *   <li>Exception-safe logging operations</li>
 * </ul>
 *
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Always provide correlation ID for request tracing</li>
 *   <li>Use meaningful event names for business events</li>
 *   <li>Include relevant context information</li>
 *   <li>Handle exceptions gracefully in logging operations</li>
 * </ul>
 *
 * <p><strong>Example:</strong>
 * <pre>{@code
 * // Generate correlation ID
 * String correlationId = CorrelationIdGenerator.generate("payment");
 *
 * // Log business event
 * StructuredLogger.logBusinessEvent(correlationId, "PAYMENT_PROCESSED",
 *     Map.of("amount", 99.99, "currency", "USD"));
 *
 * // Log error
 * StructuredLogger.logError(correlationId, "Payment failed", exception,
 *     Map.of("orderId", "order123"));
 * }</pre>
 *
 * @since 1.0.0
 * @author java-base-core
 */
package io.github.base.core.logging;
