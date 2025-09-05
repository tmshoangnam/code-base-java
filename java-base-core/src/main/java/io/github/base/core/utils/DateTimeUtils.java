package io.github.base.core.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Utility class for date and time operations.
 * 
 * <p>This class provides common date and time utility methods that are
 * frequently needed in enterprise applications. It focuses on the most
 * commonly used operations while maintaining thread safety.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Thread-safe static methods</li>
 *   <li>Null-safe operations where appropriate</li>
 *   <li>Uses Java 8+ Time API (java.time)</li>
 *   <li>Minimal dependencies - pure Java implementation</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Format current time
 * String timestamp = DateTimeUtils.formatCurrentTime("yyyy-MM-dd HH:mm:ss");
 * 
 * // Parse date string
 * LocalDate date = DateTimeUtils.parseDate("2023-12-01", "yyyy-MM-dd");
 * 
 * // Convert between time zones
 * ZonedDateTime utcTime = DateTimeUtils.toUtc(LocalDateTime.now());
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public final class DateTimeUtils {
    
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    private DateTimeUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Formats the current time using the specified pattern.
     * 
     * @param pattern the date/time pattern
     * @return formatted current time string
     * @throws IllegalArgumentException if pattern is null or empty
     */
    public static String formatCurrentTime(String pattern) {
        validatePattern(pattern);
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Formats the current time using the default datetime pattern.
     * 
     * @return formatted current time string (yyyy-MM-dd HH:mm:ss)
     */
    public static String formatCurrentTime() {
        return formatCurrentTime(DEFAULT_DATETIME_FORMAT);
    }
    
    /**
     * Formats the current date using the specified pattern.
     * 
     * @param pattern the date pattern
     * @return formatted current date string
     * @throws IllegalArgumentException if pattern is null or empty
     */
    public static String formatCurrentDate(String pattern) {
        validatePattern(pattern);
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Formats the current date using the default date pattern.
     * 
     * @return formatted current date string (yyyy-MM-dd)
     */
    public static String formatCurrentDate() {
        return formatCurrentDate(DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Formats a LocalDateTime using the specified pattern.
     * 
     * @param dateTime the LocalDateTime to format
     * @param pattern the date/time pattern
     * @return formatted date/time string
     * @throws IllegalArgumentException if pattern is null or empty
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        validatePattern(pattern);
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Formats a LocalDate using the specified pattern.
     * 
     * @param date the LocalDate to format
     * @param pattern the date pattern
     * @return formatted date string
     * @throws IllegalArgumentException if pattern is null or empty
     */
    public static String formatDate(LocalDate date, String pattern) {
        validatePattern(pattern);
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Parses a date string using the specified pattern.
     * 
     * @param dateString the date string to parse
     * @param pattern the date pattern
     * @return parsed LocalDate
     * @throws IllegalArgumentException if pattern is null or empty
     * @throws DateTimeParseException if the date string cannot be parsed
     */
    public static LocalDate parseDate(String dateString, String pattern) {
        validatePattern(pattern);
        if (dateString == null) {
            return null;
        }
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Parses a datetime string using the specified pattern.
     * 
     * @param dateTimeString the datetime string to parse
     * @param pattern the datetime pattern
     * @return parsed LocalDateTime
     * @throws IllegalArgumentException if pattern is null or empty
     * @throws DateTimeParseException if the datetime string cannot be parsed
     */
    public static LocalDateTime parseDateTime(String dateTimeString, String pattern) {
        validatePattern(pattern);
        if (dateTimeString == null) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Converts a LocalDateTime to UTC timezone.
     * 
     * @param dateTime the LocalDateTime to convert
     * @return ZonedDateTime in UTC
     */
    public static ZonedDateTime toUtc(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
    }
    
    /**
     * Converts a LocalDateTime to the specified timezone.
     * 
     * @param dateTime the LocalDateTime to convert
     * @param zoneId the target timezone
     * @return ZonedDateTime in the specified timezone
     */
    public static ZonedDateTime toTimezone(LocalDateTime dateTime, ZoneId zoneId) {
        if (dateTime == null || zoneId == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(zoneId);
    }
    
    /**
     * Converts an Instant to a LocalDateTime in the system default timezone.
     * 
     * @param instant the Instant to convert
     * @return LocalDateTime in system default timezone
     */
    public static LocalDateTime fromInstant(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    
    /**
     * Converts a LocalDateTime to an Instant.
     * 
     * @param dateTime the LocalDateTime to convert
     * @return Instant
     */
    public static Instant toInstant(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
    
    /**
     * Formats an Instant as ISO datetime string.
     * 
     * @param instant the Instant to format
     * @return ISO formatted datetime string
     */
    public static String formatAsIso(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.toString();
    }
    
    /**
     * Parses an ISO datetime string to Instant.
     * 
     * @param isoString the ISO datetime string
     * @return parsed Instant
     * @throws DateTimeParseException if the string cannot be parsed
     */
    public static Instant parseIso(String isoString) {
        if (isoString == null) {
            return null;
        }
        return Instant.parse(isoString);
    }
    
    /**
     * Validates that the pattern is not null or empty.
     * 
     * @param pattern the pattern to validate
     * @throws IllegalArgumentException if pattern is invalid
     */
    private static void validatePattern(String pattern) {
        Objects.requireNonNull(pattern, "pattern cannot be null");
        if (pattern.trim().isEmpty()) {
            throw new IllegalArgumentException("pattern cannot be empty");
        }
    }
}
