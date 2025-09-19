package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.TemplateType;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Metadata for report generation.
 * 
 * @since 1.0.0
 */
public class ReportMetadata {
    
    private String reportId;
    private String templateName;
    private TemplateType templateType;
    private ReportFormat format;
    private String generatorName;
    private String version;
    private LocalDateTime createdAt;
    private LocalDateTime generatedAt;
    private String createdBy;
    private Map<String, Object> custom;
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private ReportMetadata metadata = new ReportMetadata();
        
        public Builder reportId(String reportId) {
            metadata.reportId = reportId;
            return this;
        }
        
        public Builder templateName(String templateName) {
            metadata.templateName = templateName;
            return this;
        }
        
        public Builder templateType(TemplateType templateType) {
            metadata.templateType = templateType;
            return this;
        }
        
        public Builder format(ReportFormat format) {
            metadata.format = format;
            return this;
        }
        
        public Builder generatorName(String generatorName) {
            metadata.generatorName = generatorName;
            return this;
        }
        
        public Builder version(String version) {
            metadata.version = version;
            return this;
        }
        
        public Builder createdAt(LocalDateTime createdAt) {
            metadata.createdAt = createdAt;
            return this;
        }
        
        public Builder generatedAt(LocalDateTime generatedAt) {
            metadata.generatedAt = generatedAt;
            return this;
        }
        
        public Builder createdBy(String createdBy) {
            metadata.createdBy = createdBy;
            return this;
        }
        
        public Builder custom(Map<String, Object> custom) {
            metadata.custom = custom;
            return this;
        }
        
        public ReportMetadata build() {
            if (metadata.custom == null) {
                metadata.custom = new HashMap<>();
            }
            if (metadata.createdAt == null) {
                metadata.createdAt = LocalDateTime.now();
            }
            if (metadata.reportId == null) {
                metadata.reportId = java.util.UUID.randomUUID().toString();
            }
            return metadata;
        }
    }
    
    // Getters and setters
    public String getReportId() {
        return reportId;
    }
    
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public TemplateType getTemplateType() {
        return templateType;
    }
    
    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }
    
    public ReportFormat getFormat() {
        return format;
    }
    
    public void setFormat(ReportFormat format) {
        this.format = format;
    }
    
    public String getGeneratorName() {
        return generatorName;
    }
    
    public void setGeneratorName(String generatorName) {
        this.generatorName = generatorName;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public Map<String, Object> getCustom() {
        return custom;
    }
    
    public void setCustom(Map<String, Object> custom) {
        this.custom = custom;
    }
}
