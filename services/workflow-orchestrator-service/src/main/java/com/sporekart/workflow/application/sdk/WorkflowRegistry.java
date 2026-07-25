package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.application.service.WorkflowRegistryService;
import com.sporekart.workflow.domain.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorkflowRegistry {
    private final WorkflowRegistryService registryService;

    public WorkflowRegistry(WorkflowRegistryService registryService) {
        this.registryService = registryService;
    }

    public List<WorkflowDefinition> getAllDefinitions() {
        return registryService.listDefinitions();
    }

    public Optional<WorkflowDefinition> getDefinition(String id) {
        return registryService.getDefinition(id);
    }

    public List<WorkflowDefinition> getDefinitionsByType(WorkflowType type) {
        return registryService.getDefinitionsByType(type);
    }

    public List<WorkflowDefinition> getActiveDefinitions() {
        return registryService.getActiveDefinitions();
    }

    public List<WorkflowDefinition> generateAll() {
        return registryService.generateDefinitions();
    }

    public WorkflowDefinition create(String name, String description, WorkflowType type,
                                      String domain, String owner, String version,
                                      List<WorkflowStep> steps, Map<String, Object> config) {
        return registryService.createDefinition(name, description, type, domain, owner, version, steps, config);
    }
}
