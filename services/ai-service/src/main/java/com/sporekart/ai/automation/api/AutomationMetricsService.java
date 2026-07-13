package com.sporekart.ai.automation.api;

import java.util.Map;

public interface AutomationMetricsService {
    void recordWorkflow();
    void recordJob(boolean success);
    void recordRetry();
    void recordEscalation();
    void recordLatency(long ms);
    long getTotalWorkflows();
    long getTotalJobs();
    double getJobSuccessRate();
    long getRetryCount();
    long getEscalationCount();
    Map<String, Object> getStatistics();
}
