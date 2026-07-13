package com.sporekart.ai.automation.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class LifecycleStateTypeTest {

    static Stream<LifecycleStateType> provideValues() {
        return Stream.of(LifecycleStateType.values());
    }

    @ParameterizedTest
    @MethodSource("provideValues")
    void allEnumValuesShouldBeDefined(LifecycleStateType state) {
        assertNotNull(state);
        assertNotNull(state.name());
    }

    @Test
    void shouldHaveExpectedCount() {
        assertEquals(10, LifecycleStateType.values().length);
    }
}
