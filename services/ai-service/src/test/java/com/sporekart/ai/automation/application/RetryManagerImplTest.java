package com.sporekart.ai.automation.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.infrastructure.persistence.AutomationJobRepository;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetryManagerImplTest {

    @Mock
    private AutomationJobRepository jobRepository;
    @Mock
    private AutomationAuditService auditService;

    private RetryManagerImpl retryManager;

    @BeforeEach
    void setUp() {
        retryManager = new RetryManagerImpl(jobRepository, auditService);
    }

    @Test
    void shouldRetryShouldReturnTrueWhenBelowMax() {
        assertTrue(retryManager.shouldRetry(0, 3));
        assertTrue(retryManager.shouldRetry(1, 3));
        assertTrue(retryManager.shouldRetry(2, 3));
    }

    @Test
    void shouldRetryShouldReturnFalseWhenAtOrAboveMax() {
        assertFalse(retryManager.shouldRetry(3, 3));
        assertFalse(retryManager.shouldRetry(4, 3));
        assertFalse(retryManager.shouldRetry(5, 3));
    }

    @Test
    void shouldRetryShouldHandleZeroMaxRetries() {
        assertFalse(retryManager.shouldRetry(0, 0));
    }

    @Test
    void calculateBackoffShouldReturnExponentialDelay() {
        long initialDelayMs = 1000;
        double multiplier = 2.0;

        assertEquals(1000, retryManager.calculateBackoff(0, initialDelayMs, multiplier));
        assertEquals(2000, retryManager.calculateBackoff(1, initialDelayMs, multiplier));
        assertEquals(4000, retryManager.calculateBackoff(2, initialDelayMs, multiplier));
        assertEquals(8000, retryManager.calculateBackoff(3, initialDelayMs, multiplier));
    }

    @Test
    void calculateBackoffShouldCapAtMaxDelay() {
        long delay = retryManager.calculateBackoff(10, 1000, 2.0);
        assertEquals(60000, delay);
    }

    @Test
    void retryJobShouldIncrementRetryCount() {
        var id = UUID.randomUUID();
        var existing = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.FAILED, 1, 3, Instant.now(), Instant.now(), Instant.now(), "err");
        var retried = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.PENDING, 2, 3, Instant.now(), null, null, null);

        when(jobRepository.findById(id)).thenReturn(Optional.of(existing));
        when(jobRepository.save(any())).thenReturn(retried);

        var result = retryManager.retryJob(id);

        assertEquals(2, result.retryCount());
        assertEquals(AutomationStatus.PENDING, result.status());
        verify(auditService).recordAudit(eq("JOB_RETRIED"), any(), any(), any(), any(), any());
    }

    @Test
    void retryJobShouldThrowWhenNotFound() {
        when(jobRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> retryManager.retryJob(UUID.randomUUID()));
    }

    @Test
    void retryWorkflowShouldReturnPendingExecution() {
        var executionId = UUID.randomUUID();
        var result = retryManager.retryWorkflow(executionId);

        assertNotNull(result);
        assertEquals(WorkflowExecutionStatus.PENDING, result.status());
        verify(auditService).recordAudit(eq("WORKFLOW_RETRIED"), any(), any(), any(), any(), any());
    }
}
