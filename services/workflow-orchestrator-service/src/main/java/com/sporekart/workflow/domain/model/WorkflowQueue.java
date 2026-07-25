package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record WorkflowQueue(
    String id,
    String instanceId,
    String workflowName,
    WorkflowType type,
    WorkflowState state,
    int priority,
    String queueType,
    Map<String, Object> payload,
    Instant queuedAt,
    Instant processedAt
) {
    public static WorkflowQueue create(
        String instanceId,
        String workflowName,
        WorkflowType type,
        int priority,
        String queueType,
        Map<String, Object> payload
    ) {
        return new WorkflowQueue(
            UUID.randomUUID().toString(),
            instanceId, workflowName, type, WorkflowState.QUEUED,
            priority, queueType,
            payload == null ? Map.of() : Map.copyOf(payload),
            Instant.now(), null
        );
    }

    public WorkflowQueue withProcessed() {
        return new WorkflowQueue(id, instanceId, workflowName, type, WorkflowState.RUNNING,
            priority, queueType, payload, queuedAt, Instant.now());
    }
}
