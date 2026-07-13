package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DecisionLifecycle(
    UUID id, UUID decisionId, DecisionStatus fromStatus,
    DecisionStatus toStatus, String triggeredBy,
    String reason, OffsetDateTime timestamp) {}
