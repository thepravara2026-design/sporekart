package com.sporekart.alert.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnomalyTest {

    @Test
    void createShouldReturnAnomalyWithGivenValues() {
        var anomaly = Anomaly.create(AnomalyType.UNEXPECTED_GROWTH, "operations",
                AlertSeverity.HIGH, "Traffic spike detected", 1000, 1500, 50.0, 0.92);
        assertNotNull(anomaly.id());
        assertEquals(AnomalyType.UNEXPECTED_GROWTH, anomaly.type());
        assertEquals("operations", anomaly.domain());
        assertEquals(AlertSeverity.HIGH, anomaly.severity());
        assertEquals(1000, anomaly.expectedValue());
        assertEquals(1500, anomaly.actualValue());
        assertEquals(50.0, anomaly.deviation());
        assertEquals(0.92, anomaly.confidence());
    }

    @Test
    void createShouldSetDetectedAt() {
        var anomaly = Anomaly.create(AnomalyType.OUTLIER, "infra",
                AlertSeverity.MEDIUM, "System anomaly", 0, 100, 100.0, 0.85);
        assertNotNull(anomaly.detectedAt());
    }
}
