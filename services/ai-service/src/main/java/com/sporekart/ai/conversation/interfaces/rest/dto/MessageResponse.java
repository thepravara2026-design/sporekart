package com.sporekart.ai.conversation.interfaces.rest.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class MessageResponse {
    private final String id;
    private final String conversationId;
    private final String type;
    private final String content;
    private final String role;
    private final Map<String, Object> metadata;
    private final List<CitationResponse> citations;
    private final List<AttachmentResponse> attachments;
    private final ToolCallResponse toolCall;
    private final Integer tokenCount;
    private final Integer tokenUsage;
    private final String providerUsed;
    private final Long latencyMs;
    private final String status;
    private final Instant createdAt;

    public MessageResponse(String id, String conversationId, String type, String content, String role,
                           Map<String, Object> metadata, List<CitationResponse> citations,
                           List<AttachmentResponse> attachments, ToolCallResponse toolCall,
                           Integer tokenCount, Integer tokenUsage, String providerUsed,
                           Long latencyMs, String status, Instant createdAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.type = type;
        this.content = content;
        this.role = role;
        this.metadata = metadata;
        this.citations = citations;
        this.attachments = attachments;
        this.toolCall = toolCall;
        this.tokenCount = tokenCount;
        this.tokenUsage = tokenUsage;
        this.providerUsed = providerUsed;
        this.latencyMs = latencyMs;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getType() {
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

    public List<CitationResponse> getCitations() {
        return citations;
    }

    public List<AttachmentResponse> getAttachments() {
        return attachments;
    }

    public ToolCallResponse getToolCall() {
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
}
