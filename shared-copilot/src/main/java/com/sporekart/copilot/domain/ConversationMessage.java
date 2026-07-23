package com.sporekart.copilot.domain;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public record ConversationMessage(
    SessionId sessionId,
    MessageRole role,
    String content,
    Map<String, Object> metadata,
    OffsetDateTime timestamp
) {
    public ConversationMessage {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        Objects.requireNonNull(role, "role must not be null");
        Objects.requireNonNull(content, "content must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
        metadata = metadata == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(metadata));
    }

    public static ConversationMessage of(SessionId sessionId, MessageRole role, String content) {
        return new ConversationMessage(sessionId, role, content, Collections.emptyMap(), OffsetDateTime.now());
    }

    public static ConversationMessage of(SessionId sessionId, MessageRole role, String content, Map<String, Object> metadata) {
        return new ConversationMessage(sessionId, role, content, metadata, OffsetDateTime.now());
    }
}
