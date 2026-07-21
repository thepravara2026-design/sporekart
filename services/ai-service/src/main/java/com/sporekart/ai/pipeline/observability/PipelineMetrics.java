package com.sporekart.ai.pipeline.observability;

import com.sporekart.ai.pipeline.PipelineContext;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PipelineMetrics {
    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger successfulRequests = new AtomicInteger(0);
    private final AtomicInteger failedRequests = new AtomicInteger(0);
    private final AtomicInteger retryCount = new AtomicInteger(0);
    private final AtomicInteger timeoutCount = new AtomicInteger(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);
    private final Map<String, AtomicInteger> moduleCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> providerCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> errorCounts = new ConcurrentHashMap<>();

    public void recordPipelineStart(PipelineContext context) {
        totalRequests.incrementAndGet();
        recordModule(context.request().module());
        context.setAttribute("metricsStartTime", Instant.now());
    }

    public void recordPipelineCompletion(PipelineContext context) {
        var latency = Duration.between(
                context.getAttribute("metricsStartTime", Instant.now()),
                Instant.now());
        totalLatencyMs.addAndGet(latency.toMillis());

        if (context.failed()) {
            failedRequests.incrementAndGet();
            recordError(context.failureReason());
        } else {
            successfulRequests.incrementAndGet();
            if (context.response() != null && context.response().provider() != null) {
                recordProvider(context.response().provider());
            }
        }
        if (context.retryCount() > 0) {
            retryCount.addAndGet(context.retryCount());
        }
    }

    public void recordTimeout() {
        timeoutCount.incrementAndGet();
    }

    public MetricsSnapshot getSnapshot() {
        int total = totalRequests.get();
        return new MetricsSnapshot(
                total,
                successfulRequests.get(),
                failedRequests.get(),
                retryCount.get(),
                timeoutCount.get(),
                total == 0 ? 0.0 : (double) successfulRequests.get() / total,
                total == 0 ? 0.0 : (double) totalLatencyMs.get() / total,
                Map.copyOf(moduleCounts).entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey, e -> e.getValue().get())),
                Map.copyOf(providerCounts).entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey, e -> e.getValue().get())),
                Map.copyOf(errorCounts).entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey, e -> e.getValue().get())));
    }

    public void reset() {
        totalRequests.set(0);
        successfulRequests.set(0);
        failedRequests.set(0);
        retryCount.set(0);
        timeoutCount.set(0);
        totalLatencyMs.set(0);
        moduleCounts.clear();
        providerCounts.clear();
        errorCounts.clear();
    }

    private void recordModule(String module) {
        if (module != null) {
            moduleCounts.computeIfAbsent(module, k -> new AtomicInteger(0)).incrementAndGet();
        }
    }

    private void recordProvider(String provider) {
        if (provider != null) {
            providerCounts.computeIfAbsent(provider, k -> new AtomicInteger(0)).incrementAndGet();
        }
    }

    private void recordError(String error) {
        if (error != null) {
            var key = error.contains(":") ? error.substring(0, error.indexOf(':')) : error;
            errorCounts.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
        }
    }

    public record MetricsSnapshot(
            int totalRequests,
            int successfulRequests,
            int failedRequests,
            int retryCount,
            int timeoutCount,
            double successRate,
            double averageLatencyMs,
            Map<String, Integer> moduleCounts,
            Map<String, Integer> providerCounts,
            Map<String, Integer> errorCounts) {}
}
