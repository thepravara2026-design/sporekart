package com.sporekart.ai.conversation.interfaces.rest.dto;

import com.sporekart.ai.conversation.domain.ConversationMessage;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ConversationMessageResponse(
    UUID id,
    UUID sessionId,
    String role,
    String content,
    String status,
    OffsetDateTime createdAt
) {
    public static ConversationMessageResponse from(ConversationMessage message) {
        return new ConversationMessageResponse(
            message.id(),
            message.sessionId(),
            message.role().name(),
            message.content(),
            message.status().name(),
            message.createdAt()
        );
    }
}
