package com.sporekart.ai.analytics.application;

import com.sporekart.ai.analytics.domain.GovernanceTrend;
import com.sporekart.ai.analytics.domain.TrendDirection;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceMetricRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrendAnalysisServiceImplTest {

    @Mock private GovernanceMetricRepository metricRepository;
    private TrendAnalysisServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TrendAnalysisServiceImpl(metricRepository);
    }

    @Test
    void testCalculateTrend_Up() {
        GovernanceTrend trend = service.calculateTrend("test-trend", "module1",
                List.of(10.0, 11.0, 13.0, 15.0),
                List.of("2026-01-01T00:00:00Z", "2026-01-02T00:00:00Z", "2026-01-03T00:00:00Z", "2026-01-04T00:00:00Z"));

        assertNotNull(trend);
        assertEquals("test-trend", trend.name());
        assertEquals("module1", trend.module());
        assertEquals(TrendDirection.UP, trend.direction());
        assertTrue(trend.changePercentage() > 5.0);
    }

    @Test
    void testCalculateTrend_Down() {
        GovernanceTrend trend = service.calculateTrend("test-trend", "module1",
                List.of(100.0, 90.0, 85.0, 70.0),
                List.of("2026-01-01T00:00:00Z", "2026-01-02T00:00:00Z", "2026-01-03T00:00:00Z", "2026-01-04T00:00:00Z"));

        assertEquals(TrendDirection.DOWN, trend.direction());
        assertTrue(trend.changePercentage() < -5.0);
    }

    @Test
    void testCalculateTrend_Stable() {
        GovernanceTrend trend = service.calculateTrend("test-trend", "module1",
                List.of(50.0, 51.0, 50.5, 51.5),
                List.of("2026-01-01T00:00:00Z", "2026-01-02T00:00:00Z", "2026-01-03T00:00:00Z", "2026-01-04T00:00:00Z"));

        assertEquals(TrendDirection.STABLE, trend.direction());
        assertTrue(trend.changePercentage() > -5.0 && trend.changePercentage() < 5.0);
    }

    @Test
    void testGetTrend() {
        service.calculateTrend("stored", "mod1", List.of(1.0, 2.0), List.of("2026-01-01T00:00:00Z", "2026-01-02T00:00:00Z"));

        GovernanceTrend trend = service.getTrend("stored", "mod1");

        assertNotNull(trend);
        assertEquals("stored", trend.name());
    }

    @Test
    void testGetTrendsByModule() {
        service.calculateTrend("t1", "mod1", List.of(1.0, 2.0), List.of("2026-01-01T00:00:00Z", "2026-01-02T00:00:00Z"));
        service.calculateTrend("t2", "mod1", List.of(3.0, 4.0), List.of("2026-01-01T00:00:00Z", "2026-01-02T00:00:00Z"));

        List<GovernanceTrend> trends = service.getTrendsByModule("mod1");

        assertEquals(2, trends.size());
    }
}
