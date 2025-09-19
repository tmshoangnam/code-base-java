package com.tms.base.report.api;

/**
 * Exception thrown when report generation fails.
 * 
 * @since 1.0.0
 */
public class ReportGenerationException extends ReportException {
    
    public ReportGenerationException(String message) {
        super("GENERATION_ERROR", message, null);
    }
    
    public ReportGenerationException(String message, Throwable cause) {
        super("GENERATION_ERROR", message, null, cause);
    }
    
    public ReportGenerationException(String message, String reportId) {
        super("GENERATION_ERROR", message, reportId);
    }
    
    public ReportGenerationException(String message, String reportId, Throwable cause) {
        super("GENERATION_ERROR", message, reportId, cause);
    }
}
