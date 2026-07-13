package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import java.util.UUID;

public interface TrustEngine {
    TrustAssessment calculateTrust(RiskAssessment assessment);
    TrustAssessment recalculateTrust(UUID assessmentId);
    double getTrustScore(UUID assessmentId);
}
