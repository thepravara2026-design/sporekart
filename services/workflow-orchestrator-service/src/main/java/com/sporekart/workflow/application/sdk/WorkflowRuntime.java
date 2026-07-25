package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.domain.engine.WorkflowExecutionEngine;
import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorkflowRuntime {
    private final WorkflowExecutionEngine executionEngine;
    private final WorkflowRepositoryPort repository;

    public WorkflowRuntime(WorkflowExecutionEngine executionEngine, WorkflowRepositoryPort repository) {
        this.executionEngine = executionEngine;
        this.repository = repository;
    }

    public WorkflowInstance start(String definitionId, String triggeredBy, Map<String, Object> context) {
        return executionEngine.startWorkflow(definitionId, triggeredBy, context);
    }

    public WorkflowInstance startSimulation(String definitionId, String triggeredBy, Map<String, Object> context) {
        return executionEngine.startSimulation(definitionId, triggeredBy, context);
    }

    public Optional<WorkflowInstance> getInstance(String id) {
        return repository.findInstanceById(id);
    }

    public List<WorkflowInstance> getAllInstances() {
        return repository.findAllInstances();
    }

    public List<WorkflowInstance> getInstancesByState(WorkflowState state) {
        return repository.findInstancesByState(state);
    }

    public WorkflowInstance pause(String instanceId, String triggeredBy) {
        return executionEngine.pauseWorkflow(instanceId, triggeredBy);
    }

    public WorkflowInstance resume(String instanceId, String triggeredBy) {
        return executionEngine.resumeWorkflow(instanceId, triggeredBy);
    }

    public WorkflowInstance cancel(String instanceId, String triggeredBy) {
        return executionEngine.cancelWorkflow(instanceId, triggeredBy);
    }

    public WorkflowInstance retry(String instanceId, String triggeredBy) {
        return executionEngine.retryWorkflow(instanceId, triggeredBy);
    }

    public WorkflowInstance complete(String instanceId, Map<String, Object> result, String triggeredBy) {
        return executionEngine.completeWorkflow(instanceId, result, triggeredBy);
    }

    public WorkflowInstance fail(String instanceId, String error, String triggeredBy) {
        return executionEngine.failWorkflow(instanceId, error, triggeredBy);
    }
}
