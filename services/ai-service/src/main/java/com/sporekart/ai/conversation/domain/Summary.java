package com.sporekart.ai.conversation.domain;

import java.time.Instant;
import java.util.Objects;

public final class Summary {

    private final String id;
    private final ConversationId conversationId;
    private final String summary;
    private final String compressionStrategy;
    private final Integer originalTokenCount;
    private final Integer compressedTokenCount;
    private final Integer compressionRatio;
    private final Instant createdAt;

    public Summary(
            String id,
            ConversationId conversationId,
            String summary,
            String compressionStrategy,
            Integer originalTokenCount,
            Integer compressedTokenCount,
            Integer compressionRatio,
            Instant createdAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.summary = summary;
        this.compressionStrategy = compressionStrategy;
        this.originalTokenCount = originalTokenCount;
        this.compressedTokenCount = compressedTokenCount;
        this.compressionRatio = compressionRatio;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public ConversationId getConversationId() {
        return conversationId;
    }

    public String getSummary() {
        return summary;
    }

    public String getCompressionStrategy() {
        return compressionStrategy;
    }

    public Integer getOriginalTokenCount() {
        return originalTokenCount;
    }

    public Integer getCompressedTokenCount() {
        return compressedTokenCount;
    }

    public Integer getCompressionRatio() {
        return compressionRatio;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Summary summary1)) return false;
        return Objects.equals(id, summary1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Summary{" +
                "id='" + id + '\'' +
                ", conversationId=" + conversationId +
                ", compressionStrategy='" + compressionStrategy + '\'' +
                '}';
    }
}
