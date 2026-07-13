package com.sporekart.ai.conversation.interfaces.rest.dto;

import com.sporekart.ai.conversation.domain.MemoryEntry;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ConversationMemoryResponse(
    UUID id,
    UUID sessionId,
    String memoryType,
    String summary,
    String keywords,
    double relevanceScore,
    OffsetDateTime createdAt,
    OffsetDateTime expiresAt
) {
    public static ConversationMemoryResponse from(MemoryEntry entry) {
        return new ConversationMemoryResponse(
            entry.id(),
            entry.sessionId(),
            entry.type().name(),
            entry.summary(),
            entry.keywords(),
            entry.relevanceScore(),
            entry.createdAt(),
            entry.expiresAt()
        );
    }
}
