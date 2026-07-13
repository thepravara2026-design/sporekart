package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApprovalDelegation(
    UUID id,
    UUID requestId,
    UUID fromReviewerId,
    UUID toReviewerId,
    String reason,
    boolean isActive,
    OffsetDateTime delegatedAt,
    OffsetDateTime expiresAt
) {}
