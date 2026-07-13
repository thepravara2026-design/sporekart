package com.sporekart.ai.usagetracking.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "ut_usage_record")
public class UsageRecordEntity {

    @Id
    @Column(name = "usage_id", nullable = false, length = 64)
    private String usageId;

    @Column(name = "request_id", length = 64)
    private String requestId;

    @Column(name = "provider_id", length = 128)
    private String providerId;

    @Column(name = "model_id", length = 128)
    private String modelId;

    @Column(name = "prompt_tokens")
    private int promptTokens;

    @Column(name = "completion_tokens")
    private int completionTokens;

    @Column(name = "total_tokens")
    private int totalTokens;

    @Column(name = "execution_time_ms")
    private long executionTimeMs;

    @Column(name = "success")
    private boolean success;

    @Column(name = "failure_reason", length = 1024)
    private String failureReason;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "session_id", length = 64)
    private String sessionId;

    @Column(name = "module", length = 128)
    private String module;

    public UsageRecordEntity() {
    }

    public UsageRecordEntity(String usageId, String requestId, String providerId, String modelId,
                             int promptTokens, int completionTokens, int totalTokens, long executionTimeMs,
                             boolean success, String failureReason, Instant timestamp, String userId,
                             String sessionId, String module) {
        this.usageId = usageId;
        this.requestId = requestId;
        this.providerId = providerId;
        this.modelId = modelId;
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
        this.executionTimeMs = executionTimeMs;
        this.success = success;
        this.failureReason = failureReason;
        this.timestamp = timestamp;
        this.userId = userId;
        this.sessionId = sessionId;
        this.module = module;
    }

    public String getUsageId() {
        return usageId;
    }

    public void setUsageId(String usageId) {
        this.usageId = usageId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
