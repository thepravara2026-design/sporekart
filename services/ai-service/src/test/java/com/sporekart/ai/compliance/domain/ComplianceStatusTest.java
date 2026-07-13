package com.sporekart.ai.compliance.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class ComplianceStatusTest {

    @ParameterizedTest
    @CsvSource({
        "PENDING, 0",
        "IN_PROGRESS, 1",
        "PASSED, 2",
        "FAILED, 3",
        "WAIVED, 4",
        "EXCEPTION_GRANTED, 5",
        "NOT_APPLICABLE, 6",
        "ERROR, 7"
    })
    void testComplianceStatusValues(String name, int ordinal) {
        ComplianceStatus status = ComplianceStatus.valueOf(name);
        assertEquals(name, status.name());
        assertEquals(ordinal, status.ordinal());
    }

    @ParameterizedTest
    @CsvSource({
        "PENDING",
        "IN_PROGRESS",
        "PASSED",
        "FAILED",
        "WAIVED",
        "EXCEPTION_GRANTED",
        "NOT_APPLICABLE",
        "ERROR"
    })
    void testValueOf(String name) {
        assertNotNull(ComplianceStatus.valueOf(name));
    }
}
