package com.sporekart.ai.admin.infrastructure.monitoring;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class AdminMonitoringService {

    private final MeterRegistry meterRegistry;
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();
    private final AtomicLong activeConfigs = new AtomicLong(0);
    private final AtomicLong activeFeatureFlags = new AtomicLong(0);

    public AdminMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("admin.operations.total");
        meterRegistry.counter("admin.config.changes");
        meterRegistry.counter("admin.feature.flag.changes");
        meterRegistry.counter("admin.rollbacks");
        Gauge.builder("admin.active.configs", activeConfigs, AtomicLong::get)
                .description("Active configurations").register(meterRegistry);
        Gauge.builder("admin.active.feature.flags", activeFeatureFlags, AtomicLong::get)
                .description("Active feature flags").register(meterRegistry);
    }

    public void recordOperation() {
        meterRegistry.counter("admin.operations.total").increment();
    }

    public void recordConfigurationChange() {
        meterRegistry.counter("admin.config.changes").increment();
    }

    public void recordFeatureFlagChange() {
        meterRegistry.counter("admin.feature.flag.changes").increment();
    }

    public void recordRollback() {
        meterRegistry.counter("admin.rollbacks").increment();
    }

    public void recordConfigurationValidationTime(long latencyMs) {
        var timer = timers.computeIfAbsent("admin.config.validation.time",
                k -> meterRegistry.timer("admin.config.validation.time"));
        timer.record(latencyMs, TimeUnit.MILLISECONDS);
    }

    public void recordOperationTime(long latencyMs) {
        var timer = timers.computeIfAbsent("admin.operation.time",
                k -> meterRegistry.timer("admin.operation.time"));
        timer.record(latencyMs, TimeUnit.MILLISECONDS);
    }

    public void setActiveConfigs(long count) {
        activeConfigs.set(count);
    }

    public void setActiveFeatureFlags(long count) {
        activeFeatureFlags.set(count);
    }
}
