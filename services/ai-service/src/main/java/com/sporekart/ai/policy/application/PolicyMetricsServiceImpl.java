package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyMetricsService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PolicyMetricsServiceImpl implements PolicyMetricsService {

    private final AtomicLong evaluationCount = new AtomicLong(0);
    private final AtomicLong violationCount = new AtomicLong(0);
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private final AtomicLong activations = new AtomicLong(0);
    private final AtomicLong deactivations = new AtomicLong(0);
    private final AtomicLong totalTimeMs = new AtomicLong(0);

    @Override
    public void recordEvaluation(long timeMs, String decision) {
        evaluationCount.incrementAndGet();
        totalTimeMs.addAndGet(timeMs);
    }

    @Override
    public void recordViolation(String severity) {
        violationCount.incrementAndGet();
    }

    @Override
    public void recordCacheHit() {
        cacheHits.incrementAndGet();
    }

    @Override
    public void recordCacheMiss() {
        cacheMisses.incrementAndGet();
    }

    @Override
    public void recordPolicyActivation() {
        activations.incrementAndGet();
    }

    @Override
    public void recordPolicyDeactivation() {
        deactivations.incrementAndGet();
    }

    @Override
    public Map<String, Object> getMetrics() {
        return Map.of(
            "evaluationCount", evaluationCount.get(),
            "violationCount", violationCount.get(),
            "cacheHits", cacheHits.get(),
            "cacheMisses", cacheMisses.get(),
            "activations", activations.get(),
            "deactivations", deactivations.get(),
            "averageEvaluationTimeMs", getAverageEvaluationTime()
        );
    }

    @Override
    public long getEvaluationCount() {
        return evaluationCount.get();
    }

    @Override
    public long getViolationCount() {
        return violationCount.get();
    }

    @Override
    public double getAverageEvaluationTime() {
        long count = evaluationCount.get();
        return count > 0 ? (double) totalTimeMs.get() / count : 0.0;
    }
}
