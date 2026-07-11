package com.sporekart.ai.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ConversationMessage {
    private final UUID id;
    private final String role;
    private final String content;
    private final OffsetDateTime createdAt;

    public ConversationMessage(UUID id, String role, String content, OffsetDateTime createdAt) {
        this.id = id;
        this.role = role;
        this.content = content;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}

