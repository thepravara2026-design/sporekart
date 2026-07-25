package com.sporekart.workflow.application.service;

import com.sporekart.workflow.domain.engine.WorkflowDefinitionEngine;
import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowRegistryServiceTest {
    private WorkflowRegistryService registryService;
    private WorkflowRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        var definitionEngine = new WorkflowDefinitionEngine(repository);
        registryService = new WorkflowRegistryService(repository, definitionEngine);
    }

    @Test
    void shouldListDefinitions() {
        registryService.generateDefinitions();
        assertFalse(registryService.listDefinitions().isEmpty());
    }

    @Test
    void shouldGetDefinition() {
        registryService.generateDefinitions();
        var defs = registryService.listDefinitions();
        var result = registryService.getDefinition(defs.getFirst().id());
        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyForUnknownDefinition() {
        var result = registryService.getDefinition("unknown");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetDefinitionsByType() {
        registryService.generateDefinitions();
        var orderDefs = registryService.getDefinitionsByType(WorkflowType.ORDER);
        assertFalse(orderDefs.isEmpty());
    }

    @Test
    void shouldGetActiveDefinitions() {
        registryService.generateDefinitions();
        assertFalse(registryService.getActiveDefinitions().isEmpty());
    }

    @Test
    void shouldGenerateDefinitions() {
        var defs = registryService.generateDefinitions();
        assertFalse(defs.isEmpty());
    }

    @Test
    void shouldCreateDefinition() {
        var steps = List.of(new WorkflowStep("Step 1", "validation", "desc", List.of(), Map.of(), 1));
        var def = registryService.createDefinition("Test", "Desc", WorkflowType.ORDER, "Domain", "owner", "1.0.0", steps, Map.of());
        assertNotNull(def);
        assertEquals("Test", def.name());
    }

    @Test
    void shouldThrowOnDeleteUnknown() {
        assertThrows(IllegalArgumentException.class, () -> registryService.updateDefinitionStatus("unknown", WorkflowStatus.ARCHIVED));
    }
}
