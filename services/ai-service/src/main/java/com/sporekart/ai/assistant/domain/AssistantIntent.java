package com.sporekart.ai.assistant.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AssistantIntent(
    UUID id,
    UUID sessionId,
    String userInput,
    String resolvedIntent,
    double confidence,
    IntentStatus status,
    IntentPriority priority,
    Map<String, Object> metadata,
    List<String> entities,
    String fallbackIntent,
    OffsetDateTime createdAt,
    OffsetDateTime resolvedAt
) {}
