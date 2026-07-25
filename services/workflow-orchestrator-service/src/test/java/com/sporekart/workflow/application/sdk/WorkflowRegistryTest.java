package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.application.service.WorkflowRegistryService;
import com.sporekart.workflow.domain.engine.WorkflowDefinitionEngine;
import com.sporekart.workflow.domain.model.WorkflowType;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowRegistryTest {
    private WorkflowRegistry registry;

    @BeforeEach
    void setUp() {
        var repository = new InMemoryWorkflowRepository();
        var definitionEngine = new WorkflowDefinitionEngine(repository);
        var registryService = new WorkflowRegistryService(repository, definitionEngine);
        registry = new WorkflowRegistry(registryService);
    }

    @Test
    void shouldGenerateAll() {
        var defs = registry.generateAll();
        assertFalse(defs.isEmpty());
    }

    @Test
    void shouldGetByType() {
        registry.generateAll();
        assertFalse(registry.getDefinitionsByType(WorkflowType.ORDER).isEmpty());
    }

    @Test
    void shouldCreate() {
        var def = registry.create("New", "Desc", WorkflowType.AUTOMATION, "Domain", "owner", "1.0.0", List.of(), Map.of());
        assertNotNull(def);
    }
}
