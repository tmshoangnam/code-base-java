package com.tms.base.report.api;

/**
 * Exception thrown when report request validation fails.
 * 
 * @since 1.0.0
 */
public class ReportValidationException extends ReportException {
    
    public ReportValidationException(String message) {
        super("VALIDATION_ERROR", message, null);
    }
    
    public ReportValidationException(String message, Throwable cause) {
        super("VALIDATION_ERROR", message, null, cause);
    }
    
    public ReportValidationException(String message, String reportId) {
        super("VALIDATION_ERROR", message, reportId);
    }
    
    public ReportValidationException(String message, String reportId, Throwable cause) {
        super("VALIDATION_ERROR", message, reportId, cause);
    }
}
