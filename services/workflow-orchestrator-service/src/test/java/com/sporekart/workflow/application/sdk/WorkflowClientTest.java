package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.application.service.*;
import com.sporekart.workflow.domain.engine.*;
import com.sporekart.workflow.domain.model.WorkflowState;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.cache.WorkflowCacheService;
import com.sporekart.workflow.infrastructure.executor.MockActionExecutorService;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import com.sporekart.workflow.infrastructure.queue.MockWorkflowQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowClientTest {
    private WorkflowClient client;
    private WorkflowExecutionEngine executionEngine;
    private WorkflowRepositoryPort repository;
    private String definitionId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        var stateMachine = new WorkflowStateMachine();
        var decisionEngine = new WorkflowDecisionEngine();
        var definitionEngine = new WorkflowDefinitionEngine(repository);
        executionEngine = new WorkflowExecutionEngine(repository, stateMachine);
        var simulationEngine = new WorkflowSimulationEngine(repository, decisionEngine);
        var auditEngine = new WorkflowAuditEngine(repository);
        var registryService = new WorkflowRegistryService(repository, definitionEngine);
        var telemetry = new WorkflowTelemetryService(repository);
        var queueService = new MockWorkflowQueueService(repository);
        var actionExecutor = new MockActionExecutorService();
        var cacheService = new WorkflowCacheService(300, 500);
        var service = new WorkflowOrchestratorService(repository, executionEngine, simulationEngine,
            auditEngine, registryService, telemetry, queueService, actionExecutor, cacheService, stateMachine);
        client = new WorkflowClient(service);
        var defs = client.generateAllDefinitions();
        definitionId = defs.getFirst().id();
    }

    private String runInstance(String instanceId) {
        var queued = repository.findInstanceById(instanceId).orElseThrow();
        var pending = executionEngine.transitionState(queued.id(), WorkflowState.PENDING, "PROCESS", "user");
        var running = executionEngine.transitionState(pending.id(), WorkflowState.RUNNING, "EXECUTE", "user");
        return running.id();
    }

    @Test
    void shouldReturnHealth() {
        var health = client.health();
        assertEquals("UP", health.get("status"));
    }

    @Test
    void shouldListDefinitions() {
        assertFalse(client.getAllDefinitions().isEmpty());
    }

    @Test
    void shouldStartWorkflow() {
        var instance = client.startWorkflow(definitionId, "user", Map.of());
        assertNotNull(instance);
    }

    @Test
    void shouldStartSimulation() {
        var instance = client.startSimulation(definitionId, "user", Map.of());
        assertTrue(instance.simulationMode());
    }

    @Test
    void shouldPauseResumeCancel() {
        var instance = client.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var paused = client.pauseWorkflow(runningId, "user");
        assertEquals("PAUSED", paused.state().name());
        var resumed = client.resumeWorkflow(paused.id(), "user");
        assertEquals("RUNNING", resumed.state().name());
        var cancelled = client.cancelWorkflow(resumed.id(), "user");
        assertEquals("CANCELLED", cancelled.state().name());
    }

    @Test
    void shouldSimulate() {
        var instance = client.startWorkflow(definitionId, "user", Map.of());
        var sim = client.simulateWorkflow(instance.id());
        assertNotNull(sim);
    }

    @Test
    void shouldGetTelemetry() {
        assertNotNull(client.getTelemetry());
    }

    @Test
    void shouldGetWorkflowHealth() {
        assertNotNull(client.getWorkflowHealth());
    }
}
