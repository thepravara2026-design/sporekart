package com.sporekart.ai.automation.api;

import com.sporekart.ai.automation.domain.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WorkflowOrchestrator {
    WorkflowExecution startWorkflow(String workflowName, Map<String, Object> context);
    WorkflowExecution getWorkflowStatus(UUID executionId);
    WorkflowExecution cancelWorkflow(UUID executionId);
    WorkflowExecution retryWorkflow(UUID executionId);
    List<WorkflowHistory> getWorkflowHistory(UUID executionId);
}
