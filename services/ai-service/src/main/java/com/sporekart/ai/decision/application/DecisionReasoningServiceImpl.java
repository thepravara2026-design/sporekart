package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.DecisionReasoningService;
import com.sporekart.ai.decision.domain.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DecisionReasoningServiceImpl implements DecisionReasoningService {

    @Override
    public DecisionAction resolveDecision(List<DecisionRule> rules, DecisionRequest request) {
        if (rules == null || rules.isEmpty()) return DecisionAction.ALLOW;
        return rules.stream()
            .filter(DecisionRule::isActive)
            .sorted(Comparator.comparingInt(DecisionRule::priority).thenComparingInt(DecisionRule::weight).reversed())
            .map(DecisionRule::action)
            .findFirst()
            .orElse(DecisionAction.ALLOW);
    }

    @Override
    public DecisionAction resolveConflict(List<DecisionAction> conflictingActions, ConflictStrategy strategy) {
        if (conflictingActions == null || conflictingActions.isEmpty()) return DecisionAction.ALLOW;
        return switch (strategy) {
            case DENY_OVERRIDES -> conflictingActions.contains(DecisionAction.DENY) ? DecisionAction.DENY : DecisionAction.ALLOW;
            case ALLOW_OVERRIDES -> conflictingActions.contains(DecisionAction.ALLOW) ? DecisionAction.ALLOW : DecisionAction.DENY;
            case SAFE_DEFAULT -> DecisionAction.DENY;
            case FAIL_CLOSED -> DecisionAction.BLOCK_REQUEST;
            case MOST_RECENT -> conflictingActions.get(conflictingActions.size() - 1);
            default -> conflictingActions.get(0);
        };
    }

    @Override
    public DecisionConfidence calculateConfidence(List<DecisionRule> matchedRules, DecisionAction action) {
        if (matchedRules == null || matchedRules.isEmpty()) return DecisionConfidence.LOW;
        int totalWeight = matchedRules.stream().mapToInt(DecisionRule::weight).sum();
        if (totalWeight >= 100) return DecisionConfidence.CERTAIN;
        if (totalWeight >= 75) return DecisionConfidence.HIGH;
        if (totalWeight >= 50) return DecisionConfidence.MEDIUM;
        if (totalWeight >= 25) return DecisionConfidence.LOW;
        return DecisionConfidence.VERY_LOW;
    }

    @Override
    public List<DecisionReason> buildReasons(DecisionAction action, List<DecisionRule> matchedRules) {
        List<DecisionReason> reasons = new ArrayList<>();
        reasons.add(new DecisionReason(UUID.randomUUID(), "DECISION_" + action.name(),
            "Decision resolved to: " + action.name(), "outcome", DecisionConfidence.HIGH, Map.of()));
        if (matchedRules != null) {
            for (DecisionRule rule : matchedRules) {
                reasons.add(new DecisionReason(UUID.randomUUID(), "RULE_" + rule.name(),
                    "Matched rule: " + rule.name(), "rule", DecisionConfidence.MEDIUM, Map.of("priority", rule.priority())));
            }
        }
        return reasons;
    }

    @Override
    public boolean requiresOverride(DecisionAction action, DecisionRequest request) {
        return action == DecisionAction.REQUIRE_APPROVAL || action == DecisionAction.ESCALATE_TO_ADMIN;
    }
}
