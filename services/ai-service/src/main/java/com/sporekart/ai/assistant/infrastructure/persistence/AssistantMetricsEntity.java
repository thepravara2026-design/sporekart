package com.sporekart.ai.assistant.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_assistant_metrics")
public class AssistantMetricsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "assistant_id", nullable = false)
    private UUID assistantId;

    @Column(name = "total_requests")
    private Long totalRequests;

    @Column(name = "successful_requests")
    private Long successfulRequests;

    @Column(name = "failed_requests")
    private Long failedRequests;

    @Column(name = "avg_latency_ms")
    private Double avgLatencyMs;

    @Column(name = "intent_accuracy")
    private Double intentAccuracy;

    @Column(name = "tasks_created")
    private Long tasksCreated;

    @Column(name = "tasks_completed")
    private Long tasksCompleted;

    @Column(name = "tasks_failed")
    private Long tasksFailed;

    @Column(name = "recorded_at")
    private OffsetDateTime recordedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public AssistantMetricsEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getAssistantId() { return assistantId; }
    public void setAssistantId(UUID assistantId) { this.assistantId = assistantId; }
    public Long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(Long totalRequests) { this.totalRequests = totalRequests; }
    public Long getSuccessfulRequests() { return successfulRequests; }
    public void setSuccessfulRequests(Long successfulRequests) { this.successfulRequests = successfulRequests; }
    public Long getFailedRequests() { return failedRequests; }
    public void setFailedRequests(Long failedRequests) { this.failedRequests = failedRequests; }
    public Double getAvgLatencyMs() { return avgLatencyMs; }
    public void setAvgLatencyMs(Double avgLatencyMs) { this.avgLatencyMs = avgLatencyMs; }
    public Double getIntentAccuracy() { return intentAccuracy; }
    public void setIntentAccuracy(Double intentAccuracy) { this.intentAccuracy = intentAccuracy; }
    public Long getTasksCreated() { return tasksCreated; }
    public void setTasksCreated(Long tasksCreated) { this.tasksCreated = tasksCreated; }
    public Long getTasksCompleted() { return tasksCompleted; }
    public void setTasksCompleted(Long tasksCompleted) { this.tasksCompleted = tasksCompleted; }
    public Long getTasksFailed() { return tasksFailed; }
    public void setTasksFailed(Long tasksFailed) { this.tasksFailed = tasksFailed; }
    public OffsetDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(OffsetDateTime recordedAt) { this.recordedAt = recordedAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
