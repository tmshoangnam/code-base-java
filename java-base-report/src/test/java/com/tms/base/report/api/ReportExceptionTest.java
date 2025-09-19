package com.tms.base.report.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReportException classes.
 * 
 * @since 1.0.0
 */
class ReportExceptionTest {
    
    @Test
    void shouldCreateReportExceptionWithMessage() {
        ReportException exception = new ReportException("Test error");
        
        assertEquals("Test error", exception.getMessage());
        assertEquals("REPORT_ERROR", exception.getErrorCode());
        assertNull(exception.getReportId());
    }
    
    @Test
    void shouldCreateReportExceptionWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("Root cause");
        ReportException exception = new ReportException("Test error", cause);
        
        assertEquals("Test error", exception.getMessage());
        assertEquals("REPORT_ERROR", exception.getErrorCode());
        assertNull(exception.getReportId());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    void shouldCreateReportExceptionWithErrorCodeAndReportId() {
        ReportException exception = new ReportException("GENERATION_ERROR", "Generation failed", "report-123");
        
        assertEquals("Generation failed", exception.getMessage());
        assertEquals("GENERATION_ERROR", exception.getErrorCode());
        assertEquals("report-123", exception.getReportId());
    }
    
    @Test
    void shouldCreateReportGenerationException() {
        ReportGenerationException exception = new ReportGenerationException("Generation failed");
        
        assertEquals("Generation failed", exception.getMessage());
        assertEquals("GENERATION_ERROR", exception.getErrorCode());
        assertNull(exception.getReportId());
    }
    
    @Test
    void shouldCreateReportValidationException() {
        ReportValidationException exception = new ReportValidationException("Validation failed");
        
        assertEquals("Validation failed", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertNull(exception.getReportId());
    }
    
    @Test
    void shouldCreateReportTimeoutException() {
        ReportTimeoutException exception = new ReportTimeoutException("Timeout occurred", 5000L);
        
        assertEquals("Timeout occurred", exception.getMessage());
        assertEquals("TIMEOUT_ERROR", exception.getErrorCode());
        assertNull(exception.getReportId());
        assertEquals(5000L, exception.getTimeoutMs());
    }
}
