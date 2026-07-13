package com.sporekart.ai.workflow.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowVersion(
    UUID id,
    UUID workflowId,
    String version,
    String definitionJson,
    boolean isActive,
    OffsetDateTime createdAt
) {}
