package com.sporekart.workflow.application.service;

import com.sporekart.workflow.domain.engine.WorkflowDefinitionEngine;
import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorkflowRegistryService {
    private final WorkflowRepositoryPort repository;
    private final WorkflowDefinitionEngine definitionEngine;

    public WorkflowRegistryService(WorkflowRepositoryPort repository, WorkflowDefinitionEngine definitionEngine) {
        this.repository = repository;
        this.definitionEngine = definitionEngine;
    }

    public List<WorkflowDefinition> listDefinitions() {
        return repository.findAllDefinitions();
    }

    public Optional<WorkflowDefinition> getDefinition(String id) {
        return repository.findDefinitionById(id);
    }

    public List<WorkflowDefinition> getDefinitionsByType(WorkflowType type) {
        return repository.findDefinitionsByType(type);
    }

    public List<WorkflowDefinition> getDefinitionsByDomain(String domain) {
        return repository.findDefinitionsByDomain(domain);
    }

    public List<WorkflowDefinition> getActiveDefinitions() {
        return repository.findActiveDefinitions();
    }

    public List<WorkflowDefinition> generateDefinitions() {
        return definitionEngine.generateAllDefinitions();
    }

    public WorkflowDefinition createDefinition(String name, String description, WorkflowType type,
                                                String domain, String owner, String version,
                                                List<WorkflowStep> steps, Map<String, Object> config) {
        var def = WorkflowDefinition.create(name, description, type, domain, owner, version, steps, config, Map.of());
        return repository.saveDefinition(def);
    }

    public WorkflowDefinition updateDefinitionStatus(String id, WorkflowStatus status) {
        var def = repository.findDefinitionById(id)
            .orElseThrow(() -> new IllegalArgumentException("Definition not found: " + id));
        var updated = def.withStatus(status);
        return repository.saveDefinition(updated);
    }

    public void deleteDefinition(String id) {
        var def = repository.findDefinitionById(id)
            .orElseThrow(() -> new IllegalArgumentException("Definition not found: " + id));
        repository.saveDefinition(def.withStatus(WorkflowStatus.ARCHIVED));
    }
}
