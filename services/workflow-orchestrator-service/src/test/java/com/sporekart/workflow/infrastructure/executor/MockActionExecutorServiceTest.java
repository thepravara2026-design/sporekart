package com.sporekart.workflow.infrastructure.executor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MockActionExecutorServiceTest {
    private MockActionExecutorService executor;

    @BeforeEach
    void setUp() {
        executor = new MockActionExecutorService();
    }

    @Test
    void shouldExecuteAction() {
        var action = executor.executeAction("Test", "execution", "Test desc", Map.of("key", "value"));
        assertNotNull(action);
        assertNotNull(action.id());
    }

    @Test
    void shouldExecuteActions() {
        var actions = List.of(
            Map.<String, Object>of("name", "A1", "type", "execution", "description", "First", "input", Map.of()),
            Map.<String, Object>of("name", "A2", "type", "validation", "description", "Second", "input", Map.of())
        );
        var results = executor.executeActions(actions);
        assertEquals(2, results.size());
    }

    @Test
    void shouldExecuteRollback() {
        var actions = executor.executeActions(List.of(
            Map.<String, Object>of("name", "A1", "type", "execution", "description", "First", "input", Map.of()),
            Map.<String, Object>of("name", "A2", "type", "execution", "description", "Second", "input", Map.of())
        ));
        var rollbacks = executor.executeRollback(actions);
        assertEquals(2, rollbacks.size());
        assertTrue(rollbacks.getFirst().name().startsWith("ROLLBACK_"));
    }

    @Test
    void shouldHandleEmptyInput() {
        var action = executor.executeAction("Test", "execution", "Test", null);
        assertNotNull(action);
    }
}
