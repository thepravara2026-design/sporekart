package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.WorkflowState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowStateMachineTest {
    private WorkflowStateMachine stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine = new WorkflowStateMachine();
    }

    @Test
    void shouldAllowValidTransition() {
        assertTrue(stateMachine.isValidTransition(WorkflowState.CREATED, WorkflowState.QUEUED));
        assertTrue(stateMachine.isValidTransition(WorkflowState.QUEUED, WorkflowState.PENDING));
        assertTrue(stateMachine.isValidTransition(WorkflowState.PENDING, WorkflowState.RUNNING));
        assertTrue(stateMachine.isValidTransition(WorkflowState.RUNNING, WorkflowState.COMPLETED));
        assertTrue(stateMachine.isValidTransition(WorkflowState.RUNNING, WorkflowState.FAILED));
        assertTrue(stateMachine.isValidTransition(WorkflowState.RUNNING, WorkflowState.PAUSED));
        assertTrue(stateMachine.isValidTransition(WorkflowState.PAUSED, WorkflowState.RUNNING));
        assertTrue(stateMachine.isValidTransition(WorkflowState.FAILED, WorkflowState.RETRYING));
        assertTrue(stateMachine.isValidTransition(WorkflowState.COMPLETED, WorkflowState.ARCHIVED));
    }

    @Test
    void shouldRejectInvalidTransition() {
        assertFalse(stateMachine.isValidTransition(WorkflowState.CREATED, WorkflowState.COMPLETED));
        assertFalse(stateMachine.isValidTransition(WorkflowState.CREATED, WorkflowState.RUNNING));
        assertFalse(stateMachine.isValidTransition(WorkflowState.QUEUED, WorkflowState.COMPLETED));
        assertFalse(stateMachine.isValidTransition(WorkflowState.COMPLETED, WorkflowState.RUNNING));
        assertFalse(stateMachine.isValidTransition(WorkflowState.ARCHIVED, WorkflowState.RUNNING));
    }

    @Test
    void shouldTransitionValidStates() {
        assertEquals(WorkflowState.QUEUED, stateMachine.transition(WorkflowState.CREATED, WorkflowState.QUEUED));
        assertEquals(WorkflowState.CANCELLED, stateMachine.transition(WorkflowState.CREATED, WorkflowState.CANCELLED));
    }

    @Test
    void shouldThrowOnInvalidTransition() {
        assertThrows(IllegalStateException.class,
            () -> stateMachine.transition(WorkflowState.CREATED, WorkflowState.COMPLETED));
    }

    @Test
    void shouldReturnAllowedTransitions() {
        var allowed = stateMachine.getAllowedTransitions(WorkflowState.RUNNING);
        assertTrue(allowed.contains(WorkflowState.PAUSED));
        assertTrue(allowed.contains(WorkflowState.COMPLETED));
        assertTrue(allowed.contains(WorkflowState.FAILED));
    }

    @Test
    void shouldReturnEmptyForTerminalStates() {
        assertTrue(stateMachine.getAllowedTransitions(WorkflowState.ARCHIVED).isEmpty());
    }

    @Test
    void shouldFindTransitionPath() {
        var path = stateMachine.getTransitionPath(WorkflowState.CREATED, WorkflowState.COMPLETED);
        assertFalse(path.isEmpty());
        assertEquals(WorkflowState.CREATED, path.getFirst());
        assertEquals(WorkflowState.COMPLETED, path.getLast());
    }

    @Test
    void shouldReturnEmptyForUnreachable() {
        var path = stateMachine.getTransitionPath(WorkflowState.ARCHIVED, WorkflowState.RUNNING);
        assertTrue(path.isEmpty());
    }

    @Test
    void shouldIdentifyTerminalStates() {
        assertTrue(stateMachine.isTerminal(WorkflowState.COMPLETED));
        assertTrue(stateMachine.isTerminal(WorkflowState.CANCELLED));
        assertTrue(stateMachine.isTerminal(WorkflowState.ARCHIVED));
        assertFalse(stateMachine.isTerminal(WorkflowState.RUNNING));
    }

    @Test
    void shouldIdentifyActiveStates() {
        assertTrue(stateMachine.isActive(WorkflowState.RUNNING));
        assertTrue(stateMachine.isActive(WorkflowState.PAUSED));
        assertFalse(stateMachine.isActive(WorkflowState.COMPLETED));
        assertFalse(stateMachine.isActive(WorkflowState.ARCHIVED));
    }

    @Test
    void shouldReturnSingleStatePathForSameState() {
        assertEquals(List.of(WorkflowState.CREATED), stateMachine.getTransitionPath(WorkflowState.CREATED, WorkflowState.CREATED));
    }
}
