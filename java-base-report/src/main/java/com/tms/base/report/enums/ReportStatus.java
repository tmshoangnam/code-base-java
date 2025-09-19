package com.tms.base.report.enums;

/**
 * Report generation status.
 * 
 * @since 1.0.0
 */
public enum ReportStatus {
    SUCCESS("success", "Report generated successfully"),
    FAILED("failed", "Report generation failed"),
    PROCESSING("processing", "Report is being generated"),
    CANCELLED("cancelled", "Report generation was cancelled"),
    PENDING("pending", "Report is pending generation");
    
    private final String value;
    private final String description;
    
    ReportStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
}
