package com.sporekart.workflow.application.service;

import com.sporekart.workflow.domain.engine.*;
import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.cache.WorkflowCacheService;
import com.sporekart.workflow.infrastructure.executor.MockActionExecutorService;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import com.sporekart.workflow.infrastructure.queue.MockWorkflowQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowOrchestratorServiceTest {
    private WorkflowOrchestratorService service;
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

        service = new WorkflowOrchestratorService(repository, executionEngine, simulationEngine,
            auditEngine, registryService, telemetry, queueService, actionExecutor, cacheService, stateMachine);

        var defs = service.generateDefinitions();
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
        var health = service.health();
        assertEquals("UP", health.get("status"));
    }

    @Test
    void shouldListDefinitions() {
        assertFalse(service.listDefinitions().isEmpty());
    }

    @Test
    void shouldGetDefinition() {
        var result = service.getDefinition(definitionId);
        assertTrue(result.isPresent());
    }

    @Test
    void shouldStartWorkflow() {
        var instance = service.startWorkflow(definitionId, "user", Map.of());
        assertNotNull(instance);
        assertEquals(WorkflowState.QUEUED, instance.state());
    }

    @Test
    void shouldStartSimulation() {
        var instance = service.startSimulation(definitionId, "user", Map.of());
        assertTrue(instance.simulationMode());
    }

    @Test
    void shouldListInstances() {
        service.startWorkflow(definitionId, "user", Map.of());
        assertFalse(service.listInstances().isEmpty());
    }

    @Test
    void shouldPauseResumeAndCancel() {
        var instance = service.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var paused = service.pauseWorkflow(runningId, "user");
        assertEquals(WorkflowState.PAUSED, paused.state());
        var resumed = service.resumeWorkflow(paused.id(), "user");
        assertEquals(WorkflowState.RUNNING, resumed.state());
        var cancelled = service.cancelWorkflow(resumed.id(), "user");
        assertEquals(WorkflowState.CANCELLED, cancelled.state());
    }

    @Test
    void shouldRetryAndComplete() {
        var instance = service.startWorkflow(definitionId, "user", Map.of());
        var runningId = runInstance(instance.id());
        var failed = service.failWorkflow(runningId, "error", "user");
        assertEquals(WorkflowState.FAILED, failed.state());
        var retried = service.retryWorkflow(failed.id(), "user");
        assertEquals(WorkflowState.RETRYING, retried.state());
    }

    @Test
    void shouldSimulate() {
        var instance = service.startWorkflow(definitionId, "user", Map.of());
        var simulation = service.simulateWorkflow(instance.id());
        assertNotNull(simulation);
    }

    @Test
    void shouldSimulateRollback() {
        var instance = service.startWorkflow(definitionId, "user", Map.of());
        var rollback = service.simulateRollback(instance.id());
        assertNotNull(rollback);
    }

    @Test
    void shouldDryRun() {
        var dryRun = service.dryRunWorkflow(definitionId, Map.of());
        assertNotNull(dryRun);
    }

    @Test
    void shouldManageApprovals() {
        var approval = service.requestApproval("inst-1", "user1", "user2");
        assertEquals("PENDING", approval.status());
        var approved = service.approveWorkflow(approval.id(), "Looks good");
        assertEquals("APPROVED", approved.status());
    }

    @Test
    void shouldRejectApprovals() {
        var approval = service.requestApproval("inst-1", "user1", "user2");
        var rejected = service.rejectWorkflow(approval.id(), "Not approved");
        assertEquals("REJECTED", rejected.status());
    }

    @Test
    void shouldValidateTransitions() {
        assertTrue(service.isValidTransition(WorkflowState.CREATED, WorkflowState.QUEUED));
        assertFalse(service.isValidTransition(WorkflowState.CREATED, WorkflowState.COMPLETED));
    }

    @Test
    void shouldGetAllowedTransitions() {
        var allowed = service.getAllowedTransitions(WorkflowState.RUNNING);
        assertFalse(allowed.isEmpty());
    }

    @Test
    void shouldGetTransitionPath() {
        var path = service.getTransitionPath(WorkflowState.CREATED, WorkflowState.COMPLETED);
        assertFalse(path.isEmpty());
    }

    @Test
    void shouldGetTelemetry() {
        var telemetry = service.getTelemetry();
        assertNotNull(telemetry);
    }

    @Test
    void shouldGetWorkflowHealth() {
        var health = service.getWorkflowHealth();
        assertNotNull(health.get("status"));
    }

    @Test
    void shouldGetAuditTrail() {
        var instance = service.startWorkflow(definitionId, "user", Map.of());
        var audits = service.getAuditTrail(instance.id());
        assertFalse(audits.isEmpty());
    }

    @Test
    void shouldManageCache() {
        var cacheInfo = service.getCacheInfo();
        assertNotNull(cacheInfo);
        service.clearCache();
        assertNotNull(service.getCacheInfo());
    }

    @Test
    void shouldManageQueue() {
        service.startWorkflow(definitionId, "user", Map.of());
        var queue = service.listQueue();
        assertFalse(queue.isEmpty());
        var metrics = service.getQueueMetrics();
        assertNotNull(metrics.get("total"));
    }
}
