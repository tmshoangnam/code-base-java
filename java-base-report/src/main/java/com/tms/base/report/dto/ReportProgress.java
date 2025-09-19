package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportStatus;
import java.time.LocalDateTime;

/**
 * Progress tracking for report generation.
 *
 * @since 1.0.0
 */
public class ReportProgress {

    private String requestId;
    private ReportStatus status;
    private int percentage;
    private String currentStep;
    private String message;
    private LocalDateTime startedAt;
    private LocalDateTime lastUpdatedAt;
    private long estimatedTimeRemainingMs;
    private long totalSteps;
    private long completedSteps;

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ReportProgress progress = new ReportProgress();

        public Builder requestId(String requestId) {
            progress.requestId = requestId;
            return this;
        }

        public Builder status(ReportStatus status) {
            progress.status = status;
            return this;
        }

        public Builder percentage(int percentage) {
            progress.percentage = Math.clamp(percentage, 0, 100);
            return this;
        }

        public Builder currentStep(String currentStep) {
            progress.currentStep = currentStep;
            return this;
        }

        public Builder message(String message) {
            progress.message = message;
            return this;
        }

        public Builder startedAt(LocalDateTime startedAt) {
            progress.startedAt = startedAt;
            return this;
        }

        public Builder lastUpdatedAt(LocalDateTime lastUpdatedAt) {
            progress.lastUpdatedAt = lastUpdatedAt;
            return this;
        }

        public Builder estimatedTimeRemainingMs(long estimatedTimeRemainingMs) {
            progress.estimatedTimeRemainingMs = estimatedTimeRemainingMs;
            return this;
        }

        public Builder totalSteps(long totalSteps) {
            progress.totalSteps = totalSteps;
            return this;
        }

        public Builder completedSteps(long completedSteps) {
            progress.completedSteps = completedSteps;
            return this;
        }

        public ReportProgress build() {
            if (progress.startedAt == null) {
                progress.startedAt = LocalDateTime.now();
            }
            if (progress.lastUpdatedAt == null) {
                progress.lastUpdatedAt = LocalDateTime.now();
            }
            if (progress.status == null) {
                progress.status = ReportStatus.PROCESSING;
            }
            return progress;
        }
    }

    // Getters and setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = Math.clamp(percentage, 0, 100);
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public long getEstimatedTimeRemainingMs() {
        return estimatedTimeRemainingMs;
    }

    public void setEstimatedTimeRemainingMs(long estimatedTimeRemainingMs) {
        this.estimatedTimeRemainingMs = estimatedTimeRemainingMs;
    }

    public long getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(long totalSteps) {
        this.totalSteps = totalSteps;
    }

    public long getCompletedSteps() {
        return completedSteps;
    }

    public void setCompletedSteps(long completedSteps) {
        this.completedSteps = completedSteps;
    }
}
