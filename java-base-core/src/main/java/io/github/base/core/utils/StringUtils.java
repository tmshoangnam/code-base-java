package io.github.base.core.utils;

/**
 * Simple String helper utilities.
 * 
 * <p>This class provides lightweight string utility methods that are
 * frequently needed in enterprise applications. It avoids duplicating
 * functionality from Apache Commons Lang while providing essential
 * string operations.
 * 
 * <p><strong>Design Notes:</strong>
 * <ul>
 *   <li>Avoid duplicating Apache Commons Lang functionality</li>
 *   <li>Keep only lightweight helpers frequently needed</li>
 *   <li>All methods are null-safe</li>
 *   <li>Thread-safe static methods</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * if (StringUtils.isBlank(userInput)) {
 *     throw new IllegalArgumentException("Input cannot be empty");
 * }
 * 
 * String result = StringUtils.defaultIfBlank(input, "defaultValue");
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public final class StringUtils {

    private StringUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Checks if a string is blank (null, empty, or contains only whitespace).
     * 
     * @param value the string to check
     * @return true if the string is blank, false otherwise
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Checks if a string has text (not blank).
     * 
     * @param value the string to check
     * @return true if the string has text, false otherwise
     */
    public static boolean hasText(String value) {
        return !isBlank(value);
    }
    
    /**
     * Returns the default value if the input string is blank.
     * 
     * @param value the input string
     * @param defaultValue the default value to return if input is blank
     * @return the input string if it has text, otherwise the default value
     */
    public static String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value;
    }
    
    /**
     * Checks if a string is empty (null or empty string).
     * 
     * @param value the string to check
     * @return true if the string is empty, false otherwise
     */
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
    
    /**
     * Checks if a string is not empty.
     * 
     * @param value the string to check
     * @return true if the string is not empty, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
    
    /**
     * Trims a string and returns null if the result is empty.
     * 
     * @param value the string to trim
     * @return the trimmed string or null if empty
     */
    public static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
    
    /**
     * Trims a string and returns empty string if the result is null.
     * 
     * @param value the string to trim
     * @return the trimmed string or empty string if null
     */
    public static String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
