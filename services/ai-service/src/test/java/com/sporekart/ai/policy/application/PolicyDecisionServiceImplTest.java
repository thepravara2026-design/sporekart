package com.sporekart.ai.policy.application;
import com.sporekart.ai.policy.domain.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PolicyDecisionServiceImplTest {
    private final PolicyDecisionServiceImpl service = new PolicyDecisionServiceImpl();

    @Test void testDecide_AllowWhenEmpty() { assertEquals(PolicyDecision.ALLOW, service.decide(null, List.of())); }
    @Test void testDecide_AllowWhenNull() { assertEquals(PolicyDecision.ALLOW, service.decide(null, null)); }
    @Test void testDecide_DenyWhenAnyDeny() {
        assertEquals(PolicyDecision.DENY, service.decide(null, List.of(
            new PolicyEvaluation(null, null, null, PolicyDecision.ALLOW, List.of(), Map.of(), 0, 0, 0, 0, true, null),
            new PolicyEvaluation(null, null, null, PolicyDecision.DENY, List.of(), Map.of(), 0, 0, 0, 0, true, null))));
    }
    @Test void testDecide_ChallengeWhenBlock() {
        assertEquals(PolicyDecision.CHALLENGE, service.decide(null, List.of(
            new PolicyEvaluation(null, null, null, PolicyDecision.CHALLENGE, List.of(), Map.of(), 0, 0, 0, 0, true, null))));
    }
    @Test void testDecide_ReviewWhenReview() {
        assertEquals(PolicyDecision.REVIEW, service.decide(null, List.of(
            new PolicyEvaluation(null, null, null, PolicyDecision.REVIEW, List.of(), Map.of(), 0, 0, 0, 0, true, null))));
    }
    @Test void testResolveConflict_DenyOverrides() {
        assertEquals(PolicyDecision.DENY, service.resolveConflict(List.of(
            new PolicyEvaluation(null, null, null, PolicyDecision.ALLOW, List.of(), Map.of(), 0, 0, 0, 0, true, null),
            new PolicyEvaluation(null, null, null, PolicyDecision.DENY, List.of(), Map.of(), 0, 0, 0, 0, true, null)
        ), ConflictStrategy.DENY_OVERRIDES));
    }
    @Test void testResolveConflict_AllowOverrides() {
        assertEquals(PolicyDecision.ALLOW, service.resolveConflict(List.of(
            new PolicyEvaluation(null, null, null, PolicyDecision.DENY, List.of(), Map.of(), 0, 0, 0, 0, true, null),
            new PolicyEvaluation(null, null, null, PolicyDecision.ALLOW, List.of(), Map.of(), 0, 0, 0, 0, true, null)
        ), ConflictStrategy.ALLOW_OVERRIDES));
    }
    @Test void testIsAllowed() { assertTrue(service.isAllowed(PolicyDecision.ALLOW)); assertTrue(service.isAllowed(PolicyDecision.BYPASS)); assertFalse(service.isAllowed(PolicyDecision.DENY)); }
    @Test void testRequiresReview() { assertTrue(service.requiresReview(PolicyDecision.REVIEW)); assertTrue(service.requiresReview(PolicyDecision.CHALLENGE)); assertFalse(service.requiresReview(PolicyDecision.ALLOW)); }
}
