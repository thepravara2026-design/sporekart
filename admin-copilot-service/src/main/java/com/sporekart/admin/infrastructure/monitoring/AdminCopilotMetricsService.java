package com.sporekart.admin.infrastructure.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AdminCopilotMetricsService {

    private static final Logger log = LoggerFactory.getLogger(AdminCopilotMetricsService.class);

    private final AtomicLong queryLatencyTotal = new AtomicLong(0);
    private final AtomicLong queryCount = new AtomicLong(0);
    private final AtomicLong dashboardGenerationTime = new AtomicLong(0);
    private final AtomicLong dashboardCount = new AtomicLong(0);
    private final AtomicLong analyticsUsageCount = new AtomicLong(0);
    private final AtomicLong reportCount = new AtomicLong(0);
    private final AtomicLong knowledgeUsageCount = new AtomicLong(0);
    private final AtomicLong promptCount = new AtomicLong(0);
    private final AtomicLong totalTokensUsed = new AtomicLong(0);
    private final AtomicLong totalTokenCostMicros = new AtomicLong(0);

    private final ConcurrentHashMap<String, AtomicLong> providerUsage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> adminActivityCounts = new ConcurrentHashMap<>();

    public void recordQuery(long latencyMs) {
        queryLatencyTotal.addAndGet(latencyMs);
        queryCount.incrementAndGet();
        log.debug("Recorded query with latency {}ms", latencyMs);
    }

    public void recordDashboardGeneration(long timeMs) {
        dashboardGenerationTime.addAndGet(timeMs);
        dashboardCount.incrementAndGet();
        log.debug("Recorded dashboard generation: {}ms", timeMs);
    }

    public void recordAnalyticsUsage() {
        analyticsUsageCount.incrementAndGet();
    }

    public void recordReportGenerated() {
        reportCount.incrementAndGet();
    }

    public void recordKnowledgeAccess() {
        knowledgeUsageCount.incrementAndGet();
    }

    public void recordPromptExecution(long tokensUsed, long costMicros) {
        promptCount.incrementAndGet();
        totalTokensUsed.addAndGet(tokensUsed);
        totalTokenCostMicros.addAndGet(costMicros);
    }

    public void recordProviderUsage(String providerName) {
        providerUsage.computeIfAbsent(providerName, k -> new AtomicLong(0)).incrementAndGet();
    }

    public void recordAdminActivity(String activityType) {
        adminActivityCounts.computeIfAbsent(activityType, k -> new AtomicLong(0)).incrementAndGet();
    }

    public double getAverageQueryLatency() {
        long count = queryCount.get();
        return count > 0 ? (double) queryLatencyTotal.get() / count : 0.0;
    }

    public double getAverageDashboardGenerationTime() {
        long count = dashboardCount.get();
        return count > 0 ? (double) dashboardGenerationTime.get() / count : 0.0;
    }

    public long getQueryCount() {
        return queryCount.get();
    }

    public long getDashboardCount() {
        return dashboardCount.get();
    }

    public long getAnalyticsUsageCount() {
        return analyticsUsageCount.get();
    }

    public long getReportCount() {
        return reportCount.get();
    }

    public long getKnowledgeUsageCount() {
        return knowledgeUsageCount.get();
    }

    public long getPromptCount() {
        return promptCount.get();
    }

    public long getTotalTokensUsed() {
        return totalTokensUsed.get();
    }

    public long getTotalTokenCostMicros() {
        return totalTokenCostMicros.get();
    }

    public Map<String, Long> getProviderUsage() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        providerUsage.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }

    public Map<String, Long> getAdminActivityCounts() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        adminActivityCounts.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }

    public Map<String, Object> getAllMetrics() {
        Map<String, Object> all = new java.util.LinkedHashMap<>();
        all.put("averageQueryLatency", getAverageQueryLatency());
        all.put("queriesTotal", queryCount.get());
        all.put("averageDashboardGenerationMs", getAverageDashboardGenerationTime());
        all.put("dashboardsGenerated", dashboardCount.get());
        all.put("analyticsUsageCount", analyticsUsageCount.get());
        all.put("reportsGenerated", reportCount.get());
        all.put("knowledgeAccesses", knowledgeUsageCount.get());
        all.put("promptsExecuted", promptCount.get());
        all.put("totalTokensUsed", totalTokensUsed.get());
        all.put("totalTokenCostMicros", totalTokenCostMicros.get());
        all.put("totalTokenCostUsd", String.format("%.6f", totalTokenCostMicros.get() / 1_000_000.0));
        all.put("providerUsage", getProviderUsage());
        all.put("adminActivityCounts", getAdminActivityCounts());
        return all;
    }

    public void resetMetrics() {
        queryLatencyTotal.set(0);
        queryCount.set(0);
        dashboardGenerationTime.set(0);
        dashboardCount.set(0);
        analyticsUsageCount.set(0);
        reportCount.set(0);
        knowledgeUsageCount.set(0);
        promptCount.set(0);
        totalTokensUsed.set(0);
        totalTokenCostMicros.set(0);
        providerUsage.clear();
        adminActivityCounts.clear();
        log.info("All metrics have been reset");
    }
}
