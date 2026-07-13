package com.sporekart.ai.approval.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ApprovalStatusTest {

    @ParameterizedTest
    @EnumSource(ApprovalStatus.class)
    void shouldHaveValidNameAndOrdinal(ApprovalStatus status) {
        assertNotNull(status.name());
        assertTrue(status.ordinal() >= 0);
    }

    @ParameterizedTest
    @EnumSource(ApprovalStatus.class)
    void shouldBeParseable(ApprovalStatus status) {
        assertEquals(status, ApprovalStatus.valueOf(status.name()));
    }
}
