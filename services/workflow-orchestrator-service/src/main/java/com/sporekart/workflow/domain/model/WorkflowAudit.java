package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record WorkflowAudit(
    String id,
    String instanceId,
    String action,
    String actor,
    String details,
    String outcome,
    Map<String, Object> metadata,
    Instant timestamp
) {
    public static WorkflowAudit create(
        String instanceId,
        String action,
        String actor,
        String details,
        String outcome,
        Map<String, Object> metadata
    ) {
        return new WorkflowAudit(
            UUID.randomUUID().toString(),
            instanceId, action, actor, details, outcome,
            metadata == null ? Map.of() : Map.copyOf(metadata),
            Instant.now()
        );
    }
}
