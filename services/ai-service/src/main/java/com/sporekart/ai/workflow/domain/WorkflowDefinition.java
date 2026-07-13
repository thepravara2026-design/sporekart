package com.sporekart.ai.workflow.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record WorkflowDefinition(
    UUID id,
    String name,
    String description,
    String category,
    WorkflowStatus status,
    String version,
    WorkflowTriggerType triggerType,
    String triggerConfig,
    Map<String, String> metadata,
    boolean isTemplate,
    UUID createdBy,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
