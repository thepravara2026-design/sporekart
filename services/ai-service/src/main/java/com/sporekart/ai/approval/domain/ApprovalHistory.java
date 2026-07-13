package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ApprovalHistory(
    UUID id,
    UUID requestId,
    UUID reviewerId,
    ApprovalDecision decision,
    String comment,
    Map<String, Object> details,
    OffsetDateTime timestamp
) {}
