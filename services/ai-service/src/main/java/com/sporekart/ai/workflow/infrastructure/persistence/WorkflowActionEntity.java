package com.sporekart.ai.workflow.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_workflow_actions")
public class WorkflowActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "workflow_id", nullable = false)
    private UUID workflowId;

    @Column(name = "step_id")
    private UUID stepId;

    @Column(name = "action_type", nullable = false, length = 50)
    private String actionType;

    @Column(name = "config", columnDefinition = "TEXT")
    private String config;

    @Column(name = "on_success", length = 100)
    private String onSuccess;

    @Column(name = "on_failure", length = 100)
    private String onFailure;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public WorkflowActionEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getWorkflowId() { return workflowId; }
    public void setWorkflowId(UUID workflowId) { this.workflowId = workflowId; }
    public UUID getStepId() { return stepId; }
    public void setStepId(UUID stepId) { this.stepId = stepId; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getConfig() { return config; }
    public void setConfig(String config) { this.config = config; }
    public String getOnSuccess() { return onSuccess; }
    public void setOnSuccess(String onSuccess) { this.onSuccess = onSuccess; }
    public String getOnFailure() { return onFailure; }
    public void setOnFailure(String onFailure) { this.onFailure = onFailure; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}
