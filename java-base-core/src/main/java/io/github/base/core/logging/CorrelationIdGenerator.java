package io.github.base.core.logging;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Utility for generating correlation IDs for request tracing.
 *
 * <p>This class provides methods for generating unique correlation IDs
 * that can be used for tracing requests across distributed systems.
 * The generated IDs are designed to be human-readable and include
 * timestamp information for easier debugging.
 *
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Thread-safe operations using SecureRandom</li>
 *   <li>Human-readable format with timestamp</li>
 *   <li>Configurable prefix for different environments</li>
 *   <li>Minimal dependencies - pure Java implementation</li>
 * </ul>
 *
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Generate with default prefix
 * String correlationId = CorrelationIdGenerator.generate();
 * // Result: "req-20231201-143022-abc123def456"
 *
 * // Generate with custom prefix
 * String correlationId = CorrelationIdGenerator.generate("payment");
 * // Result: "payment-20231201-143022-abc123def456"
 *
 * // Generate with custom prefix and length
 * String correlationId = CorrelationIdGenerator.generate("api", 8);
 * // Result: "api-20231201-143022-abc12345"
 * }</pre>
 *
 * @since 1.0.0
 * @author java-base-core
 */
public final class CorrelationIdGenerator {

    private static final String DEFAULT_PREFIX = "req";
    private static final int DEFAULT_RANDOM_LENGTH = 12;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private CorrelationIdGenerator() {
        // Utility class - prevent instantiation
    }

    /**
     * Generates a correlation ID with default prefix and random length.
     *
     * <p>The generated ID has the format: {@code prefix-timestamp-randomString}
     * where:
     * <ul>
     *   <li>prefix is "req"</li>
     *   <li>timestamp is in format "yyyyMMdd-HHmmss"</li>
     *   <li>randomString is 12 characters long</li>
     * </ul>
     *
     * @return a unique correlation ID
     */
    public static String generate() {
        return generate(DEFAULT_PREFIX, DEFAULT_RANDOM_LENGTH);
    }

    /**
     * Generates a correlation ID with custom prefix and default random length.
     *
     * @param prefix the prefix for the correlation ID
     * @return a unique correlation ID
     * @throws IllegalArgumentException if prefix is null or empty
     */
    public static String generate(String prefix) {
        return generate(prefix, DEFAULT_RANDOM_LENGTH);
    }

    /**
     * Generates a correlation ID with custom prefix and random length.
     *
     * @param prefix the prefix for the correlation ID
     * @param randomLength the length of the random string component
     * @return a unique correlation ID
     * @throws IllegalArgumentException if prefix is null/empty or randomLength is invalid
     */
    public static String generate(String prefix, int randomLength) {
        validateParameters(prefix, randomLength);

        String timestamp = Instant.now().atZone(java.time.ZoneId.systemDefault())
                .format(TIMESTAMP_FORMATTER);
        String randomString = generateRandomString(randomLength);

        return String.format("%s-%s-%s", prefix, timestamp, randomString);
    }

    /**
     * Generates a simple correlation ID with just a prefix and random string.
     *
     * <p>This method is useful when you don't need timestamp information
     * in the correlation ID, making it shorter and more compact.
     *
     * @param prefix the prefix for the correlation ID
     * @return a unique correlation ID
     * @throws IllegalArgumentException if prefix is null or empty
     */
    public static String generateSimple(String prefix) {
        return generateSimple(prefix, DEFAULT_RANDOM_LENGTH);
    }

    /**
     * Generates a simple correlation ID with custom prefix and random length.
     *
     * @param prefix the prefix for the correlation ID
     * @param randomLength the length of the random string component
     * @return a unique correlation ID
     * @throws IllegalArgumentException if prefix is null/empty or randomLength is invalid
     */
    public static String generateSimple(String prefix, int randomLength) {
        validateParameters(prefix, randomLength);

        String randomString = generateRandomString(randomLength);
        return String.format("%s-%s", prefix, randomString);
    }

    /**
     * Generates a random string of specified length using secure random.
     *
     * @param length the length of the random string
     * @return a random string
     */
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(SECURE_RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    /**
     * Validates the input parameters.
     *
     * @param prefix the prefix to validate
     * @param randomLength the random length to validate
     * @throws IllegalArgumentException if parameters are invalid
     */
    private static void validateParameters(String prefix, int randomLength) {
        Objects.requireNonNull(prefix, "prefix cannot be null");
        if (prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("prefix cannot be empty");
        }
        if (randomLength < 1) {
            throw new IllegalArgumentException("randomLength must be positive");
        }
        if (randomLength > 50) {
            throw new IllegalArgumentException("randomLength cannot exceed 50");
        }
    }
}
