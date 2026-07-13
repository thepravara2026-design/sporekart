package com.sporekart.ai.automation.api;

import java.util.Map;

public interface AutomationMonitoringService {
    void recordJobCreated();
    void recordJobCompleted();
    void recordJobFailed();
    void recordWorkflowStarted();
    void recordWorkflowCompleted();
    void recordWorkflowFailed();
    Map<String, Object> getHealthStatus();
}
