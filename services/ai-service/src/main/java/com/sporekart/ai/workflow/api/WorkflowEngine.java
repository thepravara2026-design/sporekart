package com.sporekart.ai.workflow.api;

import com.sporekart.ai.workflow.domain.*;
import java.util.UUID;

public interface WorkflowEngine {
    WorkflowExecution execute(UUID workflowId, String triggerData, UUID startedBy);
    void pause(UUID executionId);
    void resume(UUID executionId);
    void cancel(UUID executionId);
    WorkflowExecutionStatus getStatus(UUID executionId);
}
