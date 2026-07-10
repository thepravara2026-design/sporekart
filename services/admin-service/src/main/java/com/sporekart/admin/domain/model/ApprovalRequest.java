package com.sporekart.admin.domain.model;

import java.time.Instant;
import java.util.UUID;

public class ApprovalRequest {
    private final String id;
    private final String targetType;
    private final String targetId;
    private final ApprovalStatus status;
    private final Instant createdAt;

    public ApprovalRequest(String id, String targetType, String targetId, ApprovalStatus status, Instant createdAt) {
        this.id = id;
        this.targetType = targetType;
        this.targetId = targetId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static ApprovalRequest create(String targetType, String targetId) {
        return new ApprovalRequest(UUID.randomUUID().toString(), targetType, targetId, ApprovalStatus.PENDING,
                Instant.now());
    }

    public ApprovalRequest approve() {
        return new ApprovalRequest(id, targetType, targetId, ApprovalStatus.APPROVED, createdAt);
    }

    public ApprovalRequest reject() {
        return new ApprovalRequest(id, targetType, targetId, ApprovalStatus.REJECTED, createdAt);
    }

    public String getId() {
        return id;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
