package com.sporekart.ai.approval.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ApprovalTypeTest {

    @ParameterizedTest
    @EnumSource(AssignmentStrategy.class)
    void shouldHaveValidNameAndOrdinal(AssignmentStrategy strategy) {
        assertNotNull(strategy.name());
        assertTrue(strategy.ordinal() >= 0);
    }

    @ParameterizedTest
    @EnumSource(AssignmentStrategy.class)
    void shouldBeParseable(AssignmentStrategy strategy) {
        assertEquals(strategy, AssignmentStrategy.valueOf(strategy.name()));
    }
}
