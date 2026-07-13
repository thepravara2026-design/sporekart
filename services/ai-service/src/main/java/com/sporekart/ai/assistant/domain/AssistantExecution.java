package com.sporekart.ai.assistant.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record AssistantExecution(
    UUID id,
    UUID assistantId,
    UUID sessionId,
    UUID taskId,
    CopilotType copilotType,
    String action,
    Map<String, Object> request,
    Map<String, Object> response,
    boolean success,
    long latencyMs,
    String errorMessage,
    OffsetDateTime createdAt,
    OffsetDateTime completedAt
) {}
