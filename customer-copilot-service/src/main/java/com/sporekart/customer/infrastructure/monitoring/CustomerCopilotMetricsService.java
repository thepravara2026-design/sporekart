package com.sporekart.customer.infrastructure.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

@Service
public class CustomerCopilotMetricsService {

    private static final Logger log = LoggerFactory.getLogger(CustomerCopilotMetricsService.class);

    private final AtomicLong conversationsCount = new AtomicLong(0);
    private final DoubleAdder totalLatencyMs = new DoubleAdder();
    private final AtomicLong latencySamples = new AtomicLong(0);
    private final AtomicLong knowledgeUsageCount = new AtomicLong(0);
    private final AtomicLong recommendationAccuracyHits = new AtomicLong(0);
    private final AtomicLong recommendationTotal = new AtomicLong(0);
    private final AtomicLong productClicks = new AtomicLong(0);
    private final AtomicLong escalationsCount = new AtomicLong(0);
    private final DoubleAdder totalTokensUsed = new DoubleAdder();
    private final DoubleAdder totalCostTracked = new DoubleAdder();
    private final ConcurrentHashMap<String, AtomicLong> providerUsage = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, AtomicLong> customCounters = new ConcurrentHashMap<>();

    public void recordConversation() {
        conversationsCount.incrementAndGet();
        log.debug("Metrics: conversation recorded, total={}", conversationsCount.get());
    }

    public void recordLatency(long latencyMs) {
        totalLatencyMs.add(latencyMs);
        latencySamples.incrementAndGet();
        log.debug("Metrics: latency recorded={}ms, avg={}ms", latencyMs, getAverageLatency());
    }

    public void recordKnowledgeUsage() {
        knowledgeUsageCount.incrementAndGet();
        log.debug("Metrics: knowledge usage recorded, total={}", knowledgeUsageCount.get());
    }

    public void recordRecommendation(boolean accurate) {
        recommendationTotal.incrementAndGet();
        if (accurate) {
            recommendationAccuracyHits.incrementAndGet();
        }
        log.debug("Metrics: recommendation recorded, accuracy={}%", getRecommendationAccuracy());
    }

    public void recordProductClick() {
        productClicks.incrementAndGet();
        log.debug("Metrics: product click recorded, total={}", productClicks.get());
    }

    public void recordEscalation() {
        escalationsCount.incrementAndGet();
        log.warn("Metrics: escalation recorded, total={}", escalationsCount.get());
    }

    public void recordTokenUsage(double tokens) {
        totalTokensUsed.add(tokens);
        log.debug("Metrics: tokens used={}, total={}", tokens, totalTokensUsed.sum());
    }

    public void recordCost(double costUsd) {
        totalCostTracked.add(costUsd);
        log.debug("Metrics: cost recorded=${}, total=${}", String.format("%.4f", costUsd), String.format("%.4f", totalCostTracked.sum()));
    }

    public void recordProviderUsage(String provider) {
        providerUsage.computeIfAbsent(provider, k -> new AtomicLong(0)).incrementAndGet();
        log.debug("Metrics: provider '{}' usage recorded", provider);
    }

    public void incrementCounter(String name) {
        customCounters.computeIfAbsent(name, k -> new AtomicLong(0)).incrementAndGet();
    }

    public void incrementCounterBy(String name, long delta) {
        customCounters.computeIfAbsent(name, k -> new AtomicLong(0)).addAndGet(delta);
    }

    public long getConversationsCount() {
        return conversationsCount.get();
    }

    public double getAverageLatency() {
        long samples = latencySamples.get();
        return samples > 0 ? totalLatencyMs.sum() / samples : 0.0;
    }

    public long getKnowledgeUsageCount() {
        return knowledgeUsageCount.get();
    }

    public double getRecommendationAccuracy() {
        long total = recommendationTotal.get();
        return total > 0 ? (recommendationAccuracyHits.get() * 100.0 / total) : 0.0;
    }

    public long getProductClicks() {
        return productClicks.get();
    }

    public long getEscalationsCount() {
        return escalationsCount.get();
    }

    public double getTotalTokensUsed() {
        return totalTokensUsed.sum();
    }

    public double getTotalCostTracked() {
        return totalCostTracked.sum();
    }

    public Map<String, Long> getProviderUsage() {
        var result = new ConcurrentHashMap<String, Long>();
        providerUsage.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }

    public Map<String, Object> getAllMetrics() {
        return Map.of(
            "conversationsCount", conversationsCount.get(),
            "averageLatencyMs", getAverageLatency(),
            "knowledgeUsageCount", knowledgeUsageCount.get(),
            "recommendationAccuracyPercent", getRecommendationAccuracy(),
            "productClicks", productClicks.get(),
            "escalationsCount", escalationsCount.get(),
            "totalTokensUsed", totalTokensUsed.sum(),
            "totalCostTracked", totalCostTracked.sum(),
            "providerUsage", getProviderUsage(),
            "customCounters", customCounters.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()))
        );
    }

    public void resetAll() {
        conversationsCount.set(0);
        totalLatencyMs.reset();
        latencySamples.set(0);
        knowledgeUsageCount.set(0);
        recommendationAccuracyHits.set(0);
        recommendationTotal.set(0);
        productClicks.set(0);
        escalationsCount.set(0);
        totalTokensUsed.reset();
        totalCostTracked.reset();
        providerUsage.clear();
        customCounters.clear();
        log.info("All metrics have been reset");
    }
}
