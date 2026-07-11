package com.sporekart.ai.chat.domain;

import com.sporekart.ai.core.domain.AiRole;
import java.time.OffsetDateTime;
import java.util.Map;

public record ConversationMessage(
        String id,
        String conversationId,
        AiRole role,
        String content,
        OffsetDateTime timestamp,
        Map<String, Object> metadata) {
    public ConversationMessage(AiRole role, String content) {
        this(null, null, role, content, OffsetDateTime.now(), Map.of());
    }
}
