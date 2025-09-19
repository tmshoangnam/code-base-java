package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for ReportRequest DTO.
 * 
 * @since 1.0.0
 */
class ReportRequestTest {
    
    @Test
    void shouldBuildRequestWithAllFields() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("param1", "value1");
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("meta1", "value1");
        
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .templateName("test-template")
            .data("test data")
            .parameters(parameters)
            .outputFileName("test.pdf")
            .metadata(metadata)
            .requestId("test-request-123")
            .timeoutMs(60000L)
            .build();
        
        assertEquals(ReportFormat.PDF, request.getFormat());
        assertEquals("test-template", request.getTemplateName());
        assertEquals("test data", request.getData());
        assertEquals(parameters, request.getParameters());
        assertEquals("test.pdf", request.getOutputFileName());
        assertEquals(metadata, request.getMetadata());
        assertEquals("test-request-123", request.getRequestId());
        assertEquals(60000L, request.getTimeoutMs());
    }
    
    @Test
    void shouldBuildRequestWithMinimalFields() {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .data("test data")
            .build();
        
        assertEquals(ReportFormat.PDF, request.getFormat());
        assertEquals("test data", request.getData());
        assertNotNull(request.getParameters());
        assertNotNull(request.getMetadata());
        assertNotNull(request.getRequestId());
        assertEquals(300000L, request.getTimeoutMs()); // default timeout
    }
    
    @Test
    void shouldGenerateRequestIdIfNotProvided() {
        ReportRequest<String> request1 = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .data("test data")
            .build();
        
        ReportRequest<String> request2 = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .data("test data")
            .build();
        
        assertNotNull(request1.getRequestId());
        assertNotNull(request2.getRequestId());
        assertNotEquals(request1.getRequestId(), request2.getRequestId());
    }
    
    @Test
    void shouldInitializeEmptyMapsIfNotProvided() {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .data("test data")
            .build();
        
        assertNotNull(request.getParameters());
        assertTrue(request.getParameters().isEmpty());
        
        assertNotNull(request.getMetadata());
        assertTrue(request.getMetadata().isEmpty());
    }
}
