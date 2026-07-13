package com.sporekart.ai.assistant.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record AssistantSession(
    UUID id,
    UUID assistantId,
    UUID userId,
    UUID conversationId,
    Map<String, Object> context,
    AssistantStatus status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    OffsetDateTime expiresAt
) {}
