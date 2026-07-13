package com.sporekart.ai.risk.domain;

import java.util.Map;
import java.util.UUID;

public record RiskFactor(
    UUID id,
    UUID assessmentId,
    String name,
    RiskCategory category,
    double weight,
    double score,
    Map<String, Object> evidence
) {}
