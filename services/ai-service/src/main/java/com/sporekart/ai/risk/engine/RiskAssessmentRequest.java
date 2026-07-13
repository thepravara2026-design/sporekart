package com.sporekart.ai.risk.engine;

import java.util.Map;
import java.util.UUID;

public record RiskAssessmentRequest(
    UUID assessmentId,
    String module,
    String action,
    Map<String, Object> context
) {}
