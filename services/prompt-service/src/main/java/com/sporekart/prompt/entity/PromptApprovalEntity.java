package com.sporekart.prompt.entity;

import com.sporekart.prompt.domain.ApprovalStatus;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "prompt_approvals")
public class PromptApprovalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "version_id", nullable = false)
    private UUID versionId;

    @Column(name = "template_id", nullable = false)
    private UUID templateId;

    @Column(name = "approver", nullable = false)
    private UUID approver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ApprovalStatus status;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "approved_at")
    private OffsetDateTime approvedAt;

    @Column(name = "requested_at", nullable = false)
    private OffsetDateTime requestedAt;

    @Column(name = "step", nullable = false, length = 50)
    private String step;

    public PromptApprovalEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getVersionId() { return versionId; }
    public void setVersionId(UUID versionId) { this.versionId = versionId; }
    public UUID getTemplateId() { return templateId; }
    public void setTemplateId(UUID templateId) { this.templateId = templateId; }
    public UUID getApprover() { return approver; }
    public void setApprover(UUID approver) { this.approver = approver; }
    public ApprovalStatus getStatus() { return status; }
    public void setStatus(ApprovalStatus status) { this.status = status; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public OffsetDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(OffsetDateTime approvedAt) { this.approvedAt = approvedAt; }
    public OffsetDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(OffsetDateTime requestedAt) { this.requestedAt = requestedAt; }
    public String getStep() { return step; }
    public void setStep(String step) { this.step = step; }
}
