package com.sporekart.ai.analytics.infrastructure.monitoring;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Slf4j
@Service
public class AnalyticsMonitoringService {

    private final MeterRegistry meterRegistry;
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();
    private final AtomicReference<Double> cacheHitRatio = new AtomicReference<>(0.0);
    private final AtomicReference<Double> processingTime = new AtomicReference<>(0.0);

    public AnalyticsMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void initGauges() {
        Gauge.builder("analytics.cache.hit.ratio", cacheHitRatio, AtomicReference::get)
                .description("Analytics cache hit ratio")
                .register(meterRegistry);
        Gauge.builder("analytics.processing.time", processingTime, AtomicReference::get)
                .description("Analytics average processing time")
                .register(meterRegistry);
    }

    public void recordDashboardLoad() {
        meterRegistry.counter("analytics.dashboard.loads").increment();
    }

    public void recordReportGenerated() {
        meterRegistry.counter("analytics.reports.generated").increment();
    }

    public void recordExportCompleted() {
        meterRegistry.counter("analytics.exports.completed").increment();
    }

    public void recordKPICalculation() {
        meterRegistry.counter("analytics.kpi.calculations").increment();
    }

    public <T> T recordDashboardLoadTime(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("analytics.dashboard.load.time",
                k -> meterRegistry.timer("analytics.dashboard.load.time"));
        return timer.record(operation);
    }

    public <T> T recordReportGenerationTime(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("analytics.report.generation.time",
                k -> meterRegistry.timer("analytics.report.generation.time"));
        return timer.record(operation);
    }

    public <T> T recordKPICalculationTime(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("analytics.kpi.calculation.time",
                k -> meterRegistry.timer("analytics.kpi.calculation.time"));
        return timer.record(operation);
    }

    public <T> T recordExportTime(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("analytics.export.time",
                k -> meterRegistry.timer("analytics.export.time"));
        return timer.record(operation);
    }

    public void updateCacheHitRatio(double ratio) {
        cacheHitRatio.set(ratio);
    }

    public void updateProcessingTime(double timeMs) {
        processingTime.set(timeMs);
    }
}
