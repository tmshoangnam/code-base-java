package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.StreamingMode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for StreamingReportResponse DTO.
 * 
 * @since 1.0.0
 */
class StreamingReportResponseTest {
    
    @Test
    void shouldBuildResponseWithAllFields() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("meta1", "value1");
        
        InputStream dataStream = new ByteArrayInputStream("test data".getBytes());
        
        StreamingReportResponse response = StreamingReportResponse.builder()
            .format(ReportFormat.PDF)
            .fileName("test.pdf")
            .mimeType("application/pdf")
            .streamingMode(StreamingMode.CHUNKED)
            .dataStream(dataStream)
            .totalSize(1024L)
            .chunkSize(8192)
            .metadata(metadata)
            .chunkConsumer(bytes -> {})
            .onComplete(() -> {})
            .onError(() -> {})
            .requestId("test-request-123")
            .build();
        
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertEquals("test.pdf", response.getFileName());
        assertEquals("application/pdf", response.getMimeType());
        assertEquals(StreamingMode.CHUNKED, response.getStreamingMode());
        assertEquals(dataStream, response.getDataStream());
        assertEquals(1024L, response.getTotalSize());
        assertEquals(8192, response.getChunkSize());
        assertEquals(metadata, response.getMetadata());
        assertNotNull(response.getChunkConsumer());
        assertNotNull(response.getOnComplete());
        assertNotNull(response.getOnError());
        assertEquals("test-request-123", response.getRequestId());
    }
    
    @Test
    void shouldBuildResponseWithMinimalFields() {
        InputStream dataStream = new ByteArrayInputStream("test data".getBytes());
        
        StreamingReportResponse response = StreamingReportResponse.builder()
            .format(ReportFormat.PDF)
            .dataStream(dataStream)
            .build();
        
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertEquals(dataStream, response.getDataStream());
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
        assertNotNull(response.getRequestId()); // auto-generated
    }
    
    @Test
    void shouldGenerateRequestIdIfNotProvided() {
        InputStream dataStream = new ByteArrayInputStream("test data".getBytes());
        
        StreamingReportResponse response1 = StreamingReportResponse.builder()
            .format(ReportFormat.PDF)
            .dataStream(dataStream)
            .build();
        
        StreamingReportResponse response2 = StreamingReportResponse.builder()
            .format(ReportFormat.PDF)
            .dataStream(dataStream)
            .build();
        
        assertNotNull(response1.getRequestId());
        assertNotNull(response2.getRequestId());
        assertNotEquals(response1.getRequestId(), response2.getRequestId());
    }
    
    @Test
    void shouldInitializeEmptyMetadataIfNotProvided() {
        InputStream dataStream = new ByteArrayInputStream("test data".getBytes());
        
        StreamingReportResponse response = StreamingReportResponse.builder()
            .format(ReportFormat.PDF)
            .dataStream(dataStream)
            .build();
        
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
    }
    
    @Test
    void shouldAllowNullCallbacks() {
        InputStream dataStream = new ByteArrayInputStream("test data".getBytes());
        
        StreamingReportResponse response = StreamingReportResponse.builder()
            .format(ReportFormat.PDF)
            .dataStream(dataStream)
            .chunkConsumer(null)
            .onComplete(null)
            .onError(null)
            .build();
        
        assertNull(response.getChunkConsumer());
        assertNull(response.getOnComplete());
        assertNull(response.getOnError());
    }
}
