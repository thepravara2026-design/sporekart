package com.sporekart.ai.risk.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RiskAssessmentStatusTest {

    @ParameterizedTest
    @EnumSource(RiskAssessmentStatus.class)
    void shouldContainAllExpectedValues(RiskAssessmentStatus status) {
        assertNotNull(status);
        assertNotNull(status.name());
    }

    @ParameterizedTest
    @EnumSource(value = RiskAssessmentStatus.class, names = { "PENDING", "IN_PROGRESS", "COMPLETED", "FAILED", "CANCELLED" })
    void shouldHaveFiveValues(RiskAssessmentStatus status) {
        assertTrue(status.ordinal() >= 0);
    }

    @Test
    void pendingShouldBeFirst() {
        assertEquals(0, RiskAssessmentStatus.PENDING.ordinal());
    }

    @Test
    void completedShouldBeThird() {
        assertEquals(2, RiskAssessmentStatus.COMPLETED.ordinal());
    }

    @Test
    void valueOfShouldWorkForAll() {
        assertEquals(RiskAssessmentStatus.PENDING, RiskAssessmentStatus.valueOf("PENDING"));
        assertEquals(RiskAssessmentStatus.IN_PROGRESS, RiskAssessmentStatus.valueOf("IN_PROGRESS"));
        assertEquals(RiskAssessmentStatus.COMPLETED, RiskAssessmentStatus.valueOf("COMPLETED"));
        assertEquals(RiskAssessmentStatus.FAILED, RiskAssessmentStatus.valueOf("FAILED"));
        assertEquals(RiskAssessmentStatus.CANCELLED, RiskAssessmentStatus.valueOf("CANCELLED"));
    }
}
