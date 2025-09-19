package com.tms.base.report.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReportFormat enum.
 * 
 * @since 1.0.0
 */
class ReportFormatTest {
    
    @Test
    void shouldHaveCorrectExtensions() {
        assertEquals("pdf", ReportFormat.PDF.getExtension());
        assertEquals("xlsx", ReportFormat.EXCEL.getExtension());
        assertEquals("docx", ReportFormat.WORD.getExtension());
        assertEquals("csv", ReportFormat.CSV.getExtension());
        assertEquals("html", ReportFormat.HTML.getExtension());
    }
    
    @Test
    void shouldHaveCorrectMimeTypes() {
        assertEquals("application/pdf", ReportFormat.PDF.getMimeType());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    ReportFormat.EXCEL.getMimeType());
        assertEquals("application/vnd.openxmlformats-officedocument.wordprocessingml.document", 
                    ReportFormat.WORD.getMimeType());
        assertEquals("text/csv", ReportFormat.CSV.getMimeType());
        assertEquals("text/html", ReportFormat.HTML.getMimeType());
    }
    
    @Test
    void shouldHaveAllFormats() {
        ReportFormat[] formats = ReportFormat.values();
        assertEquals(5, formats.length);
        
        assertTrue(java.util.Arrays.asList(formats).contains(ReportFormat.PDF));
        assertTrue(java.util.Arrays.asList(formats).contains(ReportFormat.EXCEL));
        assertTrue(java.util.Arrays.asList(formats).contains(ReportFormat.WORD));
        assertTrue(java.util.Arrays.asList(formats).contains(ReportFormat.CSV));
        assertTrue(java.util.Arrays.asList(formats).contains(ReportFormat.HTML));
    }
}
