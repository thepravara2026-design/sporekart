package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.infrastructure.executor.MockActionExecutorService;
import com.sporekart.workflow.domain.model.MockAction;

import java.util.List;
import java.util.Map;

public class WorkflowExecutor {
    private final MockActionExecutorService actionExecutor;

    public WorkflowExecutor(MockActionExecutorService actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    public MockAction execute(String name, String actionType, String description, Map<String, Object> input) {
        return actionExecutor.executeAction(name, actionType, description, input);
    }

    public List<MockAction> executeBatch(List<Map<String, Object>> actions) {
        return actionExecutor.executeActions(actions);
    }

    public List<MockAction> rollback(List<MockAction> executedActions) {
        return actionExecutor.executeRollback(executedActions);
    }
}
