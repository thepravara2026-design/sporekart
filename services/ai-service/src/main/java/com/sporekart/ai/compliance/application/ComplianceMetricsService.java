package com.sporekart.ai.compliance.application;

import java.util.Map;

public interface ComplianceMetricsService {
    void recordValidation(boolean passed);
    void recordViolation();
    void recordAssessmentLatency(long ms);
    long getTotalValidations();
    long getPassCount();
    long getFailureCount();
    long getViolationCount();
    double getPassRate();
    double getAverageAssessmentLatencyMs();
    Map<String, Object> getStatistics();
}
