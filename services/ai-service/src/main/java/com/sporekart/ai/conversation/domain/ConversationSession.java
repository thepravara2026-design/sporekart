package com.sporekart.ai.conversation.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ConversationSession(
    UUID id,
    String userId,
    String title,
    ConversationStatus status,
    Map<String, String> metadata,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    OffsetDateTime expiresAt
) {}
