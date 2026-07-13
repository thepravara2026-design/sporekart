package com.sporekart.ai.automation.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class WorkflowExecutionStatusTest {

    static Stream<WorkflowExecutionStatus> provideValues() {
        return Stream.of(WorkflowExecutionStatus.values());
    }

    @ParameterizedTest
    @MethodSource("provideValues")
    void allEnumValuesShouldBeDefined(WorkflowExecutionStatus status) {
        assertNotNull(status);
        assertNotNull(status.name());
    }

    @Test
    void shouldHaveExpectedCount() {
        assertEquals(7, WorkflowExecutionStatus.values().length);
    }
}
