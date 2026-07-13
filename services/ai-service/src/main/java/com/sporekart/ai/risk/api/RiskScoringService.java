package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import java.util.UUID;

public interface RiskScoringService {
    RiskScore calculateScore(RiskAssessment assessment);
    RiskScore getScore(UUID assessmentId);
    RiskLevel determineRiskLevel(double score);
}
