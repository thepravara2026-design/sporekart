package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ApprovalAudit(
    UUID id,
    UUID requestId,
    UUID reviewerId,
    String action,
    ApprovalDecision decision,
    ApprovalStatus status,
    Map<String, Object> details,
    String userId,
    long processingTimeMs,
    boolean success,
    OffsetDateTime timestamp,
    OffsetDateTime createdAt
) {}
