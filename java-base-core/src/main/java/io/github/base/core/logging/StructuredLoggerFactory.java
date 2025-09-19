package io.github.base.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating StructuredLogger instances.
 * 
 * <p>This factory provides a convenient way to create StructuredLogger instances
 * for different classes, following the same pattern as SLF4J LoggerFactory.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public final class StructuredLoggerFactory {
    
    private StructuredLoggerFactory() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Gets a StructuredLogger instance for the specified class.
     * 
     * @param clazz the class to create a logger for
     * @return a StructuredLogger instance
     */
    public static StructuredLogger getLogger(Class<?> clazz) {
        return new StructuredLogger(LoggerFactory.getLogger(clazz));
    }
    
    /**
     * Gets a StructuredLogger instance for the specified name.
     * 
     * @param name the name to create a logger for
     * @return a StructuredLogger instance
     */
    public static StructuredLogger getLogger(String name) {
        return new StructuredLogger(LoggerFactory.getLogger(name));
    }
}
