package com.sporekart.ai.gateway.application;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GatewayMetricsCollector {
    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();
    private final Map<String, Timer> timerCache = new ConcurrentHashMap<>();

    public GatewayMetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordExecution(String module, long durationMs, boolean success) {
        Timer timer = timerCache.computeIfAbsent("ai.execution." + module,
                k -> Timer.builder("ai.execution")
                        .tag("module", module)
                        .register(meterRegistry));
        timer.record(java.time.Duration.ofMillis(durationMs));

        String status = success ? "success" : "failure";
        Counter counter = counterCache.computeIfAbsent("ai.execution.count." + module + "." + status,
                k -> Counter.builder("ai.execution.count")
                        .tag("module", module)
                        .tag("status", status)
                        .register(meterRegistry));
        counter.increment();
    }

    public void incrementError(String module, String errorType) {
        Counter counter = counterCache.computeIfAbsent("ai.error." + module + "." + errorType,
                k -> Counter.builder("ai.error.count")
                        .tag("module", module)
                        .tag("error_type", errorType)
                        .register(meterRegistry));
        counter.increment();
    }

    public void recordLatency(String module, String provider, long durationMs) {
        Timer timer = timerCache.computeIfAbsent("ai.latency." + module + "." + provider,
                k -> Timer.builder("ai.latency")
                        .tag("module", module)
                        .tag("provider", provider)
                        .register(meterRegistry));
        timer.record(java.time.Duration.ofMillis(durationMs));
    }
}
