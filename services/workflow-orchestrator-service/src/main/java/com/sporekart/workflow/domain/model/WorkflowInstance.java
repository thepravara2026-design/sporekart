package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record WorkflowInstance(
    String id,
    String definitionId,
    String name,
    WorkflowType type,
    WorkflowState state,
    String domain,
    String owner,
    String traceId,
    List<WorkflowTransition> transitions,
    Map<String, Object> context,
    Map<String, Object> result,
    String triggeredBy,
    int retryCount,
    int maxRetries,
    boolean simulationMode,
    boolean approvalRequired,
    Instant createdAt,
    Instant updatedAt,
    Instant completedAt
) {
    public static WorkflowInstance create(
        String definitionId,
        String name,
        WorkflowType type,
        String domain,
        String owner,
        String triggeredBy,
        boolean simulationMode,
        boolean approvalRequired,
        int maxRetries
    ) {
        return new WorkflowInstance(
            UUID.randomUUID().toString(),
            definitionId, name, type, WorkflowState.CREATED,
            domain, owner, UUID.randomUUID().toString(),
            List.of(), Map.of(), Map.of(),
            triggeredBy, 0, maxRetries,
            simulationMode, approvalRequired,
            Instant.now(), Instant.now(), null
        );
    }

    public WorkflowInstance withState(WorkflowState state) {
        return new WorkflowInstance(id, definitionId, name, type, state,
            domain, owner, traceId, transitions, context, result,
            triggeredBy, retryCount, maxRetries,
            simulationMode, approvalRequired,
            createdAt, Instant.now(), state == WorkflowState.COMPLETED || state == WorkflowState.FAILED || state == WorkflowState.CANCELLED || state == WorkflowState.ARCHIVED ? Instant.now() : completedAt);
    }

    public WorkflowInstance withTransition(WorkflowTransition transition) {
        var newTransitions = new java.util.ArrayList<>(transitions);
        newTransitions.add(transition);
        return new WorkflowInstance(id, definitionId, name, type, state,
            domain, owner, traceId, List.copyOf(newTransitions), context, result,
            triggeredBy, retryCount, maxRetries,
            simulationMode, approvalRequired,
            createdAt, Instant.now(), completedAt);
    }

    public WorkflowInstance withContext(Map<String, Object> newContext) {
        var merged = new java.util.HashMap<>(context);
        merged.putAll(newContext);
        return new WorkflowInstance(id, definitionId, name, type, state,
            domain, owner, traceId, transitions, Map.copyOf(merged), result,
            triggeredBy, retryCount, maxRetries,
            simulationMode, approvalRequired,
            createdAt, updatedAt, completedAt);
    }

    public WorkflowInstance withResult(Map<String, Object> newResult) {
        return new WorkflowInstance(id, definitionId, name, type, state,
            domain, owner, traceId, transitions, context, Map.copyOf(newResult),
            triggeredBy, retryCount, maxRetries,
            simulationMode, approvalRequired,
            createdAt, updatedAt, completedAt);
    }

    public WorkflowInstance incrementRetry() {
        return new WorkflowInstance(id, definitionId, name, type, state,
            domain, owner, traceId, transitions, context, result,
            triggeredBy, retryCount + 1, maxRetries,
            simulationMode, approvalRequired,
            createdAt, updatedAt, completedAt);
    }
}
