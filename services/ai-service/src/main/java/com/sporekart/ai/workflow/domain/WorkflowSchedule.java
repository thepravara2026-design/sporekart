package com.sporekart.ai.workflow.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowSchedule(
    UUID id,
    UUID workflowId,
    String cronExpression,
    OffsetDateTime startAt,
    OffsetDateTime endAt,
    boolean isActive,
    String timezone,
    OffsetDateTime lastExecutedAt,
    OffsetDateTime nextExecutionAt
) {}
