package com.sporekart.ai.policy.engine;
import com.sporekart.ai.policy.domain.*;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class RuleEngineTest {
    private final RuleEngine ruleEngine = new RuleEngine();

    @Test void testMatch() {
        PolicyRule rule = new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "r1", null,
            "module:content action:read", Map.of(), PolicyDecision.ALLOW, 1, true, OffsetDateTime.now(), OffsetDateTime.now());
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read",
            Map.of(), Map.of(), "u", List.of("admin"), Map.of(), OffsetDateTime.now());
        RuleEngine.RuleMatchResult result = ruleEngine.match(rule, req, null);
        assertTrue(result.matched());
    }

    @Test void testMatch_InactiveRule() {
        PolicyRule rule = new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "r1", null,
            "expr", Map.of(), PolicyDecision.ALLOW, 1, false, OffsetDateTime.now(), OffsetDateTime.now());
        RuleEngine.RuleMatchResult result = ruleEngine.match(rule, null, null);
        assertFalse(result.matched());
    }

    @Test void testResolveConflict_HighestPriority() {
        PolicyRule r1 = new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "r1", null, "e", Map.of(), PolicyDecision.DENY, 1, true, null, null);
        PolicyRule r2 = new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "r2", null, "e", Map.of(), PolicyDecision.ALLOW, 5, true, null, null);
        List<PolicyRule> result = ruleEngine.resolveConflict(List.of(r1, r2), ConflictStrategy.HIGHEST_PRIORITY_WINS);
        assertEquals(1, result.size());
        assertEquals("r1", result.get(0).name());
    }

    @Test void testResolveConflict_LowestPriority() {
        PolicyRule r1 = new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "r1", null, "e", Map.of(), PolicyDecision.DENY, 1, true, null, null);
        PolicyRule r2 = new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "r2", null, "e", Map.of(), PolicyDecision.ALLOW, 5, true, null, null);
        List<PolicyRule> result = ruleEngine.resolveConflict(List.of(r1, r2), ConflictStrategy.LOWEST_PRIORITY_WINS);
        assertEquals(1, result.size());
        assertEquals("r2", result.get(0).name());
    }

    @Test void testResolveConflict_DenyOverrides() {
        PolicyRule r1 = new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "r1", null, "e", Map.of(), PolicyDecision.ALLOW, 1, true, null, null);
        PolicyRule r2 = new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "r2", null, "e", Map.of(), PolicyDecision.DENY, 1, true, null, null);
        List<PolicyRule> result = ruleEngine.resolveConflict(List.of(r1, r2), ConflictStrategy.DENY_OVERRIDES);
        assertEquals(1, result.size());
        assertEquals(PolicyDecision.DENY, result.get(0).decision());
    }

    @Test void testFilterApplicable() {
        Policy active = new Policy(UUID.randomUUID(), "a", null, PolicyType.MODULE, PolicyStatus.ACTIVE,
            PolicySeverity.INFO, PolicyScope.MODULE, 100, "content", List.of(), List.of(), Map.of(), true, false, null, null, null);
        Policy inactive = new Policy(UUID.randomUUID(), "i", null, PolicyType.MODULE, PolicyStatus.INACTIVE,
            PolicySeverity.INFO, PolicyScope.MODULE, 50, "content", List.of(), List.of(), Map.of(), true, false, null, null, null);
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read", Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        List<Policy> result = ruleEngine.filterApplicable(List.of(active, inactive), req);
        assertEquals(1, result.size());
        assertEquals("a", result.get(0).name());
    }
}
