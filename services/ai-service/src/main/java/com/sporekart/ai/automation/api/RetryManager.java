package com.sporekart.ai.automation.api;

import com.sporekart.ai.automation.domain.*;
import java.util.UUID;

public interface RetryManager {
    AutomationJob retryJob(UUID jobId);
    WorkflowExecution retryWorkflow(UUID executionId);
    boolean shouldRetry(int retryCount, int maxRetries);
    long calculateBackoff(int retryCount, long initialDelayMs, double backoffMultiplier);
}
