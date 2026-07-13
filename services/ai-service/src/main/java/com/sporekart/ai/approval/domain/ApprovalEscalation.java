package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApprovalEscalation(
    UUID id,
    UUID requestId,
    UUID fromReviewerId,
    UUID toReviewerId,
    EscalationReason reason,
    String details,
    int level,
    OffsetDateTime escalatedAt,
    OffsetDateTime resolvedAt
) {}
