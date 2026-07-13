package com.sporekart.ai.governance.domain;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GovernanceRecordTest {

    @Test
    void testGovernancePolicy() {
        UUID id = UUID.randomUUID();
        GovernancePolicy policy = new GovernancePolicy(id, "test-policy", "Test description",
            GovernanceScope.ALL, GovernanceStatus.ACTIVE, 100, Map.of(), Map.of(),
            true, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        assertEquals(id, policy.id());
        assertEquals("test-policy", policy.name());
        assertTrue(policy.isActive());
    }

    @Test
    void testGovernanceRule() {
        GovernanceRule rule = new GovernanceRule(UUID.randomUUID(), UUID.randomUUID(),
            "test-rule", "Test rule", "expression", Map.of(),
            GovernanceDecision.ALLOW, 1, true, OffsetDateTime.now());
        assertEquals("test-rule", rule.name());
        assertEquals(1, rule.order());
    }

    @Test
    void testGovernanceRequest() {
        GovernanceRequest req = new GovernanceRequest(UUID.randomUUID(), "content", "generate",
            Map.of("type", "article"), Map.of(), "user1", List.of("admin"), OffsetDateTime.now());
        assertEquals("content", req.module());
        assertEquals("generate", req.action());
        assertEquals("user1", req.userId());
    }

    @Test
    void testGovernanceResponse() {
        GovernanceResponse resp = new GovernanceResponse(UUID.randomUUID(), UUID.randomUUID(),
            GovernanceDecision.ALLOW, List.of(), Map.of("key", "value"),
            42L, OffsetDateTime.now());
        assertEquals(GovernanceDecision.ALLOW, resp.decision());
        assertEquals(42L, resp.processingTimeMs());
    }

    @Test
    void testGovernanceViolation() {
        GovernanceViolation v = new GovernanceViolation(UUID.randomUUID(), "rule1", "message",
            GovernanceSeverity.WARNING, Map.of(), true, OffsetDateTime.now());
        assertEquals("rule1", v.ruleName());
        assertTrue(v.overridable());
    }

    @Test
    void testGovernanceConfiguration() {
        GovernanceConfiguration config = new GovernanceConfiguration(UUID.randomUUID(), "key1", "value1",
            "desc", GovernanceScope.CONFIGURATION, GovernanceMode.DEVELOPMENT,
            Map.of(), true, 1, OffsetDateTime.now(), OffsetDateTime.now());
        assertEquals("key1", config.key());
        assertEquals("value1", config.value());
    }

    @Test
    void testGovernanceAudit() {
        GovernanceAudit audit = new GovernanceAudit(UUID.randomUUID(), UUID.randomUUID(),
            "validate", "content", GovernanceDecision.ALLOW, List.of(), Map.of(),
            "user1", 100L, true, OffsetDateTime.now(), OffsetDateTime.now());
        assertEquals("validate", audit.action());
        assertTrue(audit.success());
    }

    @Test
    void testGovernanceRegistry() {
        GovernanceRegistry reg = new GovernanceRegistry(UUID.randomUUID(), "reg1", "content",
            "/api/v1/content", GovernanceScope.REQUEST, GovernanceMode.DEVELOPMENT,
            Map.of(), true, OffsetDateTime.now(), OffsetDateTime.now());
        assertEquals("reg1", reg.name());
        assertTrue(reg.isRegistered());
    }

    @Test
    void testGovernanceContext() {
        GovernanceContext ctx = new GovernanceContext(UUID.randomUUID(), UUID.randomUUID(),
            "content", "generate", Map.of(), Map.of(), Map.of(), OffsetDateTime.now());
        assertEquals("content", ctx.module());
    }

    @Test
    void testGovernanceLifecycle() {
        GovernanceLifecycle lifecycle = new GovernanceLifecycle(UUID.randomUUID(), UUID.randomUUID(),
            "transition", "DRAFT", "ACTIVE", "admin", Map.of(), OffsetDateTime.now());
        assertEquals("DRAFT", lifecycle.fromStatus());
        assertEquals("ACTIVE", lifecycle.toStatus());
    }
}
