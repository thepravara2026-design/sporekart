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
@Table(name = "ai_approval_delegations")
public class ApprovalDelegationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "request_id")
    private UUID requestId;

    @Column(name = "from_reviewer_id")
    private UUID fromReviewerId;

    @Column(name = "to_reviewer_id")
    private UUID toReviewerId;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "delegated_at")
    private OffsetDateTime delegatedAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public ApprovalDelegationEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }
    public UUID getFromReviewerId() { return fromReviewerId; }
    public void setFromReviewerId(UUID fromReviewerId) { this.fromReviewerId = fromReviewerId; }
    public UUID getToReviewerId() { return toReviewerId; }
    public void setToReviewerId(UUID toReviewerId) { this.toReviewerId = toReviewerId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public OffsetDateTime getDelegatedAt() { return delegatedAt; }
    public void setDelegatedAt(OffsetDateTime delegatedAt) { this.delegatedAt = delegatedAt; }
    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(OffsetDateTime expiresAt) { this.expiresAt = expiresAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
