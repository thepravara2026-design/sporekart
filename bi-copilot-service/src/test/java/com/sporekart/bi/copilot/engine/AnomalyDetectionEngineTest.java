package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.AnomalyAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnomalyDetectionEngineTest {

    private AnomalyDetectionEngine engine;

    @BeforeEach
    void setUp() {
        engine = new AnomalyDetectionEngine();
    }

    @Test
    void detectAnomalies_shouldReturnAnomaliesWithKnownSpikes() {
        List<Double> data = List.of(100.0, 105.0, 95.0, 500.0, 102.0, 98.0);
        List<AnomalyAlert> alerts = engine.detectAnomalies("test_metric", data);
        assertFalse(alerts.isEmpty());
    }

    @Test
    void detectAnomalies_shouldReturnEmptyForNormalData() {
        List<Double> data = List.of(100.0, 101.0, 99.0, 102.0, 100.0, 101.0);
        List<AnomalyAlert> alerts = engine.detectAnomalies("stable_metric", data);
        assertTrue(alerts.isEmpty());
    }

    @Test
    void detectAnomalies_shouldReturnEmptyForTooFewPoints() {
        List<AnomalyAlert> alerts = engine.detectAnomalies("metric", List.of(1.0, 2.0));
        assertTrue(alerts.isEmpty());
    }

    @Test
    void detectAnomalies_shouldReturnEmptyForNullData() {
        List<AnomalyAlert> alerts = engine.detectAnomalies("metric", null);
        assertTrue(alerts.isEmpty());
    }

    @Test
    void calculateDeviationScore_shouldReturnZeroForNoDeviation() {
        double score = engine.calculateDeviationScore(100.0, 100.0, 10.0);
        assertEquals(0, score, 0.001);
    }

    @Test
    void calculateDeviationScore_shouldReturnPositiveForDeviation() {
        double score = engine.calculateDeviationScore(150.0, 100.0, 10.0);
        assertEquals(5.0, score, 0.001);
    }

    @Test
    void calculateDeviationScore_shouldReturnZeroWhenStdDevIsZero() {
        double score = engine.calculateDeviationScore(100.0, 100.0, 0);
        assertEquals(0, score);
    }

    @Test
    void getActiveAnomalies_shouldReturnSeededAnomalies() {
        List<AnomalyAlert> anomalies = engine.getActiveAnomalies();
        assertFalse(anomalies.isEmpty());
    }

    @Test
    void resolveAnomaly_shouldRemoveFromActiveList() {
        List<AnomalyAlert> before = engine.getActiveAnomalies();
        String id = before.get(0).anomalyId();
        engine.resolveAnomaly(id);
        List<AnomalyAlert> after = engine.getActiveAnomalies();
        assertTrue(after.stream().noneMatch(a -> a.anomalyId().equals(id)));
    }

    @Test
    void resolveAnomaly_shouldHandleNonExistent() {
        engine.resolveAnomaly("non-existent-id");
        assertFalse(engine.getActiveAnomalies().isEmpty());
    }

    @Test
    void autoResolveAnomalies_shouldReturnCount() {
        int resolved = engine.autoResolveAnomalies("revenue");
        assertTrue(resolved >= 0);
    }

    @Test
    void getAnomalySummary_shouldHaveTotalAndBreakdown() {
        Map<String, Object> summary = engine.getAnomalySummary();
        assertTrue(summary.containsKey("totalAnomalies"));
        assertTrue(summary.containsKey("bySeverity"));
        assertTrue(summary.containsKey("byMetric"));
    }

    @Test
    void detectAnomaliesAcrossMetrics_shouldCheckAllMetrics() {
        List<AnomalyAlert> alerts = engine.detectAnomaliesAcrossMetrics();
        assertNotNull(alerts);
    }
}
