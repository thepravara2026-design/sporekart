package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.util.List;

public interface PolicyValidator {
    List<PolicyViolation> validatePolicy(Policy policy);
    List<PolicyViolation> validateRule(PolicyRule rule);
    List<PolicyViolation> validateRequest(EvaluationRequest request);
    boolean isValidPolicy(Policy policy);
    boolean isValidRequest(EvaluationRequest request);
}
