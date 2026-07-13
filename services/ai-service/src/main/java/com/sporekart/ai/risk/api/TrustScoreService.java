package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import java.util.Map;
import java.util.UUID;

public interface TrustScoreService {
    TrustAssessment calculateTrustScore(UUID assessmentId);
    Map<TrustFactor, Double> getFactorDetails(UUID assessmentId);
    double getFactorScore(UUID assessmentId, String factor);
}
