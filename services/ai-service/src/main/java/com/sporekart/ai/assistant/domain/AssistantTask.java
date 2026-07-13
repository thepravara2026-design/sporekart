package com.sporekart.ai.assistant.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AssistantTask(
    UUID id,
    UUID sessionId,
    UUID intentId,
    String name,
    String description,
    TaskStatus status,
    int priority,
    Map<String, Object> input,
    Map<String, Object> output,
    List<String> dependencies,
    int retryCount,
    int maxRetries,
    long timeoutMs,
    String errorMessage,
    OffsetDateTime createdAt,
    OffsetDateTime startedAt,
    OffsetDateTime completedAt
) {}
