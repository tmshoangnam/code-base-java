package com.tms.base.report.enums;

/**
 * Supported template types for report generation.
 * 
 * @since 1.0.0
 */
public enum TemplateType {
    HTML("html", "text/html"),
    XML("xml", "application/xml"),
    JSON("json", "application/json"),
    TEXT("txt", "text/plain"),
    VELOCITY("vm", "text/velocity"),
    FREEMARKER("ftl", "text/freemarker"),
    THYMELEAF("html", "text/html");
    
    private final String extension;
    private final String mimeType;
    
    TemplateType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }
    
    public String getExtension() {
        return extension;
    }
    
    public String getMimeType() {
        return mimeType;
    }
}
