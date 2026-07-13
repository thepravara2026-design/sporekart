package com.sporekart.ai.automation.api;

import com.sporekart.ai.automation.domain.AutomationJob;
import com.sporekart.ai.automation.domain.WorkflowExecution;

public interface AutomationKafkaEventPublisher {
    void publishJobCreated(AutomationJob job);
    void publishJobUpdated(AutomationJob job);
    void publishWorkflowStarted(WorkflowExecution execution);
    void publishWorkflowCompleted(WorkflowExecution execution);
    void publishWorkflowFailed(WorkflowExecution execution);
}
