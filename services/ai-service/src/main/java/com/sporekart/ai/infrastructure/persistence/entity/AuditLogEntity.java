package com.sporekart.ai.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_audit_logs")
public class AuditLogEntity {
    @Id
    private UUID id;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "details")
    private String details;

    @Column(name = "performed_by")
    private UUID performedBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public AuditLogEntity() {
    }

    public AuditLogEntity(UUID id, String entityType, String entityId, String action, String details) {
        this(id, entityType, entityId, action, details, null, OffsetDateTime.now());
    }

    public AuditLogEntity(UUID id, String entityType, String entityId, String action, String details,
            UUID performedBy, OffsetDateTime createdAt) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.details = details;
        this.performedBy = performedBy;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    public UUID getPerformedBy() {
        return performedBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
