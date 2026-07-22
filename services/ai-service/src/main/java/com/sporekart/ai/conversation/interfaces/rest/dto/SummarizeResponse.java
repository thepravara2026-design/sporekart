package com.sporekart.ai.conversation.interfaces.rest.dto;

import java.time.Instant;

public class SummarizeResponse {
    private final String summaryId;
    private final String conversationId;
    private final String summary;
    private final String compressionStrategy;
    private final Integer originalTokenCount;
    private final Integer compressedTokenCount;
    private final Integer compressionRatio;
    private final Instant createdAt;

    public SummarizeResponse(String summaryId, String conversationId, String summary,
                             String compressionStrategy, Integer originalTokenCount,
                             Integer compressedTokenCount, Integer compressionRatio, Instant createdAt) {
        this.summaryId = summaryId;
        this.conversationId = conversationId;
        this.summary = summary;
        this.compressionStrategy = compressionStrategy;
        this.originalTokenCount = originalTokenCount;
        this.compressedTokenCount = compressedTokenCount;
        this.compressionRatio = compressionRatio;
        this.createdAt = createdAt;
    }

    public String getSummaryId() {
        return summaryId;
    }

    public String getConversationId() {
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
}
