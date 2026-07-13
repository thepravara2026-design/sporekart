package com.sporekart.ai.content.infrastructure.monitoring;

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
public class ContentMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(ContentMonitoringService.class);

    private final MeterRegistry meterRegistry;
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();

    public ContentMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public <T> T recordGenerationLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("content.generation.latency",
                k -> meterRegistry.timer("content.generation.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Content generation took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public <T> T recordSummaryLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("content.summary.latency",
                k -> meterRegistry.timer("content.summary.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Content summary took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public <T> T recordTranslationLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("content.translation.latency",
                k -> meterRegistry.timer("content.translation.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Content translation took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public <T> T recordClassificationLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("content.classification.latency",
                k -> meterRegistry.timer("content.classification.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Content classification took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public <T> T recordModerationLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("content.moderation.latency",
                k -> meterRegistry.timer("content.moderation.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Content moderation took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public <T> T recordSEOLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("content.seo.latency",
                k -> meterRegistry.timer("content.seo.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Content SEO took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public void recordGenerationCompleted() {
        meterRegistry.counter("content.generation.completed").increment();
    }

    public void recordGenerationFailed() {
        meterRegistry.counter("content.generation.failed").increment();
    }

    public void recordSummaryCompleted() {
        meterRegistry.counter("content.summary.completed").increment();
    }

    public void recordTranslationCompleted() {
        meterRegistry.counter("content.translation.completed").increment();
    }

    public void recordClassificationCompleted() {
        meterRegistry.counter("content.classification.completed").increment();
    }

    public void recordModerationCompleted() {
        meterRegistry.counter("content.moderation.completed").increment();
    }

    public void recordSEOCompleted() {
        meterRegistry.counter("content.seo.completed").increment();
    }

    public void recordCacheHit() {
        meterRegistry.counter("content.cache.hits").increment();
    }

    public void recordCacheMiss() {
        meterRegistry.counter("content.cache.misses").increment();
    }

    public double getCacheHitRatio() {
        double hits = meterRegistry.counter("content.cache.hits").count();
        double misses = meterRegistry.counter("content.cache.misses").count();
        double total = hits + misses;
        return total == 0 ? 0.0 : hits / total;
    }

    public HealthStatus checkHealth() {
        try {
            log.info("Health check: content monitoring UP");
            return new HealthStatus("UP");
        } catch (Exception e) {
            log.warn("Health check failed: {}", e.getMessage());
            return new HealthStatus("DOWN");
        }
    }

    public record HealthStatus(String status) {}
}
