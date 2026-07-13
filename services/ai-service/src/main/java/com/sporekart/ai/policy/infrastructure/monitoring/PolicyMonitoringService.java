package com.sporekart.ai.policy.infrastructure.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PolicyMonitoringService {

    private final Counter totalEvaluations;
    private final Counter totalViolations;
    private final Counter totalAllowed;
    private final Counter totalDenied;
    private final Counter totalReviews;
    private final Counter totalActivations;
    private final Counter totalDeactivations;
    private final Timer evaluationTimer;
    private final AtomicLong registrySize;
    private final AtomicLong cacheHits;
    private final AtomicLong cacheMisses;

    public PolicyMonitoringService(MeterRegistry registry) {
        this.totalEvaluations = registry.counter("policy.evaluations.total");
        this.totalViolations = registry.counter("policy.violations.total");
        this.totalAllowed = registry.counter("policy.decisions.allowed");
        this.totalDenied = registry.counter("policy.decisions.denied");
        this.totalReviews = registry.counter("policy.decisions.review");
        this.totalActivations = registry.counter("policy.activations.total");
        this.totalDeactivations = registry.counter("policy.deactivations.total");
        this.evaluationTimer = registry.timer("policy.evaluation.time");
        this.registrySize = registry.gauge("policy.registry.size", new AtomicLong(0));
        this.cacheHits = registry.gauge("policy.cache.hits", new AtomicLong(0));
        this.cacheMisses = registry.gauge("policy.cache.misses", new AtomicLong(0));
    }

    public void recordEvaluation(long timeMs) {
        totalEvaluations.increment();
        evaluationTimer.record(timeMs, TimeUnit.MILLISECONDS);
    }

    public void recordViolation() {
        totalViolations.increment();
    }

    public void recordDecision(String decision) {
        switch (decision) {
            case "ALLOW", "BYPASS" -> totalAllowed.increment();
            case "DENY", "CHALLENGE" -> totalDenied.increment();
            case "REVIEW" -> totalReviews.increment();
        }
    }

    public void recordActivation() {
        totalActivations.increment();
    }

    public void recordDeactivation() {
        totalDeactivations.increment();
    }

    public void recordCacheHit() {
        cacheHits.incrementAndGet();
    }

    public void recordCacheMiss() {
        cacheMisses.incrementAndGet();
    }

    public void setRegistrySize(long size) {
        registrySize.set(size);
    }
}
