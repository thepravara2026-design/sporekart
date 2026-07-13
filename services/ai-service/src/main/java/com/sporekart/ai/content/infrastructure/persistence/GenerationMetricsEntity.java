package com.sporekart.ai.content.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_generation_metrics")
public class GenerationMetricsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "request_id")
    private UUID requestId;

    @Column(name = "content_type", length = 50)
    private String contentType;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "pipeline_stage", length = 100)
    private String pipelineStage;

    @Column(name = "stage_latency_ms")
    private long stageLatencyMs;

    @Column(name = "total_latency_ms")
    private long totalLatencyMs;

    @Column(name = "token_count")
    private int tokenCount;

    @Column(name = "success")
    private boolean success;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public GenerationMetricsEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPipelineStage() { return pipelineStage; }
    public void setPipelineStage(String pipelineStage) { this.pipelineStage = pipelineStage; }
    public long getStageLatencyMs() { return stageLatencyMs; }
    public void setStageLatencyMs(long stageLatencyMs) { this.stageLatencyMs = stageLatencyMs; }
    public long getTotalLatencyMs() { return totalLatencyMs; }
    public void setTotalLatencyMs(long totalLatencyMs) { this.totalLatencyMs = totalLatencyMs; }
    public int getTokenCount() { return tokenCount; }
    public void setTokenCount(int tokenCount) { this.tokenCount = tokenCount; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
