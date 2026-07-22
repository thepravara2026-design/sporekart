package com.sporekart.ai.conversation.interfaces.rest.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ConversationResponse {
    private final String id;
    private final String title;
    private final String status;
    private final String sessionId;
    private final String workspaceId;
    private final String userId;
    private final List<String> participantIds;
    private final Map<String, String> metadata;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant closedAt;
    private final Integer messageCount;
    private final Integer totalTokenUsage;

    public ConversationResponse(String id, String title, String status, String sessionId, String workspaceId,
                                String userId, List<String> participantIds, Map<String, String> metadata,
                                Instant createdAt, Instant updatedAt, Instant closedAt,
                                Integer messageCount, Integer totalTokenUsage) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.sessionId = sessionId;
        this.workspaceId = workspaceId;
        this.userId = userId;
        this.participantIds = participantIds;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.closedAt = closedAt;
        this.messageCount = messageCount;
        this.totalTokenUsage = totalTokenUsage;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public Integer getTotalTokenUsage() {
        return totalTokenUsage;
    }
}
