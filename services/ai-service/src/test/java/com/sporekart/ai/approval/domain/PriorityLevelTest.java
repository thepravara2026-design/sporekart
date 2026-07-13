package com.sporekart.ai.approval.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class PriorityLevelTest {

    @ParameterizedTest
    @EnumSource(ApprovalDecision.class)
    void shouldHaveValidNameAndOrdinal(ApprovalDecision decision) {
        assertNotNull(decision.name());
        assertTrue(decision.ordinal() >= 0);
    }

    @ParameterizedTest
    @EnumSource(ApprovalDecision.class)
    void shouldBeParseable(ApprovalDecision decision) {
        assertEquals(decision, ApprovalDecision.valueOf(decision.name()));
    }
}
