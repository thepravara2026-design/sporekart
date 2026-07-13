package com.sporekart.ai.decision.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DecisionEnumTest {

    @Test
    void conflictStrategyHasAllValues() {
        assertNotNull(ConflictStrategy.valueOf("PRIORITY_BASED"));
        assertNotNull(ConflictStrategy.valueOf("WEIGHTED"));
        assertNotNull(ConflictStrategy.valueOf("DENY_OVERRIDES"));
        assertNotNull(ConflictStrategy.valueOf("ALLOW_OVERRIDES"));
        assertNotNull(ConflictStrategy.valueOf("MOST_RECENT"));
        assertNotNull(ConflictStrategy.valueOf("SAFE_DEFAULT"));
        assertNotNull(ConflictStrategy.valueOf("FAIL_CLOSED"));
        assertNotNull(ConflictStrategy.valueOf("CUSTOM"));
        assertEquals(8, ConflictStrategy.values().length);
    }

    @Test
    void decisionActionHasAllValues() {
        assertNotNull(DecisionAction.valueOf("ALLOW"));
        assertNotNull(DecisionAction.valueOf("DENY"));
        assertNotNull(DecisionAction.valueOf("REQUIRE_APPROVAL"));
        assertNotNull(DecisionAction.valueOf("LIMIT_RESPONSE"));
        assertNotNull(DecisionAction.valueOf("REDACT_CONTENT"));
        assertNotNull(DecisionAction.valueOf("ESCALATE_TO_ADMIN"));
        assertNotNull(DecisionAction.valueOf("RETRY"));
        assertNotNull(DecisionAction.valueOf("FALLBACK_PROVIDER"));
        assertNotNull(DecisionAction.valueOf("BLOCK_REQUEST"));
        assertNotNull(DecisionAction.valueOf("CUSTOM_EXTENSION"));
        assertEquals(10, DecisionAction.values().length);
    }

    @Test
    void decisionConfidenceHasAllValues() {
        assertNotNull(DecisionConfidence.valueOf("CERTAIN"));
        assertNotNull(DecisionConfidence.valueOf("HIGH"));
        assertNotNull(DecisionConfidence.valueOf("MEDIUM"));
        assertNotNull(DecisionConfidence.valueOf("LOW"));
        assertNotNull(DecisionConfidence.valueOf("VERY_LOW"));
        assertNotNull(DecisionConfidence.valueOf("INCONCLUSIVE"));
        assertEquals(6, DecisionConfidence.values().length);
    }

    @Test
    void decisionStatusHasAllValues() {
        assertNotNull(DecisionStatus.valueOf("PENDING"));
        assertNotNull(DecisionStatus.valueOf("EVALUATING"));
        assertNotNull(DecisionStatus.valueOf("ALLOWED"));
        assertNotNull(DecisionStatus.valueOf("DENIED"));
        assertNotNull(DecisionStatus.valueOf("ESCALATED"));
        assertNotNull(DecisionStatus.valueOf("APPROVED"));
        assertNotNull(DecisionStatus.valueOf("FAILED"));
        assertNotNull(DecisionStatus.valueOf("REJECTED"));
        assertEquals(8, DecisionStatus.values().length);
    }
}
