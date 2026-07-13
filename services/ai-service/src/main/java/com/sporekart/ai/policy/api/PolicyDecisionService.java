package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.util.List;

public interface PolicyDecisionService {
    PolicyDecision decide(EvaluationRequest request, List<PolicyEvaluation> evaluations);
    PolicyDecision resolveConflict(List<PolicyEvaluation> conflictingEvaluations, ConflictStrategy strategy);
    boolean isAllowed(PolicyDecision decision);
    boolean requiresReview(PolicyDecision decision);
}
