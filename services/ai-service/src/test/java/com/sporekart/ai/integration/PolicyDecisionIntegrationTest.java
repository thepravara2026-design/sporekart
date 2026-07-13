package com.sporekart.ai.integration;

import com.sporekart.ai.policy.api.PolicyEngine;
import com.sporekart.ai.policy.domain.*;
import com.sporekart.ai.decision.api.DecisionEngine;
import com.sporekart.ai.decision.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyDecisionIntegrationTest {

    @Mock private PolicyEngine policyEngine;
    @Mock private DecisionEngine decisionEngine;

    @Test
    void testPolicyEngineToDecisionEngineIntegration() {
        UUID requestId = UUID.randomUUID();
        EvaluationRequest evalRequest = new EvaluationRequest(
            requestId, "catalog", "publish_item",
            Map.of("itemId", "123", "price", 250.0),
            Map.of("region", "US"), "user1", List.of("editor"),
            Map.of("User-Agent", "web"), OffsetDateTime.now()
        );

        PolicyEvaluation evaluation = new PolicyEvaluation(
            UUID.randomUUID(), requestId, UUID.randomUUID(), PolicyDecision.ALLOW,
            List.of(), Map.of("rule", "price-cap"),
            3L, 1, 1, 0, true, OffsetDateTime.now()
        );

        EvaluationResult evalResult = new EvaluationResult(
            requestId, PolicyDecision.ALLOW,
            List.of(evaluation), List.of(),
            3L, true, OffsetDateTime.now()
        );
        when(policyEngine.evaluate(evalRequest)).thenReturn(evalResult);

        DecisionRequest decRequest = new DecisionRequest(
            requestId, "catalog", "publish_item",
            Map.of("itemId", "123", "price", 250.0),
            Map.of("policyResult", evalResult.finalDecision().name()),
            "user1", List.of("editor"),
            List.of(), List.of(),
            Map.of("policyDecision", evalResult.finalDecision().name()),
            OffsetDateTime.now()
        );

        DecisionResult decResult = new DecisionResult(
            UUID.randomUUID(), requestId, DecisionAction.ALLOW,
            DecisionStatus.ALLOWED, DecisionConfidence.HIGH, "Publish allowed",
            List.of(new DecisionReason(UUID.randomUUID(), "PRICE_OK",
                "Price within limits", "policy",
                DecisionConfidence.HIGH, Map.of("rule", "price-cap"))),
            List.of(), null, 5L, false, false, OffsetDateTime.now()
        );
        when(decisionEngine.evaluate(decRequest)).thenReturn(decResult);

        EvaluationResult actualEval = policyEngine.evaluate(evalRequest);
        assertNotNull(actualEval);
        assertTrue(actualEval.passed());
        assertEquals(PolicyDecision.ALLOW, actualEval.finalDecision());

        DecisionResult actualDec = decisionEngine.evaluate(decRequest);
        assertNotNull(actualDec);
        assertEquals(DecisionAction.ALLOW, actualDec.action());
        assertEquals(DecisionStatus.ALLOWED, actualDec.status());
        assertFalse(actualDec.requiresApproval());

        verify(policyEngine).evaluate(evalRequest);
        verify(decisionEngine).evaluate(decRequest);
    }

    @Test
    void testPolicyDenyLeadsToDecisionDeny() {
        UUID requestId = UUID.randomUUID();
        EvaluationRequest evalRequest = new EvaluationRequest(
            requestId, "catalog", "delete_item",
            Map.of("itemId", "999"), Map.of(), "user2",
            List.of("viewer"), Map.of(), OffsetDateTime.now()
        );

        PolicyViolation violation = new PolicyViolation(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            "delete-restriction", "User lacks delete permission",
            PolicySeverity.HIGH, Map.of("requiredRole", "admin"),
            false, OffsetDateTime.now()
        );

        EvaluationResult evalResult = new EvaluationResult(
            requestId, PolicyDecision.DENY, List.of(), List.of(violation),
            2L, false, OffsetDateTime.now()
        );
        when(policyEngine.evaluate(evalRequest)).thenReturn(evalResult);

        DecisionRequest decRequest = new DecisionRequest(
            requestId, "catalog", "delete_item",
            Map.of("itemId", "999"), Map.of(),
            "user2", List.of("viewer"),
            List.of(), List.of(),
            Map.of("policyDecision", evalResult.finalDecision().name()),
            OffsetDateTime.now()
        );

        DecisionResult decResult = new DecisionResult(
            UUID.randomUUID(), requestId, DecisionAction.DENY,
            DecisionStatus.DENIED, DecisionConfidence.CERTAIN,
            "Delete denied due to policy violation",
            List.of(new DecisionReason(UUID.randomUUID(), "DELETE_RESTRICTED",
                "User role viewer cannot delete", "policy",
                DecisionConfidence.CERTAIN, Map.of("violation", "delete-restriction"))),
            List.of(), null, 3L, false, false, OffsetDateTime.now()
        );
        when(decisionEngine.evaluate(decRequest)).thenReturn(decResult);

        EvaluationResult actualEval = policyEngine.evaluate(evalRequest);
        assertFalse(actualEval.passed());
        assertEquals(PolicyDecision.DENY, actualEval.finalDecision());

        DecisionResult actualDec = decisionEngine.evaluate(decRequest);
        assertEquals(DecisionAction.DENY, actualDec.action());
        assertEquals(DecisionStatus.DENIED, actualDec.status());

        verify(policyEngine).evaluate(evalRequest);
        verify(decisionEngine).evaluate(decRequest);
    }

    @Test
    void testPolicyReviewTriggersApprovalDecision() {
        UUID requestId = UUID.randomUUID();
        EvaluationRequest evalRequest = new EvaluationRequest(
            requestId, "finance", "approve_payment",
            Map.of("amount", 50000.0), Map.of(), "user3",
            List.of("finance_user"), Map.of(), OffsetDateTime.now()
        );

        EvaluationResult evalResult = new EvaluationResult(
            requestId, PolicyDecision.REVIEW, List.of(), List.of(),
            4L, true, OffsetDateTime.now()
        );
        when(policyEngine.evaluate(evalRequest)).thenReturn(evalResult);

        DecisionRequest decRequest = new DecisionRequest(
            requestId, "finance", "approve_payment",
            Map.of("amount", 50000.0), Map.of(),
            "user3", List.of("finance_user"),
            List.of(), List.of(),
            Map.of("policyDecision", evalResult.finalDecision().name()),
            OffsetDateTime.now()
        );

        DecisionResult decResult = new DecisionResult(
            UUID.randomUUID(), requestId, DecisionAction.REQUIRE_APPROVAL,
            DecisionStatus.PENDING, DecisionConfidence.MEDIUM,
            "High value payment requires approval",
            List.of(), List.of(), null, 6L, true, true, OffsetDateTime.now()
        );
        when(decisionEngine.evaluate(decRequest)).thenReturn(decResult);

        EvaluationResult actualEval = policyEngine.evaluate(evalRequest);
        assertEquals(PolicyDecision.REVIEW, actualEval.finalDecision());

        DecisionResult actualDec = decisionEngine.evaluate(decRequest);
        assertEquals(DecisionAction.REQUIRE_APPROVAL, actualDec.action());
        assertTrue(actualDec.requiresApproval());

        verify(policyEngine).evaluate(evalRequest);
        verify(decisionEngine).evaluate(decRequest);
    }
}
