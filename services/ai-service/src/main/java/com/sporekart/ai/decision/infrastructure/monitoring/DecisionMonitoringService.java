package com.sporekart.ai.decision.infrastructure.monitoring;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DecisionMonitoringService {
    private final Counter totalDecisions; private final Counter totalAllowed; private final Counter totalDenied;
    private final Counter totalEscalated; private final Counter totalApprovals; private final Counter totalConflicts;
    private final Counter totalReplays; private final Timer decisionTimer;
    private final AtomicLong cacheHits; private final AtomicLong cacheMisses;

    public DecisionMonitoringService(MeterRegistry registry) {
        this.totalDecisions = registry.counter("decision.total");
        this.totalAllowed = registry.counter("decision.allowed");
        this.totalDenied = registry.counter("decision.denied");
        this.totalEscalated = registry.counter("decision.escalated");
        this.totalApprovals = registry.counter("decision.approvals");
        this.totalConflicts = registry.counter("decision.conflicts");
        this.totalReplays = registry.counter("decision.replays");
        this.decisionTimer = registry.timer("decision.evaluation.time");
        this.cacheHits = registry.gauge("decision.cache.hits", new AtomicLong(0));
        this.cacheMisses = registry.gauge("decision.cache.misses", new AtomicLong(0));
    }

    public void recordDecision(long timeMs) { totalDecisions.increment(); decisionTimer.record(timeMs, TimeUnit.MILLISECONDS); }
    public void recordAllowed() { totalAllowed.increment(); }
    public void recordDenied() { totalDenied.increment(); }
    public void recordEscalated() { totalEscalated.increment(); }
    public void recordApproval() { totalApprovals.increment(); }
    public void recordConflict() { totalConflicts.increment(); }
    public void recordReplay() { totalReplays.increment(); }
    public void recordCacheHit() { cacheHits.incrementAndGet(); }
    public void recordCacheMiss() { cacheMisses.incrementAndGet(); }
}
