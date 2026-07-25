package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowSimulationEngineTest {
    private WorkflowSimulationEngine simulationEngine;
    private WorkflowExecutionEngine executionEngine;
    private WorkflowDefinitionEngine definitionEngine;
    private WorkflowRepositoryPort repository;
    private WorkflowDecisionEngine decisionEngine;
    private WorkflowStateMachine stateMachine;
    private String instanceId;
    private String simInstanceId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        stateMachine = new WorkflowStateMachine();
        decisionEngine = new WorkflowDecisionEngine();
        definitionEngine = new WorkflowDefinitionEngine(repository);
        executionEngine = new WorkflowExecutionEngine(repository, stateMachine);
        simulationEngine = new WorkflowSimulationEngine(repository, decisionEngine);
        var defs = definitionEngine.generateAllDefinitions();
        var defId = defs.getFirst().id();
        var instance = executionEngine.startWorkflow(defId, "user", Map.of());
        instanceId = instance.id();
        var simInstance = executionEngine.startSimulation(defId, "user", Map.of());
        simInstanceId = simInstance.id();
    }

    @Test
    void shouldSimulateWorkflow() {
        var simulation = simulationEngine.simulateWorkflow(instanceId);
        assertNotNull(simulation);
        assertFalse(simulation.actions().isEmpty());
        assertFalse(simulation.decisions().isEmpty());
    }

    @Test
    void shouldSimulateWithExecutionMode() {
        var simulation = simulationEngine.simulateWorkflow(simInstanceId);
        assertNotNull(simulation);
        assertNotNull(simulation.decisions());
    }

    @Test
    void shouldIncludeDecisionsInSimulation() {
        var simulation = simulationEngine.simulateWorkflow(instanceId);
        assertFalse(simulation.decisions().isEmpty());
    }

    @Test
    void shouldIncludeMockActionsInSimulation() {
        var simulation = simulationEngine.simulateWorkflow(instanceId);
        assertFalse(simulation.actions().isEmpty());
        assertTrue(simulation.actions().stream().anyMatch(a -> "EXECUTED".equals(a.status())));
    }

    @Test
    void shouldThrowOnInvalidInstance() {
        assertThrows(java.util.NoSuchElementException.class,
            () -> simulationEngine.simulateWorkflow("invalid-id"));
    }

    @Test
    void shouldSimulateRollback() {
        var rollback = simulationEngine.simulateRollback(instanceId);
        assertNotNull(rollback);
        assertTrue(rollback.success());
    }

    @Test
    void shouldHaveRollbackActions() {
        var rollback = simulationEngine.simulateRollback(instanceId);
        assertFalse(rollback.actions().isEmpty());
    }

    @Test
    void shouldRunDryRun() {
        var defs = repository.findAllDefinitions();
        var defId = defs.getFirst().id();
        var dryRun = simulationEngine.dryRunWorkflow(defId, Map.of("env", "test"));
        assertNotNull(dryRun);
        assertTrue(dryRun.success());
    }

    @Test
    void shouldCreateAuditForSimulation() {
        simulationEngine.simulateWorkflow(instanceId);
        var audits = repository.findAuditsByInstanceId(instanceId);
        assertFalse(audits.isEmpty());
    }
}
