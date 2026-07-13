package com.sporekart.ai.compliance.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class ViolationSeverityTest {

    @ParameterizedTest
    @CsvSource({
        "INFO, 0",
        "WARNING, 1",
        "MINOR, 2",
        "MAJOR, 3",
        "CRITICAL, 4"
    })
    void testViolationSeverityValues(String name, int ordinal) {
        ViolationSeverity severity = ViolationSeverity.valueOf(name);
        assertEquals(name, severity.name());
        assertEquals(ordinal, severity.ordinal());
    }

    @ParameterizedTest
    @CsvSource({
        "INFO",
        "WARNING",
        "MINOR",
        "MAJOR",
        "CRITICAL"
    })
    void testValueOf(String name) {
        assertNotNull(ViolationSeverity.valueOf(name));
    }
}
