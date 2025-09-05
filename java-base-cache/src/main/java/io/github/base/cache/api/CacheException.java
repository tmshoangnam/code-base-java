package io.github.base.cache.api;

/**
 * Unified runtime exception for cache operations.
 * <p>
 * This exception is thrown when cache operations fail due to various reasons
 * such as network issues, serialization problems, or provider-specific errors.
 * <p>
 * <strong>Error Handling:</strong> Applications should catch this exception
 * and handle it appropriately, typically by falling back to alternative
 * data sources or logging the error for monitoring.
 *
 * @since 1.0.0
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new cache exception with the specified detail message.
     *
     * @param message the detail message
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * Constructs a new cache exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new cache exception with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public CacheException(Throwable cause) {
        super(cause);
    }
}
