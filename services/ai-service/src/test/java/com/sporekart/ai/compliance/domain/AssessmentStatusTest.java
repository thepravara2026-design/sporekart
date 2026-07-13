package com.sporekart.ai.compliance.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class AssessmentStatusTest {

    @ParameterizedTest
    @CsvSource({
        "PLANNED, 0",
        "IN_PROGRESS, 1",
        "COMPLETED, 2",
        "FAILED, 3",
        "CANCELLED, 4"
    })
    void testAssessmentStatusValues(String name, int ordinal) {
        AssessmentStatus status = AssessmentStatus.valueOf(name);
        assertEquals(name, status.name());
        assertEquals(ordinal, status.ordinal());
    }

    @ParameterizedTest
    @CsvSource({
        "PLANNED",
        "IN_PROGRESS",
        "COMPLETED",
        "FAILED",
        "CANCELLED"
    })
    void testValueOf(String name) {
        assertNotNull(AssessmentStatus.valueOf(name));
    }
}
