package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.api.RetryManager;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.infrastructure.persistence.AutomationJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryManagerImpl implements RetryManager {

    private final AutomationJobRepository jobRepository;
    private final AutomationAuditService auditService;

    @Override
    public AutomationJob retryJob(UUID jobId) {
        var existing = jobRepository.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));
        var now = Instant.now();
        var retried = new AutomationJob(
            existing.id(), existing.type(), existing.name(), existing.params(),
            AutomationStatus.PENDING, existing.retryCount() + 1, existing.maxRetries(),
            now, null, null, null
        );
        var saved = jobRepository.save(retried);
        auditService.recordAudit("JOB_RETRIED", "AutomationJob", jobId, null,
            Map.of("name", existing.name(), "attempt", existing.retryCount() + 1), null);
        log.info("Retrying job: {} attempt {}", jobId, existing.retryCount() + 1);
        return saved;
    }

    @Override
    public WorkflowExecution retryWorkflow(UUID executionId) {
        var now = Instant.now();
        var retried = new WorkflowExecution(
            executionId, null, WorkflowExecutionStatus.PENDING,
            null, null, 0, 3, now, null, null
        );
        auditService.recordAudit("WORKFLOW_RETRIED", "WorkflowExecution", executionId, null,
            Map.of("executionId", executionId.toString()), null);
        log.info("Retrying workflow execution: {}", executionId);
        return retried;
    }

    @Override
    public boolean shouldRetry(int retryCount, int maxRetries) {
        return retryCount < maxRetries;
    }

    @Override
    public long calculateBackoff(int retryCount, long initialDelayMs, double backoffMultiplier) {
        long delay = (long) (initialDelayMs * Math.pow(backoffMultiplier, retryCount));
        long maxDelayMs = 60000;
        return Math.min(delay, maxDelayMs);
    }
}
