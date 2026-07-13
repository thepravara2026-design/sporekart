package com.sporekart.ai.policy.engine;

import com.sporekart.ai.policy.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RuleEngine {

    public RuleMatchResult match(PolicyRule rule, EvaluationRequest request, PolicyContext context) {
        if (!rule.isActive()) return new RuleMatchResult(rule.id(), false, 0, "Rule is inactive");
        if (rule.expression() == null || rule.expression().isBlank()) return new RuleMatchResult(rule.id(), false, 0, "No expression");

        int score = 0;
        List<String> matchedConditions = new ArrayList<>();

        if (rule.expression().toLowerCase().contains(request.module().toLowerCase())) {
            score += 10;
            matchedConditions.add("module");
        }
        if (rule.expression().toLowerCase().contains(request.action().toLowerCase())) {
            score += 10;
            matchedConditions.add("action");
        }
        if (request.roles() != null) {
            for (String role : request.roles()) {
                if (rule.expression().toLowerCase().contains(role.toLowerCase())) {
                    score += 5;
                    matchedConditions.add("role:" + role);
                }
            }
        }
        if (rule.parameters() != null && !rule.parameters().isEmpty()) {
            for (Map.Entry<String, Object> param : rule.parameters().entrySet()) {
                if (request.payload() != null && request.payload().containsKey(param.getKey())) {
                    Object val = request.payload().get(param.getKey());
                    if (param.getValue().equals(val) || param.getValue().toString().equalsIgnoreCase(val.toString())) {
                        score += 15;
                        matchedConditions.add("param:" + param.getKey());
                    }
                }
            }
        }

        boolean matched = score > 0;
        return new RuleMatchResult(rule.id(), matched, score, matched ? String.join(",", matchedConditions) : "No match");
    }

    public List<PolicyRule> resolveConflict(List<PolicyRule> conflictingRules, ConflictStrategy strategy) {
        if (conflictingRules == null || conflictingRules.isEmpty()) return List.of();
        return switch (strategy) {
            case HIGHEST_PRIORITY_WINS -> conflictingRules.stream()
                .min(Comparator.comparingInt(PolicyRule::order)).stream().toList();
            case LOWEST_PRIORITY_WINS -> conflictingRules.stream()
                .max(Comparator.comparingInt(PolicyRule::order)).stream().toList();
            case DENY_OVERRIDES -> conflictingRules.stream()
                .filter(r -> r.decision() == PolicyDecision.DENY)
                .findFirst().stream().toList();
            case ALLOW_OVERRIDES -> conflictingRules.stream()
                .filter(r -> r.decision() == PolicyDecision.ALLOW)
                .findFirst().stream().toList();
            default -> List.of(conflictingRules.get(0));
        };
    }

    public List<Policy> filterApplicable(List<Policy> policies, EvaluationRequest request) {
        return policies.stream()
            .filter(p -> p.isActive() && p.status() == PolicyStatus.ACTIVE)
            .filter(p -> p.module() == null || p.module().isBlank() || p.module().equalsIgnoreCase(request.module()))
            .sorted(Comparator.comparingInt(Policy::priority).reversed())
            .toList();
    }

    public record RuleMatchResult(UUID ruleId, boolean matched, int score, String details) {}
}
