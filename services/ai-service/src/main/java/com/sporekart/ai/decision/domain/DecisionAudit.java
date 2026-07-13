package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record DecisionAudit(
    UUID id, UUID requestId, UUID decisionId, DecisionAction action,
    DecisionStatus status, DecisionConfidence confidence,
    List<DecisionReason> reasons, Map<String, Object> context,
    String userId, long processingTimeMs, boolean success,
    OffsetDateTime timestamp, OffsetDateTime createdAt) {}
