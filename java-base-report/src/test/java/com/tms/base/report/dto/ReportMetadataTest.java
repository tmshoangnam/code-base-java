package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.TemplateType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for ReportMetadata DTO.
 * 
 * @since 1.0.0
 */
class ReportMetadataTest {
    
    @Test
    void shouldBuildMetadataWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> custom = new HashMap<>();
        custom.put("custom1", "value1");
        
        ReportMetadata metadata = ReportMetadata.builder()
            .reportId("report-123")
            .templateName("test-template")
            .templateType(TemplateType.HTML)
            .format(ReportFormat.PDF)
            .generatorName("test-generator")
            .version("1.0.0")
            .createdAt(now)
            .generatedAt(now.plusMinutes(1))
            .createdBy("test-user")
            .custom(custom)
            .build();
        
        assertEquals("report-123", metadata.getReportId());
        assertEquals("test-template", metadata.getTemplateName());
        assertEquals(TemplateType.HTML, metadata.getTemplateType());
        assertEquals(ReportFormat.PDF, metadata.getFormat());
        assertEquals("test-generator", metadata.getGeneratorName());
        assertEquals("1.0.0", metadata.getVersion());
        assertEquals(now, metadata.getCreatedAt());
        assertEquals(now.plusMinutes(1), metadata.getGeneratedAt());
        assertEquals("test-user", metadata.getCreatedBy());
        assertEquals(custom, metadata.getCustom());
    }
    
    @Test
    void shouldBuildMetadataWithMinimalFields() {
        ReportMetadata metadata = ReportMetadata.builder()
            .templateName("test-template")
            .format(ReportFormat.PDF)
            .build();
        
        assertEquals("test-template", metadata.getTemplateName());
        assertEquals(ReportFormat.PDF, metadata.getFormat());
        assertNotNull(metadata.getReportId()); // auto-generated
        assertNotNull(metadata.getCreatedAt()); // auto-generated
        assertNotNull(metadata.getCustom());
        assertTrue(metadata.getCustom().isEmpty());
    }
    
    @Test
    void shouldGenerateReportIdIfNotProvided() {
        ReportMetadata metadata1 = ReportMetadata.builder()
            .templateName("test-template")
            .format(ReportFormat.PDF)
            .build();
        
        ReportMetadata metadata2 = ReportMetadata.builder()
            .templateName("test-template")
            .format(ReportFormat.PDF)
            .build();
        
        assertNotNull(metadata1.getReportId());
        assertNotNull(metadata2.getReportId());
        assertNotEquals(metadata1.getReportId(), metadata2.getReportId());
    }
    
    @Test
    void shouldSetDefaultCreatedAtIfNotProvided() {
        LocalDateTime before = LocalDateTime.now();
        
        ReportMetadata metadata = ReportMetadata.builder()
            .templateName("test-template")
            .format(ReportFormat.PDF)
            .build();
        
        LocalDateTime after = LocalDateTime.now();
        
        assertNotNull(metadata.getCreatedAt());
        assertTrue(metadata.getCreatedAt().isAfter(before.minusSeconds(1)));
        assertTrue(metadata.getCreatedAt().isBefore(after.plusSeconds(1)));
    }
    
    @Test
    void shouldInitializeEmptyCustomMapIfNotProvided() {
        ReportMetadata metadata = ReportMetadata.builder()
            .templateName("test-template")
            .format(ReportFormat.PDF)
            .build();
        
        assertNotNull(metadata.getCustom());
        assertTrue(metadata.getCustom().isEmpty());
    }
}
