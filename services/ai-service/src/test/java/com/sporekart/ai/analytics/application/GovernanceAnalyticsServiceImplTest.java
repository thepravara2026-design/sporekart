package com.sporekart.ai.analytics.application;

import com.sporekart.ai.analytics.api.KPIService;
import com.sporekart.ai.analytics.api.MetricsAggregationService;
import com.sporekart.ai.analytics.api.SnapshotService;
import com.sporekart.ai.analytics.api.TrendAnalysisService;
import com.sporekart.ai.analytics.domain.GovernanceSnapshot;
import com.sporekart.ai.analytics.domain.GovernanceSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernanceAnalyticsServiceImplTest {

    @Mock private MetricsAggregationService metricsAggregationService;
    @Mock private KPIService kpiService;
    @Mock private TrendAnalysisService trendAnalysisService;
    @Mock private SnapshotService snapshotService;

    private GovernanceAnalyticsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new GovernanceAnalyticsServiceImpl(metricsAggregationService, kpiService, trendAnalysisService, snapshotService);
    }

    @Test
    void testGetSummary() {
        when(metricsAggregationService.getMetricsSummary()).thenReturn(Map.of("module1", 5L));
        when(kpiService.getKPISummary()).thenReturn(Map.of("totals", Map.of("onTrack", 3L)));
        when(trendAnalysisService.getAllTrends()).thenReturn(Map.of("module1", List.of()));

        GovernanceSummary summary = service.getSummary();

        assertNotNull(summary);
        assertNotNull(summary.id());
        assertEquals("Governance Summary", summary.title());
        assertNotNull(summary.metrics());
        assertNotNull(summary.kpis());
        assertNotNull(summary.trends());
        assertNotNull(summary.generatedAt());
    }

    @Test
    void testCaptureSnapshot() {
        when(metricsAggregationService.getMetricsSummary()).thenReturn(Map.of());
        when(kpiService.getKPISummary()).thenReturn(Map.of());
        when(trendAnalysisService.getAllTrends()).thenReturn(Map.of());
        when(snapshotService.createSnapshot(anyString(), any())).thenReturn(
                new GovernanceSnapshot(UUID.randomUUID(), "test-snapshot", Map.of(), Instant.now()));

        GovernanceSnapshot snapshot = service.captureSnapshot("test-snapshot");

        assertNotNull(snapshot);
        assertEquals("test-snapshot", snapshot.name());
        verify(snapshotService).createSnapshot(eq("test-snapshot"), any());
    }
}
