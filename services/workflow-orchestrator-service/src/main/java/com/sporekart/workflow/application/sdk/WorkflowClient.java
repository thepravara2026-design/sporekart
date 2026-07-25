package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.application.service.WorkflowOrchestratorService;
import com.sporekart.workflow.domain.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorkflowClient implements WorkflowSDK {
    private final WorkflowOrchestratorService service;

    public WorkflowClient(WorkflowOrchestratorService service) {
        this.service = service;
    }

    @Override
    public List<WorkflowDefinition> getAllDefinitions() { return service.listDefinitions(); }

    @Override
    public Optional<WorkflowDefinition> getDefinitionById(String id) { return service.getDefinition(id); }

    @Override
    public List<WorkflowDefinition> getDefinitionsByType(WorkflowType type) { return service.getDefinitionsByType(type); }

    @Override
    public List<WorkflowDefinition> getActiveDefinitions() { return service.getActiveDefinitions(); }

    @Override
    public List<WorkflowDefinition> generateAllDefinitions() { return service.generateDefinitions(); }

    @Override
    public List<WorkflowInstance> getAllInstances() { return service.listInstances(); }

    @Override
    public Optional<WorkflowInstance> getInstanceById(String id) { return service.getInstance(id); }

    @Override
    public List<WorkflowInstance> getInstancesByState(WorkflowState state) { return service.getInstancesByState(state); }

    @Override
    public WorkflowInstance startWorkflow(String definitionId, String triggeredBy, Map<String, Object> context) {
        return service.startWorkflow(definitionId, triggeredBy, context);
    }

    @Override
    public WorkflowInstance startSimulation(String definitionId, String triggeredBy, Map<String, Object> context) {
        return service.startSimulation(definitionId, triggeredBy, context);
    }

    @Override
    public WorkflowInstance pauseWorkflow(String instanceId, String triggeredBy) {
        return service.pauseWorkflow(instanceId, triggeredBy);
    }

    @Override
    public WorkflowInstance resumeWorkflow(String instanceId, String triggeredBy) {
        return service.resumeWorkflow(instanceId, triggeredBy);
    }

    @Override
    public WorkflowInstance cancelWorkflow(String instanceId, String triggeredBy) {
        return service.cancelWorkflow(instanceId, triggeredBy);
    }

    @Override
    public WorkflowInstance retryWorkflow(String instanceId, String triggeredBy) {
        return service.retryWorkflow(instanceId, triggeredBy);
    }

    @Override
    public WorkflowSimulation simulateWorkflow(String instanceId) { return service.simulateWorkflow(instanceId); }

    @Override
    public WorkflowSimulation simulateRollback(String instanceId) { return service.simulateRollback(instanceId); }

    @Override
    public WorkflowSimulation dryRunWorkflow(String definitionId, Map<String, Object> context) {
        return service.dryRunWorkflow(definitionId, context);
    }

    @Override
    public List<WorkflowAudit> getAuditTrail(String instanceId) { return service.getAuditTrail(instanceId); }

    @Override
    public List<WorkflowQueue> getQueue() { return service.listQueue(); }

    @Override
    public List<WorkflowApproval> getApprovals() { return service.listApprovals(); }

    @Override
    public WorkflowApproval approveWorkflow(String approvalId, String reason) {
        return service.approveWorkflow(approvalId, reason);
    }

    @Override
    public WorkflowApproval rejectWorkflow(String approvalId, String reason) {
        return service.rejectWorkflow(approvalId, reason);
    }

    @Override
    public Map<String, Object> getTelemetry() { return service.getTelemetry(); }

    @Override
    public Map<String, Object> getWorkflowHealth() { return service.getWorkflowHealth(); }

    @Override
    public Map<String, Object> health() { return service.health(); }
}
