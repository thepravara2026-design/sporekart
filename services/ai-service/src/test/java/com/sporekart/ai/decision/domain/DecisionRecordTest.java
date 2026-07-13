package com.sporekart.ai.decision.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DecisionRecordTest {

    @Test
    void decisionRequestCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionRequest r = new DecisionRequest(id, "module1", "ALLOW", Map.of("key", "val"),
            Map.of("ctx", "val"), "user1", List.of("admin"), List.of("pol1"), List.of("rule1"),
            Map.of("result", "ok"), now);
        assertEquals(id, r.id());
        assertEquals("module1", r.module());
        assertEquals("ALLOW", r.action());
        assertEquals("user1", r.userId());
        assertTrue(r.roles().contains("admin"));
        assertTrue(r.matchedPolicyIds().contains("pol1"));
        assertTrue(r.matchedRuleIds().contains("rule1"));
        assertEquals(now, r.timestamp());
    }

    @Test
    void decisionContextCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        UUID reqId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionContext c = new DecisionContext(id, reqId, "mod", "DENY",
            Map.of("res", "v"), Map.of("sub", "v"), Map.of("env", "v"),
            List.of("role1"), List.of("pid1"), Map.of("sum", "ok"), now);
        assertEquals(id, c.id());
        assertEquals(reqId, c.requestId());
        assertEquals("mod", c.module());
        assertEquals("DENY", c.action());
        assertEquals(now, c.timestamp());
    }

    @Test
    void decisionResultCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        UUID reqId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionReason reason = new DecisionReason(UUID.randomUUID(), "R01", "msg", "cat", DecisionConfidence.HIGH, Map.of());
        DecisionEvidence ev = new DecisionEvidence(UUID.randomUUID(), "src", "type", "val", 0.8, Map.of());
        DecisionExplanation expl = new DecisionExplanation(UUID.randomUUID(), id, "summary",
            List.of(), List.of(), DecisionConfidence.HIGH, Map.of(), List.of(), "ALLOW", "audit", "text");
        DecisionResult r = new DecisionResult(id, reqId, DecisionAction.ALLOW, DecisionStatus.ALLOWED,
            DecisionConfidence.CERTAIN, "summary", List.of(reason), List.of(ev), expl, 100L,
            true, false, now);
        assertEquals(id, r.id());
        assertEquals(reqId, r.requestId());
        assertEquals(DecisionAction.ALLOW, r.action());
        assertEquals(DecisionStatus.ALLOWED, r.status());
        assertEquals(DecisionConfidence.CERTAIN, r.confidence());
        assertEquals(100L, r.processingTimeMs());
        assertTrue(r.requiresApproval());
        assertFalse(r.overrideable());
        assertEquals(now, r.timestamp());
    }

    @Test
    void decisionRuleCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionRule r = new DecisionRule(id, "rule1", "desc", DecisionAction.DENY,
            10, 50, Map.of("k", "v"), Map.of("o", "v"), true, now, now);
        assertEquals(id, r.id());
        assertEquals("rule1", r.name());
        assertEquals("desc", r.description());
        assertEquals(DecisionAction.DENY, r.action());
        assertEquals(10, r.priority());
        assertEquals(50, r.weight());
        assertTrue(r.isActive());
        assertEquals(now, r.createdAt());
        assertEquals(now, r.updatedAt());
    }

    @Test
    void decisionReasonCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        DecisionReason r = new DecisionReason(id, "C001", "msg", "cat", DecisionConfidence.MEDIUM, Map.of("d", 1));
        assertEquals(id, r.id());
        assertEquals("C001", r.code());
        assertEquals("msg", r.message());
        assertEquals("cat", r.category());
        assertEquals(DecisionConfidence.MEDIUM, r.confidence());
        assertEquals(1, r.details().get("d"));
    }

    @Test
    void decisionEvidenceCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        DecisionEvidence e = new DecisionEvidence(id, "source1", "type1", "value1", 0.95, Map.of("k", "v"));
        assertEquals(id, e.id());
        assertEquals("source1", e.source());
        assertEquals("type1", e.type());
        assertEquals("value1", e.value());
        assertEquals(0.95, e.relevance(), 0.001);
        assertEquals("v", e.metadata().get("k"));
    }

    @Test
    void decisionExplanationCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        UUID decId = UUID.randomUUID();
        DecisionEvidence ev = new DecisionEvidence(UUID.randomUUID(), "s", "t", "v", 0.5, Map.of());
        DecisionExplanation e = new DecisionExplanation(id, decId, "summary",
            List.of("p1"), List.of("r1"), DecisionConfidence.HIGH,
            Map.of("key", "val"), List.of(ev), "ALLOW", "audit", "text");
        assertEquals(id, e.id());
        assertEquals(decId, e.decisionId());
        assertEquals("summary", e.summary());
        assertTrue(e.matchedPolicies().contains("p1"));
        assertTrue(e.triggeredRules().contains("r1"));
        assertEquals(DecisionConfidence.HIGH, e.confidence());
        assertEquals("ALLOW", e.recommendedAction());
        assertEquals("audit", e.auditMetadata());
        assertEquals("text", e.explanationText());
    }

    @Test
    void decisionMetadataCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        UUID decId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionMetadata m = new DecisionMetadata(id, decId, "1.0", "prod",
            Map.of("tag1", "v1"), Map.of("attr1", "v2"), now);
        assertEquals(id, m.id());
        assertEquals(decId, m.decisionId());
        assertEquals("1.0", m.version());
        assertEquals("prod", m.environment());
        assertEquals("v1", m.tags().get("tag1"));
        assertEquals("v2", m.attributes().get("attr1"));
        assertEquals(now, m.createdAt());
    }

    @Test
    void decisionAuditCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        UUID reqId = UUID.randomUUID();
        UUID decId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionReason reason = new DecisionReason(UUID.randomUUID(), "R01", "msg", "cat", DecisionConfidence.LOW, Map.of());
        DecisionAudit a = new DecisionAudit(id, reqId, decId, DecisionAction.DENY, DecisionStatus.DENIED,
            DecisionConfidence.LOW, List.of(reason), Map.of("k", "v"), "user1", 50L, false, now, now);
        assertEquals(id, a.id());
        assertEquals(reqId, a.requestId());
        assertEquals(decId, a.decisionId());
        assertEquals(DecisionAction.DENY, a.action());
        assertEquals(DecisionStatus.DENIED, a.status());
        assertEquals(DecisionConfidence.LOW, a.confidence());
        assertEquals("user1", a.userId());
        assertEquals(50L, a.processingTimeMs());
        assertFalse(a.success());
        assertEquals(now, a.timestamp());
        assertEquals(now, a.createdAt());
    }

    @Test
    void decisionLifecycleCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        UUID decId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionLifecycle l = new DecisionLifecycle(id, decId, DecisionStatus.PENDING, DecisionStatus.ALLOWED,
            "system", "completed", now);
        assertEquals(id, l.id());
        assertEquals(decId, l.decisionId());
        assertEquals(DecisionStatus.PENDING, l.fromStatus());
        assertEquals(DecisionStatus.ALLOWED, l.toStatus());
        assertEquals("system", l.triggeredBy());
        assertEquals("completed", l.reason());
        assertEquals(now, l.timestamp());
    }

    @Test
    void decisionRegistryCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionRegistry r = new DecisionRegistry(id, "reg1", "mod1", "http://endpoint",
            true, true, Map.of("k", "v"), now, now);
        assertEquals(id, r.id());
        assertEquals("reg1", r.name());
        assertEquals("mod1", r.module());
        assertEquals("http://endpoint", r.endpoint());
        assertTrue(r.isActive());
        assertTrue(r.isRegistered());
        assertEquals(now, r.registeredAt());
        assertEquals(now, r.updatedAt());
    }

    @Test
    void decisionConfigCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionConfig c = new DecisionConfig(id, "key1", "val1", "desc", true, 2, now, now);
        assertEquals(id, c.id());
        assertEquals("key1", c.key());
        assertEquals("val1", c.value());
        assertEquals("desc", c.description());
        assertTrue(c.isActive());
        assertEquals(2, c.version());
        assertEquals(now, c.createdAt());
        assertEquals(now, c.updatedAt());
    }

    @Test
    void decisionStatisticsCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionStatistics s = new DecisionStatistics(id, 100L, 60L, 20L, 5L, 10L, 5L, 0.85, 45.5, now);
        assertEquals(id, s.id());
        assertEquals(100L, s.totalDecisions());
        assertEquals(60L, s.allowedCount());
        assertEquals(20L, s.deniedCount());
        assertEquals(5L, s.escalatedCount());
        assertEquals(10L, s.approvalCount());
        assertEquals(5L, s.conflictCount());
        assertEquals(0.85, s.averageConfidence(), 0.001);
        assertEquals(45.5, s.averageLatencyMs(), 0.001);
        assertEquals(now, s.calculatedAt());
    }

    @Test
    void decisionOverrideCreationAndAccessors() {
        UUID id = UUID.randomUUID();
        UUID decId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        DecisionOverride o = new DecisionOverride(id, decId, DecisionAction.DENY, DecisionAction.ALLOW,
            "reason", "admin", Map.of("k", "v"), now);
        assertEquals(id, o.id());
        assertEquals(decId, o.decisionId());
        assertEquals(DecisionAction.DENY, o.originalAction());
        assertEquals(DecisionAction.ALLOW, o.overrideAction());
        assertEquals("reason", o.reason());
        assertEquals("admin", o.overriddenBy());
        assertEquals(now, o.timestamp());
    }
}
