package com.sporekart.trainer.copilot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sporekart.trainer.copilot")
public class TrainerCopilotConfig {

    private boolean enabled = true;
    private int maxBatchSize = 50;
    private int defaultCurriculumLength = 12;
    private List<String> assessmentDifficultyLevels = List.of("BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT");
    private int defaultPassThreshold = 70;
    private int maxRetries = 3;
    private String requestTimeout = "30s";
    private int sessionTimeoutMinutes = 60;
    private boolean autoEnrollmentEnabled = true;
    private boolean certificationEnabled = true;
    private boolean progressTrackingEnabled = true;
    private boolean practicalGuideGenerationEnabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }

    public int getDefaultCurriculumLength() {
        return defaultCurriculumLength;
    }

    public void setDefaultCurriculumLength(int defaultCurriculumLength) {
        this.defaultCurriculumLength = defaultCurriculumLength;
    }

    public List<String> getAssessmentDifficultyLevels() {
        return assessmentDifficultyLevels;
    }

    public void setAssessmentDifficultyLevels(List<String> assessmentDifficultyLevels) {
        this.assessmentDifficultyLevels = assessmentDifficultyLevels;
    }

    public int getDefaultPassThreshold() {
        return defaultPassThreshold;
    }

    public void setDefaultPassThreshold(int defaultPassThreshold) {
        this.defaultPassThreshold = defaultPassThreshold;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(String requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getSessionTimeoutMinutes() {
        return sessionTimeoutMinutes;
    }

    public void setSessionTimeoutMinutes(int sessionTimeoutMinutes) {
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
    }

    public boolean isAutoEnrollmentEnabled() {
        return autoEnrollmentEnabled;
    }

    public void setAutoEnrollmentEnabled(boolean autoEnrollmentEnabled) {
        this.autoEnrollmentEnabled = autoEnrollmentEnabled;
    }

    public boolean isCertificationEnabled() {
        return certificationEnabled;
    }

    public void setCertificationEnabled(boolean certificationEnabled) {
        this.certificationEnabled = certificationEnabled;
    }

    public boolean isProgressTrackingEnabled() {
        return progressTrackingEnabled;
    }

    public void setProgressTrackingEnabled(boolean progressTrackingEnabled) {
        this.progressTrackingEnabled = progressTrackingEnabled;
    }

    public boolean isPracticalGuideGenerationEnabled() {
        return practicalGuideGenerationEnabled;
    }

    public void setPracticalGuideGenerationEnabled(boolean practicalGuideGenerationEnabled) {
        this.practicalGuideGenerationEnabled = practicalGuideGenerationEnabled;
    }
}
