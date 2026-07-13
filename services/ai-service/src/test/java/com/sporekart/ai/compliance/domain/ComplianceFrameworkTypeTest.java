package com.sporekart.ai.compliance.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class ComplianceFrameworkTypeTest {

    @ParameterizedTest
    @CsvSource({
        "INTERNAL_AI_GOVERNANCE, 0",
        "RESPONSIBLE_AI, 1",
        "GDPR, 2",
        "ISO_42001, 3",
        "ISO_27001, 4",
        "SOC_2, 5",
        "CUSTOM, 6"
    })
    void testComplianceFrameworkTypeValues(String name, int ordinal) {
        ComplianceFrameworkType type = ComplianceFrameworkType.valueOf(name);
        assertEquals(name, type.name());
        assertEquals(ordinal, type.ordinal());
    }

    @ParameterizedTest
    @CsvSource({
        "INTERNAL_AI_GOVERNANCE",
        "RESPONSIBLE_AI",
        "GDPR",
        "ISO_42001",
        "ISO_27001",
        "SOC_2",
        "CUSTOM"
    })
    void testValueOf(String name) {
        assertNotNull(ComplianceFrameworkType.valueOf(name));
    }
}
