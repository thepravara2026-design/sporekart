package com.sporekart.ai.automation.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.api.AutomationMetricsService;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.infrastructure.persistence.AutomationJobRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobExecutionServiceImplTest {

    @Mock
    private AutomationJobRepository jobRepository;
    @Mock
    private AutomationAuditService auditService;
    @Mock
    private AutomationMetricsService metricsService;

    private JobExecutionServiceImpl jobExecutionService;

    @BeforeEach
    void setUp() {
        jobExecutionService = new JobExecutionServiceImpl(jobRepository, auditService, metricsService);
    }

    @Test
    void createJobShouldSavePendingJob() {
        when(jobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = jobExecutionService.createJob(JobType.HEALTH_CHECK, "test", Map.of("key", "value"));

        assertNotNull(result.id());
        assertEquals(JobType.HEALTH_CHECK, result.type());
        assertEquals("test", result.name());
        assertEquals(AutomationStatus.PENDING, result.status());
        assertEquals(0, result.retryCount());
        assertEquals(3, result.maxRetries());
        assertNotNull(result.scheduledAt());
        assertNull(result.startedAt());
        assertNull(result.completedAt());
        verify(auditService).recordAudit(eq("JOB_CREATED"), any(), any(), any(), any(), any());
    }

    @Test
    void startJobShouldUpdateStatus() {
        var id = UUID.randomUUID();
        var pending = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null);
        var running = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.RUNNING, 0, 3, Instant.now(), Instant.now(), null, null);

        when(jobRepository.findById(id)).thenReturn(Optional.of(pending));
        when(jobRepository.save(any())).thenReturn(running);

        var result = jobExecutionService.startJob(id);

        assertEquals(AutomationStatus.RUNNING, result.status());
        assertNotNull(result.startedAt());
        verify(auditService).recordAudit(eq("JOB_STARTED"), any(), any(), any(), any(), any());
    }

    @Test
    void startJobShouldThrowWhenNotFound() {
        when(jobRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> jobExecutionService.startJob(UUID.randomUUID()));
    }

    @Test
    void completeJobShouldUpdateStatus() {
        var id = UUID.randomUUID();
        var running = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.RUNNING, 0, 3, Instant.now(), Instant.now(), null, null);
        var completed = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.COMPLETED, 0, 3, Instant.now(), Instant.now(), Instant.now(), null);

        when(jobRepository.findById(id)).thenReturn(Optional.of(running));
        when(jobRepository.save(any())).thenReturn(completed);

        var result = jobExecutionService.completeJob(id, Map.of("status", "ok"));

        assertEquals(AutomationStatus.COMPLETED, result.status());
        assertNotNull(result.completedAt());
        verify(metricsService).recordJob(true);
        verify(auditService).recordAudit(eq("JOB_COMPLETED"), any(), any(), any(), any(), any());
    }

    @Test
    void failJobShouldUpdateStatus() {
        var id = UUID.randomUUID();
        var running = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.RUNNING, 0, 3, Instant.now(), Instant.now(), null, null);
        var failed = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.FAILED, 0, 3, Instant.now(), Instant.now(), Instant.now(), "error");

        when(jobRepository.findById(id)).thenReturn(Optional.of(running));
        when(jobRepository.save(any())).thenReturn(failed);

        var result = jobExecutionService.failJob(id, "error");

        assertEquals(AutomationStatus.FAILED, result.status());
        assertEquals("error", result.errorMessage());
        verify(metricsService).recordJob(false);
        verify(auditService).recordAudit(eq("JOB_FAILED"), any(), any(), any(), any(), any());
    }

    @Test
    void cancelJobShouldUpdateStatus() {
        var id = UUID.randomUUID();
        var pending = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null);
        var cancelled = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.CANCELLED, 0, 3, Instant.now(), null, Instant.now(), "Cancelled");

        when(jobRepository.findById(id)).thenReturn(Optional.of(pending));
        when(jobRepository.save(any())).thenReturn(cancelled);

        var result = jobExecutionService.cancelJob(id);

        assertEquals(AutomationStatus.CANCELLED, result.status());
        verify(auditService).recordAudit(eq("JOB_CANCELLED"), any(), any(), any(), any(), any());
    }

    @Test
    void getJobShouldReturnJob() {
        var id = UUID.randomUUID();
        var job = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null);

        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        var result = jobExecutionService.getJob(id);

        assertEquals(job, result);
    }

    @Test
    void getJobShouldReturnNullWhenNotFound() {
        when(jobRepository.findById(any())).thenReturn(Optional.empty());
        assertNull(jobExecutionService.getJob(UUID.randomUUID()));
    }

    @Test
    void getJobsByStatusShouldReturnFiltered() {
        var jobs = List.of(
            new AutomationJob(UUID.randomUUID(), JobType.HEALTH_CHECK, "test", Map.of(),
                AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null)
        );

        when(jobRepository.findByStatus(AutomationStatus.PENDING)).thenReturn(jobs);

        var result = jobExecutionService.getJobsByStatus(AutomationStatus.PENDING);

        assertEquals(jobs, result);
    }
}
