package com.sporekart.ai.workflow.infrastructure.monitoring;

import com.sporekart.ai.workflow.api.WorkflowService;
import com.sporekart.ai.workflow.infrastructure.redis.WorkflowRedisCacheService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class WorkflowMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowMonitoringService.class);

    private final MeterRegistry meterRegistry;
    private final WorkflowService workflowService;
    private final WorkflowRedisCacheService cacheService;
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();

    public WorkflowMonitoringService(MeterRegistry meterRegistry,
                                     WorkflowService workflowService,
                                     WorkflowRedisCacheService cacheService) {
        this.meterRegistry = meterRegistry;
        this.workflowService = workflowService;
        this.cacheService = cacheService;
    }

    public <T> T recordExecutionLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("workflow.execution.latency",
                k -> meterRegistry.timer("workflow.execution.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Workflow execution took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public <T> T recordStepLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("workflow.step.latency",
                k -> meterRegistry.timer("workflow.step.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Workflow step took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public void recordExecutionStarted() {
        meterRegistry.counter("workflow.execution.started").increment();
    }

    public void recordExecutionCompleted() {
        meterRegistry.counter("workflow.execution.completed").increment();
    }

    public void recordExecutionFailed() {
        meterRegistry.counter("workflow.execution.failed").increment();
    }

    public void recordStepCompleted(String stepType) {
        meterRegistry.counter("workflow.step.completed", "stepType", stepType).increment();
    }

    public void recordCacheHit(String cacheName) {
        meterRegistry.counter("workflow.cache.hit", "cache", cacheName).increment();
    }

    public void recordCacheMiss(String cacheName) {
        meterRegistry.counter("workflow.cache.miss", "cache", cacheName).increment();
    }

    public double getCacheHitRatio(String cacheName) {
        double hits = meterRegistry.counter("workflow.cache.hit", "cache", cacheName).count();
        double misses = meterRegistry.counter("workflow.cache.miss", "cache", cacheName).count();
        double total = hits + misses;
        return total == 0 ? 0.0 : hits / total;
    }

    public HealthStatus checkHealth() {
        try {
            int definitionCount = workflowService.listDefinitions().size();
            boolean cacheAvailable = cacheService != null;
            log.info("Health check: definitions={}, cache={}", definitionCount, cacheAvailable);
            return new HealthStatus("UP", definitionCount, cacheAvailable);
        } catch (Exception e) {
            log.warn("Health check failed: {}", e.getMessage());
            return new HealthStatus("DOWN", 0, false);
        }
    }

    public record HealthStatus(String status, int definitionCount, boolean cacheAvailable) {}
}
