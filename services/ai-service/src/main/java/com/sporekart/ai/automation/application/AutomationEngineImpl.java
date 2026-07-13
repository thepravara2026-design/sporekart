package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.*;
import com.sporekart.ai.automation.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutomationEngineImpl implements AutomationEngine {

    private final JobExecutionService jobExecutionService;
    private final WorkflowOrchestrator workflowOrchestrator;
    private final LifecycleManager lifecycleManager;
    private final RetryManager retryManager;
    private final AutomationAuditService auditService;
    private final AutomationMetricsService metricsService;

    @Override
    public AutomationJob executeJob(JobType type, String name, Map<String, Object> params) {
        var job = jobExecutionService.createJob(type, name, params);
        jobExecutionService.startJob(job.id());
        auditService.recordAudit("JOB_STARTED", "Job", job.id(), null, Map.of("name", name), null);
        try {
            Map<String, Object> result = Map.of("status", "success");
            var completed = jobExecutionService.completeJob(job.id(), result);
            metricsService.recordJob(true);
            auditService.recordAudit("JOB_COMPLETED", "Job", job.id(), null, result, null);
            return completed;
        } catch (Exception e) {
            var failed = jobExecutionService.failJob(job.id(), e.getMessage());
            metricsService.recordJob(false);
            auditService.recordAudit("JOB_FAILED", "Job", job.id(), null, Map.of("error", e.getMessage()), null);
            if (retryManager.shouldRetry(failed.retryCount(), failed.maxRetries())) {
                metricsService.recordRetry();
                return retryManager.retryJob(job.id());
            }
            return failed;
        }
    }

    @Override
    public WorkflowExecution executeWorkflow(String workflowName, Map<String, Object> context) {
        var execution = workflowOrchestrator.startWorkflow(workflowName, context);
        metricsService.recordWorkflow();
        auditService.recordAudit("WORKFLOW_STARTED", "WorkflowExecution", execution.id(), null, Map.of("workflowName", workflowName), null);
        try {
            var now = Instant.now();
            var completed = new WorkflowExecution(
                execution.id(), execution.workflowName(), WorkflowExecutionStatus.COMPLETED,
                execution.context(), Map.of("status", "success"),
                execution.retryCount(), execution.maxRetries(),
                execution.startedAt(), now, null
            );
            auditService.recordAudit("WORKFLOW_COMPLETED", "WorkflowExecution", execution.id(), null, Map.of("workflowName", workflowName), null);
            return completed;
        } catch (Exception e) {
            var now = Instant.now();
            var failed = new WorkflowExecution(
                execution.id(), execution.workflowName(), WorkflowExecutionStatus.FAILED,
                execution.context(), null,
                execution.retryCount(), execution.maxRetries(),
                execution.startedAt(), now, e.getMessage()
            );
            auditService.recordAudit("WORKFLOW_FAILED", "WorkflowExecution", execution.id(), null, Map.of("error", e.getMessage()), null);
            return failed;
        }
    }

    @Override
    public LifecycleState transitionState(UUID entityId, String entityType, LifecycleStateType targetState, String triggeredBy, String reason) {
        return lifecycleManager.transition(entityId, entityType, targetState, triggeredBy, reason);
    }
}
