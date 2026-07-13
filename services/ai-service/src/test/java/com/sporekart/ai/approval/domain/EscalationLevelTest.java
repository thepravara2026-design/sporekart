package com.sporekart.ai.approval.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class EscalationLevelTest {

    @ParameterizedTest
    @EnumSource(EscalationReason.class)
    void shouldHaveValidNameAndOrdinal(EscalationReason reason) {
        assertNotNull(reason.name());
        assertTrue(reason.ordinal() >= 0);
    }

    @ParameterizedTest
    @EnumSource(EscalationReason.class)
    void shouldBeParseable(EscalationReason reason) {
        assertEquals(reason, EscalationReason.valueOf(reason.name()));
    }
}
