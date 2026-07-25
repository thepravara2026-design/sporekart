package com.sporekart.alert.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertTelemetryServiceTest {

    private AlertTelemetryService telemetry;

    @BeforeEach
    void setUp() { telemetry = new AlertTelemetryService(); }

    @Test
    void getMetricsShouldReturnDefaultValues() {
        var metrics = telemetry.getMetrics();
        assertEquals(0L, metrics.get("alertCount"));
        assertEquals(0L, metrics.get("riskEvaluations"));
        assertEquals(0L, metrics.get("anomalyDetections"));
    }

    @Test
    void recordAlertGeneratedShouldIncrementCount() {
        telemetry.recordAlertGenerated("inventory", 100);
        var metrics = telemetry.getMetrics();
        assertEquals(1L, metrics.get("alertCount"));
    }

    @Test
    void recordRiskEvaluationShouldIncrementCount() {
        telemetry.recordRiskEvaluation();
        telemetry.recordRiskEvaluation();
        var metrics = telemetry.getMetrics();
        assertEquals(2L, metrics.get("riskEvaluations"));
    }

    @Test
    void recordAnomalyDetectionShouldIncrementCount() {
        telemetry.recordAnomalyDetection("security");
        var metrics = telemetry.getMetrics();
        assertEquals(1L, metrics.get("anomalyDetections"));
    }

    @Test
    void recordTimelineQueryShouldIncrementCount() {
        telemetry.recordTimelineQuery();
        var metrics = telemetry.getMetrics();
        assertEquals(1L, metrics.get("timelineQueries"));
    }

    @Test
    void recordCacheHitShouldIncrementCount() {
        telemetry.recordCacheHit();
        telemetry.recordCacheHit();
        telemetry.recordCacheHit();
        var metrics = telemetry.getMetrics();
        assertEquals(3L, metrics.get("cacheHits"));
    }

    @Test
    void recordCacheMissShouldIncrementCount() {
        telemetry.recordCacheMiss();
        var metrics = telemetry.getMetrics();
        assertEquals(1L, metrics.get("cacheMisses"));
    }

    @Test
    void cacheHitRateShouldBeCalculated() {
        telemetry.recordCacheHit();
        telemetry.recordCacheHit();
        telemetry.recordCacheMiss();
        var metrics = telemetry.getMetrics();
        assertEquals(66.67, (double) metrics.get("cacheHitRate"), 0.1);
    }

    @Test
    void recordErrorShouldIncrementCount() {
        telemetry.recordError();
        var metrics = telemetry.getMetrics();
        assertEquals(1L, metrics.get("errors"));
    }

    @Test
    void domainUsageShouldTrackDomains() {
        telemetry.recordAlertGenerated("inventory", 50);
        telemetry.recordAnomalyDetection("security");
        var metrics = telemetry.getMetrics();
        var usage = (java.util.List<?>) metrics.get("domainUsage");
        assertEquals(2, usage.size());
    }
}
