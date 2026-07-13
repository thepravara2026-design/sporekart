package com.sporekart.ai.automation.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.api.AutomationMetricsService;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.infrastructure.persistence.WorkflowHistoryRepository;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkflowOrchestratorImplTest {

    @Mock
    private WorkflowHistoryRepository historyRepository;
    @Mock
    private AutomationAuditService auditService;
    @Mock
    private AutomationMetricsService metricsService;

    private WorkflowOrchestratorImpl orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new WorkflowOrchestratorImpl(historyRepository, auditService, metricsService);
    }

    @Test
    void startWorkflowShouldCreateExecution() {
        var result = orchestrator.startWorkflow("test-workflow", Map.of("key", "value"));

        assertNotNull(result.id());
        assertEquals("test-workflow", result.workflowName());
        assertEquals(WorkflowExecutionStatus.PENDING, result.status());
        assertEquals(Map.of("key", "value"), result.context());
        assertEquals(0, result.retryCount());
        assertEquals(3, result.maxRetries());

        verify(historyRepository).save(any(WorkflowHistory.class));
        verify(metricsService).recordWorkflow();
        verify(auditService).recordAudit(eq("WORKFLOW_CREATED"), any(), any(), any(), any(), any());
    }

    @Test
    void cancelWorkflowShouldReturnCancelledExecution() {
        var execution = orchestrator.startWorkflow("test-workflow", Map.of());
        var cancelled = orchestrator.cancelWorkflow(execution.id());

        assertNotNull(cancelled);
        assertEquals(WorkflowExecutionStatus.CANCELLED, cancelled.status());
        assertEquals("test-workflow", cancelled.workflowName());
        verify(historyRepository, times(2)).save(any(WorkflowHistory.class));
        verify(auditService).recordAudit(eq("WORKFLOW_CANCELLED"), any(), any(), any(), any(), any());
    }

    @Test
    void cancelWorkflowShouldReturnNullWhenNotFound() {
        var result = orchestrator.cancelWorkflow(UUID.randomUUID());
        assertNull(result);
    }

    @Test
    void retryWorkflowShouldIncrementRetryCount() {
        var execution = orchestrator.startWorkflow("test-workflow", Map.of());
        var retried = orchestrator.retryWorkflow(execution.id());

        assertNotNull(retried);
        assertEquals("test-workflow", retried.workflowName());
        assertEquals(WorkflowExecutionStatus.PENDING, retried.status());
        assertEquals(1, retried.retryCount());
        assertEquals(3, retried.maxRetries());
        verify(historyRepository, times(2)).save(any(WorkflowHistory.class));
        verify(auditService).recordAudit(eq("WORKFLOW_RETRIED"), any(), any(), any(), any(), any());
    }

    @Test
    void retryWorkflowShouldReturnNullWhenNotFound() {
        var result = orchestrator.retryWorkflow(UUID.randomUUID());
        assertNull(result);
    }

    @Test
    void getWorkflowStatusShouldReturnExecution() {
        var execution = orchestrator.startWorkflow("test-workflow", Map.of());
        var status = orchestrator.getWorkflowStatus(execution.id());

        assertNotNull(status);
        assertEquals(execution.id(), status.id());
    }

    @Test
    void getWorkflowStatusShouldReturnNullWhenNotFound() {
        var result = orchestrator.getWorkflowStatus(UUID.randomUUID());
        assertNull(result);
    }

    @Test
    void getWorkflowHistoryShouldDelegateToRepository() {
        var executionId = UUID.randomUUID();
        orchestrator.getWorkflowHistory(executionId);
        verify(historyRepository).findByExecutionId(executionId);
    }
}
