package com.mycompany.base.core.api.error;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Problem model aligned with RFC 7807 (Problem Details for HTTP APIs).
 * 
 * <p>This class represents a standardized way to describe problems that occur
 * during HTTP API request processing. It provides a consistent structure
 * for error responses across the application.
 * 
 * <p><strong>RFC 7807 Compliance:</strong>
 * <ul>
 *   <li>{@code type} - A URI reference that identifies the problem type</li>
 *   <li>{@code title} - A short, human-readable summary of the problem type</li>
 *   <li>{@code status} - The HTTP status code</li>
 *   <li>{@code detail} - A human-readable explanation specific to this occurrence</li>
 *   <li>{@code instance} - A URI reference that identifies the specific occurrence</li>
 * </ul>
 * 
 * <p><strong>Additional Fields:</strong>
 * <ul>
 *   <li>{@code correlationId} - Unique identifier for request tracing</li>
 *   <li>{@code timestamp} - When the problem occurred</li>
 *   <li>{@code extensions} - Additional context-specific information</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * Problem problem = new Problem(
 *     "https://api.example.com/problems/validation-error",
 *     "Validation Failed",
 *     400,
 *     "The request body contains invalid data",
 *     "/api/users/123",
 *     "req-12345",
 *     Instant.now(),
 *     Map.of("field", "email", "reason", "Invalid format")
 * );
 * }</pre>
 * 
 * @param type A URI reference that identifies the problem type
 * @param title A short, human-readable summary of the problem type
 * @param status The HTTP status code
 * @param detail A human-readable explanation specific to this occurrence
 * @param instance A URI reference that identifies the specific occurrence
 * @param correlationId Unique identifier for request tracing
 * @param timestamp When the problem occurred
 * @param extensions Additional context-specific information
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public record Problem(
    String type,
    String title,
    int status,
    String detail,
    String instance,
    String correlationId,
    Instant timestamp,
    Map<String, Object> extensions
) {
    
    /**
     * Creates a Problem with empty extensions.
     * 
     * @param type A URI reference that identifies the problem type
     * @param title A short, human-readable summary of the problem type
     * @param status The HTTP status code
     * @param detail A human-readable explanation specific to this occurrence
     * @param instance A URI reference that identifies the specific occurrence
     * @param correlationId Unique identifier for request tracing
     * @param timestamp When the problem occurred
     */
    public Problem(String type, String title, int status, String detail, 
                   String instance, String correlationId, Instant timestamp) {
        this(type, title, status, detail, instance, correlationId, timestamp, Map.of());
    }
    
    /**
     * Creates a Problem with current timestamp and empty extensions.
     * 
     * @param type A URI reference that identifies the problem type
     * @param title A short, human-readable summary of the problem type
     * @param status The HTTP status code
     * @param detail A human-readable explanation specific to this occurrence
     * @param instance A URI reference that identifies the specific occurrence
     * @param correlationId Unique identifier for request tracing
     */
    public Problem(String type, String title, int status, String detail, 
                   String instance, String correlationId) {
        this(type, title, status, detail, instance, correlationId, Instant.now(), Map.of());
    }
    
    /**
     * Validates that required fields are not null or empty.
     * 
     * @throws IllegalArgumentException if required fields are invalid
     */
    public Problem {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(title, "title cannot be null");
        Objects.requireNonNull(detail, "detail cannot be null");
        Objects.requireNonNull(correlationId, "correlationId cannot be null");
        Objects.requireNonNull(timestamp, "timestamp cannot be null");
        
        if (type.trim().isEmpty()) {
            throw new IllegalArgumentException("type cannot be empty");
        }
        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("title cannot be empty");
        }
        if (detail.trim().isEmpty()) {
            throw new IllegalArgumentException("detail cannot be empty");
        }
        if (correlationId.trim().isEmpty()) {
            throw new IllegalArgumentException("correlationId cannot be empty");
        }
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("status must be between 100 and 599");
        }
    }
}
