package com.sporekart.ai.conversation.interfaces.rest.dto;

import com.sporekart.ai.conversation.domain.ConversationSession;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ConversationSessionResponse(
    UUID id,
    String userId,
    String title,
    String status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    OffsetDateTime expiresAt
) {
    public static ConversationSessionResponse from(ConversationSession session) {
        return new ConversationSessionResponse(
            session.id(),
            session.userId(),
            session.title(),
            session.status().name(),
            session.createdAt(),
            session.updatedAt(),
            session.expiresAt()
        );
    }
}
