package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.engine.*;
import java.util.Map;
import java.util.UUID;

public interface RiskEngine {
    RiskAssessmentResult assess(RiskAssessmentRequest request);
    RiskAssessmentResult reassess(UUID assessmentId, Map<String, Object> context);
}
