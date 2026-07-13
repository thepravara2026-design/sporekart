package com.sporekart.ai.assistant.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AssistantMetrics(
    UUID id,
    UUID assistantId,
    long totalRequests,
    long successfulRequests,
    long failedRequests,
    double avgLatencyMs,
    double intentAccuracy,
    long tasksCreated,
    long tasksCompleted,
    long tasksFailed,
    OffsetDateTime recordedAt
) {}
