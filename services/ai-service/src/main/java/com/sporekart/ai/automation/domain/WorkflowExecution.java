package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record WorkflowExecution(
    UUID id,
    String workflowName,
    WorkflowExecutionStatus status,
    Map<String, Object> context,
    Map<String, Object> result,
    int retryCount,
    int maxRetries,
    Instant startedAt,
    Instant completedAt,
    String errorMessage
) {}
