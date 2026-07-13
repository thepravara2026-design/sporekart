package com.sporekart.ai.compliance.api;

import java.util.Map;

public interface ComplianceMetricsService {
    long getTotalValidations();
    long getPassCount();
    long getFailureCount();
    long getViolationCount();
    double getPassRate();
    double getAverageAssessmentLatencyMs();
    Map<String, Object> getStatistics();
}
