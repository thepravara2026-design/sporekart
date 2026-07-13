package com.sporekart.ai.conversation.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ConversationMessage(
    UUID id,
    UUID sessionId,
    MessageRole role,
    String content,
    Map<String, String> metadata,
    MessageStatus status,
    OffsetDateTime createdAt
) {}
