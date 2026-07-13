package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.RiskMetricsService;
import com.sporekart.ai.risk.domain.RecommendationType;
import com.sporekart.ai.risk.domain.RiskLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class RiskMetricsServiceImpl implements RiskMetricsService {

    private final AtomicLong totalAssessments = new AtomicLong(0);
    private final AtomicLong totalRiskScore = new AtomicLong(0);
    private final AtomicLong totalTrustScore = new AtomicLong(0);
    private final AtomicLong totalConfidence = new AtomicLong(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);
    private final AtomicLong assessmentCount = new AtomicLong(0);
    private final AtomicLong trustCount = new AtomicLong(0);
    private final AtomicLong confidenceCount = new AtomicLong(0);
    private final AtomicLong latencyCount = new AtomicLong(0);

    private final ConcurrentHashMap<RiskLevel, AtomicLong> riskDistribution = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<RecommendationType, AtomicLong> recommendationCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> trustTrends = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> confidenceTrends = new ConcurrentHashMap<>();

    public void recordAssessment(RiskLevel level) {
        totalAssessments.incrementAndGet();
        riskDistribution.computeIfAbsent(level, k -> new AtomicLong(0)).incrementAndGet();
    }

    public void recordRecommendation(RecommendationType type) {
        recommendationCounts.computeIfAbsent(type, k -> new AtomicLong(0)).incrementAndGet();
    }

    public void recordTrustScore(double score) {
        totalTrustScore.addAndGet((long) (score * 100));
        trustCount.incrementAndGet();
        trustTrends.computeIfAbsent("average", k -> new AtomicLong(0))
            .addAndGet((long) (score * 100));
    }

    public void recordConfidence(double score) {
        totalConfidence.addAndGet((long) (score * 100));
        confidenceCount.incrementAndGet();
        confidenceTrends.computeIfAbsent("average", k -> new AtomicLong(0))
            .addAndGet((long) (score * 100));
    }

    public void recordLatency(long ms) {
        totalLatencyMs.addAndGet(ms);
        latencyCount.incrementAndGet();
    }

    @Override
    public long getTotalAssessments() {
        return totalAssessments.get();
    }

    @Override
    public double getAverageRiskScore() {
        long count = assessmentCount.get();
        return count > 0 ? (double) totalRiskScore.get() / count : 0.0;
    }

    @Override
    public Map<String, Long> getRiskDistribution() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        riskDistribution.forEach((level, count) -> result.put(level.name(), count.get()));
        return result;
    }

    @Override
    public Map<String, Double> getTrustTrends() {
        long count = trustCount.get();
        double avg = count > 0 ? (double) totalTrustScore.get() / (count * 100.0) : 0.0;
        return Map.of("average", avg, "count", (double) count);
    }

    @Override
    public Map<String, Double> getConfidenceTrends() {
        long count = confidenceCount.get();
        double avg = count > 0 ? (double) totalConfidence.get() / (count * 100.0) : 0.0;
        return Map.of("average", avg, "count", (double) count);
    }

    @Override
    public Map<String, Long> getRecommendationCounts() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        recommendationCounts.forEach((type, count) -> result.put(type.name(), count.get()));
        return result;
    }

    @Override
    public Map<String, Object> getStatistics() {
        long assessCount = totalAssessments.get();
        long tCount = trustCount.get();
        long cCount = confidenceCount.get();
        long lCount = latencyCount.get();

        return Map.of(
            "totalAssessments", assessCount,
            "averageRiskScore", getAverageRiskScore(),
            "riskDistribution", getRiskDistribution(),
            "trustTrends", Map.of(
                "average", tCount > 0 ? (double) totalTrustScore.get() / (tCount * 100.0) : 0.0,
                "count", tCount
            ),
            "confidenceTrends", Map.of(
                "average", cCount > 0 ? (double) totalConfidence.get() / (cCount * 100.0) : 0.0,
                "count", cCount
            ),
            "recommendationCounts", getRecommendationCounts(),
            "averageLatencyMs", lCount > 0 ? (double) totalLatencyMs.get() / lCount : 0.0
        );
    }
}
