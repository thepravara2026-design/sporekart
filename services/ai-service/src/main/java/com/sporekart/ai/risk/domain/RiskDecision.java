package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.UUID;

public record RiskDecision(
    UUID id,
    UUID assessmentId,
    RecommendationType decision,
    String reason,
    UUID decidedBy,
    Instant decidedAt
) {}
