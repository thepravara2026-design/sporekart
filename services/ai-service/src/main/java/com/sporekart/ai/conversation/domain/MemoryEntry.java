package com.sporekart.ai.conversation.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MemoryEntry(
    UUID id,
    UUID sessionId,
    MemoryType type,
    String summary,
    String keywords,
    double relevanceScore,
    OffsetDateTime createdAt,
    OffsetDateTime expiresAt
) {}
