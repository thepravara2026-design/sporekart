package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;

import java.time.Instant;
import java.util.*;

public class WorkflowExecutionEngine {
    private final WorkflowRepositoryPort repository;
    private final WorkflowStateMachine stateMachine;

    public WorkflowExecutionEngine(WorkflowRepositoryPort repository, WorkflowStateMachine stateMachine) {
        this.repository = repository;
        this.stateMachine = stateMachine;
    }

    public WorkflowInstance startWorkflow(String definitionId, String triggeredBy, Map<String, Object> context) {
        var def = repository.findDefinitionById(definitionId)
            .orElseThrow(() -> new NoSuchElementException("Definition not found: " + definitionId));

        var instance = WorkflowInstance.create(
            definitionId, def.name(), def.type(), def.domain(), def.owner(),
            triggeredBy, false, true, 3
        );

        var transition = WorkflowTransition.create(
            WorkflowState.CREATED, WorkflowState.QUEUED, "START", triggeredBy, context
        );
        instance = instance.withState(WorkflowState.QUEUED).withTransition(transition).withContext(context);

        var saved = repository.saveInstance(instance);
        repository.saveAudit(WorkflowAudit.create(saved.id(), "WORKFLOW_STARTED", triggeredBy,
            "Workflow " + def.name() + " started", "SUCCESS", context));
        return saved;
    }

    public WorkflowInstance startSimulation(String definitionId, String triggeredBy, Map<String, Object> context) {
        var def = repository.findDefinitionById(definitionId)
            .orElseThrow(() -> new NoSuchElementException("Definition not found: " + definitionId));

        var instance = WorkflowInstance.create(
            definitionId, def.name(), def.type(), def.domain(), def.owner(),
            triggeredBy, true, false, 3
        );

        var transition = WorkflowTransition.create(
            WorkflowState.CREATED, WorkflowState.QUEUED, "SIMULATE", triggeredBy, context
        );
        instance = instance.withState(WorkflowState.QUEUED).withTransition(transition).withContext(context);

        var saved = repository.saveInstance(instance);
        repository.saveAudit(WorkflowAudit.create(saved.id(), "SIMULATION_STARTED", triggeredBy,
            "Simulation for " + def.name() + " started", "SUCCESS", context));
        return saved;
    }

    public WorkflowInstance transitionState(String instanceId, WorkflowState targetState, String action, String triggeredBy) {
        var instance = repository.findInstanceById(instanceId)
            .orElseThrow(() -> new NoSuchElementException("Instance not found: " + instanceId));

        var newState = stateMachine.transition(instance.state(), targetState);
        var transition = WorkflowTransition.create(instance.state(), newState, action, triggeredBy, Map.of());

        var updated = instance.withState(newState).withTransition(transition);
        var saved = repository.saveInstance(updated);

        repository.saveAudit(WorkflowAudit.create(saved.id(), "STATE_TRANSITION_" + action, triggeredBy,
            "Transitioned from " + instance.state() + " to " + newState, "SUCCESS", Map.of("action", action)));

        return saved;
    }

    public WorkflowInstance pauseWorkflow(String instanceId, String triggeredBy) {
        return transitionState(instanceId, WorkflowState.PAUSED, "PAUSE", triggeredBy);
    }

    public WorkflowInstance resumeWorkflow(String instanceId, String triggeredBy) {
        return transitionState(instanceId, WorkflowState.RUNNING, "RESUME", triggeredBy);
    }

    public WorkflowInstance cancelWorkflow(String instanceId, String triggeredBy) {
        return transitionState(instanceId, WorkflowState.CANCELLED, "CANCEL", triggeredBy);
    }

    public WorkflowInstance retryWorkflow(String instanceId, String triggeredBy) {
        var instance = repository.findInstanceById(instanceId)
            .orElseThrow(() -> new NoSuchElementException("Instance not found: " + instanceId));

        if (instance.retryCount() >= instance.maxRetries()) {
            throw new IllegalStateException("Max retries exceeded for instance: " + instanceId);
        }

        var retryInstance = instance.incrementRetry();
        var newState = stateMachine.transition(retryInstance.state(), WorkflowState.RETRYING);
        var transition = WorkflowTransition.create(retryInstance.state(), newState, "RETRY", triggeredBy, Map.of("attempt", retryInstance.retryCount()));

        var updated = retryInstance.withState(newState).withTransition(transition);
        var saved = repository.saveInstance(updated);

        repository.saveAudit(WorkflowAudit.create(saved.id(), "RETRY", triggeredBy,
            "Retry attempt " + saved.retryCount() + "/" + saved.maxRetries(), "SUCCESS", Map.of("attempt", saved.retryCount())));

        return saved;
    }

    public WorkflowInstance completeWorkflow(String instanceId, Map<String, Object> result, String triggeredBy) {
        var instance = repository.findInstanceById(instanceId)
            .orElseThrow(() -> new NoSuchElementException("Instance not found: " + instanceId));

        var newState = stateMachine.transition(instance.state(), WorkflowState.COMPLETED);
        var transition = WorkflowTransition.create(instance.state(), newState, "COMPLETE", triggeredBy, result);

        var updated = instance.withState(newState).withTransition(transition).withResult(result);
        var saved = repository.saveInstance(updated);

        repository.saveAudit(WorkflowAudit.create(saved.id(), "COMPLETED", triggeredBy,
            "Workflow completed successfully", "SUCCESS", result));

        return saved;
    }

    public WorkflowInstance failWorkflow(String instanceId, String error, String triggeredBy) {
        var instance = repository.findInstanceById(instanceId)
            .orElseThrow(() -> new NoSuchElementException("Instance not found: " + instanceId));

        var newState = stateMachine.transition(instance.state(), WorkflowState.FAILED);
        var transition = WorkflowTransition.create(instance.state(), newState, "FAIL", triggeredBy, Map.of("error", error));

        var updated = instance.withState(newState).withTransition(transition);
        var saved = repository.saveInstance(updated);

        repository.saveAudit(WorkflowAudit.create(saved.id(), "FAILED", triggeredBy,
            "Workflow failed: " + error, "FAILURE", Map.of("error", error)));

        return saved;
    }

    public WorkflowInstance requestApproval(String instanceId, String triggeredBy) {
        var instance = repository.findInstanceById(instanceId)
            .orElseThrow(() -> new NoSuchElementException("Instance not found: " + instanceId));

        var newState = stateMachine.transition(instance.state(), WorkflowState.WAITING_APPROVAL);
        var transition = WorkflowTransition.create(instance.state(), newState, "REQUEST_APPROVAL", triggeredBy, Map.of());

        var updated = instance.withState(newState).withTransition(transition);
        var saved = repository.saveInstance(updated);

        repository.saveAudit(WorkflowAudit.create(saved.id(), "APPROVAL_REQUESTED", triggeredBy,
            "Approval requested", "SUCCESS", Map.of()));

        return saved;
    }

    public WorkflowInstance archiveWorkflow(String instanceId, String triggeredBy) {
        return transitionState(instanceId, WorkflowState.ARCHIVED, "ARCHIVE", triggeredBy);
    }
}
