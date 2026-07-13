package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.DecisionMetricsService;
import com.sporekart.ai.decision.domain.*;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DecisionMetricsServiceImpl implements DecisionMetricsService {
    private final AtomicLong totalDecisions = new AtomicLong(0);
    private final AtomicLong allowedCount = new AtomicLong(0);
    private final AtomicLong deniedCount = new AtomicLong(0);
    private final AtomicLong escalatedCount = new AtomicLong(0);
    private final AtomicLong approvalCount = new AtomicLong(0);
    private final AtomicLong conflictCount = new AtomicLong(0);
    private final AtomicLong replayCount = new AtomicLong(0);
    private final AtomicLong totalTimeMs = new AtomicLong(0);
    private final AtomicLong totalConfidenceScore = new AtomicLong(0);

    @Override
    public void recordDecision(String action, DecisionConfidence confidence, long timeMs) {
        totalDecisions.incrementAndGet();
        totalTimeMs.addAndGet(timeMs);
        totalConfidenceScore.addAndGet(confidence.ordinal());
        switch (action) {
            case "ALLOW" -> allowedCount.incrementAndGet();
            case "DENY", "BLOCK_REQUEST" -> deniedCount.incrementAndGet();
            case "ESCALATE_TO_ADMIN" -> escalatedCount.incrementAndGet();
            case "REQUIRE_APPROVAL" -> approvalCount.incrementAndGet();
        }
    }

    @Override public void recordConflict(String strategy) { conflictCount.incrementAndGet(); }
    @Override public void recordReplay() { replayCount.incrementAndGet(); }

    @Override
    public DecisionStatistics getStatistics() {
        return new DecisionStatistics(UUID.randomUUID(), totalDecisions.get(), allowedCount.get(),
            deniedCount.get(), escalatedCount.get(), approvalCount.get(), conflictCount.get(),
            getAverageConfidence(), getAverageLatency(), OffsetDateTime.now());
    }

    @Override
    public Map<String, Object> getDetailedMetrics() {
        return Map.of("totalDecisions", totalDecisions.get(), "allowed", allowedCount.get(),
            "denied", deniedCount.get(), "escalated", escalatedCount.get(),
            "approvals", approvalCount.get(), "conflicts", conflictCount.get(),
            "replays", replayCount.get(), "avgConfidence", getAverageConfidence(),
            "avgLatencyMs", getAverageLatency());
    }

    @Override public long getTotalDecisions() { return totalDecisions.get(); }
    @Override public double getAverageConfidence() {
        long count = totalDecisions.get();
        return count > 0 ? (double) totalConfidenceScore.get() / count : 0.0;
    }
    private double getAverageLatency() {
        long count = totalDecisions.get();
        return count > 0 ? (double) totalTimeMs.get() / count : 0.0;
    }
}
