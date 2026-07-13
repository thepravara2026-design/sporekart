package com.sporekart.ai.assistant.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record AssistantFeedback(
    UUID id,
    UUID sessionId,
    UUID userId,
    int rating,
    String comment,
    String category,
    Map<String, Object> metadata,
    boolean helpful,
    OffsetDateTime createdAt
) {}
