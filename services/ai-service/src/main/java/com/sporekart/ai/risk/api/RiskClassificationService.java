package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import java.util.List;
import java.util.Map;

public interface RiskClassificationService {
    RiskCategory classify(RiskAssessment assessment);
    List<RiskCategory> classifyFactors(RiskAssessment assessment);
    Map<RiskCategory, Double> categorizeScores(Map<RiskCategory, Double> scores);
}
