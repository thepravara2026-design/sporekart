package com.sporekart.ai.workflow.api;

import com.sporekart.ai.workflow.domain.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowExecutionService {
    WorkflowExecution executeWorkflow(UUID workflowId, String triggerData, UUID startedBy);
    WorkflowExecution startExecution(UUID workflowId, Map<String, Object> inputData, UUID startedBy);
    WorkflowExecution pauseExecution(UUID executionId);
    WorkflowExecution resumeExecution(UUID executionId);
    WorkflowExecution cancelExecution(UUID executionId);
    Optional<WorkflowExecution> getExecution(UUID executionId);
    List<WorkflowExecution> listExecutions(UUID workflowId);
    List<WorkflowExecution> listAllExecutions();
    Optional<WorkflowExecutionState> getExecutionState(UUID executionId);
}
