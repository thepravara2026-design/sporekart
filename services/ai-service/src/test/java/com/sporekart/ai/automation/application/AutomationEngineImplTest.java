package com.sporekart.ai.automation.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.automation.api.*;
import com.sporekart.ai.automation.domain.*;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AutomationEngineImplTest {

    @Mock
    private JobExecutionService jobExecutionService;
    @Mock
    private WorkflowOrchestrator workflowOrchestrator;
    @Mock
    private LifecycleManager lifecycleManager;
    @Mock
    private RetryManager retryManager;
    @Mock
    private AutomationAuditService auditService;
    @Mock
    private AutomationMetricsService metricsService;

    private AutomationEngineImpl engine;

    @BeforeEach
    void setUp() {
        engine = new AutomationEngineImpl(
            jobExecutionService, workflowOrchestrator, lifecycleManager,
            retryManager, auditService, metricsService
        );
    }

    @Test
    void executeJobShouldCompleteSuccessfully() {
        var jobId = UUID.randomUUID();
        var createdJob = new AutomationJob(jobId, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null);
        var startedJob = new AutomationJob(jobId, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.RUNNING, 0, 3, Instant.now(), Instant.now(), null, null);
        var completedJob = new AutomationJob(jobId, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.COMPLETED, 0, 3, Instant.now(), Instant.now(), Instant.now(), null);

        when(jobExecutionService.createJob(any(), any(), any())).thenReturn(createdJob);
        when(jobExecutionService.startJob(jobId)).thenReturn(startedJob);
        when(jobExecutionService.completeJob(eq(jobId), any())).thenReturn(completedJob);

        var result = engine.executeJob(JobType.HEALTH_CHECK, "test", Map.of());

        assertEquals(completedJob, result);
        verify(auditService).recordAudit(eq("JOB_STARTED"), any(), any(), any(), any(), any());
        verify(auditService).recordAudit(eq("JOB_COMPLETED"), any(), any(), any(), any(), any());
        verify(metricsService).recordJob(true);
    }

    @Test
    void executeJobShouldRetryOnFailure() {
        var jobId = UUID.randomUUID();
        var createdJob = new AutomationJob(jobId, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null);
        var startedJob = new AutomationJob(jobId, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.RUNNING, 0, 3, Instant.now(), Instant.now(), null, null);

        when(jobExecutionService.createJob(any(), any(), any())).thenReturn(createdJob);
        when(jobExecutionService.startJob(jobId)).thenReturn(startedJob);
        when(jobExecutionService.completeJob(eq(jobId), any())).thenThrow(new RuntimeException("failure"));
        when(jobExecutionService.failJob(eq(jobId), any())).thenAnswer(invocation -> {
            var err = invocation.getArgument(1, String.class);
            return new AutomationJob(jobId, JobType.HEALTH_CHECK, "test", Map.of(),
                AutomationStatus.FAILED, 0, 3, Instant.now(), Instant.now(), Instant.now(), err);
        });
        when(retryManager.shouldRetry(0, 3)).thenReturn(true);
        var retriedJob = new AutomationJob(jobId, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.PENDING, 1, 3, Instant.now(), null, null, null);
        when(retryManager.retryJob(jobId)).thenReturn(retriedJob);

        var result = engine.executeJob(JobType.HEALTH_CHECK, "test", Map.of());

        assertEquals(retriedJob, result);
        verify(metricsService).recordJob(false);
        verify(metricsService).recordRetry();
        verify(auditService).recordAudit(eq("JOB_FAILED"), any(), any(), any(), any(), any());
    }

    @Test
    void executeWorkflowShouldStartAndComplete() {
        var executionId = UUID.randomUUID();
        var now = Instant.now();
        var pending = new WorkflowExecution(executionId, "wf-test", WorkflowExecutionStatus.PENDING,
            Map.of(), null, 0, 3, now, null, null);

        when(workflowOrchestrator.startWorkflow(anyString(), any())).thenReturn(pending);

        var result = engine.executeWorkflow("wf-test", Map.of());

        assertEquals(WorkflowExecutionStatus.COMPLETED, result.status());
        verify(metricsService).recordWorkflow();
        verify(auditService).recordAudit(eq("WORKFLOW_STARTED"), any(), any(), any(), any(), any());
        verify(auditService).recordAudit(eq("WORKFLOW_COMPLETED"), any(), any(), any(), any(), any());
    }

    @Test
    void transitionStateShouldDelegateToLifecycleManager() {
        var entityId = UUID.randomUUID();
        var state = new LifecycleState(UUID.randomUUID(), entityId, "policy",
            LifecycleStateType.ACTIVE, Map.of(), Instant.now(), Instant.now());

        when(lifecycleManager.transition(entityId, "policy", LifecycleStateType.ACTIVE, "user", "reason"))
            .thenReturn(state);

        var result = engine.transitionState(entityId, "policy", LifecycleStateType.ACTIVE, "user", "reason");

        assertEquals(state, result);
    }
}
