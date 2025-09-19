package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

/**
 * Unit tests for ReportProgress DTO.
 * 
 * @since 1.0.0
 */
class ReportProgressTest {
    
    @Test
    void shouldBuildProgressWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        
        ReportProgress progress = ReportProgress.builder()
            .requestId("request-123")
            .status(ReportStatus.PROCESSING)
            .percentage(75)
            .currentStep("Generating PDF")
            .message("Processing data...")
            .startedAt(now)
            .lastUpdatedAt(now.plusMinutes(1))
            .estimatedTimeRemainingMs(30000L)
            .totalSteps(10L)
            .completedSteps(7L)
            .build();
        
        assertEquals("request-123", progress.getRequestId());
        assertEquals(ReportStatus.PROCESSING, progress.getStatus());
        assertEquals(75, progress.getPercentage());
        assertEquals("Generating PDF", progress.getCurrentStep());
        assertEquals("Processing data...", progress.getMessage());
        assertEquals(now, progress.getStartedAt());
        assertEquals(now.plusMinutes(1), progress.getLastUpdatedAt());
        assertEquals(30000L, progress.getEstimatedTimeRemainingMs());
        assertEquals(10L, progress.getTotalSteps());
        assertEquals(7L, progress.getCompletedSteps());
    }
    
    @Test
    void shouldBuildProgressWithMinimalFields() {
        ReportProgress progress = ReportProgress.builder()
            .requestId("request-123")
            .percentage(50)
            .build();
        
        assertEquals("request-123", progress.getRequestId());
        assertEquals(50, progress.getPercentage());
        assertNotNull(progress.getStartedAt()); // auto-generated
        assertNotNull(progress.getLastUpdatedAt()); // auto-generated
        assertEquals(ReportStatus.PROCESSING, progress.getStatus()); // default status
    }
    
    @Test
    void shouldClampPercentageToValidRange() {
        ReportProgress progress1 = ReportProgress.builder()
            .requestId("request-123")
            .percentage(-10)
            .build();
        assertEquals(0, progress1.getPercentage());
        
        ReportProgress progress2 = ReportProgress.builder()
            .requestId("request-123")
            .percentage(150)
            .build();
        assertEquals(100, progress2.getPercentage());
        
        ReportProgress progress3 = ReportProgress.builder()
            .requestId("request-123")
            .percentage(75)
            .build();
        assertEquals(75, progress3.getPercentage());
    }
    
    @Test
    void shouldSetDefaultValuesIfNotProvided() {
        ReportProgress progress = ReportProgress.builder()
            .requestId("request-123")
            .build();
        
        assertNotNull(progress.getStartedAt());
        assertNotNull(progress.getLastUpdatedAt());
        assertEquals(ReportStatus.PROCESSING, progress.getStatus());
    }
    
    @Test
    void shouldSetPercentageCorrectly() {
        ReportProgress progress = new ReportProgress();
        
        progress.setPercentage(-5);
        assertEquals(0, progress.getPercentage());
        
        progress.setPercentage(105);
        assertEquals(100, progress.getPercentage());
        
        progress.setPercentage(50);
        assertEquals(50, progress.getPercentage());
    }
}
