package com.sporekart.ai.automation.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class JobTypeTest {

    static Stream<JobType> provideValues() {
        return Stream.of(JobType.values());
    }

    @ParameterizedTest
    @MethodSource("provideValues")
    void allEnumValuesShouldBeDefined(JobType type) {
        assertNotNull(type);
        assertNotNull(type.name());
    }

    @Test
    void shouldHaveExpectedCount() {
        assertEquals(9, JobType.values().length);
    }
}
