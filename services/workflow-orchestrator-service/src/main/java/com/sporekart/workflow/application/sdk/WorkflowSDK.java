package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.domain.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkflowSDK {
    List<WorkflowDefinition> getAllDefinitions();
    Optional<WorkflowDefinition> getDefinitionById(String id);
    List<WorkflowDefinition> getDefinitionsByType(WorkflowType type);
    List<WorkflowDefinition> getActiveDefinitions();
    List<WorkflowDefinition> generateAllDefinitions();
    List<WorkflowInstance> getAllInstances();
    Optional<WorkflowInstance> getInstanceById(String id);
    List<WorkflowInstance> getInstancesByState(WorkflowState state);
    WorkflowInstance startWorkflow(String definitionId, String triggeredBy, Map<String, Object> context);
    WorkflowInstance startSimulation(String definitionId, String triggeredBy, Map<String, Object> context);
    WorkflowInstance pauseWorkflow(String instanceId, String triggeredBy);
    WorkflowInstance resumeWorkflow(String instanceId, String triggeredBy);
    WorkflowInstance cancelWorkflow(String instanceId, String triggeredBy);
    WorkflowInstance retryWorkflow(String instanceId, String triggeredBy);
    WorkflowSimulation simulateWorkflow(String instanceId);
    WorkflowSimulation simulateRollback(String instanceId);
    WorkflowSimulation dryRunWorkflow(String definitionId, Map<String, Object> context);
    List<WorkflowAudit> getAuditTrail(String instanceId);
    List<WorkflowQueue> getQueue();
    List<WorkflowApproval> getApprovals();
    WorkflowApproval approveWorkflow(String approvalId, String reason);
    WorkflowApproval rejectWorkflow(String approvalId, String reason);
    Map<String, Object> getTelemetry();
    Map<String, Object> getWorkflowHealth();
    Map<String, Object> health();
}
