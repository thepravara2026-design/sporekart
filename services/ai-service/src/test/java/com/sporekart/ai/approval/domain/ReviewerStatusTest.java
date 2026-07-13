package com.sporekart.ai.approval.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ReviewerStatusTest {

    @ParameterizedTest
    @EnumSource(ReviewerType.class)
    void shouldHaveValidNameAndOrdinal(ReviewerType type) {
        assertNotNull(type.name());
        assertTrue(type.ordinal() >= 0);
    }

    @ParameterizedTest
    @EnumSource(ReviewerType.class)
    void shouldBeParseable(ReviewerType type) {
        assertEquals(type, ReviewerType.valueOf(type.name()));
    }
}
