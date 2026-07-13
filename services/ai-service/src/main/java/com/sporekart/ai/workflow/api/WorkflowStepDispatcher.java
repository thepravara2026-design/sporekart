package com.sporekart.ai.workflow.api;

import com.sporekart.ai.workflow.domain.*;
import java.util.UUID;

public interface WorkflowStepDispatcher {
    WorkflowStepStatus dispatch(UUID executionId, WorkflowStep step);
    void handleStepCompletion(UUID executionId, UUID stepId, Object result);
    void handleStepFailure(UUID executionId, UUID stepId, String error);
}
