package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.WorkflowType;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowDefinitionEngineTest {
    private WorkflowDefinitionEngine engine;
    private WorkflowRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        engine = new WorkflowDefinitionEngine(repository);
    }

    @Test
    void shouldGenerateAllDefinitions() {
        var defs = engine.generateAllDefinitions();
        assertFalse(defs.isEmpty());
        assertTrue(defs.size() >= 14);
    }

    @Test
    void shouldGenerateOrderDefinitions() {
        var defs = engine.generateAllDefinitions();
        var orderDefs = defs.stream().filter(d -> d.type() == WorkflowType.ORDER).toList();
        assertFalse(orderDefs.isEmpty());
    }

    @Test
    void shouldGenerateInventoryDefinitions() {
        var defs = engine.generateAllDefinitions();
        var inventoryDefs = defs.stream().filter(d -> d.type() == WorkflowType.INVENTORY).toList();
        assertFalse(inventoryDefs.isEmpty());
    }

    @Test
    void shouldGenerateTrainingDefinitions() {
        var defs = engine.generateAllDefinitions();
        var trainingDefs = defs.stream().filter(d -> d.type() == WorkflowType.TRAINING).toList();
        assertFalse(trainingDefs.isEmpty());
    }

    @Test
    void shouldGenerateVendorDefinitions() {
        var defs = engine.generateAllDefinitions();
        var vendorDefs = defs.stream().filter(d -> d.type() == WorkflowType.VENDOR).toList();
        assertFalse(vendorDefs.isEmpty());
    }

    @Test
    void shouldGenerateGrowerDefinitions() {
        var defs = engine.generateAllDefinitions();
        var growerDefs = defs.stream().filter(d -> d.type() == WorkflowType.GROWER).toList();
        assertFalse(growerDefs.isEmpty());
    }

    @Test
    void shouldGenerateCustomerDefinitions() {
        var defs = engine.generateAllDefinitions();
        var customerDefs = defs.stream().filter(d -> d.type() == WorkflowType.CUSTOMER_LIFECYCLE).toList();
        assertFalse(customerDefs.isEmpty());
    }

    @Test
    void shouldGenerateMarketingDefinitions() {
        var defs = engine.generateAllDefinitions();
        var marketingDefs = defs.stream().filter(d -> d.type() == WorkflowType.MARKETING).toList();
        assertFalse(marketingDefs.isEmpty());
    }

    @Test
    void shouldGenerateExecutiveDefinitions() {
        var defs = engine.generateAllDefinitions();
        var execDefs = defs.stream().filter(d -> d.type() == WorkflowType.EXECUTIVE).toList();
        assertFalse(execDefs.isEmpty());
    }

    @Test
    void shouldGenerateAiDefinitions() {
        var defs = engine.generateAllDefinitions();
        var aiDefs = defs.stream().filter(d -> d.type() == WorkflowType.AI).toList();
        assertFalse(aiDefs.isEmpty());
    }

    @Test
    void shouldGenerateAutomationDefinitions() {
        var defs = engine.generateAllDefinitions();
        var autoDefs = defs.stream().filter(d -> d.type() == WorkflowType.AUTOMATION).toList();
        assertFalse(autoDefs.isEmpty());
    }

    @Test
    void shouldPersistDefinitionsToRepository() {
        engine.generateAllDefinitions();
        var stored = repository.findAllDefinitions();
        assertFalse(stored.isEmpty());
    }

    @Test
    void shouldCreateDefinitionsWithSteps() {
        var defs = engine.generateAllDefinitions();
        assertTrue(defs.stream().allMatch(d -> !d.steps().isEmpty()));
    }
}
