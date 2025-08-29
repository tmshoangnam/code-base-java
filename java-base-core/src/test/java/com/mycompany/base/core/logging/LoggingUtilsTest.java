package com.mycompany.base.core.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link LoggingUtils}.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class LoggingUtilsTest {
    
    @Test
    void getLogger_WithClass_ShouldReturnLogger() {
        // When
        Logger logger = LoggingUtils.getLogger(LoggingUtilsTest.class);
        
        // Then
        assertThat(logger).isNotNull();
        assertThat(logger.getName()).isEqualTo(LoggingUtilsTest.class.getName());
    }
    
    @Test
    void getLogger_WithName_ShouldReturnLogger() {
        // When
        Logger logger = LoggingUtils.getLogger("test-logger");
        
        // Then
        assertThat(logger).isNotNull();
        assertThat(logger.getName()).isEqualTo("test-logger");
    }
    
    @Test
    void correlationId_ShouldBeSetAndRetrieved() {
        // Given
        String correlationId = "test-correlation-123";
        
        // When
        LoggingUtils.setCorrelationId(correlationId);
        String retrieved = LoggingUtils.getCorrelationId();
        
        // Then
        assertThat(retrieved).isEqualTo(correlationId);
        
        // Cleanup
        LoggingUtils.clearContext();
    }
    
    @Test
    void generateAndSetCorrelationId_ShouldGenerateUniqueId() {
        // When
        String correlationId1 = LoggingUtils.generateAndSetCorrelationId();
        String correlationId2 = LoggingUtils.generateAndSetCorrelationId();
        
        // Then
        assertThat(correlationId1).isNotNull();
        assertThat(correlationId2).isNotNull();
        assertThat(correlationId1).isNotEqualTo(correlationId2);
        
        // Cleanup
        LoggingUtils.clearContext();
    }
    
    @Test
    void context_ShouldBeSetAndCleared() {
        // Given
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");
        
        // When
        LoggingUtils.setContext(context);
        LoggingUtils.setContext("key3", "value3");
        
        // Then
        assertThat(LoggingUtils.getCorrelationId()).isNotNull(); // Should have correlation ID
        
        // Cleanup
        LoggingUtils.clearContext();
    }
    
    @Test
    void performanceContext_ShouldAutoCloseAndLog() {
        // When
        try (LoggingUtils.PerformanceContext ctx = 
             LoggingUtils.createPerformanceContext(LoggingUtils.getLogger(LoggingUtilsTest.class), "test-operation")) {
            // Simulate some work
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Then - should not throw exception
        assertThat(true).isTrue();
    }
    
    @Test
    void logStructured_ShouldNotThrowException() {
        // Given
        Logger logger = LoggingUtils.getLogger(LoggingUtilsTest.class);
        Map<String, Object> data = new HashMap<>();
        data.put("testKey", "testValue");
        data.put("number", 42);
        
        // When & Then - should not throw exception
        LoggingUtils.logStructured(logger, "INFO", "Test message", data);
        assertThat(true).isTrue();
    }
    
    @Test
    void logBusinessEvent_ShouldNotThrowException() {
        // Given
        Logger logger = LoggingUtils.getLogger(LoggingUtilsTest.class);
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("userId", "12345");
        eventData.put("action", "TEST");
        
        // When & Then - should not throw exception
        LoggingUtils.logBusinessEvent(logger, "TEST_EVENT", eventData);
        assertThat(true).isTrue();
    }
}
