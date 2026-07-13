package com.sporekart.ai.compliance.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class ControlTypeTest {

    @ParameterizedTest
    @CsvSource({
        "PREVENTIVE, 0",
        "DETECTIVE, 1",
        "CORRECTIVE, 2",
        "DIRECTIVE, 3",
        "COMPENSATING, 4"
    })
    void testControlTypeValues(String name, int ordinal) {
        ControlType type = ControlType.valueOf(name);
        assertEquals(name, type.name());
        assertEquals(ordinal, type.ordinal());
    }

    @ParameterizedTest
    @CsvSource({
        "PREVENTIVE",
        "DETECTIVE",
        "CORRECTIVE",
        "DIRECTIVE",
        "COMPENSATING"
    })
    void testValueOf(String name) {
        assertNotNull(ControlType.valueOf(name));
    }
}
