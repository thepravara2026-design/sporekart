package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.api.AutomationMetricsService;
import com.sporekart.ai.automation.api.JobExecutionService;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.infrastructure.persistence.AutomationJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final AutomationJobRepository jobRepository;
    private final AutomationAuditService auditService;
    private final AutomationMetricsService metricsService;

    @Override
    public AutomationJob createJob(JobType type, String name, Map<String, Object> params) {
        var now = Instant.now();
        var job = new AutomationJob(
            UUID.randomUUID(), type, name, params, AutomationStatus.PENDING,
            0, 3, now, null, null, null
        );
        var saved = jobRepository.save(job);
        auditService.recordAudit("JOB_CREATED", "AutomationJob", saved.id(), null,
            Map.of("name", name, "type", type.toString()), null);
        log.info("Created job: {} of type {}", saved.id(), type);
        return saved;
    }

    @Override
    public AutomationJob startJob(UUID id) {
        var existing = jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
        var now = Instant.now();
        var started = new AutomationJob(
            existing.id(), existing.type(), existing.name(), existing.params(),
            AutomationStatus.RUNNING, existing.retryCount(), existing.maxRetries(),
            existing.scheduledAt(), now, null, null
        );
        var saved = jobRepository.save(started);
        auditService.recordAudit("JOB_STARTED", "AutomationJob", id, null,
            Map.of("name", existing.name()), null);
        log.info("Started job: {}", id);
        return saved;
    }

    @Override
    public AutomationJob completeJob(UUID id, Map<String, Object> result) {
        var existing = jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
        var now = Instant.now();
        var completed = new AutomationJob(
            existing.id(), existing.type(), existing.name(), existing.params(),
            AutomationStatus.COMPLETED, existing.retryCount(), existing.maxRetries(),
            existing.scheduledAt(), existing.startedAt(), now, null
        );
        var saved = jobRepository.save(completed);
        metricsService.recordJob(true);
        auditService.recordAudit("JOB_COMPLETED", "AutomationJob", id, null,
            Map.of("name", existing.name(), "result", result), null);
        log.info("Completed job: {}", id);
        return saved;
    }

    @Override
    public AutomationJob failJob(UUID id, String error) {
        var existing = jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
        var now = Instant.now();
        var failed = new AutomationJob(
            existing.id(), existing.type(), existing.name(), existing.params(),
            AutomationStatus.FAILED, existing.retryCount(), existing.maxRetries(),
            existing.scheduledAt(), existing.startedAt(), now, error
        );
        var saved = jobRepository.save(failed);
        metricsService.recordJob(false);
        auditService.recordAudit("JOB_FAILED", "AutomationJob", id, null,
            Map.of("name", existing.name(), "error", error), null);
        log.info("Failed job: {} with error: {}", id, error);
        return saved;
    }

    @Override
    public AutomationJob cancelJob(UUID id) {
        var existing = jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
        var now = Instant.now();
        var cancelled = new AutomationJob(
            existing.id(), existing.type(), existing.name(), existing.params(),
            AutomationStatus.CANCELLED, existing.retryCount(), existing.maxRetries(),
            existing.scheduledAt(), existing.startedAt(), now, "Cancelled"
        );
        var saved = jobRepository.save(cancelled);
        auditService.recordAudit("JOB_CANCELLED", "AutomationJob", id, null,
            Map.of("name", existing.name()), null);
        log.info("Cancelled job: {}", id);
        return saved;
    }

    @Override
    public AutomationJob getJob(UUID id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public List<AutomationJob> getJobsByStatus(AutomationStatus status) {
        return jobRepository.findByStatus(status);
    }
}
