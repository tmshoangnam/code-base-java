package io.github.base.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.Objects;

/**
 * Utility for structured logging using MDC (Mapped Diagnostic Context) and correlation IDs.
 *
 * <p>This class provides methods for structured logging that automatically
 * manage MDC context and correlation IDs. It ensures proper cleanup of
 * MDC to prevent context leakage across threads.
 *
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Core does not enforce JSON formatting (starter decides)</li>
 *   <li>Always clear MDC to prevent context leakage across threads</li>
 *   <li>Thread-safe operations with proper exception handling</li>
 *   <li>Minimal dependencies - only SLF4J API</li>
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
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Log business event
 * StructuredLogger.logBusinessEvent(
 *     "req-12345",
 *     "USER_LOGIN",
 *     Map.of("userId", "user123", "ipAddress", "192.168.1.1")
 * );
 *
 * // Log error
 * StructuredLogger.logError(
 *     "req-12345",
 *     "Failed to process payment",
 *     exception,
 *     Map.of("orderId", "order456", "amount", 99.99)
 * );
 * }</pre>
 *
 * @since 1.0.0
 * @author java-base-core
 */
public final class StructuredLogger {

    private static final Logger logger = LoggerFactory.getLogger(StructuredLogger.class);

    private StructuredLogger() {
        // Utility class - prevent instantiation
    }

    /**
     * Logs a business event with structured context.
     *
     * <p>This method sets up MDC context with correlation ID, event name,
     * and additional context information, logs the event, and ensures
     * proper cleanup of MDC.
     *
     * @param correlationId the correlation ID for request tracing
     * @param event the business event name
     * @param context additional context information
     * @throws IllegalArgumentException if correlationId or event is null or empty
     */
    public static void logBusinessEvent(String correlationId, String event, Map<String, Object> context) {
        validateParameters(correlationId, event);

        try {
            // Set up MDC context
            MDC.put("correlationId", correlationId);
            MDC.put("event", event);

            // Add context information to MDC
            if (context != null) {
                context.forEach((key, value) -> {
                    if (key != null && value != null) {
                        MDC.put(key, String.valueOf(value));
                    }
                });
            }

            // Log the business event
            logger.info("Business event: {}", event);

        } catch (Exception e) {
            // Log error but don't throw to avoid breaking application flow
            logger.warn("Failed to log business event: {}", event, e);
        } finally {
            // Always clear MDC to prevent context leakage
            clearMDC();
        }
    }

    /**
     * Logs an error with structured context.
     *
     * <p>This method sets up MDC context with correlation ID, error flag,
     * and additional context information, logs the error with exception,
     * and ensures proper cleanup of MDC.
     *
     * @param correlationId the correlation ID for request tracing
     * @param message the error message
     * @param throwable the exception that caused the error
     * @param context additional context information
     * @throws IllegalArgumentException if correlationId or message is null or empty
     */
    public static void logError(String correlationId, String message, Throwable throwable, Map<String, Object> context) {
        validateParameters(correlationId, message);

        try {
            // Set up MDC context
            MDC.put("correlationId", correlationId);
            MDC.put("error", "true");

            // Add context information to MDC
            if (context != null) {
                context.forEach((key, value) -> {
                    if (key != null && value != null) {
                        MDC.put(key, String.valueOf(value));
                    }
                });
            }

            // Log the error
            logger.error(message, throwable);

        } catch (Exception e) {
            // Log error but don't throw to avoid breaking application flow
            logger.warn("Failed to log error: {}", message, e);
        } finally {
            // Always clear MDC to prevent context leakage
            clearMDC();
        }
    }

    /**
     * Logs an error with structured context (without exception).
     *
     * @param correlationId the correlation ID for request tracing
     * @param message the error message
     * @param context additional context information
     * @throws IllegalArgumentException if correlationId or message is null or empty
     */
    public static void logError(String correlationId, String message, Map<String, Object> context) {
        logError(correlationId, message, null, context);
    }

    /**
     * Logs an informational message with structured context.
     *
     * @param correlationId the correlation ID for request tracing
     * @param message the informational message
     * @param context additional context information
     * @throws IllegalArgumentException if correlationId or message is null or empty
     */
    public static void logInfo(String correlationId, String message, Map<String, Object> context) {
        validateParameters(correlationId, message);

        try {
            // Set up MDC context
            MDC.put("correlationId", correlationId);

            // Add context information to MDC
            if (context != null) {
                context.forEach((key, value) -> {
                    if (key != null && value != null) {
                        MDC.put(key, String.valueOf(value));
                    }
                });
            }

            // Log the informational message
            logger.info(message);

        } catch (Exception e) {
            // Log error but don't throw to avoid breaking application flow
            logger.warn("Failed to log info message: {}", message, e);
        } finally {
            // Always clear MDC to prevent context leakage
            clearMDC();
        }
    }

    /**
     * Logs a warning message with structured context.
     *
     * @param correlationId the correlation ID for request tracing
     * @param message the warning message
     * @param context additional context information
     * @throws IllegalArgumentException if correlationId or message is null or empty
     */
    public static void logWarning(String correlationId, String message, Map<String, Object> context) {
        validateParameters(correlationId, message);

        try {
            // Set up MDC context
            MDC.put("correlationId", correlationId);
            MDC.put("level", "WARNING");

            // Add context information to MDC
            if (context != null) {
                context.forEach((key, value) -> {
                    if (key != null && value != null) {
                        MDC.put(key, String.valueOf(value));
                    }
                });
            }

            // Log the warning message
            logger.warn(message);

        } catch (Exception e) {
            // Log error but don't throw to avoid breaking application flow
            logger.warn("Failed to log warning message: {}", message, e);
        } finally {
            // Always clear MDC to prevent context leakage
            clearMDC();
        }
    }

    /**
     * Validates that required parameters are not null or empty.
     *
     * @param correlationId the correlation ID to validate
     * @param message the message to validate
     * @throws IllegalArgumentException if parameters are invalid
     */
    private static void validateParameters(String correlationId, String message) {
        Objects.requireNonNull(correlationId, "correlationId cannot be null");
        Objects.requireNonNull(message, "message cannot be null");

        if (correlationId.trim().isEmpty()) {
            throw new IllegalArgumentException("correlationId cannot be empty");
        }
        if (message.trim().isEmpty()) {
            throw new IllegalArgumentException("message cannot be empty");
        }
    }

    /**
     * Clears the MDC context safely.
     *
     * <p>This method ensures that MDC is cleared even if an exception occurs,
     * preventing context leakage across threads.
     */
    private static void clearMDC() {
        try {
            MDC.clear();
        } catch (Exception e) {
            // Log error but don't throw to avoid breaking application flow
            logger.warn("Failed to clear MDC context", e);
        }
    }
}
