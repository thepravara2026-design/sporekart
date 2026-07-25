package com.sporekart.alert.application.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AlertTelemetryService {

    private final AtomicLong alertCount = new AtomicLong(0);
    private final AtomicLong riskEvaluations = new AtomicLong(0);
    private final AtomicLong anomalyDetections = new AtomicLong(0);
    private final AtomicLong timelineQueries = new AtomicLong(0);
    private final AtomicLong alertResolutions = new AtomicLong(0);
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private final AtomicLong totalLatency = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final Map<String, AtomicLong> domainUsage = new ConcurrentHashMap<>();
    private final boolean enabled;

    public AlertTelemetryService() { this.enabled = true; }

    public void recordAlertGenerated(String domain, long latencyMs) {
        if (!enabled) return; alertCount.incrementAndGet(); totalLatency.addAndGet(latencyMs);
        domainUsage.computeIfAbsent(domain, k -> new AtomicLong(0)).incrementAndGet(); }
    public void recordRiskEvaluation() { if (enabled) riskEvaluations.incrementAndGet(); }
    public void recordAnomalyDetection(String domain) {
        if (enabled) { anomalyDetections.incrementAndGet(); domainUsage.computeIfAbsent(domain, k -> new AtomicLong(0)).incrementAndGet(); }}
    public void recordTimelineQuery() { if (enabled) timelineQueries.incrementAndGet(); }
    public void recordAlertResolution() { if (enabled) alertResolutions.incrementAndGet(); }
    public void recordCacheHit() { if (enabled) cacheHits.incrementAndGet(); }
    public void recordCacheMiss() { if (enabled) cacheMisses.incrementAndGet(); }
    public void recordError() { if (enabled) errorCount.incrementAndGet(); }

    public Map<String, Object> getMetrics() {
        long totalReqs = alertCount.get() + riskEvaluations.get() + anomalyDetections.get() + timelineQueries.get();
        long totalCache = cacheHits.get() + cacheMisses.get();
        var metrics = new java.util.LinkedHashMap<String, Object>();
        metrics.put("alertCount", alertCount.get());
        metrics.put("riskEvaluations", riskEvaluations.get());
        metrics.put("anomalyDetections", anomalyDetections.get());
        metrics.put("timelineQueries", timelineQueries.get());
        metrics.put("alertResolutions", alertResolutions.get());
        metrics.put("cacheHits", cacheHits.get());
        metrics.put("cacheMisses", cacheMisses.get());
        metrics.put("cacheHitRate", totalCache > 0 ? (double) cacheHits.get() / totalCache * 100 : 0.0);
        metrics.put("avgLatencyMs", totalReqs > 0 ? (double) totalLatency.get() / totalReqs : 0.0);
        metrics.put("errors", errorCount.get());
        metrics.put("domainUsage", domainUsage.entrySet().stream()
                .map(e -> Map.of("domain", e.getKey(), "requests", e.getValue().get())).toList());
        return metrics;
    }
}
