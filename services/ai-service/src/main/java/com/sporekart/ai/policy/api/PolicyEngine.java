package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.util.List;

public interface PolicyEngine {
    EvaluationResult evaluate(EvaluationRequest request);
    EvaluationResult evaluateWithContext(EvaluationRequest request, PolicyContext context);
    boolean isAllowed(EvaluationRequest request);
    PolicyDecision determineDecision(EvaluationRequest request, List<PolicyViolation> violations);
}
