package com.sporekart.grower.copilot.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class GrowerCopilotMetricsService {

    private static final Logger log = LoggerFactory.getLogger(GrowerCopilotMetricsService.class);

    private final AtomicLong totalQueries = new AtomicLong(0);
    private final AtomicLong totalKnowledgeRetrievals = new AtomicLong(0);
    private final AtomicLong totalYieldPredictions = new AtomicLong(0);
    private final AtomicLong totalDiseaseQueries = new AtomicLong(0);
    private final AtomicLong totalPromptUsage = new AtomicLong(0);
    private final AtomicLong totalTokenCost = new AtomicLong(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);
    private final AtomicInteger scientificReferencesCount = new AtomicInteger(0);
    private final ConcurrentHashMap<String, AtomicInteger> intentCounts = new ConcurrentHashMap<>();

    public void recordQuery(String intent) {
        totalQueries.incrementAndGet();
        intentCounts.computeIfAbsent(intent, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void recordKnowledgeRetrieval(int resultCount) {
        totalKnowledgeRetrievals.incrementAndGet();
        scientificReferencesCount.addAndGet(resultCount);
    }

    public void recordYieldPrediction() {
        totalYieldPredictions.incrementAndGet();
    }

    public void recordDiseaseQuery() {
        totalDiseaseQueries.incrementAndGet();
    }

    public void recordTokenUsage(long tokens) {
        totalPromptUsage.incrementAndGet();
        totalTokenCost.addAndGet(tokens);
    }

    public void recordLatency(long latencyMs) {
        totalLatencyMs.addAndGet(latencyMs);
    }

    public Map<String, Object> getMetricsSummary() {
        long queries = totalQueries.get();
        return Map.of(
            "totalQueries", queries,
            "totalKnowledgeRetrievals", totalKnowledgeRetrievals.get(),
            "totalYieldPredictions", totalYieldPredictions.get(),
            "totalDiseaseQueries", totalDiseaseQueries.get(),
            "totalPromptUsage", totalPromptUsage.get(),
            "totalTokenCost", totalTokenCost.get(),
            "averageLatencyMs", queries > 0 ? totalLatencyMs.get() / queries : 0,
            "scientificReferencesCount", scientificReferencesCount.get(),
            "intentBreakdown", intentCounts.entrySet().stream()
                .map(e -> Map.of("intent", e.getKey(), "count", e.getValue().get()))
                .toList()
        );
    }

    public void resetMetrics() {
        totalQueries.set(0);
        totalKnowledgeRetrievals.set(0);
        totalYieldPredictions.set(0);
        totalDiseaseQueries.set(0);
        totalPromptUsage.set(0);
        totalTokenCost.set(0);
        totalLatencyMs.set(0);
        scientificReferencesCount.set(0);
        intentCounts.clear();
        log.info("Grower copilot metrics reset");
    }
}
