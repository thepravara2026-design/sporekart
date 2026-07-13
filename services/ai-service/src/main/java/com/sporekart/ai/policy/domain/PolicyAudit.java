package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record PolicyAudit(
    UUID id, UUID policyId, UUID requestId, String action,
    PolicyDecision decision, List<PolicyViolation> violations,
    Map<String, Object> details, String userId,
    long processingTimeMs, boolean success,
    OffsetDateTime timestamp, OffsetDateTime createdAt) {}
