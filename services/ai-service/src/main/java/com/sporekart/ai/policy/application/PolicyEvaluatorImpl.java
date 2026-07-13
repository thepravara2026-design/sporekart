package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyEvaluator;
import com.sporekart.ai.policy.api.PolicyDecisionService;
import com.sporekart.ai.policy.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyEvaluatorImpl implements PolicyEvaluator {

    private final PolicyDecisionService decisionService;

    @Override
    public PolicyEvaluation evaluate(Policy policy, EvaluationRequest request, PolicyContext context) {
        long start = System.currentTimeMillis();
        List<PolicyViolation> violations = evaluateRules(policy.rules(), request);
        PolicyDecision decision = decisionService.decide(request, List.of(
            new PolicyEvaluation(UUID.randomUUID(), request.id(), policy.id(), decision, violations, Map.of(), 0, 0, 0, 0, true, OffsetDateTime.now())
        ));
        long elapsed = System.currentTimeMillis() - start;

        int rulesEvaluated = policy.rules() != null ? policy.rules().size() : 0;
        int rulesPassed = (int) violations.stream().filter(v -> v.severity() == PolicySeverity.INFO).count();
        int rulesFailed = violations.size();

        return new PolicyEvaluation(
            UUID.randomUUID(), request.id(), policy.id(), decision,
            violations, Map.of("policy", policy.name(), "module", request.module()),
            elapsed, rulesEvaluated, rulesPassed, rulesFailed,
            true, OffsetDateTime.now()
        );
    }

    @Override
    public List<PolicyViolation> evaluateRules(List<PolicyRule> rules, EvaluationRequest request) {
        if (rules == null) return List.of();
        return rules.stream()
            .filter(PolicyRule::isActive)
            .sorted(Comparator.comparingInt(PolicyRule::order))
            .map(rule -> evaluateRule(rule, request))
            .filter(Objects::nonNull)
            .toList();
    }

    private PolicyViolation evaluateRule(PolicyRule rule, EvaluationRequest request) {
        if (rule.expression() == null || rule.expression().isBlank()) return null;
        String expr = rule.expression().toLowerCase();
        Map<String, Object> params = rule.parameters() != null ? rule.parameters() : new HashMap<>();

        boolean matched = evaluateExpression(expr, request, params);
        if (matched) {
            return new PolicyViolation(
                UUID.randomUUID(), UUID.randomUUID(), rule.id(), rule.name(),
                "Rule matched: " + rule.name(), PolicySeverity.INFO,
                Map.of("expression", rule.expression(), "decision", rule.decision().name()),
                true, OffsetDateTime.now()
            );
        }
        return null;
    }

    private boolean evaluateExpression(String expression, EvaluationRequest request, Map<String, Object> params) {
        if (expression.contains("module") && request.module() != null) {
            String module = request.module().toLowerCase();
            if (expression.contains(module)) return true;
        }
        if (expression.contains("action") && request.action() != null) {
            String action = request.action().toLowerCase();
            if (expression.contains(action)) return true;
        }
        if (expression.contains("role") && request.roles() != null) {
            for (String role : request.roles()) {
                if (expression.contains(role.toLowerCase())) return true;
            }
        }
        return !params.isEmpty();
    }

    @Override
    public PolicyDecision resolveDecision(List<PolicyEvaluation> evaluations) {
        return decisionService.decide(null, evaluations);
    }

    @Override
    public boolean matches(Policy policy, EvaluationRequest request) {
        if (!policy.isActive()) return false;
        if (policy.module() != null && !policy.module().isBlank()) {
            return policy.module().equalsIgnoreCase(request.module());
        }
        return true;
    }
}
