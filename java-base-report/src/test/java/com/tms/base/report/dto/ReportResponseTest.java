package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.ReportStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for ReportResponse DTO.
 * 
 * @since 1.0.0
 */
class ReportResponseTest {
    
    @Test
    void shouldBuildResponseWithAllFields() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("meta1", "value1");
        
        byte[] data = "test data".getBytes();
        
        ReportResponse response = ReportResponse.builder()
            .format(ReportFormat.PDF)
            .fileName("test.pdf")
            .mimeType("application/pdf")
            .data(data)
            .status(ReportStatus.SUCCESS)
            .message("Report generated successfully")
            .size(9L)
            .generationTimeMs(1500L)
            .metadata(metadata)
            .requestId("test-request-123")
            .build();
        
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertEquals("test.pdf", response.getFileName());
        assertEquals("application/pdf", response.getMimeType());
        assertArrayEquals(data, response.getData());
        assertEquals(ReportStatus.SUCCESS, response.getStatus());
        assertEquals("Report generated successfully", response.getMessage());
        assertEquals(9L, response.getSize());
        assertEquals(1500L, response.getGenerationTimeMs());
        assertEquals(metadata, response.getMetadata());
        assertEquals("test-request-123", response.getRequestId());
    }
    
    @Test
    void shouldBuildResponseWithMinimalFields() {
        byte[] data = "test data".getBytes();
        
        ReportResponse response = ReportResponse.builder()
            .format(ReportFormat.PDF)
            .data(data)
            .build();
        
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertArrayEquals(data, response.getData());
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
        assertEquals(ReportStatus.SUCCESS, response.getStatus()); // default status
        assertEquals(9L, response.getSize()); // auto-calculated from data
    }
    
    @Test
    void shouldAutoCalculateSizeFromData() {
        byte[] data = "hello world".getBytes();
        
        ReportResponse response = ReportResponse.builder()
            .format(ReportFormat.PDF)
            .data(data)
            .build();
        
        assertEquals(data.length, response.getSize());
    }
    
    @Test
    void shouldSetDefaultStatusIfNotProvided() {
        ReportResponse response = ReportResponse.builder()
            .format(ReportFormat.PDF)
            .data("test".getBytes())
            .build();
        
        assertEquals(ReportStatus.SUCCESS, response.getStatus());
    }
    
    @Test
    void shouldInitializeEmptyMetadataIfNotProvided() {
        ReportResponse response = ReportResponse.builder()
            .format(ReportFormat.PDF)
            .data("test".getBytes())
            .build();
        
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
    }
}
