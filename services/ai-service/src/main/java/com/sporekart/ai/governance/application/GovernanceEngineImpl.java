package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.*;
import com.sporekart.ai.governance.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GovernanceEngineImpl implements GovernanceEngine {

    private final GovernanceContextResolver contextResolver;
    private final GovernanceValidator validator;
    private final GovernanceAuditService auditService;
    private final GovernanceManager manager;

    @Override
    public GovernanceResponse validate(GovernanceRequest request) {
        long start = System.currentTimeMillis();
        GovernanceContext context = contextResolver.resolveContext(request);
        return validateWithContext(request, context);
    }

    @Override
    public GovernanceResponse validateWithContext(GovernanceRequest request, GovernanceContext context) {
        long start = System.currentTimeMillis();
        List<GovernanceViolation> violations = validator.validateRequest(request);
        GovernanceDecision decision = decide(request, violations);
        long elapsed = System.currentTimeMillis() - start;
        GovernanceResponse response = new GovernanceResponse(
            UUID.randomUUID(), request.id(), decision, violations,
            Map.of("context", context, "module", request.module()),
            elapsed, OffsetDateTime.now()
        );
        auditService.recordAudit(new GovernanceAudit(
            UUID.randomUUID(), request.id(), request.action(), request.module(),
            decision, violations, Map.of(), request.userId(), elapsed,
            decision == GovernanceDecision.ALLOW || decision == GovernanceDecision.LOG ||
            decision == GovernanceDecision.BYPASS, OffsetDateTime.now(), OffsetDateTime.now()
        ));
        log.info("Governance validation for request {}: {}", request.id(), decision);
        return response;
    }

    @Override
    public List<GovernanceViolation> checkPolicy(GovernancePolicy policy, GovernanceRequest request) {
        List<GovernanceViolation> violations = new ArrayList<>();
        if (!policy.isActive() || policy.status() != GovernanceStatus.ACTIVE) {
            violations.add(new GovernanceViolation(
                UUID.randomUUID(), "policy_inactive", "Policy is not active",
                GovernanceSeverity.WARNING, Map.of("policyId", policy.id()), true, OffsetDateTime.now()
            ));
        }
        if (policy.scope() != GovernanceScope.ALL && !request.module().equalsIgnoreCase(policy.scope().name())) {
            violations.add(new GovernanceViolation(
                UUID.randomUUID(), "scope_mismatch", "Request module does not match policy scope",
                GovernanceSeverity.INFO, Map.of("expected", policy.scope(), "actual", request.module()), true, OffsetDateTime.now()
            ));
        }
        return violations;
    }

    @Override
    public GovernanceDecision decide(GovernanceRequest request, List<GovernanceViolation> violations) {
        if (violations == null || violations.isEmpty()) {
            return GovernanceDecision.ALLOW;
        }
        boolean hasCritical = violations.stream().anyMatch(v -> v.severity() == GovernanceSeverity.CRITICAL);
        boolean hasError = violations.stream().anyMatch(v -> v.severity() == GovernanceSeverity.ERROR);
        if (hasCritical) return GovernanceDecision.DENY;
        if (hasError) return GovernanceDecision.REVIEW;
        if (violations.stream().anyMatch(v -> v.severity() == GovernanceSeverity.WARNING)) {
            return GovernanceDecision.LOG;
        }
        return GovernanceDecision.ALLOW;
    }

    @Override
    public boolean isAllowed(GovernanceRequest request) {
        return validate(request).decision() == GovernanceDecision.ALLOW;
    }
}
