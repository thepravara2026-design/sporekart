package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record WorkflowDefinition(
    String id,
    String name,
    String description,
    WorkflowType type,
    WorkflowStatus status,
    String domain,
    String owner,
    String version,
    List<WorkflowStep> steps,
    Map<String, Object> config,
    Map<String, Object> metadata,
    Instant createdAt,
    Instant updatedAt
) {
    public static WorkflowDefinition create(
        String name,
        String description,
        WorkflowType type,
        String domain,
        String owner,
        String version,
        List<WorkflowStep> steps,
        Map<String, Object> config,
        Map<String, Object> metadata
    ) {
        return new WorkflowDefinition(
            UUID.randomUUID().toString(),
            name, description, type, WorkflowStatus.ACTIVE,
            domain, owner, version,
            List.copyOf(steps),
            Map.copyOf(config),
            Map.copyOf(metadata),
            Instant.now(), Instant.now()
        );
    }

    public WorkflowDefinition withStatus(WorkflowStatus status) {
        return new WorkflowDefinition(id, name, description, type, status,
            domain, owner, version, steps, config, metadata, createdAt, Instant.now());
    }
}
