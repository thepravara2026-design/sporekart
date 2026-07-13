package com.sporekart.ai.governance.infrastructure.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GovernanceMonitoringService {

    private final Counter totalRequests;
    private final Counter totalValidations;
    private final Counter totalAudits;
    private final Counter totalAllowed;
    private final Counter totalDenied;
    private final Counter totalConfigReloads;
    private final Timer validationTimer;
    private final Timer pipelineTimer;
    private final AtomicLong activePolicies;
    private final AtomicLong cacheHits;
    private final AtomicLong cacheMisses;

    public GovernanceMonitoringService(MeterRegistry registry) {
        this.totalRequests = registry.counter("governance.requests.total");
        this.totalValidations = registry.counter("governance.validations.total");
        this.totalAudits = registry.counter("governance.audits.total");
        this.totalAllowed = registry.counter("governance.decisions.allowed");
        this.totalDenied = registry.counter("governance.decisions.denied");
        this.totalConfigReloads = registry.counter("governance.config.reloads");
        this.validationTimer = registry.timer("governance.validation.time");
        this.pipelineTimer = registry.timer("governance.pipeline.time");
        this.activePolicies = registry.gauge("governance.policies.active", new AtomicLong(0));
        this.cacheHits = registry.gauge("governance.cache.hits", new AtomicLong(0));
        this.cacheMisses = registry.gauge("governance.cache.misses", new AtomicLong(0));
    }

    public void recordRequest() {
        totalRequests.increment();
    }

    public void recordValidation(long timeMs) {
        totalValidations.increment();
        validationTimer.record(timeMs, TimeUnit.MILLISECONDS);
    }

    public void recordPipeline(long timeMs) {
        pipelineTimer.record(timeMs, TimeUnit.MILLISECONDS);
    }

    public void recordAudit() {
        totalAudits.increment();
    }

    public void recordDecision(String decision) {
        if ("ALLOW".equals(decision)) {
            totalAllowed.increment();
        } else if ("DENY".equals(decision)) {
            totalDenied.increment();
        }
    }

    public void recordConfigReload() {
        totalConfigReloads.increment();
    }

    public void recordCacheHit() {
        cacheHits.incrementAndGet();
    }

    public void recordCacheMiss() {
        cacheMisses.incrementAndGet();
    }

    public void setActivePolicies(long count) {
        activePolicies.set(count);
    }
}
