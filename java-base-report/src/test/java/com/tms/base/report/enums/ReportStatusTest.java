package com.tms.base.report.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReportStatus enum.
 * 
 * @since 1.0.0
 */
class ReportStatusTest {
    
    @Test
    void shouldHaveCorrectValues() {
        assertEquals("success", ReportStatus.SUCCESS.getValue());
        assertEquals("failed", ReportStatus.FAILED.getValue());
        assertEquals("processing", ReportStatus.PROCESSING.getValue());
        assertEquals("cancelled", ReportStatus.CANCELLED.getValue());
        assertEquals("pending", ReportStatus.PENDING.getValue());
    }
    
    @Test
    void shouldHaveCorrectDescriptions() {
        assertEquals("Report generated successfully", ReportStatus.SUCCESS.getDescription());
        assertEquals("Report generation failed", ReportStatus.FAILED.getDescription());
        assertEquals("Report is being generated", ReportStatus.PROCESSING.getDescription());
        assertEquals("Report generation was cancelled", ReportStatus.CANCELLED.getDescription());
        assertEquals("Report is pending generation", ReportStatus.PENDING.getDescription());
    }
    
    @Test
    void shouldHaveAllStatuses() {
        ReportStatus[] statuses = ReportStatus.values();
        assertEquals(5, statuses.length);
        
        assertTrue(java.util.Arrays.asList(statuses).contains(ReportStatus.SUCCESS));
        assertTrue(java.util.Arrays.asList(statuses).contains(ReportStatus.FAILED));
        assertTrue(java.util.Arrays.asList(statuses).contains(ReportStatus.PROCESSING));
        assertTrue(java.util.Arrays.asList(statuses).contains(ReportStatus.CANCELLED));
        assertTrue(java.util.Arrays.asList(statuses).contains(ReportStatus.PENDING));
    }
}
