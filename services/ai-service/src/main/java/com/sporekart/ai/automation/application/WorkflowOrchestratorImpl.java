package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.api.AutomationMetricsService;
import com.sporekart.ai.automation.api.WorkflowOrchestrator;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.infrastructure.persistence.WorkflowHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowOrchestratorImpl implements WorkflowOrchestrator {

    private final WorkflowHistoryRepository historyRepository;
    private final AutomationAuditService auditService;
    private final AutomationMetricsService metricsService;

    private final ConcurrentHashMap<UUID, WorkflowExecution> executions = new ConcurrentHashMap<>();

    @Override
    public WorkflowExecution startWorkflow(String workflowName, Map<String, Object> context) {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var execution = new WorkflowExecution(
            id, workflowName, WorkflowExecutionStatus.PENDING, context, null,
            0, 3, now, null, null
        );
        executions.put(id, execution);
        var history = new WorkflowHistory(
            UUID.randomUUID(), id, "START", "CREATE", "PENDING",
            "Workflow " + workflowName + " created", now
        );
        historyRepository.save(history);
        metricsService.recordWorkflow();
        auditService.recordAudit("WORKFLOW_CREATED", "WorkflowExecution", id, null,
            Map.of("workflowName", workflowName), null);
        log.info("Started workflow execution: {} for {}", id, workflowName);
        return execution;
    }

    @Override
    public WorkflowExecution getWorkflowStatus(UUID executionId) {
        return executions.get(executionId);
    }

    @Override
    public WorkflowExecution cancelWorkflow(UUID executionId) {
        var existing = executions.get(executionId);
        if (existing == null) {
            return null;
        }
        var now = Instant.now();
        var cancelled = new WorkflowExecution(
            existing.id(), existing.workflowName(), WorkflowExecutionStatus.CANCELLED,
            existing.context(), existing.result(),
            existing.retryCount(), existing.maxRetries(),
            existing.startedAt(), now, "Cancelled by user"
        );
        executions.put(executionId, cancelled);
        var history = new WorkflowHistory(
            UUID.randomUUID(), executionId, "CANCEL", "UPDATE", "CANCELLED",
            "Workflow " + existing.workflowName() + " cancelled", now
        );
        historyRepository.save(history);
        auditService.recordAudit("WORKFLOW_CANCELLED", "WorkflowExecution", executionId, null,
            Map.of("workflowName", existing.workflowName()), null);
        log.info("Cancelled workflow execution: {}", executionId);
        return cancelled;
    }

    @Override
    public WorkflowExecution retryWorkflow(UUID executionId) {
        var existing = executions.get(executionId);
        if (existing == null) {
            return null;
        }
        var now = Instant.now();
        var retried = new WorkflowExecution(
            existing.id(), existing.workflowName(), WorkflowExecutionStatus.PENDING,
            existing.context(), null,
            existing.retryCount() + 1, existing.maxRetries(),
            now, null, null
        );
        executions.put(executionId, retried);
        var history = new WorkflowHistory(
            UUID.randomUUID(), executionId, "RETRY", "RESET", "PENDING",
            "Workflow " + existing.workflowName() + " retry attempt " + (existing.retryCount() + 1), now
        );
        historyRepository.save(history);
        auditService.recordAudit("WORKFLOW_RETRIED", "WorkflowExecution", executionId, null,
            Map.of("workflowName", existing.workflowName(), "attempt", existing.retryCount() + 1), null);
        log.info("Retrying workflow execution: {} attempt {}", executionId, existing.retryCount() + 1);
        return retried;
    }

    @Override
    public List<WorkflowHistory> getWorkflowHistory(UUID executionId) {
        return historyRepository.findByExecutionId(executionId);
    }
}
