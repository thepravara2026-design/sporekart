package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApprovalLifecycle(
    UUID id,
    UUID requestId,
    ApprovalStatus fromStatus,
    ApprovalStatus toStatus,
    String triggeredBy,
    String reason,
    OffsetDateTime timestamp
) {}
