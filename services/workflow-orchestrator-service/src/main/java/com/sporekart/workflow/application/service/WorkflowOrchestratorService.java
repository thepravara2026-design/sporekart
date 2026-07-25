package com.sporekart.workflow.application.service;

import com.sporekart.workflow.domain.engine.*;
import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.cache.WorkflowCacheService;
import com.sporekart.workflow.infrastructure.executor.MockActionExecutorService;
import com.sporekart.workflow.infrastructure.queue.MockWorkflowQueueService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorkflowOrchestratorService {
    private final WorkflowRepositoryPort repository;
    private final WorkflowExecutionEngine executionEngine;
    private final WorkflowSimulationEngine simulationEngine;
    private final WorkflowAuditEngine auditEngine;
    private final WorkflowRegistryService registryService;
    private final WorkflowTelemetryService telemetry;
    private final MockWorkflowQueueService queueService;
    private final MockActionExecutorService actionExecutor;
    private final WorkflowCacheService cacheService;
    private final WorkflowStateMachine stateMachine;

    public WorkflowOrchestratorService(
        WorkflowRepositoryPort repository,
        WorkflowExecutionEngine executionEngine,
        WorkflowSimulationEngine simulationEngine,
        WorkflowAuditEngine auditEngine,
        WorkflowRegistryService registryService,
        WorkflowTelemetryService telemetry,
        MockWorkflowQueueService queueService,
        MockActionExecutorService actionExecutor,
        WorkflowCacheService cacheService,
        WorkflowStateMachine stateMachine
    ) {
        this.repository = repository;
        this.executionEngine = executionEngine;
        this.simulationEngine = simulationEngine;
        this.auditEngine = auditEngine;
        this.registryService = registryService;
        this.telemetry = telemetry;
        this.queueService = queueService;
        this.actionExecutor = actionExecutor;
        this.cacheService = cacheService;
        this.stateMachine = stateMachine;
    }

    // Definitions
    public List<WorkflowDefinition> listDefinitions() { return registryService.listDefinitions(); }
    public Optional<WorkflowDefinition> getDefinition(String id) { return registryService.getDefinition(id); }
    public List<WorkflowDefinition> getDefinitionsByType(WorkflowType type) { return registryService.getDefinitionsByType(type); }
    public List<WorkflowDefinition> getDefinitionsByDomain(String domain) { return registryService.getDefinitionsByDomain(domain); }
    public List<WorkflowDefinition> getActiveDefinitions() { return registryService.getActiveDefinitions(); }
    public List<WorkflowDefinition> generateDefinitions() { return registryService.generateDefinitions(); }
    public WorkflowDefinition createDefinition(String name, String description, WorkflowType type, String domain,
                                                String owner, String version, List<WorkflowStep> steps, Map<String, Object> config) {
        return registryService.createDefinition(name, description, type, domain, owner, version, steps, config);
    }

    // Instances
    public List<WorkflowInstance> listInstances() { return repository.findAllInstances(); }
    public Optional<WorkflowInstance> getInstance(String id) { return repository.findInstanceById(id); }
    public List<WorkflowInstance> getInstancesByState(WorkflowState state) { return repository.findInstancesByState(state); }
    public List<WorkflowInstance> getInstancesByType(WorkflowType type) { return repository.findInstancesByType(type); }

    public WorkflowInstance startWorkflow(String definitionId, String triggeredBy, Map<String, Object> context) {
        var instance = executionEngine.startWorkflow(definitionId, triggeredBy, context);
        queueService.enqueue(instance.id(), instance.name(), instance.type(), 5);
        return instance;
    }

    public WorkflowInstance startSimulation(String definitionId, String triggeredBy, Map<String, Object> context) {
        var instance = executionEngine.startSimulation(definitionId, triggeredBy, context);
        simulationEngine.simulateWorkflow(instance.id());
        return instance;
    }

    public WorkflowInstance pauseWorkflow(String instanceId, String triggeredBy) {
        return executionEngine.pauseWorkflow(instanceId, triggeredBy);
    }

    public WorkflowInstance resumeWorkflow(String instanceId, String triggeredBy) {
        return executionEngine.resumeWorkflow(instanceId, triggeredBy);
    }

    public WorkflowInstance cancelWorkflow(String instanceId, String triggeredBy) {
        return executionEngine.cancelWorkflow(instanceId, triggeredBy);
    }

    public WorkflowInstance retryWorkflow(String instanceId, String triggeredBy) {
        return executionEngine.retryWorkflow(instanceId, triggeredBy);
    }

    public WorkflowInstance completeWorkflow(String instanceId, Map<String, Object> result, String triggeredBy) {
        return executionEngine.completeWorkflow(instanceId, result, triggeredBy);
    }

    public WorkflowInstance failWorkflow(String instanceId, String error, String triggeredBy) {
        return executionEngine.failWorkflow(instanceId, error, triggeredBy);
    }

    // Simulation
    public WorkflowSimulation simulateWorkflow(String instanceId) {
        return simulationEngine.simulateWorkflow(instanceId);
    }

    public WorkflowSimulation simulateRollback(String instanceId) {
        return simulationEngine.simulateRollback(instanceId);
    }

    public WorkflowSimulation dryRunWorkflow(String definitionId, Map<String, Object> context) {
        return simulationEngine.dryRunWorkflow(definitionId, context);
    }

    // Audit
    public List<WorkflowAudit> getAuditTrail(String instanceId) { return auditEngine.getAuditTrail(instanceId); }
    public List<WorkflowAudit> getAllAudits() { return auditEngine.getAllAudits(); }
    public Map<String, Object> getAuditSummary() { return auditEngine.getAuditSummary(); }

    // Queue
    public List<WorkflowQueue> listQueue() { return repository.findAllQueueItems(); }
    public List<WorkflowQueue> getPendingQueue() { return queueService.getPendingItems(); }
    public Map<String, Object> getQueueMetrics() { return queueService.getQueueMetrics(); }
    public void clearQueue() { queueService.clearQueue(); }

    // Approvals
    public List<WorkflowApproval> listApprovals() { return repository.findAllApprovals(); }
    public List<WorkflowApproval> getApprovalsByStatus(String status) { return repository.findApprovalsByStatus(status); }
    public WorkflowApproval requestApproval(String instanceId, String requestedBy, String assignedTo) {
        var approval = WorkflowApproval.create(instanceId, "Approval Required", requestedBy, assignedTo, Map.of());
        return repository.saveApproval(approval);
    }
    public WorkflowApproval approveWorkflow(String approvalId, String reason) {
        var approval = repository.findApprovalById(approvalId)
            .orElseThrow(() -> new IllegalArgumentException("Approval not found: " + approvalId));
        var updated = approval.withDecision("APPROVED", reason);
        return repository.saveApproval(updated);
    }
    public WorkflowApproval rejectWorkflow(String approvalId, String reason) {
        var approval = repository.findApprovalById(approvalId)
            .orElseThrow(() -> new IllegalArgumentException("Approval not found: " + approvalId));
        var updated = approval.withDecision("REJECTED", reason);
        return repository.saveApproval(updated);
    }

    // State machine
    public boolean isValidTransition(WorkflowState from, WorkflowState to) { return stateMachine.isValidTransition(from, to); }
    public List<WorkflowState> getAllowedTransitions(WorkflowState state) { return List.copyOf(stateMachine.getAllowedTransitions(state)); }
    public List<WorkflowState> getTransitionPath(WorkflowState from, WorkflowState to) { return stateMachine.getTransitionPath(from, to); }

    // Cache
    public Map<String, Object> getCacheInfo() { return cacheService.getCacheInfo(); }
    public void clearCache() { cacheService.clear(); }

    // Telemetry
    public Map<String, Object> getTelemetry() { return telemetry.getTelemetry(); }
    public Map<String, Object> getTelemetryHistory() { return telemetry.getTelemetryHistory(); }
    public Map<String, Object> getWorkflowHealth() { return telemetry.getWorkflowHealth(); }

    // Health
    public Map<String, Object> health() {
        return Map.of(
            "service", "workflow-orchestrator-service",
            "status", "UP",
            "port", 8097,
            "definitions", repository.findAllDefinitions().size(),
            "instances", repository.findAllInstances().size()
        );
    }
}
