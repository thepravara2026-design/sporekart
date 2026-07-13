package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyDecisionService;
import com.sporekart.ai.policy.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PolicyDecisionServiceImpl implements PolicyDecisionService {

    @Override
    public PolicyDecision decide(EvaluationRequest request, List<PolicyEvaluation> evaluations) {
        if (evaluations == null || evaluations.isEmpty()) {
            return PolicyDecision.ALLOW;
        }
        boolean anyDeny = evaluations.stream().anyMatch(e -> e.decision() == PolicyDecision.DENY);
        boolean anyBlock = evaluations.stream().anyMatch(e -> e.decision() == PolicyDecision.CHALLENGE);
        if (anyDeny) return PolicyDecision.DENY;
        if (anyBlock) return PolicyDecision.CHALLENGE;
        boolean anyReview = evaluations.stream().anyMatch(e -> e.decision() == PolicyDecision.REVIEW);
        if (anyReview) return PolicyDecision.REVIEW;
        return PolicyDecision.ALLOW;
    }

    @Override
    public PolicyDecision resolveConflict(List<PolicyEvaluation> conflictingEvaluations, ConflictStrategy strategy) {
        if (conflictingEvaluations == null || conflictingEvaluations.isEmpty()) {
            return PolicyDecision.ALLOW;
        }
        return switch (strategy) {
            case DENY_OVERRIDES -> conflictingEvaluations.stream()
                .anyMatch(e -> e.decision() == PolicyDecision.DENY) ? PolicyDecision.DENY : PolicyDecision.ALLOW;
            case ALLOW_OVERRIDES -> conflictingEvaluations.stream()
                .anyMatch(e -> e.decision() == PolicyDecision.ALLOW) ? PolicyDecision.ALLOW : PolicyDecision.DENY;
            default -> PolicyDecision.REVIEW;
        };
    }

    @Override
    public boolean isAllowed(PolicyDecision decision) {
        return decision == PolicyDecision.ALLOW || decision == PolicyDecision.BYPASS;
    }

    @Override
    public boolean requiresReview(PolicyDecision decision) {
        return decision == PolicyDecision.REVIEW || decision == PolicyDecision.CHALLENGE;
    }
}
