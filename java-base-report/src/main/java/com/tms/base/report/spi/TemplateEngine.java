package com.tms.base.report.spi;

import com.tms.base.report.enums.TemplateType;
import java.util.Map;

/**
 * Service Provider Interface for template engines.
 * 
 * @since 1.0.0
 */
public interface TemplateEngine {
    
    /**
     * Process template with data.
     * 
     * @param template Template content
     * @param data Data to process
     * @param parameters Additional parameters
     * @return Processed template content
     */
    String process(String template, Object data, Map<String, Object> parameters);
    
    /**
     * Process template file with data.
     * 
     * @param templateName Template name/path
     * @param data Data to process
     * @param parameters Additional parameters
     * @return Processed template content
     */
    String processFile(String templateName, Object data, Map<String, Object> parameters);
    
    /**
     * Check if template exists.
     * 
     * @param templateName Template name/path
     * @return true if template exists
     */
    boolean templateExists(String templateName);
    
    /**
     * Get supported template types.
     * 
     * @return Array of supported template types
     */
    TemplateType[] getSupportedTypes();
    
    /**
     * Get engine name.
     * 
     * @return Engine name
     */
    String getName();
    
    /**
     * Get engine priority (higher number = higher priority).
     * 
     * @return Priority value
     */
    default int getPriority() {
        return 0;
    }
}
