package com.tms.base.report.enums;

/**
 * Supported report formats.
 * 
 * @since 1.0.0
 */
public enum ReportFormat {
    PDF("pdf", "application/pdf"),
    EXCEL("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    WORD("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    CSV("csv", "text/csv"),
    HTML("html", "text/html");
    
    private final String extension;
    private final String mimeType;
    
    ReportFormat(String extension, String mimeType) {
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
