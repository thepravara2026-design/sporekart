package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import java.util.UUID;

public interface ConfidenceCalculator {
    ConfidenceScore calculateConfidence(RiskAssessment assessment);
    ConfidenceScore recalculateConfidence(UUID assessmentId);
    double getConfidenceScore(UUID assessmentId);
}
