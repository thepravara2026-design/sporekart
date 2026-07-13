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
@Table(name = "ai_approval_escalations")
public class ApprovalEscalationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "request_id")
    private UUID requestId;

    @Column(name = "from_reviewer_id")
    private UUID fromReviewerId;

    @Column(name = "to_reviewer_id")
    private UUID toReviewerId;

    @Column(name = "reason", length = 50)
    private String reason;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "level")
    private Integer level;

    @Column(name = "escalated_at")
    private OffsetDateTime escalatedAt;

    @Column(name = "resolved_at")
    private OffsetDateTime resolvedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public ApprovalEscalationEntity() {}

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
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public OffsetDateTime getEscalatedAt() { return escalatedAt; }
    public void setEscalatedAt(OffsetDateTime escalatedAt) { this.escalatedAt = escalatedAt; }
    public OffsetDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(OffsetDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
