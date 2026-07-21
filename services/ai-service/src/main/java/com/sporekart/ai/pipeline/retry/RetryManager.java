package com.sporekart.ai.pipeline.retry;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.PipelineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class RetryManager {
    private static final Logger log = LoggerFactory.getLogger(RetryManager.class);

    private final RetryPolicy policy;

    public RetryManager(RetryPolicy policy) {
        this.policy = policy;
    }

    public RetryManager() {
        this(RetryPolicy.DEFAULT);
    }

    public <T> T executeWithRetry(PipelineContext context, Callable<T> operation) {
        int attempt = 0;
        while (true) {
            try {
                return operation.call();
            } catch (Exception e) {
                attempt++;
                context.incrementRetry();
                log.warn("Pipeline [{}] attempt {} failed: {}", context.pipelineId(), attempt, e.getMessage());

                if (!policy.shouldRetry(attempt, e)) {
                    throw new PipelineException(context.request().requestId(),
                            PipelineException.ErrorCode.RETRY_EXHAUSTED,
                            "All " + attempt + " retry attempts exhausted: " + e.getMessage(), e);
                }

                var delay = policy.calculateDelay(attempt);
                context.setAttribute("retryDelayMs", delay.toMillis());
                context.setAttribute("retryAttempt", attempt);

                try {
                    Thread.sleep(delay.toMillis());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new PipelineException(context.request().requestId(),
                            PipelineException.ErrorCode.TIMEOUT,
                            "Retry interrupted", ie);
                }
            }
        }
    }

    public boolean executeWithRetry(PipelineContext context, Runnable operation) {
        return executeWithRetry(context, () -> {
            operation.run();
            return true;
        });
    }
}
