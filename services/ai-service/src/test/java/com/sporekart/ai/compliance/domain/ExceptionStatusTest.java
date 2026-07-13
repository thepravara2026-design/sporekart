package com.sporekart.ai.compliance.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionStatusTest {

    @ParameterizedTest
    @CsvSource({
        "REQUESTED, 0",
        "APPROVED, 1",
        "REJECTED, 2",
        "EXPIRED, 3",
        "REVOKED, 4"
    })
    void testExceptionStatusValues(String name, int ordinal) {
        ExceptionStatus status = ExceptionStatus.valueOf(name);
        assertEquals(name, status.name());
        assertEquals(ordinal, status.ordinal());
    }

    @ParameterizedTest
    @CsvSource({
        "REQUESTED",
        "APPROVED",
        "REJECTED",
        "EXPIRED",
        "REVOKED"
    })
    void testValueOf(String name) {
        assertNotNull(ExceptionStatus.valueOf(name));
    }
}
