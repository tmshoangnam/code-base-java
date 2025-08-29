package com.mycompany.base.core.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enhanced utility class for logging operations.
 * Provides enterprise-grade logging with structured logging, performance tracking,
 * and advanced MDC management.
 * 
 * Features:
 * - Structured logging with JSON format
 * - Performance metrics logging
 * - Advanced MDC management
 * - Log level configuration
 * - Request tracing
 * - Performance benchmarking
 * 
 * @author mycompany
 * @since 1.0.0
 */
public final class LoggingUtils {
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    
    private static final Map<String, Instant> PERFORMANCE_MARKERS = new ConcurrentHashMap<>();
    
    private LoggingUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Creates a logger for the specified class.
     * 
     * @param clazz the class to create logger for
     * @return logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
    
    /**
     * Creates a logger with the specified name.
     * 
     * @param name the logger name
     * @return logger instance
     */
    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }
    
    /**
     * Sets a correlation ID for request tracing.
     * 
     * @param correlationId the correlation ID
     */
    public static void setCorrelationId(String correlationId) {
        if (correlationId != null && !correlationId.trim().isEmpty()) {
            MDC.put("correlationId", correlationId);
        }
    }
    
    /**
     * Generates and sets a new correlation ID.
     * 
     * @return the generated correlation ID
     */
    public static String generateAndSetCorrelationId() {
        String correlationId = UUID.randomUUID().toString();
        setCorrelationId(correlationId);
        return correlationId;
    }
    
    /**
     * Sets multiple MDC values.
     * 
     * @param contextMap the context map to set
     */
    public static void setContext(Map<String, String> contextMap) {
        if (contextMap != null) {
            contextMap.forEach(MDC::put);
        }
    }
    
    /**
     * Sets a single MDC context value.
     * 
     * @param key the context key
     * @param value the context value
     */
    public static void setContext(String key, String value) {
        if (key != null && value != null) {
            MDC.put(key, value);
        }
    }
    
    /**
     * Clears all MDC values.
     */
    public static void clearContext() {
        MDC.clear();
    }
    
    /**
     * Gets the current correlation ID.
     * 
     * @return the current correlation ID, or null if not set
     */
    public static String getCorrelationId() {
        return MDC.get("correlationId");
    }
    
    /**
     * Logs method entry with parameters using structured logging.
     * 
     * @param logger the logger to use
     * @param methodName the method name
     * @param params the method parameters
     */
    public static void logMethodEntry(Logger logger, String methodName, Object... params) {
        if (logger.isDebugEnabled()) {
            Map<String, Object> logData = new HashMap<>();
            logData.put("event", "METHOD_ENTRY");
            logData.put("method", methodName);
            logData.put("timestamp", Instant.now());
            logData.put("correlationId", getCorrelationId());
            
            if (params.length > 0) {
                Map<String, Object> parameters = new HashMap<>();
                for (int i = 0; i < params.length; i++) {
                    parameters.put("param" + i, params[i]);
                }
                logData.put("parameters", parameters);
            }
            
            logStructured(logger, "DEBUG", "Entering method: " + methodName, logData);
        }
    }
    
    /**
     * Logs method exit with return value using structured logging.
     * 
     * @param logger the logger to use
     * @param methodName the method name
     * @param returnValue the return value
     */
    public static void logMethodExit(Logger logger, String methodName, Object returnValue) {
        if (logger.isDebugEnabled()) {
            Map<String, Object> logData = new HashMap<>();
            logData.put("event", "METHOD_EXIT");
            logData.put("method", methodName);
            logData.put("timestamp", Instant.now());
            logData.put("correlationId", getCorrelationId());
            logData.put("returnValue", returnValue);
            
            logStructured(logger, "DEBUG", "Exiting method: " + methodName, logData);
        }
    }
    
    /**
     * Logs method exit with execution time.
     * 
     * @param logger the logger to use
     * @param methodName the method name
     * @param startTime the start time
     * @param returnValue the return value
     */
    public static void logMethodExit(Logger logger, String methodName, Instant startTime, Object returnValue) {
        if (logger.isDebugEnabled()) {
            Duration executionTime = Duration.between(startTime, Instant.now());
            Map<String, Object> logData = new HashMap<>();
            logData.put("event", "METHOD_EXIT");
            logData.put("method", methodName);
            logData.put("timestamp", Instant.now());
            logData.put("correlationId", getCorrelationId());
            logData.put("executionTimeMs", executionTime.toMillis());
            logData.put("returnValue", returnValue);
            
            logStructured(logger, "DEBUG", "Exiting method: " + methodName + " (execution time: " + executionTime.toMillis() + "ms)", logData);
        }
    }
    
    /**
     * Starts performance measurement for a named operation.
     * 
     * @param operationName the name of the operation to measure
     */
    public static void startPerformanceMeasurement(String operationName) {
        PERFORMANCE_MARKERS.put(operationName, Instant.now());
    }
    
    /**
     * Ends performance measurement and logs the result.
     * 
     * @param logger the logger to use
     * @param operationName the name of the operation
     * @param level the log level (DEBUG, INFO, WARN, ERROR)
     */
    public static void endPerformanceMeasurement(Logger logger, String operationName, String level) {
        Instant startTime = PERFORMANCE_MARKERS.remove(operationName);
        if (startTime != null) {
            Duration executionTime = Duration.between(startTime, Instant.now());
            logPerformance(logger, operationName, executionTime, level);
        }
    }
    
    /**
     * Logs performance metrics.
     * 
     * @param logger the logger to use
     * @param operation the operation name
     * @param executionTime the execution time
     * @param level the log level
     */
    public static void logPerformance(Logger logger, String operation, Duration executionTime, String level) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "PERFORMANCE");
        logData.put("operation", operation);
        logData.put("executionTimeMs", executionTime.toMillis());
        logData.put("executionTime", executionTime.toString());
        logData.put("timestamp", Instant.now());
        logData.put("correlationId", getCorrelationId());
        
        logStructured(logger, level, "Performance: " + operation + " took " + executionTime.toMillis() + "ms", logData);
    }
    
    /**
     * Logs structured data in JSON format.
     * 
     * @param logger the logger to use
     * @param level the log level
     * @param message the log message
     * @param data the structured data to log
     */
    public static void logStructured(Logger logger, String level, String message, Map<String, Object> data) {
        try {
            String jsonData = OBJECT_MAPPER.writeValueAsString(data);
            switch (level.toUpperCase()) {
                case "DEBUG":
                    if (logger.isDebugEnabled()) {
                        logger.debug("{} - {}", message, jsonData);
                    }
                    break;
                case "INFO":
                    if (logger.isInfoEnabled()) {
                        logger.info("{} - {}", message, jsonData);
                    }
                    break;
                case "WARN":
                    if (logger.isWarnEnabled()) {
                        logger.warn("{} - {}", message, jsonData);
                    }
                    break;
                case "ERROR":
                    if (logger.isErrorEnabled()) {
                        logger.error("{} - {}", message, jsonData);
                    }
                    break;
                default:
                    logger.info("{} - {}", message, jsonData);
            }
        } catch (JsonProcessingException e) {
            // Fallback to regular logging if JSON serialization fails
            logger.warn("Failed to serialize log data to JSON: {}", e.getMessage());
            logger.info("{} - {}", message, data);
        }
    }
    
    /**
     * Logs business event with structured data.
     * 
     * @param logger the logger to use
     * @param eventType the type of business event
     * @param eventData the event data
     */
    public static void logBusinessEvent(Logger logger, String eventType, Map<String, Object> eventData) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "BUSINESS_EVENT");
        logData.put("eventType", eventType);
        logData.put("timestamp", Instant.now());
        logData.put("correlationId", getCorrelationId());
        logData.put("data", eventData);
        
        logStructured(logger, "INFO", "Business event: " + eventType, logData);
    }
    
    /**
     * Logs security event with structured data.
     * 
     * @param logger the logger to use
     * @param eventType the type of security event
     * @param eventData the event data
     */
    public static void logSecurityEvent(Logger logger, String eventType, Map<String, Object> eventData) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "SECURITY_EVENT");
        logData.put("eventType", eventType);
        logData.put("timestamp", Instant.now());
        logData.put("correlationId", getCorrelationId());
        logData.put("data", eventData);
        
        logStructured(logger, "WARN", "Security event: " + eventType, logData);
    }
    
    /**
     * Logs error with full context information.
     * 
     * @param logger the logger to use
     * @param message the error message
     * @param throwable the throwable
     * @param context additional context information
     */
    public static void logError(Logger logger, String message, Throwable throwable, Map<String, Object> context) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "ERROR");
        logData.put("timestamp", Instant.now());
        logData.put("correlationId", getCorrelationId());
        logData.put("errorMessage", throwable.getMessage());
        logData.put("errorType", throwable.getClass().getSimpleName());
        logData.put("stackTrace", getStackTraceAsString(throwable));
        
        if (context != null) {
            logData.put("context", context);
        }
        
        logStructured(logger, "ERROR", message, logData);
    }
    
    /**
     * Gets stack trace as string for logging.
     * 
     * @param throwable the throwable
     * @return stack trace as string
     */
    private static String getStackTraceAsString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Creates a performance measurement context that automatically logs execution time.
     * 
     * @param logger the logger to use
     * @param operationName the operation name
     * @return performance measurement context
     */
    public static PerformanceContext createPerformanceContext(Logger logger, String operationName) {
        return new PerformanceContext(logger, operationName);
    }
    
    /**
     * Performance measurement context for automatic timing.
     */
    public static class PerformanceContext implements AutoCloseable {
        private final Logger logger;
        private final String operationName;
        private final Instant startTime;
        
        private PerformanceContext(Logger logger, String operationName) {
            this.logger = logger;
            this.operationName = operationName;
            this.startTime = Instant.now();
        }
        
        @Override
        public void close() {
            Duration executionTime = Duration.between(startTime, Instant.now());
            logPerformance(logger, operationName, executionTime, "INFO");
        }
    }
}
