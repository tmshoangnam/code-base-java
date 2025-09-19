package com.tms.base.report.api;

/**
 * Base exception for report generation errors.
 * 
 * @since 1.0.0
 */
public class ReportException extends RuntimeException {
    
    private final String errorCode;
    private final String reportId;
    
    public ReportException(String message) {
        super(message);
        this.errorCode = "REPORT_ERROR";
        this.reportId = null;
    }
    
    public ReportException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "REPORT_ERROR";
        this.reportId = null;
    }
    
    public ReportException(String errorCode, String message, String reportId) {
        super(message);
        this.errorCode = errorCode;
        this.reportId = reportId;
    }
    
    public ReportException(String errorCode, String message, String reportId, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.reportId = reportId;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getReportId() {
        return reportId;
    }
}
