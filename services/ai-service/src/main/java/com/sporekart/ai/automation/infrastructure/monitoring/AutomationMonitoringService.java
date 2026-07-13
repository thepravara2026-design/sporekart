package com.sporekart.ai.automation.infrastructure.monitoring;

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
public class AutomationMonitoringService {

    private final MeterRegistry meterRegistry;
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();
    private final AtomicLong activeJobs = new AtomicLong(0);
    private final AtomicLong activeWorkflows = new AtomicLong(0);

    public AutomationMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("automation.workflows.total");
        meterRegistry.counter("automation.jobs.total");
        meterRegistry.counter("automation.retries");
        meterRegistry.counter("automation.escalations");
        meterRegistry.counter("automation.jobs.success");
        meterRegistry.counter("automation.jobs.failed");
        Gauge.builder("automation.active.jobs", activeJobs, AtomicLong::get)
                .description("Active automation jobs").register(meterRegistry);
        Gauge.builder("automation.active.workflows", activeWorkflows, AtomicLong::get)
                .description("Active automation workflows").register(meterRegistry);
    }

    public void recordWorkflow() {
        meterRegistry.counter("automation.workflows.total").increment();
    }

    public void recordJob() {
        meterRegistry.counter("automation.jobs.total").increment();
    }

    public void recordRetry() {
        meterRegistry.counter("automation.retries").increment();
    }

    public void recordEscalation() {
        meterRegistry.counter("automation.escalations").increment();
    }

    public void recordJobSuccess() {
        meterRegistry.counter("automation.jobs.success").increment();
    }

    public void recordJobFailed() {
        meterRegistry.counter("automation.jobs.failed").increment();
    }

    public void recordWorkflowDuration(long ms) {
        var timer = timers.computeIfAbsent("automation.workflow.duration",
                k -> meterRegistry.timer("automation.workflow.duration"));
        timer.record(ms, TimeUnit.MILLISECONDS);
    }

    public void recordJobExecutionTime(long ms) {
        var timer = timers.computeIfAbsent("automation.job.execution.time",
                k -> meterRegistry.timer("automation.job.execution.time"));
        timer.record(ms, TimeUnit.MILLISECONDS);
    }

    public void setActiveJobs(long count) {
        activeJobs.set(count);
    }

    public void setActiveWorkflows(long count) {
        activeWorkflows.set(count);
    }
}
