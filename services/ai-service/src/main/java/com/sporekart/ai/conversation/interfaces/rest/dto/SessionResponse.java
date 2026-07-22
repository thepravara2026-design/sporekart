package com.sporekart.ai.conversation.interfaces.rest.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class SessionResponse {
    private final String id;
    private final String userId;
    private final String workspaceId;
    private final String status;
    private final Map<String, String> metadata;
    private final List<String> conversationIds;
    private final Instant createdAt;
    private final Instant lastActivityAt;
    private final Instant expiresAt;

    public SessionResponse(String id, String userId, String workspaceId, String status,
                           Map<String, String> metadata, List<String> conversationIds,
                           Instant createdAt, Instant lastActivityAt, Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.workspaceId = workspaceId;
        this.status = status;
        this.metadata = metadata;
        this.conversationIds = conversationIds;
        this.createdAt = createdAt;
        this.lastActivityAt = lastActivityAt;
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public List<String> getConversationIds() {
        return conversationIds;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastActivityAt() {
        return lastActivityAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
