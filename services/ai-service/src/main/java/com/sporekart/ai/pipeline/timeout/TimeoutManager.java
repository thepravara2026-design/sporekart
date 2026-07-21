package com.sporekart.ai.pipeline.timeout;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.PipelineException;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

public class TimeoutManager {
    private final Duration defaultProviderTimeout;
    private final Duration defaultGatewayTimeout;
    private final ScheduledExecutorService scheduler;

    public TimeoutManager(Duration defaultProviderTimeout, Duration defaultGatewayTimeout) {
        this.defaultProviderTimeout = defaultProviderTimeout;
        this.defaultGatewayTimeout = defaultGatewayTimeout;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public TimeoutManager() {
        this(Duration.ofSeconds(30), Duration.ofSeconds(60));
    }

    public Duration resolveProviderTimeout(PipelineContext context) {
        var request = context.request();
        long timeoutFromRequest = 0;
        try {
            Object timeoutObj = request.context().get("timeoutMs");
            if (timeoutObj instanceof Number n) {
                timeoutFromRequest = n.longValue();
            }
        } catch (Exception ignored) {}

        if (timeoutFromRequest > 0) {
            return Duration.ofMillis(timeoutFromRequest);
        }
        return defaultProviderTimeout;
    }

    public Duration resolveGatewayTimeout(PipelineContext context) {
        return defaultGatewayTimeout;
    }

    public <T> T executeWithTimeout(PipelineContext context, Callable<T> operation) {
        var providerTimeout = resolveProviderTimeout(context);
        context.setAttribute("providerTimeoutMs", providerTimeout.toMillis());

        var future = CompletableFuture.supplyAsync(() -> {
            try {
                return operation.call();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });

        try {
            return future.get(providerTimeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new PipelineException(context.request().requestId(),
                    PipelineException.ErrorCode.PROVIDER_TIMEOUT,
                    "Provider timeout after " + providerTimeout.toMillis() + "ms");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PipelineException(context.request().requestId(),
                    PipelineException.ErrorCode.TIMEOUT, "Execution interrupted");
        } catch (ExecutionException e) {
            if (e.getCause() instanceof PipelineException pe) throw pe;
            throw new PipelineException(context.request().requestId(),
                    PipelineException.ErrorCode.PROVIDER_ERROR,
                    "Provider execution error: " + e.getCause().getMessage());
        }
    }

    public boolean isExpired(PipelineContext context) {
        var elapsed = Duration.between(context.startedAt(), Instant.now());
        var gatewayTimeout = resolveGatewayTimeout(context);
        return elapsed.compareTo(gatewayTimeout) > 0;
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public void scheduleCancellation(PipelineContext context, Runnable onTimeout) {
        var timeout = resolveGatewayTimeout(context);
        scheduler.schedule(() -> {
            if (!context.failed() && context.response() == null) {
                onTimeout.run();
            }
        }, timeout.toMillis(), TimeUnit.MILLISECONDS);
    }
}
