package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record RiskEvidence(
    UUID id,
    UUID assessmentId,
    String evidenceType,
    String source,
    Map<String, Object> data,
    Instant collectedAt
) {}
