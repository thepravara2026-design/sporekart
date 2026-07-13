package com.sporekart.ai.risk.engine;

import com.sporekart.ai.risk.domain.*;
import java.util.Map;
import java.util.UUID;

public record RiskResult(
    boolean proceed,
    RiskLevel riskLevel,
    double riskScore,
    double trustScore,
    double confidenceScore,
    RiskRecommendation recommendation,
    Map<String, Object> details,
    UUID assessmentId
) {}
