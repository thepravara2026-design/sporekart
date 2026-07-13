package com.sporekart.ai.workflow.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowExecution(
    UUID id,
    UUID workflowId,
    String workflowVersion,
    WorkflowExecutionStatus status,
    String triggerType,
    String triggerData,
    UUID startedBy,
    OffsetDateTime startedAt,
    OffsetDateTime completedAt,
    String errorMessage,
    int retryCount
) {}
