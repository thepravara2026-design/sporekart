package com.sporekart.ai.risk.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RiskCategoryTest {

    @ParameterizedTest
    @EnumSource(RiskCategory.class)
    void shouldContainAllExpectedValues(RiskCategory category) {
        assertNotNull(category);
        assertNotNull(category.name());
    }

    @ParameterizedTest
    @EnumSource(value = RiskCategory.class, names = { "TECHNICAL", "OPERATIONAL", "COMPLIANCE", "REPUTATIONAL", "FINANCIAL", "SECURITY", "PRIVACY", "ETHICAL" })
    void shouldHaveEightValues(RiskCategory category) {
        assertTrue(category.ordinal() >= 0);
    }

    @Test
    void technicalShouldBeFirst() {
        assertEquals(0, RiskCategory.TECHNICAL.ordinal());
    }

    @Test
    void ethicalShouldBeLast() {
        assertEquals(7, RiskCategory.ETHICAL.ordinal());
    }

    @Test
    void valueOfShouldWorkForAll() {
        assertEquals(RiskCategory.TECHNICAL, RiskCategory.valueOf("TECHNICAL"));
        assertEquals(RiskCategory.OPERATIONAL, RiskCategory.valueOf("OPERATIONAL"));
        assertEquals(RiskCategory.COMPLIANCE, RiskCategory.valueOf("COMPLIANCE"));
        assertEquals(RiskCategory.REPUTATIONAL, RiskCategory.valueOf("REPUTATIONAL"));
        assertEquals(RiskCategory.FINANCIAL, RiskCategory.valueOf("FINANCIAL"));
        assertEquals(RiskCategory.SECURITY, RiskCategory.valueOf("SECURITY"));
        assertEquals(RiskCategory.PRIVACY, RiskCategory.valueOf("PRIVACY"));
        assertEquals(RiskCategory.ETHICAL, RiskCategory.valueOf("ETHICAL"));
    }
}
