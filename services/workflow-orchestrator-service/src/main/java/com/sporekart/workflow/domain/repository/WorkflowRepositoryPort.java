package com.sporekart.workflow.domain.repository;

import com.sporekart.workflow.domain.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkflowRepositoryPort {
    // Definitions
    List<WorkflowDefinition> findAllDefinitions();
    Optional<WorkflowDefinition> findDefinitionById(String id);
    List<WorkflowDefinition> findDefinitionsByType(WorkflowType type);
    List<WorkflowDefinition> findDefinitionsByDomain(String domain);
    List<WorkflowDefinition> findDefinitionsByOwner(String owner);
    List<WorkflowDefinition> findActiveDefinitions();
    WorkflowDefinition saveDefinition(WorkflowDefinition definition);
    void deleteDefinition(String id);

    // Instances
    List<WorkflowInstance> findAllInstances();
    Optional<WorkflowInstance> findInstanceById(String id);
    List<WorkflowInstance> findInstancesByState(WorkflowState state);
    List<WorkflowInstance> findInstancesByType(WorkflowType type);
    List<WorkflowInstance> findInstancesByDomain(String domain);
    List<WorkflowInstance> findInstancesByOwner(String owner);
    List<WorkflowInstance> findInstancesByDefinitionId(String definitionId);
    WorkflowInstance saveInstance(WorkflowInstance instance);
    void deleteInstance(String id);

    // Queue
    List<WorkflowQueue> findAllQueueItems();
    Optional<WorkflowQueue> findQueueItemById(String id);
    List<WorkflowQueue> findQueueByType(String queueType);
    List<WorkflowQueue> findQueueByState(WorkflowState state);
    List<WorkflowQueue> findQueueByPriority(int priority);
    WorkflowQueue saveQueueItem(WorkflowQueue item);
    void deleteQueueItem(String id);
    void clearQueue(String queueType);

    // Simulations
    List<WorkflowSimulation> findAllSimulations();
    Optional<WorkflowSimulation> findSimulationById(String id);
    List<WorkflowSimulation> findSimulationsByInstanceId(String instanceId);
    WorkflowSimulation saveSimulation(WorkflowSimulation simulation);

    // Audits
    List<WorkflowAudit> findAllAudits();
    Optional<WorkflowAudit> findAuditById(String id);
    List<WorkflowAudit> findAuditsByInstanceId(String instanceId);
    WorkflowAudit saveAudit(WorkflowAudit audit);

    // Approvals
    List<WorkflowApproval> findAllApprovals();
    Optional<WorkflowApproval> findApprovalById(String id);
    List<WorkflowApproval> findApprovalsByInstanceId(String instanceId);
    List<WorkflowApproval> findApprovalsByStatus(String status);
    List<WorkflowApproval> findApprovalsByAssignedTo(String assignedTo);
    WorkflowApproval saveApproval(WorkflowApproval approval);

    // Health
    Map<String, Object> getHealthMetrics();
}
