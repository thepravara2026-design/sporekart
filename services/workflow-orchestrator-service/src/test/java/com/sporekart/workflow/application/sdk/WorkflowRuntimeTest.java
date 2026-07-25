package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.domain.engine.*;
import com.sporekart.workflow.domain.model.WorkflowState;
import com.sporekart.workflow.domain.model.WorkflowType;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowRuntimeTest {
    private WorkflowRuntime runtime;
    private WorkflowExecutionEngine executionEngine;
    private WorkflowRepositoryPort repository;
    private String definitionId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        var stateMachine = new WorkflowStateMachine();
        definitionEngine = new WorkflowDefinitionEngine(repository);
        executionEngine = new WorkflowExecutionEngine(repository, stateMachine);
        runtime = new WorkflowRuntime(executionEngine, repository);
        var defs = definitionEngine.generateAllDefinitions();
        definitionId = defs.getFirst().id();
    }

    private WorkflowDefinitionEngine definitionEngine;

    private String runInstance(String instanceId) {
        var queued = repository.findInstanceById(instanceId).orElseThrow();
        var pending = executionEngine.transitionState(queued.id(), WorkflowState.PENDING, "PROCESS", "user");
        var running = executionEngine.transitionState(pending.id(), WorkflowState.RUNNING, "EXECUTE", "user");
        return running.id();
    }

    @Test
    void shouldStartAndGetInstance() {
        var instance = runtime.start(definitionId, "user", Map.of());
        assertTrue(runtime.getInstance(instance.id()).isPresent());
    }

    @Test
    void shouldStartSimulation() {
        var instance = runtime.startSimulation(definitionId, "user", Map.of());
        assertTrue(instance.simulationMode());
    }

    @Test
    void shouldListAllInstances() {
        runtime.start(definitionId, "user", Map.of());
        assertFalse(runtime.getAllInstances().isEmpty());
    }

    @Test
    void shouldPauseAndResume() {
        var instance = runtime.start(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var paused = runtime.pause(runningId, "user");
        assertEquals("PAUSED", paused.state().name());
        var resumed = runtime.resume(paused.id(), "user");
        assertEquals("RUNNING", resumed.state().name());
    }
}
