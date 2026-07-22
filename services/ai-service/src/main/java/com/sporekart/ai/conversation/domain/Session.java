package com.sporekart.ai.conversation.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Session {

    private final SessionId id;
    private final String userId;
    private final WorkspaceId workspaceId;
    private final Map<String, String> metadata;
    private final List<ConversationId> conversationIds;
    private final Instant createdAt;
    private final Instant expiresAt;
    private final Duration idleTimeout;

    private String status;
    private Instant lastActivityAt;

    public Session(
            SessionId id,
            String userId,
            WorkspaceId workspaceId,
            String status,
            Map<String, String> metadata,
            List<ConversationId> conversationIds,
            Instant createdAt,
            Instant lastActivityAt,
            Instant expiresAt,
            Duration idleTimeout) {
        this.id = id;
        this.userId = userId;
        this.workspaceId = workspaceId;
        this.status = status;
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        this.conversationIds = conversationIds != null ? new ArrayList<>(conversationIds) : new ArrayList<>();
        this.createdAt = createdAt;
        this.lastActivityAt = lastActivityAt;
        this.expiresAt = expiresAt;
        this.idleTimeout = idleTimeout;
    }

    public SessionId getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public WorkspaceId getWorkspaceId() {
        return workspaceId;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    public List<ConversationId> getConversationIds() {
        return Collections.unmodifiableList(conversationIds);
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

    public Duration getIdleTimeout() {
        return idleTimeout;
    }

    public void recordActivity() {
        this.lastActivityAt = Instant.now();
    }

    public boolean isExpired() {
        if (expiresAt == null) {
            return false;
        }
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isIdle() {
        if (lastActivityAt == null || idleTimeout == null) {
            return false;
        }
        return Duration.between(lastActivityAt, Instant.now()).compareTo(idleTimeout) > 0;
    }

    public void addConversation(ConversationId conversationId) {
        if (conversationId != null && !this.conversationIds.contains(conversationId)) {
            this.conversationIds.add(conversationId);
        }
    }

    public void close() {
        this.status = "closed";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session session)) return false;
        return Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", lastActivityAt=" + lastActivityAt +
                '}';
    }
}
