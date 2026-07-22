package com.sporekart.ai.conversation.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public final class ContextWindow {

    private final String conversationId;
    private final List<Message> activeMessages;
    private final Integer totalTokens;
    private final Integer maxTokens;
    private final Integer compressionCount;
    private final List<String> strategies;
    private final Instant lastCompressedAt;

    public ContextWindow(
            String conversationId,
            List<Message> activeMessages,
            Integer totalTokens,
            Integer maxTokens,
            Integer compressionCount,
            List<String> strategies,
            Instant lastCompressedAt) {
        this.conversationId = conversationId;
        this.activeMessages = activeMessages != null ? List.copyOf(activeMessages) : List.of();
        this.totalTokens = totalTokens;
        this.maxTokens = maxTokens;
        this.compressionCount = compressionCount;
        this.strategies = strategies != null ? List.copyOf(strategies) : List.of();
        this.lastCompressedAt = lastCompressedAt;
    }

    public String getConversationId() {
        return conversationId;
    }

    public List<Message> getActiveMessages() {
        return activeMessages;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public Integer getCompressionCount() {
        return compressionCount;
    }

    public List<String> getStrategies() {
        return strategies;
    }

    public Instant getLastCompressedAt() {
        return lastCompressedAt;
    }

    @Override
    public String toString() {
        return "ContextWindow{" +
                "conversationId='" + conversationId + '\'' +
                ", totalTokens=" + totalTokens +
                ", maxTokens=" + maxTokens +
                ", compressionCount=" + compressionCount +
                '}';
    }
}
