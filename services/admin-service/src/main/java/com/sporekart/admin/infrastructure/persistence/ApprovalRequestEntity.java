package com.sporekart.admin.infrastructure.persistence;

import com.sporekart.admin.domain.model.ApprovalRequest;
import com.sporekart.admin.domain.model.ApprovalStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "admin_approval_requests")
public class ApprovalRequestEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "target_type", nullable = false, length = 100)
    private String targetType;

    @Column(name = "target_id", nullable = false, length = 100)
    private String targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ApprovalStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ApprovalRequestEntity() {}

    public ApprovalRequestEntity(String id, String targetType, String targetId, ApprovalStatus status, Instant createdAt) {
        this.id = id;
        this.targetType = targetType;
        this.targetId = targetId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static ApprovalRequestEntity fromDomain(ApprovalRequest request) {
        return new ApprovalRequestEntity(
            request.getId(), request.getTargetType(), request.getTargetId(),
            request.getStatus(), request.getCreatedAt());
    }

    public ApprovalRequest toDomain() {
        return new ApprovalRequest(id, targetType, targetId, status, createdAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    public ApprovalStatus getStatus() { return status; }
    public void setStatus(ApprovalStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
