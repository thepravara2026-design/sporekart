package com.sporekart.ai.compliance.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class RiskLevelTest {

    @ParameterizedTest
    @CsvSource({
        "LOW, 0",
        "MEDIUM, 1",
        "HIGH, 2",
        "CRITICAL, 3"
    })
    void testRiskLevelValues(String name, int ordinal) {
        RiskLevel level = RiskLevel.valueOf(name);
        assertEquals(name, level.name());
        assertEquals(ordinal, level.ordinal());
    }

    @ParameterizedTest
    @CsvSource({
        "LOW",
        "MEDIUM",
        "HIGH",
        "CRITICAL"
    })
    void testValueOf(String name) {
        assertNotNull(RiskLevel.valueOf(name));
    }
}
