package com.sporekart.ai.risk.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RiskLevelTest {

    @ParameterizedTest
    @EnumSource(RiskLevel.class)
    void shouldContainAllExpectedValues(RiskLevel level) {
        assertNotNull(level);
        assertNotNull(level.name());
    }

    @ParameterizedTest
    @EnumSource(value = RiskLevel.class, names = { "LOW", "MEDIUM", "HIGH", "CRITICAL", "CUSTOM" })
    void shouldHaveFiveValues(RiskLevel level) {
        assertTrue(level.ordinal() >= 0);
    }

    @Test
    void lowShouldBeFirst() {
        assertEquals(0, RiskLevel.LOW.ordinal());
    }

    @Test
    void criticalShouldBeFourth() {
        assertEquals(3, RiskLevel.CRITICAL.ordinal());
    }

    @Test
    void valueOfShouldWorkForAll() {
        assertEquals(RiskLevel.LOW, RiskLevel.valueOf("LOW"));
        assertEquals(RiskLevel.MEDIUM, RiskLevel.valueOf("MEDIUM"));
        assertEquals(RiskLevel.HIGH, RiskLevel.valueOf("HIGH"));
        assertEquals(RiskLevel.CRITICAL, RiskLevel.valueOf("CRITICAL"));
        assertEquals(RiskLevel.CUSTOM, RiskLevel.valueOf("CUSTOM"));
    }
}
