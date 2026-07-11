package com.sporekart.ai.workflow.api;

import com.sporekart.ai.workflow.domain.WorkflowTask;
import java.util.Map;

public interface TaskOrchestrator {
    Map<String, Object> executeTask(WorkflowTask task, Map<String, Object> context);
    boolean canExecute(WorkflowTask task, Map<String, Object> context);
}
