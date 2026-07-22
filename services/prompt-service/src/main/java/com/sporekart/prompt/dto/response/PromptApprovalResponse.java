package com.sporekart.prompt.dto.response;

import com.sporekart.prompt.domain.ApprovalStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PromptApprovalResponse(
        UUID id,
        UUID versionId,
        UUID templateId,
        UUID approver,
        ApprovalStatus status,
        String comments,
        OffsetDateTime approvedAt,
        OffsetDateTime requestedAt,
        String step
) {
    public static PromptApprovalResponse from(
            UUID id, UUID versionId, UUID templateId, UUID approver,
            ApprovalStatus status, String comments, OffsetDateTime approvedAt,
            OffsetDateTime requestedAt, String step) {
        return new PromptApprovalResponse(id, versionId, templateId, approver,
                status, comments, approvedAt, requestedAt, step);
    }
}
