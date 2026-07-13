package com.sporekart.ai.risk.engine;

import com.sporekart.ai.risk.domain.*;
import java.util.Map;
import java.util.UUID;

public record RiskAssessmentResult(
    UUID assessmentId,
    boolean proceed,
    RiskLevel riskLevel,
    double riskScore,
    double trustScore,
    double confidenceScore,
    RiskRecommendation recommendation,
    Map<String, Object> details
) {

    public UUID id() {
        return assessmentId;
    }

    public double overallScore() {
        return riskScore;
    }
}
