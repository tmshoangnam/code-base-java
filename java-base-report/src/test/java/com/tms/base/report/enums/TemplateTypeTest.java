package com.tms.base.report.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TemplateType enum.
 * 
 * @since 1.0.0
 */
class TemplateTypeTest {
    
    @Test
    void shouldHaveCorrectExtensions() {
        assertEquals("html", TemplateType.HTML.getExtension());
        assertEquals("xml", TemplateType.XML.getExtension());
        assertEquals("json", TemplateType.JSON.getExtension());
        assertEquals("txt", TemplateType.TEXT.getExtension());
        assertEquals("vm", TemplateType.VELOCITY.getExtension());
        assertEquals("ftl", TemplateType.FREEMARKER.getExtension());
        assertEquals("html", TemplateType.THYMELEAF.getExtension());
    }
    
    @Test
    void shouldHaveCorrectMimeTypes() {
        assertEquals("text/html", TemplateType.HTML.getMimeType());
        assertEquals("application/xml", TemplateType.XML.getMimeType());
        assertEquals("application/json", TemplateType.JSON.getMimeType());
        assertEquals("text/plain", TemplateType.TEXT.getMimeType());
        assertEquals("text/velocity", TemplateType.VELOCITY.getMimeType());
        assertEquals("text/freemarker", TemplateType.FREEMARKER.getMimeType());
        assertEquals("text/html", TemplateType.THYMELEAF.getMimeType());
    }
    
    @Test
    void shouldHaveAllTemplateTypes() {
        TemplateType[] types = TemplateType.values();
        assertEquals(7, types.length);
        
        assertTrue(java.util.Arrays.asList(types).contains(TemplateType.HTML));
        assertTrue(java.util.Arrays.asList(types).contains(TemplateType.XML));
        assertTrue(java.util.Arrays.asList(types).contains(TemplateType.JSON));
        assertTrue(java.util.Arrays.asList(types).contains(TemplateType.TEXT));
        assertTrue(java.util.Arrays.asList(types).contains(TemplateType.VELOCITY));
        assertTrue(java.util.Arrays.asList(types).contains(TemplateType.FREEMARKER));
        assertTrue(java.util.Arrays.asList(types).contains(TemplateType.THYMELEAF));
    }
}
