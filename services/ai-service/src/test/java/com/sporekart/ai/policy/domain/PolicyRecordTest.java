package com.sporekart.ai.policy.domain;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PolicyRecordTest {
    @Test void testPolicy() {
        Policy p = new Policy(UUID.randomUUID(), "test", "desc", PolicyType.GLOBAL, PolicyStatus.ACTIVE,
            PolicySeverity.INFO, PolicyScope.GLOBAL, 100, null, List.of(), List.of(),
            Map.of(), true, false, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        assertEquals("test", p.name());
        assertTrue(p.isActive());
    }
    @Test void testPolicyRule() {
        PolicyRule r = new PolicyRule(UUID.randomUUID(), UUID.randomUUID(), "rule1", "desc",
            "expression", Map.of(), PolicyDecision.ALLOW, 1, true, OffsetDateTime.now(), OffsetDateTime.now());
        assertEquals("rule1", r.name());
    }
    @Test void testPolicyCondition() {
        PolicyCondition c = new PolicyCondition(UUID.randomUUID(), UUID.randomUUID(), "module",
            ConditionOperator.EQUALS, "content", false, 1, OffsetDateTime.now());
        assertEquals("module", c.field());
    }
    @Test void testEvaluationRequest() {
        EvaluationRequest r = new EvaluationRequest(UUID.randomUUID(), "content", "generate",
            Map.of(), Map.of(), "user1", List.of("admin"), Map.of(), OffsetDateTime.now());
        assertEquals("content", r.module());
    }
    @Test void testEvaluationResult() {
        EvaluationResult r = new EvaluationResult(UUID.randomUUID(), PolicyDecision.ALLOW,
            List.of(), List.of(), 42L, true, OffsetDateTime.now());
        assertTrue(r.passed());
    }
    @Test void testPolicyViolation() {
        PolicyViolation v = new PolicyViolation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            "rule1", "msg", PolicySeverity.WARNING, Map.of(), true, OffsetDateTime.now());
        assertTrue(v.overridable());
    }
    @Test void testPolicyVersion() {
        PolicyVersion v = new PolicyVersion(UUID.randomUUID(), UUID.randomUUID(), 1, "v1",
            null, null, PolicyStatus.DRAFT, "initial", null, OffsetDateTime.now());
        assertEquals(1, v.versionNumber());
    }
    @Test void testPolicyContext() {
        PolicyContext c = new PolicyContext(UUID.randomUUID(), UUID.randomUUID(), "module", "action",
            PolicyScope.MODULE, Map.of(), Map.of(), Map.of(), List.of(), OffsetDateTime.now());
        assertEquals("module", c.module());
    }
}
