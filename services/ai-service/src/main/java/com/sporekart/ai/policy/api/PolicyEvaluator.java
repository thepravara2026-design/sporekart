package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.util.List;

public interface PolicyEvaluator {
    PolicyEvaluation evaluate(Policy policy, EvaluationRequest request, PolicyContext context);
    List<PolicyViolation> evaluateRules(List<PolicyRule> rules, EvaluationRequest request);
    PolicyDecision resolveDecision(List<PolicyEvaluation> evaluations);
    boolean matches(Policy policy, EvaluationRequest request);
}
