package com.sporekart.ai.analytics.engine;

import com.sporekart.ai.analytics.domain.GovernanceMetric;
import com.sporekart.ai.analytics.domain.MetricType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MetricsAggregatorTest {

    private MetricsAggregator aggregator;
    private List<GovernanceMetric> metrics;

    @BeforeEach
    void setUp() {
        aggregator = new MetricsAggregator();
        var now = Instant.now();
        metrics = List.of(
                new GovernanceMetric(UUID.randomUUID(), "m1", "moduleA", MetricType.COUNT, 10.0, Map.of(), now),
                new GovernanceMetric(UUID.randomUUID(), "m2", "moduleA", MetricType.RATE, 20.0, Map.of(), now),
                new GovernanceMetric(UUID.randomUUID(), "m3", "moduleB", MetricType.AVERAGE, 30.0, Map.of(), now)
        );
    }

    @Test
    void testAggregateByModule() {
        Map<String, Long> byModule = aggregator.aggregateByModule(metrics);

        assertEquals(2, byModule.size());
        assertEquals(2L, byModule.get("moduleA"));
        assertEquals(1L, byModule.get("moduleB"));
    }

    @Test
    void testComputeAverage() {
        double avg = aggregator.computeAverage(metrics);
        assertEquals(20.0, avg, 0.001);
    }

    @Test
    void testComputeSum() {
        double sum = aggregator.computeSum(metrics);
        assertEquals(60.0, sum, 0.001);
    }

    @Test
    void testAggregateByTimeRange() {
        var now = Instant.now();
        var from = now.minusSeconds(3600);
        var to = now.plusSeconds(3600);

        List<GovernanceMetric> filtered = aggregator.aggregateByTimeRange(metrics, from, to);

        assertEquals(3, filtered.size());
    }

    @Test
    void testComputeRate() {
        double rate = aggregator.computeRate(metrics);
        assertTrue(rate > 0);
    }

    @Test
    void testComputeRateEmpty() {
        assertEquals(0.0, aggregator.computeRate(List.of()));
    }
}
