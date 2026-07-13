package com.sporekart.ai.compliance.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ComplianceRuleTest {

    @Test
    void testRecordConstruction() {
        UUID id = UUID.randomUUID();
        UUID frameworkId = UUID.randomUUID();
        String ruleId = "RULE-001";
        String name = "Test Rule";
        String description = "Test rule description";
        String category = "access-control";
        RiskLevel riskLevel = RiskLevel.HIGH;
        String expression = "context.authorized == true";
        boolean active = true;
        Instant effectiveFrom = Instant.now();
        Instant effectiveTo = Instant.now().plusSeconds(86400);

        ComplianceRule rule = new ComplianceRule(
            id, frameworkId, ruleId, name, description, category,
            riskLevel, expression, active, effectiveFrom, effectiveTo
        );

        assertEquals(id, rule.id());
        assertEquals(frameworkId, rule.frameworkId());
        assertEquals(ruleId, rule.ruleId());
        assertEquals(name, rule.name());
        assertEquals(description, rule.description());
        assertEquals(category, rule.category());
        assertEquals(riskLevel, rule.riskLevel());
        assertEquals(expression, rule.expression());
        assertTrue(rule.active());
        assertEquals(effectiveFrom, rule.effectiveFrom());
        assertEquals(effectiveTo, rule.effectiveTo());
    }

    @Test
    void testRecordEquality() {
        UUID id = UUID.randomUUID();
        UUID fwId = UUID.randomUUID();
        Instant now = Instant.now();
        ComplianceRule r1 = new ComplianceRule(id, fwId, "R1", "Rule", "desc", "cat",
            RiskLevel.LOW, "expr", true, now, now);
        ComplianceRule r2 = new ComplianceRule(id, fwId, "R1", "Rule", "desc", "cat",
            RiskLevel.LOW, "expr", true, now, now);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
