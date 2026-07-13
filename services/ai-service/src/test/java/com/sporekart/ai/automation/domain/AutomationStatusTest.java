package com.sporekart.ai.automation.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class AutomationStatusTest {

    static Stream<AutomationStatus> provideValues() {
        return Stream.of(AutomationStatus.values());
    }

    @ParameterizedTest
    @MethodSource("provideValues")
    void allEnumValuesShouldBeDefined(AutomationStatus status) {
        assertNotNull(status);
        assertNotNull(status.name());
    }

    @Test
    void shouldHaveExpectedCount() {
        assertEquals(7, AutomationStatus.values().length);
    }
}
