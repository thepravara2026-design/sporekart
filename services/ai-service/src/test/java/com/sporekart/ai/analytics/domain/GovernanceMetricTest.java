package com.sporekart.ai.analytics.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class GovernanceMetricTest {

    @Test
    void testRecordConstruction() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var labels = Map.of("env", "test");
        var metric = new GovernanceMetric(id, "test-metric", "governance", MetricType.COUNT, 42.0, labels, now);

        assertEquals(id, metric.id());
        assertEquals("test-metric", metric.name());
        assertEquals("governance", metric.module());
        assertEquals(MetricType.COUNT, metric.type());
        assertEquals(42.0, metric.value());
        assertEquals(labels, metric.labels());
        assertEquals(now, metric.recordedAt());
    }
}
