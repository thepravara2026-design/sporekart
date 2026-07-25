package com.sporekart.workflow.infrastructure.persistence;

import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryWorkflowRepository implements WorkflowRepositoryPort {
    private final Map<String, WorkflowDefinition> definitions = new ConcurrentHashMap<>();
    private final Map<String, WorkflowInstance> instances = new ConcurrentHashMap<>();
    private final Map<String, WorkflowQueue> queueItems = new ConcurrentHashMap<>();
    private final Map<String, WorkflowSimulation> simulations = new ConcurrentHashMap<>();
    private final Map<String, WorkflowAudit> audits = new ConcurrentHashMap<>();
    private final Map<String, WorkflowApproval> approvals = new ConcurrentHashMap<>();

    @Override
    public List<WorkflowDefinition> findAllDefinitions() {
        return List.copyOf(definitions.values());
    }

    @Override
    public Optional<WorkflowDefinition> findDefinitionById(String id) {
        return Optional.ofNullable(definitions.get(id));
    }

    @Override
    public List<WorkflowDefinition> findDefinitionsByType(WorkflowType type) {
        return definitions.values().stream().filter(d -> d.type() == type).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowDefinition> findDefinitionsByDomain(String domain) {
        return definitions.values().stream().filter(d -> d.domain().equals(domain)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowDefinition> findDefinitionsByOwner(String owner) {
        return definitions.values().stream().filter(d -> d.owner().equals(owner)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowDefinition> findActiveDefinitions() {
        return definitions.values().stream().filter(d -> d.status() == WorkflowStatus.ACTIVE).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized WorkflowDefinition saveDefinition(WorkflowDefinition definition) {
        definitions.put(definition.id(), definition);
        return definition;
    }

    @Override
    public synchronized void deleteDefinition(String id) {
        definitions.remove(id);
    }

    @Override
    public List<WorkflowInstance> findAllInstances() {
        return List.copyOf(instances.values());
    }

    @Override
    public Optional<WorkflowInstance> findInstanceById(String id) {
        return Optional.ofNullable(instances.get(id));
    }

    @Override
    public List<WorkflowInstance> findInstancesByState(WorkflowState state) {
        return instances.values().stream().filter(i -> i.state() == state).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowInstance> findInstancesByType(WorkflowType type) {
        return instances.values().stream().filter(i -> i.type() == type).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowInstance> findInstancesByDomain(String domain) {
        return instances.values().stream().filter(i -> i.domain().equals(domain)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowInstance> findInstancesByOwner(String owner) {
        return instances.values().stream().filter(i -> i.owner().equals(owner)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowInstance> findInstancesByDefinitionId(String definitionId) {
        return instances.values().stream().filter(i -> i.definitionId().equals(definitionId)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized WorkflowInstance saveInstance(WorkflowInstance instance) {
        instances.put(instance.id(), instance);
        return instance;
    }

    @Override
    public synchronized void deleteInstance(String id) {
        instances.remove(id);
    }

    @Override
    public List<WorkflowQueue> findAllQueueItems() {
        return List.copyOf(queueItems.values());
    }

    @Override
    public Optional<WorkflowQueue> findQueueItemById(String id) {
        return Optional.ofNullable(queueItems.get(id));
    }

    @Override
    public List<WorkflowQueue> findQueueByType(String queueType) {
        return queueItems.values().stream().filter(q -> q.queueType().equals(queueType)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowQueue> findQueueByState(WorkflowState state) {
        return queueItems.values().stream().filter(q -> q.state() == state).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowQueue> findQueueByPriority(int priority) {
        return queueItems.values().stream().filter(q -> q.priority() == priority).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized WorkflowQueue saveQueueItem(WorkflowQueue item) {
        queueItems.put(item.id(), item);
        return item;
    }

    @Override
    public synchronized void deleteQueueItem(String id) {
        queueItems.remove(id);
    }

    @Override
    public synchronized void clearQueue(String queueType) {
        queueItems.entrySet().removeIf(e -> e.getValue().queueType().equals(queueType));
    }

    @Override
    public List<WorkflowSimulation> findAllSimulations() {
        return List.copyOf(simulations.values());
    }

    @Override
    public Optional<WorkflowSimulation> findSimulationById(String id) {
        return Optional.ofNullable(simulations.get(id));
    }

    @Override
    public List<WorkflowSimulation> findSimulationsByInstanceId(String instanceId) {
        return simulations.values().stream().filter(s -> s.instanceId().equals(instanceId)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized WorkflowSimulation saveSimulation(WorkflowSimulation simulation) {
        simulations.put(simulation.id(), simulation);
        return simulation;
    }

    @Override
    public List<WorkflowAudit> findAllAudits() {
        return List.copyOf(audits.values());
    }

    @Override
    public Optional<WorkflowAudit> findAuditById(String id) {
        return Optional.ofNullable(audits.get(id));
    }

    @Override
    public List<WorkflowAudit> findAuditsByInstanceId(String instanceId) {
        return audits.values().stream().filter(a -> a.instanceId().equals(instanceId)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized WorkflowAudit saveAudit(WorkflowAudit audit) {
        audits.put(audit.id(), audit);
        return audit;
    }

    @Override
    public List<WorkflowApproval> findAllApprovals() {
        return List.copyOf(approvals.values());
    }

    @Override
    public Optional<WorkflowApproval> findApprovalById(String id) {
        return Optional.ofNullable(approvals.get(id));
    }

    @Override
    public List<WorkflowApproval> findApprovalsByInstanceId(String instanceId) {
        return approvals.values().stream().filter(a -> a.instanceId().equals(instanceId)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowApproval> findApprovalsByStatus(String status) {
        return approvals.values().stream().filter(a -> a.status().equals(status)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<WorkflowApproval> findApprovalsByAssignedTo(String assignedTo) {
        return approvals.values().stream().filter(a -> a.assignedTo().equals(assignedTo)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized WorkflowApproval saveApproval(WorkflowApproval approval) {
        approvals.put(approval.id(), approval);
        return approval;
    }

    @Override
    public Map<String, Object> getHealthMetrics() {
        return Map.of(
            "definitions", definitions.size(),
            "instances", instances.size(),
            "queueItems", queueItems.size(),
            "simulations", simulations.size(),
            "audits", audits.size(),
            "approvals", approvals.size(),
            "status", "HEALTHY"
        );
    }
}
