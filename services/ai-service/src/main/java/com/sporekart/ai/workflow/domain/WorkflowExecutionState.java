package com.sporekart.ai.workflow.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record WorkflowExecutionState(
    UUID id,
    UUID executionId,
    UUID workflowId,
    String currentStep,
    Map<String, Object> context,
    Map<String, Object> variables,
    String status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
