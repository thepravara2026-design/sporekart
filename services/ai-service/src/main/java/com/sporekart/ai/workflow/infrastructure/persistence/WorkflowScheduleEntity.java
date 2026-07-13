package com.sporekart.ai.workflow.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_workflow_schedules")
public class WorkflowScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "workflow_id", nullable = false)
    private UUID workflowId;

    @Column(name = "cron_expression", nullable = false, length = 255)
    private String cronExpression;

    @Column(name = "start_at")
    private OffsetDateTime startAt;

    @Column(name = "end_at")
    private OffsetDateTime endAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "timezone", length = 100)
    private String timezone;

    @Column(name = "last_executed_at")
    private OffsetDateTime lastExecutedAt;

    @Column(name = "next_execution_at")
    private OffsetDateTime nextExecutionAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public WorkflowScheduleEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getWorkflowId() { return workflowId; }
    public void setWorkflowId(UUID workflowId) { this.workflowId = workflowId; }
    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    public OffsetDateTime getStartAt() { return startAt; }
    public void setStartAt(OffsetDateTime startAt) { this.startAt = startAt; }
    public OffsetDateTime getEndAt() { return endAt; }
    public void setEndAt(OffsetDateTime endAt) { this.endAt = endAt; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public OffsetDateTime getLastExecutedAt() { return lastExecutedAt; }
    public void setLastExecutedAt(OffsetDateTime lastExecutedAt) { this.lastExecutedAt = lastExecutedAt; }
    public OffsetDateTime getNextExecutionAt() { return nextExecutionAt; }
    public void setNextExecutionAt(OffsetDateTime nextExecutionAt) { this.nextExecutionAt = nextExecutionAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}
