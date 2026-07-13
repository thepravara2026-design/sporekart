package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApprovalAssignment(
    UUID id,
    UUID requestId,
    UUID reviewerId,
    String reviewerUserId,
    AssignmentStrategy strategy,
    int level,
    ApprovalStatus status,
    OffsetDateTime assignedAt,
    OffsetDateTime respondedAt,
    OffsetDateTime deadline
) {}
