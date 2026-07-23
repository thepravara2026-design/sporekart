package com.sporekart.copilot.infrastructure.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CopilotMetricsService {

    private static final Logger log = LoggerFactory.getLogger(CopilotMetricsService.class);

    private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();
    private final Map<String, LatencySummary> latencies = new ConcurrentHashMap<>();
    private final Map<String, TokenUsage> tokenUsage = new ConcurrentHashMap<>();
    private final Map<String, CostTracker> costs = new ConcurrentHashMap<>();

    public void incrementCounter(String metricName) {
        counters.computeIfAbsent(metricName, k -> new AtomicLong(0)).incrementAndGet();
        log.debug("Incremented counter: {}", metricName);
    }

    public void recordLatency(String copilotType, long latencyMs) {
        latencies.computeIfAbsent(copilotType, k -> new LatencySummary())
            .record(latencyMs);
        log.debug("Recorded latency for {}: {}ms", copilotType, latencyMs);
    }

    public void recordTokenUsage(String copilotType, int tokens) {
        tokenUsage.computeIfAbsent(copilotType, k -> new TokenUsage())
            .record(tokens);
        log.debug("Recorded token usage for {}: {}", copilotType, tokens);
    }

    public void recordCost(String copilotType, double cost) {
        costs.computeIfAbsent(copilotType, k -> new CostTracker())
            .record(cost);
        log.debug("Recorded cost for {}: {}", copilotType, cost);
    }

    public Map<String, Object> getMetrics() {
        var metrics = new HashMap<String, Object>();

        var counterSnapshot = new HashMap<String, Long>();
        counters.forEach((k, v) -> counterSnapshot.put(k, v.get()));
        metrics.put("counters", counterSnapshot);

        var latencySnapshot = new HashMap<String, Object>();
        latencies.forEach((k, v) -> latencySnapshot.put(k, v.snapshot()));
        metrics.put("latencies", latencySnapshot);

        var tokenSnapshot = new HashMap<String, Object>();
        tokenUsage.forEach((k, v) -> tokenSnapshot.put(k, v.snapshot()));
        metrics.put("tokenUsage", tokenSnapshot);

        var costSnapshot = new HashMap<String, Object>();
        costs.forEach((k, v) -> costSnapshot.put(k, v.snapshot()));
        metrics.put("costs", costSnapshot);

        return metrics;
    }

    private static class LatencySummary {
        long count;
        long totalMs;
        long minMs = Long.MAX_VALUE;
        long maxMs = Long.MIN_VALUE;

        synchronized void record(long ms) {
            count++;
            totalMs += ms;
            if (ms < minMs) minMs = ms;
            if (ms > maxMs) maxMs = ms;
        }

        synchronized Map<String, Object> snapshot() {
            var map = new HashMap<String, Object>();
            map.put("count", count);
            map.put("avg", count > 0 ? (double) totalMs / count : 0);
            map.put("min", count > 0 ? minMs : 0);
            map.put("max", count > 0 ? maxMs : 0);
            return map;
        }
    }

    private static class TokenUsage {
        long totalTokens;
        long requestCount;

        synchronized void record(int tokens) {
            totalTokens += tokens;
            requestCount++;
        }

        synchronized Map<String, Object> snapshot() {
            var map = new HashMap<String, Object>();
            map.put("totalTokens", totalTokens);
            map.put("requestCount", requestCount);
            map.put("avg", requestCount > 0 ? (double) totalTokens / requestCount : 0);
            return map;
        }
    }

    private static class CostTracker {
        double totalCost;

        synchronized void record(double cost) {
            totalCost += cost;
        }

        synchronized Map<String, Object> snapshot() {
            var map = new HashMap<String, Object>();
            map.put("totalCost", totalCost);
            return map;
        }
    }
}
