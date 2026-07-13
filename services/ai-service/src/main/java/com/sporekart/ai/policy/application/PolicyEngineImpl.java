package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.*;
import com.sporekart.ai.policy.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyEngineImpl implements PolicyEngine {

    private final PolicyResolver resolver;
    private final PolicyEvaluator evaluator;
    private final PolicyDecisionService decisionService;
    private final PolicyAuditService auditService;
    private final PolicyMetricsService metricsService;

    @Override
    public EvaluationResult evaluate(EvaluationRequest request) {
        long start = System.currentTimeMillis();
        List<Policy> policies = resolver.resolvePolicies(request);
        List<PolicyEvaluation> evaluations = new ArrayList<>();
        List<PolicyViolation> allViolations = new ArrayList<>();

        for (Policy policy : policies) {
            if (!policy.isActive() || policy.status() != PolicyStatus.ACTIVE) continue;
            PolicyContext context = buildContext(request, policy);
            if (!evaluator.matches(policy, request)) continue;
            PolicyEvaluation evaluation = evaluator.evaluate(policy, request, context);
            evaluations.add(evaluation);
            allViolations.addAll(evaluation.violations());
        }

        PolicyDecision finalDecision = decisionService.decide(request, evaluations);
        long elapsed = System.currentTimeMillis() - start;

        metricsService.recordEvaluation(elapsed, finalDecision.name());
        allViolations.stream()
            .map(v -> v.severity().name())
            .forEach(metricsService::recordViolation);

        EvaluationResult result = new EvaluationResult(
            request.id(), finalDecision, evaluations, allViolations,
            elapsed, finalDecision == PolicyDecision.ALLOW, OffsetDateTime.now()
        );

        auditService.recordAudit(new PolicyAudit(
            UUID.randomUUID(), null, request.id(), request.action(),
            finalDecision, allViolations, Map.of("module", request.module()),
            request.userId(), elapsed,
            finalDecision == PolicyDecision.ALLOW || finalDecision == PolicyDecision.BYPASS,
            OffsetDateTime.now(), OffsetDateTime.now()
        ));

        log.info("Policy evaluation for request {}: {} ({}ms)", request.id(), finalDecision, elapsed);
        return result;
    }

    @Override
    public EvaluationResult evaluateWithContext(EvaluationRequest request, PolicyContext context) {
        return evaluate(request);
    }

    @Override
    public boolean isAllowed(EvaluationRequest request) {
        return evaluate(request).finalDecision() == PolicyDecision.ALLOW;
    }

    @Override
    public PolicyDecision determineDecision(EvaluationRequest request, List<PolicyViolation> violations) {
        if (violations.isEmpty()) return PolicyDecision.ALLOW;
        boolean hasBlocking = violations.stream().anyMatch(v -> v.severity() == PolicySeverity.BLOCKING);
        boolean hasCritical = violations.stream().anyMatch(v -> v.severity() == PolicySeverity.CRITICAL);
        if (hasBlocking || hasCritical) return PolicyDecision.DENY;
        if (violations.stream().anyMatch(v -> v.severity() == PolicySeverity.ERROR)) return PolicyDecision.REVIEW;
        if (violations.stream().anyMatch(v -> v.severity() == PolicySeverity.WARNING)) return PolicyDecision.LOG;
        return PolicyDecision.ALLOW;
    }

    private PolicyContext buildContext(EvaluationRequest request, Policy policy) {
        return new PolicyContext(
            UUID.randomUUID(), request.id(), request.module(), request.action(),
            policy.scope(), request.payload() != null ? request.payload() : new HashMap<>(),
            Map.of("userId", request.userId(), "roles", request.roles()),
            Map.of("timestamp", OffsetDateTime.now().toString()),
            request.roles(), OffsetDateTime.now()
        );
    }
}
