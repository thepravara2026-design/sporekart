package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GovernanceAudit(
    UUID id,
    UUID requestId,
    String action,
    String module,
    GovernanceDecision decision,
    List<GovernanceViolation> violations,
    Map<String, Object> context,
    String userId,
    long processingTimeMs,
    boolean success,
    OffsetDateTime timestamp,
    OffsetDateTime createdAt) {
}
