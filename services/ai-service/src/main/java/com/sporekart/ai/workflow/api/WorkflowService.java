package com.sporekart.ai.workflow.api;

import com.sporekart.ai.workflow.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowService {
    WorkflowDefinition createDefinition(String name, String description, String category, WorkflowTriggerType triggerType, String triggerConfig, UUID createdBy);
    Optional<WorkflowDefinition> getDefinition(UUID id);
    List<WorkflowDefinition> listDefinitions();
    WorkflowDefinition updateDefinition(UUID id, String name, String description, String category, WorkflowTriggerType triggerType, String triggerConfig);
    void deleteDefinition(UUID id);
    WorkflowDefinition publishDefinition(UUID id);
    WorkflowDefinition deactivateDefinition(UUID id);
    WorkflowDefinition cloneDefinition(UUID id, String newName);
    WorkflowStep addStep(UUID workflowId, String name, WorkflowStepType stepType, int orderIndex, String config, boolean isOptional, long timeoutMs, int maxRetries);
    List<WorkflowStep> getSteps(UUID workflowId);
    void removeStep(UUID workflowId, UUID stepId);
}
