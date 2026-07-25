package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.infrastructure.executor.MockActionExecutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowExecutorTest {
    private WorkflowExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new WorkflowExecutor(new MockActionExecutorService());
    }

    @Test
    void shouldExecuteAction() {
        var action = executor.execute("Test Action", "execution", "Test", Map.of("key", "value"));
        assertNotNull(action);
        assertEquals("Test Action", action.name());
    }

    @Test
    void shouldExecuteBatch() {
        var actions = List.of(
            Map.<String, Object>of("name", "Action 1", "type", "execution", "description", "First"),
            Map.<String, Object>of("name", "Action 2", "type", "validation", "description", "Second")
        );
        var results = executor.executeBatch(actions);
        assertEquals(2, results.size());
    }

    @Test
    void shouldRollback() {
        var executed = executor.executeBatch(List.of(
            Map.<String, Object>of("name", "Action 1", "type", "execution", "description", "First")
        ));
        var rollbacks = executor.rollback(executed);
        assertFalse(rollbacks.isEmpty());
        assertTrue(rollbacks.getFirst().name().startsWith("ROLLBACK_"));
    }
}
