package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsMiddleware implements Middleware {
    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger successfulRequests = new AtomicInteger(0);
    private final AtomicInteger failedRequests = new AtomicInteger(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);

    @Override
    public String name() { return "Metrics"; }

    @Override
    public int order() { return 95; }

    @Override
    public void execute(PipelineContext context, MiddlewareChain chain) {
        var start = Instant.now();
        context.setAttribute("metricsStartTime", start);
        context.recordMiddleware(name());
        chain.next(context);
        var latency = context.elapsed().toMillis();
        totalRequests.incrementAndGet();
        totalLatencyMs.addAndGet(latency);
        if (context.failed()) {
            failedRequests.incrementAndGet();
        } else {
            successfulRequests.incrementAndGet();
        }
        context.setAttribute("pipelineTotalRequests", totalRequests.get());
        context.setAttribute("pipelineSuccessRate", getSuccessRate());
        context.setAttribute("pipelineAvgLatencyMs", getAverageLatencyMs());
    }

    public int getTotalRequests() { return totalRequests.get(); }
    public int getSuccessfulRequests() { return successfulRequests.get(); }
    public int getFailedRequests() { return failedRequests.get(); }
    public double getSuccessRate() {
        int total = totalRequests.get();
        return total == 0 ? 1.0 : (double) successfulRequests.get() / total;
    }
    public double getAverageLatencyMs() {
        int total = totalRequests.get();
        return total == 0 ? 0 : (double) totalLatencyMs.get() / total;
    }
}
