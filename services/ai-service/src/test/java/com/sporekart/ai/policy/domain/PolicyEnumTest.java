package com.sporekart.ai.policy.domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PolicyEnumTest {
    @Test void testPolicyStatusValues() {
        assertEquals(6, PolicyStatus.values().length);
        assertNotNull(PolicyStatus.valueOf("ACTIVE"));
    }
    @Test void testPolicySeverityValues() {
        assertEquals(5, PolicySeverity.values().length);
        assertNotNull(PolicySeverity.valueOf("BLOCKING"));
    }
    @Test void testPolicyDecisionValues() {
        assertEquals(6, PolicyDecision.values().length);
        assertNotNull(PolicyDecision.valueOf("CHALLENGE"));
    }
    @Test void testPolicyScopeValues() {
        assertEquals(10, PolicyScope.values().length);
        assertNotNull(PolicyScope.valueOf("CUSTOM"));
    }
    @Test void testPolicyActionValues() {
        assertEquals(7, PolicyAction.values().length);
        assertNotNull(PolicyAction.valueOf("EVALUATE"));
    }
    @Test void testConditionOperatorValues() {
        assertEquals(15, ConditionOperator.values().length);
        assertNotNull(ConditionOperator.valueOf("ENDS_WITH"));
    }
    @Test void testConflictStrategyValues() {
        assertEquals(7, ConflictStrategy.values().length);
        assertNotNull(ConflictStrategy.valueOf("REQUIRE_ALLOW"));
    }
    @Test void testPolicyTypeValues() {
        assertEquals(10, PolicyType.values().length);
        assertNotNull(PolicyType.valueOf("CUSTOM"));
    }
}
