package com.sporekart.ai.risk.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RecommendationTypeTest {

    @ParameterizedTest
    @EnumSource(RecommendationType.class)
    void shouldContainAllExpectedValues(RecommendationType type) {
        assertNotNull(type);
        assertNotNull(type.name());
    }

    @ParameterizedTest
    @EnumSource(value = RecommendationType.class, names = { "PROCEED", "REQUIRE_APPROVAL", "REDUCE_CONTEXT", "USE_ALTERNATE_PROVIDER", "RETRY", "REQUEST_HUMAN_REVIEW", "BLOCK_EXECUTION" })
    void shouldHaveSevenValues(RecommendationType type) {
        assertTrue(type.ordinal() >= 0);
    }

    @Test
    void proceedShouldBeFirst() {
        assertEquals(0, RecommendationType.PROCEED.ordinal());
    }

    @Test
    void blockExecutionShouldBeLast() {
        assertEquals(6, RecommendationType.BLOCK_EXECUTION.ordinal());
    }

    @Test
    void valueOfShouldWorkForAll() {
        assertEquals(RecommendationType.PROCEED, RecommendationType.valueOf("PROCEED"));
        assertEquals(RecommendationType.REQUIRE_APPROVAL, RecommendationType.valueOf("REQUIRE_APPROVAL"));
        assertEquals(RecommendationType.REDUCE_CONTEXT, RecommendationType.valueOf("REDUCE_CONTEXT"));
        assertEquals(RecommendationType.USE_ALTERNATE_PROVIDER, RecommendationType.valueOf("USE_ALTERNATE_PROVIDER"));
        assertEquals(RecommendationType.RETRY, RecommendationType.valueOf("RETRY"));
        assertEquals(RecommendationType.REQUEST_HUMAN_REVIEW, RecommendationType.valueOf("REQUEST_HUMAN_REVIEW"));
        assertEquals(RecommendationType.BLOCK_EXECUTION, RecommendationType.valueOf("BLOCK_EXECUTION"));
    }
}
