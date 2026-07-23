package com.sporekart.bi.copilot.infrastructure.monitoring;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BiMetricsService {

    private static final Logger log = LoggerFactory.getLogger(BiMetricsService.class);

    private final AtomicLong messagesProcessed = new AtomicLong(0);
    private final AtomicLong sessionsCreated = new AtomicLong(0);
    private final AtomicLong sessionsEnded = new AtomicLong(0);
    private final AtomicLong queriesExecuted = new AtomicLong(0);
    private final AtomicLong reportsGenerated = new AtomicLong(0);
    private final AtomicLong anomaliesDetected = new AtomicLong(0);
    private final AtomicLong forecastsGenerated = new AtomicLong(0);
    private final AtomicLong errorsEncountered = new AtomicLong(0);

    private final Map<String, AtomicLong> perEndpointHits = new ConcurrentHashMap<>();
    private final Map<String, Long> latencyBuckets = new ConcurrentHashMap<>();

    public BiMetricsService() {
        log.info("BiMetricsService initialized");
    }

    public void incrementMessagesProcessed() {
        messagesProcessed.incrementAndGet();
    }

    public void incrementSessionsCreated() {
        sessionsCreated.incrementAndGet();
    }

    public void incrementSessionsEnded() {
        sessionsEnded.incrementAndGet();
    }

    public void incrementQueriesExecuted() {
        queriesExecuted.incrementAndGet();
    }

    public void incrementReportsGenerated() {
        reportsGenerated.incrementAndGet();
    }

    public void incrementAnomaliesDetected() {
        anomaliesDetected.incrementAndGet();
    }

    public void incrementForecastsGenerated() {
        forecastsGenerated.incrementAndGet();
    }

    public void incrementErrors() {
        errorsEncountered.incrementAndGet();
    }

    public void recordEndpointHit(String endpoint) {
        perEndpointHits.computeIfAbsent(endpoint, k -> new AtomicLong(0)).incrementAndGet();
    }

    public void recordLatency(String operation, long durationMs) {
        latencyBuckets.put(operation + "_last", durationMs);
        latencyBuckets.merge(operation + "_total", durationMs, Long::sum);
    }

    public long getMessagesProcessed() { return messagesProcessed.get(); }
    public long getSessionsCreated() { return sessionsCreated.get(); }
    public long getSessionsEnded() { return sessionsEnded.get(); }
    public long getQueriesExecuted() { return queriesExecuted.get(); }
    public long getReportsGenerated() { return reportsGenerated.get(); }
    public long getAnomaliesDetected() { return anomaliesDetected.get(); }
    public long getForecastsGenerated() { return forecastsGenerated.get(); }
    public long getErrorsEncountered() { return errorsEncountered.get(); }

    public long getActiveSessions() {
        return sessionsCreated.get() - sessionsEnded.get();
    }

    public Map<String, Object> getMetricsSnapshot() {
        return Map.of(
            "messagesProcessed", messagesProcessed.get(),
            "sessionsCreated", sessionsCreated.get(),
            "sessionsEnded", sessionsEnded.get(),
            "activeSessions", getActiveSessions(),
            "queriesExecuted", queriesExecuted.get(),
            "reportsGenerated", reportsGenerated.get(),
            "anomaliesDetected", anomaliesDetected.get(),
            "forecastsGenerated", forecastsGenerated.get(),
            "errorsEncountered", errorsEncountered.get(),
            "timestamp", OffsetDateTime.now().toString()
        );
    }

    public void reset() {
        messagesProcessed.set(0);
        sessionsCreated.set(0);
        sessionsEnded.set(0);
        queriesExecuted.set(0);
        reportsGenerated.set(0);
        anomaliesDetected.set(0);
        forecastsGenerated.set(0);
        errorsEncountered.set(0);
        perEndpointHits.clear();
        latencyBuckets.clear();
        log.info("BiMetricsService metrics reset");
    }
}
