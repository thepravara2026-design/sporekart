package com.sporekart.ai.approval.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_approval_workflows")
public class ApprovalWorkflowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "module", length = 100)
    private String module;

    @Column(name = "allowed_transitions", columnDefinition = "TEXT")
    private String allowedTransitions;

    @Column(name = "max_levels")
    private Integer maxLevels;

    @Column(name = "parallel_enabled")
    private Boolean parallelEnabled;

    @Column(name = "sequential_enabled")
    private Boolean sequentialEnabled;

    @Column(name = "strategy", length = 50)
    private String strategy;

    @Column(name = "sla_minutes")
    private Integer slaMinutes;

    @Column(name = "config", columnDefinition = "TEXT")
    private String config;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public ApprovalWorkflowEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getAllowedTransitions() { return allowedTransitions; }
    public void setAllowedTransitions(String allowedTransitions) { this.allowedTransitions = allowedTransitions; }
    public Integer getMaxLevels() { return maxLevels; }
    public void setMaxLevels(Integer maxLevels) { this.maxLevels = maxLevels; }
    public Boolean getParallelEnabled() { return parallelEnabled; }
    public void setParallelEnabled(Boolean parallelEnabled) { this.parallelEnabled = parallelEnabled; }
    public Boolean getSequentialEnabled() { return sequentialEnabled; }
    public void setSequentialEnabled(Boolean sequentialEnabled) { this.sequentialEnabled = sequentialEnabled; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public Integer getSlaMinutes() { return slaMinutes; }
    public void setSlaMinutes(Integer slaMinutes) { this.slaMinutes = slaMinutes; }
    public String getConfig() { return config; }
    public void setConfig(String config) { this.config = config; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
