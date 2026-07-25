package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowExecutionEngineTest {
    private WorkflowExecutionEngine executionEngine;
    private WorkflowDefinitionEngine definitionEngine;
    private WorkflowRepositoryPort repository;
    private WorkflowStateMachine stateMachine;
    private String definitionId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        stateMachine = new WorkflowStateMachine();
        definitionEngine = new WorkflowDefinitionEngine(repository);
        executionEngine = new WorkflowExecutionEngine(repository, stateMachine);
        var defs = definitionEngine.generateAllDefinitions();
        definitionId = defs.getFirst().id();
    }

    @Test
    void shouldStartWorkflow() {
        var instance = executionEngine.startWorkflow(definitionId, "test-user", Map.of("key", "value"));
        assertNotNull(instance);
        assertEquals(WorkflowState.QUEUED, instance.state());
        assertEquals("test-user", instance.triggeredBy());
    }

    @Test
    void shouldStartSimulation() {
        var instance = executionEngine.startSimulation(definitionId, "test-user", Map.of());
        assertNotNull(instance);
        assertTrue(instance.simulationMode());
    }

    @Test
    void shouldThrowOnInvalidDefinitionId() {
        assertThrows(java.util.NoSuchElementException.class,
            () -> executionEngine.startWorkflow("invalid-id", "user", Map.of()));
    }

    private String runInstance(String instanceId) {
        var queued = repository.findInstanceById(instanceId).orElseThrow();
        var pending = executionEngine.transitionState(queued.id(), WorkflowState.PENDING, "PROCESS", "user");
        var running = executionEngine.transitionState(pending.id(), WorkflowState.RUNNING, "EXECUTE", "user");
        return running.id();
    }

    @Test
    void shouldPauseWorkflow() {
        var instance = executionEngine.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var paused = executionEngine.pauseWorkflow(runningId, "user");
        assertEquals(WorkflowState.PAUSED, paused.state());
    }

    @Test
    void shouldResumeWorkflow() {
        var instance = executionEngine.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var paused = executionEngine.pauseWorkflow(runningId, "user");
        var resumed = executionEngine.resumeWorkflow(paused.id(), "user");
        assertEquals(WorkflowState.RUNNING, resumed.state());
    }

    @Test
    void shouldCancelWorkflow() {
        var instance = executionEngine.startWorkflow(definitionId, "user", Map.of());
        var cancelled = executionEngine.cancelWorkflow(instance.id(), "user");
        assertEquals(WorkflowState.CANCELLED, cancelled.state());
    }

    @Test
    void shouldRetryWorkflow() {
        var instance = executionEngine.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var failed = executionEngine.failWorkflow(runningId, "Test error", "user");
        var retried = executionEngine.retryWorkflow(failed.id(), "user");
        assertEquals(WorkflowState.RETRYING, retried.state());
        assertEquals(1, retried.retryCount());
    }

    @Test
    void shouldThrowOnExceededRetries() {
        var instance = executionEngine.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var failed = executionEngine.failWorkflow(runningId, "Error", "user");
        var r1 = executionEngine.retryWorkflow(failed.id(), "user");
        var running2 = executionEngine.transitionState(r1.id(), WorkflowState.RUNNING, "EXECUTE", "user");
        var failed2 = executionEngine.failWorkflow(running2.id(), "Error2", "user");
        var r2 = executionEngine.retryWorkflow(failed2.id(), "user");
        var running3 = executionEngine.transitionState(r2.id(), WorkflowState.RUNNING, "EXECUTE", "user");
        var failed3 = executionEngine.failWorkflow(running3.id(), "Error3", "user");
        var r3 = executionEngine.retryWorkflow(failed3.id(), "user");
        var running4 = executionEngine.transitionState(r3.id(), WorkflowState.RUNNING, "EXECUTE", "user");
        var failed4 = executionEngine.failWorkflow(running4.id(), "Error4", "user");
        assertThrows(IllegalStateException.class, () -> executionEngine.retryWorkflow(failed4.id(), "user"));
    }

    @Test
    void shouldCompleteWorkflow() {
        var instance = executionEngine.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var completed = executionEngine.completeWorkflow(runningId, Map.of("result", "success"), "user");
        assertEquals(WorkflowState.COMPLETED, completed.state());
    }

    @Test
    void shouldFailWorkflow() {
        var instance = executionEngine.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var failed = executionEngine.failWorkflow(runningId, "Critical error", "user");
        assertEquals(WorkflowState.FAILED, failed.state());
    }

    @Test
    void shouldRequestApproval() {
        var instance = executionEngine.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var waiting = executionEngine.requestApproval(runningId, "user");
        assertEquals(WorkflowState.WAITING_APPROVAL, waiting.state());
    }

    @Test
    void shouldArchiveWorkflow() {
        var instance = executionEngine.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var completed = executionEngine.completeWorkflow(runningId, Map.of(), "user");
        var archived = executionEngine.archiveWorkflow(completed.id(), "user");
        assertEquals(WorkflowState.ARCHIVED, archived.state());
    }

    @Test
    void shouldCreateAuditTrailOnStart() {
        executionEngine.startWorkflow(definitionId, "audit-user", Map.of());
        var audits = repository.findAllAudits();
        assertFalse(audits.isEmpty());
        assertTrue(audits.stream().anyMatch(a -> a.action().contains("WORKFLOW_STARTED")));
    }
}
