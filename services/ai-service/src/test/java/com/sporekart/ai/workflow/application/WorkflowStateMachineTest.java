package com.sporekart.ai.workflow.application;

import com.sporekart.ai.workflow.domain.WorkflowExecutionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowStateMachineTest {

    @Test
    void shouldTransitionFromPendingToRunning() {
        assertEquals(WorkflowExecutionStatus.RUNNING,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.PENDING, WorkflowExecutionStatus.RUNNING));
    }

    @Test
    void shouldTransitionFromPendingToCancelled() {
        assertEquals(WorkflowExecutionStatus.CANCELLED,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.PENDING, WorkflowExecutionStatus.CANCELLED));
    }

    @Test
    void shouldTransitionFromRunningToPaused() {
        assertEquals(WorkflowExecutionStatus.PAUSED,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.PAUSED));
    }

    @Test
    void shouldTransitionFromRunningToCompleted() {
        assertEquals(WorkflowExecutionStatus.COMPLETED,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.COMPLETED));
    }

    @Test
    void shouldTransitionFromRunningToFailed() {
        assertEquals(WorkflowExecutionStatus.FAILED,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.FAILED));
    }

    @Test
    void shouldTransitionFromRunningToCancelled() {
        assertEquals(WorkflowExecutionStatus.CANCELLED,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.CANCELLED));
    }

    @Test
    void shouldTransitionFromRunningToTimeout() {
        assertEquals(WorkflowExecutionStatus.TIMEOUT,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.TIMEOUT));
    }

    @Test
    void shouldTransitionFromPausedToRunning() {
        assertEquals(WorkflowExecutionStatus.RUNNING,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.PAUSED, WorkflowExecutionStatus.RUNNING));
    }

    @Test
    void shouldTransitionFromPausedToCancelled() {
        assertEquals(WorkflowExecutionStatus.CANCELLED,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.PAUSED, WorkflowExecutionStatus.CANCELLED));
    }

    @Test
    void shouldTransitionFromFailedToPending() {
        assertEquals(WorkflowExecutionStatus.PENDING,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.FAILED, WorkflowExecutionStatus.PENDING));
    }

    @Test
    void shouldTransitionFromTimeoutToPending() {
        assertEquals(WorkflowExecutionStatus.PENDING,
                WorkflowStateMachine.transition(WorkflowExecutionStatus.TIMEOUT, WorkflowExecutionStatus.PENDING));
    }

    @Test
    void shouldNotTransitionFromCompleted() {
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.COMPLETED, WorkflowExecutionStatus.RUNNING));
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.COMPLETED, WorkflowExecutionStatus.PAUSED));
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.COMPLETED, WorkflowExecutionStatus.FAILED));
    }

    @Test
    void shouldNotTransitionFromCancelled() {
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.CANCELLED, WorkflowExecutionStatus.RUNNING));
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.CANCELLED, WorkflowExecutionStatus.PENDING));
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.CANCELLED, WorkflowExecutionStatus.COMPLETED));
    }

    @Test
    void shouldNotTransitionFromPendingToCompleted() {
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.PENDING, WorkflowExecutionStatus.COMPLETED));
    }

    @Test
    void shouldNotTransitionFromPendingToFailed() {
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.PENDING, WorkflowExecutionStatus.FAILED));
    }

    @Test
    void shouldNotTransitionFromPendingToPaused() {
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.PENDING, WorkflowExecutionStatus.PAUSED));
    }

    @Test
    void shouldNotTransitionFromPendingToTimeout() {
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.PENDING, WorkflowExecutionStatus.TIMEOUT));
    }

    @Test
    void shouldThrowOnInvalidTransition() {
        assertThrows(IllegalStateException.class,
                () -> WorkflowStateMachine.transition(WorkflowExecutionStatus.COMPLETED, WorkflowExecutionStatus.RUNNING));
    }

    @Test
    void shouldNotTransitionFromRunningToPending() {
        assertFalse(WorkflowStateMachine.canTransition(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.PENDING));
    }
}
