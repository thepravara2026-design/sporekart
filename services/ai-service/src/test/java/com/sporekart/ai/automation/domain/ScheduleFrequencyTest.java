package com.sporekart.ai.automation.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ScheduleFrequencyTest {

    static Stream<ScheduleFrequency> provideValues() {
        return Stream.of(ScheduleFrequency.values());
    }

    @ParameterizedTest
    @MethodSource("provideValues")
    void allEnumValuesShouldBeDefined(ScheduleFrequency frequency) {
        assertNotNull(frequency);
        assertNotNull(frequency.name());
    }

    @Test
    void shouldHaveExpectedCount() {
        assertEquals(6, ScheduleFrequency.values().length);
    }
}
