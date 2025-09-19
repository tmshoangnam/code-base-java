package com.tms.base.report.spi;

import com.tms.base.report.api.ReportGenerator;
import com.tms.base.report.enums.ReportFormat;

/**
 * Service Provider Interface for report generators.
 * 
 * @since 1.0.0
 */
public interface ReportProvider {
    
    /**
     * Get the report generator.
     * 
     * @return Report generator instance
     */
    ReportGenerator<?> getGenerator();
    
    /**
     * Get supported formats.
     * 
     * @return Array of supported formats
     */
    ReportFormat[] getSupportedFormats();
    
    /**
     * Get provider name.
     * 
     * @return Provider name
     */
    String getName();
    
    /**
     * Get provider priority (higher number = higher priority).
     * 
     * @return Priority value
     */
    default int getPriority() {
        return 0;
    }
}
