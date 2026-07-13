package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GovernanceResponse(
    UUID id,
    UUID requestId,
    GovernanceDecision decision,
    List<GovernanceViolation> violations,
    Map<String, Object> context,
    long processingTimeMs,
    OffsetDateTime timestamp) {
}
