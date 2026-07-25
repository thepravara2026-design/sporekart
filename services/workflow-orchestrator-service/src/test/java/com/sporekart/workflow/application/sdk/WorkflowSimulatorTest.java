package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.domain.engine.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowSimulatorTest {
    private WorkflowSimulator simulator;
    private WorkflowDefinitionEngine definitionEngine;
    private WorkflowExecutionEngine executionEngine;
    private WorkflowRepositoryPort repository;
    private String instanceId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        var stateMachine = new WorkflowStateMachine();
        var decisionEngine = new WorkflowDecisionEngine();
        definitionEngine = new WorkflowDefinitionEngine(repository);
        executionEngine = new WorkflowExecutionEngine(repository, stateMachine);
        var simulationEngine = new WorkflowSimulationEngine(repository, decisionEngine);
        simulator = new WorkflowSimulator(simulationEngine);
        var defs = definitionEngine.generateAllDefinitions();
        var instance = executionEngine.startWorkflow(defs.getFirst().id(), "user", Map.of());
        instanceId = instance.id();
    }

    @Test
    void shouldSimulate() {
        var sim = simulator.simulate(instanceId);
        assertNotNull(sim);
    }

    @Test
    void shouldSimulateRollback() {
        var rollback = simulator.simulateRollback(instanceId);
        assertNotNull(rollback);
    }

    @Test
    void shouldDryRun() {
        var defs = repository.findAllDefinitions();
        var dryRun = simulator.dryRun(defs.getFirst().id(), Map.of("key", "value"));
        assertNotNull(dryRun);
    }
}
