package com.sporekart.ai.automation.api;

import com.sporekart.ai.automation.domain.*;
import java.util.Map;
import java.util.UUID;

public interface AutomationEngine {
    AutomationJob executeJob(JobType type, String name, Map<String, Object> params);
    WorkflowExecution executeWorkflow(String workflowName, Map<String, Object> context);
    LifecycleState transitionState(UUID entityId, String entityType, LifecycleStateType targetState, String triggeredBy, String reason);
}
