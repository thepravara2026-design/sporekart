package com.sporekart.ai.conversation.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Message {

    private final MessageId id;
    private final ConversationId conversationId;
    private final MessageType type;
    private final String content;
    private final String role;
    private final Map<String, Object> metadata;
    private final List<Citation> citations;
    private final List<Attachment> attachments;
    private final ToolCall toolCall;
    private final Integer tokenCount;
    private final Integer tokenUsage;
    private final String providerUsed;
    private final Long latencyMs;
    private final Instant createdAt;
    private final Instant updatedAt;

    private String status;

    public Message(
            MessageId id,
            ConversationId conversationId,
            MessageType type,
            String content,
            String role,
            Map<String, Object> metadata,
            List<Citation> citations,
            List<Attachment> attachments,
            ToolCall toolCall,
            Integer tokenCount,
            Integer tokenUsage,
            String providerUsed,
            Long latencyMs,
            String status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.type = type;
        this.content = content;
        this.role = role;
        this.metadata = metadata != null ? Collections.unmodifiableMap(Map.copyOf(metadata)) : Collections.emptyMap();
        this.citations = citations != null ? List.copyOf(citations) : List.of();
        this.attachments = attachments != null ? List.copyOf(attachments) : List.of();
        this.toolCall = toolCall;
        this.tokenCount = tokenCount;
        this.tokenUsage = tokenUsage;
        this.providerUsed = providerUsed;
        this.latencyMs = latencyMs;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public MessageId getId() {
        return id;
    }

    public ConversationId getConversationId() {
        return conversationId;
    }

    public MessageType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getRole() {
        return role;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public List<Citation> getCitations() {
        return citations;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public ToolCall getToolCall() {
        return toolCall;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public Integer getTokenUsage() {
        return tokenUsage;
    }

    public String getProviderUsed() {
        return providerUsed;
    }

    public Long getLatencyMs() {
        return latencyMs;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message)) return false;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", type=" + type +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
