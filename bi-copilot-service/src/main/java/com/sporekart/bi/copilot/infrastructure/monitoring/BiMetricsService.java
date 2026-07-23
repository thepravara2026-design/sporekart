package com.sporekart.bi.copilot.infrastructure.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class BiMetricsService {

    private static final Logger log = LoggerFactory.getLogger(BiMetricsService.class);

    private final AtomicLong totalQueryLatencyMs = new AtomicLong(0);
    private final AtomicInteger queryCount = new AtomicInteger(0);

    private final AtomicLong totalAnalyticsGenerationMs = new AtomicLong(0);
    private final AtomicInteger analyticsGenerationCount = new AtomicInteger(0);

    private final AtomicLong totalForecastExecutionMs = new AtomicLong(0);
    private final AtomicInteger forecastExecutionCount = new AtomicInteger(0);

    private final AtomicInteger recommendationAccuracyHits = new AtomicInteger(0);
    private final AtomicInteger recommendationAccuracyTotal = new AtomicInteger(0);

    private final AtomicLong totalKnowledgeRetrievalMs = new AtomicLong(0);
    private final AtomicInteger knowledgeRetrievalCount = new AtomicInteger(0);

    private final AtomicInteger promptUsageCount = new AtomicInteger(0);
    private final AtomicLong totalTokenCost = new AtomicLong(0);

    private final ConcurrentHashMap<String, AtomicInteger> dashboardUsage = new ConcurrentHashMap<>();

    public void recordQueryLatency(long durationMs) {
        totalQueryLatencyMs.addAndGet(durationMs);
        queryCount.incrementAndGet();
    }

    public void recordAnalyticsGeneration(long durationMs) {
        totalAnalyticsGenerationMs.addAndGet(durationMs);
        analyticsGenerationCount.incrementAndGet();
    }

    public void recordForecastExecution(long durationMs) {
        totalForecastExecutionMs.addAndGet(durationMs);
        forecastExecutionCount.incrementAndGet();
    }

    public void recordRecommendationAccuracy(boolean accurate) {
        recommendationAccuracyTotal.incrementAndGet();
        if (accurate) {
            recommendationAccuracyHits.incrementAndGet();
        }
    }

    public void recordKnowledgeRetrieval(long durationMs) {
        totalKnowledgeRetrievalMs.addAndGet(durationMs);
        knowledgeRetrievalCount.incrementAndGet();
    }

    public void recordPromptUsage() {
        promptUsageCount.incrementAndGet();
    }

    public void recordTokenCost(long tokens) {
        totalTokenCost.addAndGet(tokens);
    }

    public void recordDashboardUsage(String dashboardType) {
        dashboardUsage.computeIfAbsent(dashboardType, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public double getAverageQueryLatencyMs() {
        int count = queryCount.get();
        return count > 0 ? (double) totalQueryLatencyMs.get() / count : 0;
    }

    public double getAverageAnalyticsGenerationMs() {
        int count = analyticsGenerationCount.get();
        return count > 0 ? (double) totalAnalyticsGenerationMs.get() / count : 0;
    }

    public double getAverageForecastExecutionMs() {
        int count = forecastExecutionCount.get();
        return count > 0 ? (double) totalForecastExecutionMs.get() / count : 0;
    }

    public double getRecommendationAccuracy() {
        int total = recommendationAccuracyTotal.get();
        return total > 0 ? (double) recommendationAccuracyHits.get() / total * 100 : 0;
    }

    public double getAverageKnowledgeRetrievalMs() {
        int count = knowledgeRetrievalCount.get();
        return count > 0 ? (double) totalKnowledgeRetrievalMs.get() / count : 0;
    }

    public int getPromptUsageCount() {
        return promptUsageCount.get();
    }

    public long getTotalTokenCost() {
        return totalTokenCost.get();
    }

    public Map<String, Integer> getDashboardUsage() {
        var map = new java.util.LinkedHashMap<String, Integer>();
        dashboardUsage.forEach((k, v) -> map.put(k, v.get()));
        return map;
    }

    public Map<String, Object> getAllMetrics() {
        return Map.of(
            "queryLatency", Map.of("avgMs", getAverageQueryLatencyMs(), "total", queryCount.get()),
            "analyticsGeneration", Map.of("avgMs", getAverageAnalyticsGenerationMs(), "total", analyticsGenerationCount.get()),
            "forecastExecution", Map.of("avgMs", getAverageForecastExecutionMs(), "total", forecastExecutionCount.get()),
            "recommendationAccuracy", Map.of("accuracy", getRecommendationAccuracy(), "total", recommendationAccuracyTotal.get()),
            "knowledgeRetrieval", Map.of("avgMs", getAverageKnowledgeRetrievalMs(), "total", knowledgeRetrievalCount.get()),
            "promptUsage", Map.of("count", promptUsageCount.get()),
            "tokenCost", Map.of("total", totalTokenCost.get()),
            "dashboardUsage", getDashboardUsage()
        );
    }

    public void reset() {
        totalQueryLatencyMs.set(0);
        queryCount.set(0);
        totalAnalyticsGenerationMs.set(0);
        analyticsGenerationCount.set(0);
        totalForecastExecutionMs.set(0);
        forecastExecutionCount.set(0);
        recommendationAccuracyHits.set(0);
        recommendationAccuracyTotal.set(0);
        totalKnowledgeRetrievalMs.set(0);
        knowledgeRetrievalCount.set(0);
        promptUsageCount.set(0);
        totalTokenCost.set(0);
        dashboardUsage.clear();
    }
}
