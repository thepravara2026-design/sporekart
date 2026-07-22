package com.sporekart.ai.conversation.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class Conversation {

    private final ConversationId id;
    private final String title;
    private final SessionId sessionId;
    private final WorkspaceId workspaceId;
    private final String userId;
    private final List<String> participantIds;
    private final Map<String, String> metadata;
    private final Set<String> permissions;
    private final Instant createdAt;
    private Instant closedAt;

    private ConversationStatus status;
    private Instant updatedAt;
    private int messageCount;
    private int totalTokenUsage;

    public Conversation(
            ConversationId id,
            String title,
            ConversationStatus status,
            SessionId sessionId,
            WorkspaceId workspaceId,
            String userId,
            List<String> participantIds,
            Map<String, String> metadata,
            Set<String> permissions,
            Instant createdAt,
            Instant updatedAt,
            Instant closedAt,
            int messageCount,
            int totalTokenUsage) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.sessionId = sessionId;
        this.workspaceId = workspaceId;
        this.userId = userId;
        this.participantIds = participantIds != null ? new ArrayList<>(participantIds) : new ArrayList<>();
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        this.permissions = permissions != null ? new HashSet<>(permissions) : new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.closedAt = closedAt;
        this.messageCount = messageCount;
        this.totalTokenUsage = totalTokenUsage;
    }

    public ConversationId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ConversationStatus getStatus() {
        return status;
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public WorkspaceId getWorkspaceId() {
        return workspaceId;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getParticipantIds() {
        return Collections.unmodifiableList(participantIds);
    }

    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
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

    public int getMessageCount() {
        return messageCount;
    }

    public int getTotalTokenUsage() {
        return totalTokenUsage;
    }

    public void close() {
        this.status = ConversationStatus.CLOSED;
        this.closedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void archive() {
        this.status = ConversationStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }

    public void restore() {
        this.status = ConversationStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void delete() {
        this.status = ConversationStatus.DELETED;
        this.updatedAt = Instant.now();
    }

    public void incrementMessageCount() {
        this.messageCount++;
        this.updatedAt = Instant.now();
    }

    public void addTokenUsage(int tokens) {
        this.totalTokenUsage += tokens;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conversation that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", userId='" + userId + '\'' +
                ", messageCount=" + messageCount +
                '}';
    }
}
