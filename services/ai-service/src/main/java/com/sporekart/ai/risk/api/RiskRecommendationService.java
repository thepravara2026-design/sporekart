package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import java.util.List;
import java.util.UUID;

public interface RiskRecommendationService {
    RiskRecommendation generateRecommendation(RiskAssessment assessment, RiskScore score, TrustAssessment trust, ConfidenceScore confidence);
    List<RiskRecommendation> getRecommendations(UUID assessmentId);
    RecommendationType getRecommendedAction(UUID assessmentId);
}
