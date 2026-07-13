package com.sporekart.ai.policy.application;
import com.sporekart.ai.policy.api.*;
import com.sporekart.ai.policy.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyEngineImplTest {
    @Mock private PolicyResolver resolver;
    @Mock private PolicyEvaluator evaluator;
    @Mock private PolicyDecisionService decisionService;
    @Mock private PolicyAuditService auditService;
    @Mock private PolicyMetricsService metricsService;
    private PolicyEngineImpl engine;

    @BeforeEach
    void setUp() { engine = new PolicyEngineImpl(resolver, evaluator, decisionService, auditService, metricsService); }

    @Test
    void testEvaluate_AllowsWhenNoPolicies() {
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "generate",
            Map.of(), Map.of(), "user1", List.of(), Map.of(), OffsetDateTime.now());
        when(resolver.resolvePolicies(req)).thenReturn(List.of());
        when(decisionService.decide(any(), any())).thenReturn(PolicyDecision.ALLOW);
        when(auditService.recordAudit(any())).thenReturn(null);

        EvaluationResult result = engine.evaluate(req);
        assertEquals(PolicyDecision.ALLOW, result.finalDecision());
    }

    @Test
    void testEvaluate_DeniesWithViolations() {
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "generate",
            Map.of(), Map.of(), "user1", List.of(), Map.of(), OffsetDateTime.now());
        Policy policy = new Policy(UUID.randomUUID(), "test", "desc", PolicyType.MODULE, PolicyStatus.ACTIVE,
            PolicySeverity.BLOCKING, PolicyScope.MODULE, 100, "content", List.of(), List.of(),
            Map.of(), true, false, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(resolver.resolvePolicies(req)).thenReturn(List.of(policy));
        PolicyEvaluation eval = new PolicyEvaluation(UUID.randomUUID(), req.id(), policy.id(), PolicyDecision.DENY,
            List.of(new PolicyViolation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "block", "Blocking", PolicySeverity.BLOCKING, Map.of(), false, OffsetDateTime.now())),
            Map.of(), 10L, 1, 0, 1, true, OffsetDateTime.now());
        when(evaluator.matches(policy, req)).thenReturn(true);
        when(evaluator.evaluate(policy, req, null)).thenReturn(eval);
        when(decisionService.decide(any(), any())).thenReturn(PolicyDecision.DENY);
        when(auditService.recordAudit(any())).thenReturn(null);

        EvaluationResult result = engine.evaluate(req);
        assertEquals(PolicyDecision.DENY, result.finalDecision());
    }

    @Test
    void testIsAllowed() {
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read",
            Map.of(), Map.of(), "user1", List.of(), Map.of(), OffsetDateTime.now());
        when(resolver.resolvePolicies(req)).thenReturn(List.of());
        when(decisionService.decide(any(), any())).thenReturn(PolicyDecision.ALLOW);
        when(auditService.recordAudit(any())).thenReturn(null);
        assertTrue(engine.isAllowed(req));
    }

    @Test
    void testDetermineDecision() {
        assertEquals(PolicyDecision.ALLOW, engine.determineDecision(null, List.of()));
        assertEquals(PolicyDecision.DENY, engine.determineDecision(null, List.of(
            new PolicyViolation(null, null, null, "b", "block", PolicySeverity.BLOCKING, null, false, null))));
        assertEquals(PolicyDecision.DENY, engine.determineDecision(null, List.of(
            new PolicyViolation(null, null, null, "c", "critical", PolicySeverity.CRITICAL, null, false, null))));
        assertEquals(PolicyDecision.REVIEW, engine.determineDecision(null, List.of(
            new PolicyViolation(null, null, null, "e", "error", PolicySeverity.ERROR, null, false, null))));
        assertEquals(PolicyDecision.LOG, engine.determineDecision(null, List.of(
            new PolicyViolation(null, null, null, "w", "warn", PolicySeverity.WARNING, null, true, null))));
    }
}
