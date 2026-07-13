package com.sporekart.ai.policy.application;
import com.sporekart.ai.policy.api.PolicyDecisionService;
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
class PolicyEvaluatorImplTest {
    @Mock private PolicyDecisionService decisionService;
    private PolicyEvaluatorImpl evaluator;

    @BeforeEach void setUp() { evaluator = new PolicyEvaluatorImpl(decisionService); }

    @Test void testEvaluate() {
        Policy p = new Policy(UUID.randomUUID(), "test", null, PolicyType.MODULE, PolicyStatus.ACTIVE,
            PolicySeverity.INFO, PolicyScope.MODULE, 0, "content",
            List.of(new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "r1", null, "module:content",
                Map.of(), PolicyDecision.ALLOW, 1, true, OffsetDateTime.now(), OffsetDateTime.now())),
            List.of(), Map.of(), true, false, null, null, null);
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read",
            Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        PolicyContext ctx = new PolicyContext(UUID.randomUUID(), req.id(), "content", "read", PolicyScope.MODULE,
            Map.of(), Map.of(), Map.of(), List.of(), OffsetDateTime.now());
        when(decisionService.decide(any(), anyList())).thenReturn(PolicyDecision.ALLOW);
        PolicyEvaluation eval = evaluator.evaluate(p, req, ctx);
        assertNotNull(eval);
    }

    @Test void testMatches() {
        Policy p = new Policy(UUID.randomUUID(), "t", null, PolicyType.MODULE, PolicyStatus.ACTIVE,
            PolicySeverity.INFO, PolicyScope.MODULE, 0, "content", List.of(), List.of(), Map.of(), true, false, null, null, null);
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read", Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        assertTrue(evaluator.matches(p, req));
        Policy p2 = new Policy(UUID.randomUUID(), "t", null, PolicyType.MODULE, PolicyStatus.ACTIVE,
            PolicySeverity.INFO, PolicyScope.MODULE, 0, "other", List.of(), List.of(), Map.of(), true, false, null, null, null);
        assertFalse(evaluator.matches(p2, req));
    }

    @Test void testEvaluateRules_Empty() {
        assertTrue(evaluator.evaluateRules(null, null).isEmpty());
        assertTrue(evaluator.evaluateRules(List.of(), null).isEmpty());
    }
}
