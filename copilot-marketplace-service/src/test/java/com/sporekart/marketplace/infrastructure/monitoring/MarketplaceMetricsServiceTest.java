package com.sporekart.marketplace.infrastructure.monitoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MarketplaceMetricsServiceTest {

    private MarketplaceMetricsService metricsService;

    @BeforeEach
    void setUp() {
        metricsService = new MarketplaceMetricsService();
    }

    @Test
    void recordQueryLatency_shouldAccumulateTotalAndCount() {
        metricsService.recordQueryLatency(100);
        metricsService.recordQueryLatency(200);
        assertEquals(150.0, metricsService.getAverageQueryLatencyMs(), 0.001);
    }

    @Test
    void getAverageQueryLatencyMs_shouldReturnZeroWhenNoQueries() {
        assertEquals(0.0, metricsService.getAverageQueryLatencyMs());
    }

    @Test
    void recordPluginInstall_shouldIncrementCounter() {
        assertEquals(0, metricsService.getPluginInstalls());
        metricsService.recordPluginInstall();
        assertEquals(1, metricsService.getPluginInstalls());
        metricsService.recordPluginInstall();
        assertEquals(2, metricsService.getPluginInstalls());
    }

    @Test
    void recordPluginUninstall_shouldIncrementCounter() {
        assertEquals(0, metricsService.getPluginUninstalls());
        metricsService.recordPluginUninstall();
        assertEquals(1, metricsService.getPluginUninstalls());
    }

    @Test
    void recordPluginUpdate_shouldIncrementCounter() {
        assertEquals(0, metricsService.getPluginUpdates());
        metricsService.recordPluginUpdate();
        assertEquals(1, metricsService.getPluginUpdates());
    }

    @Test
    void recordPluginError_shouldIncrementCounter() {
        assertEquals(0, metricsService.getPluginErrors());
        metricsService.recordPluginError();
        assertEquals(1, metricsService.getPluginErrors());
    }

    @Test
    void recordAction_shouldIncrementPerActionType() {
        metricsService.recordAction("execute");
        metricsService.recordAction("execute");
        metricsService.recordAction("healthCheck");

        var actionCounts = metricsService.getActionCounts();
        assertEquals(2, actionCounts.get("execute"));
        assertEquals(1, actionCounts.get("healthCheck"));
    }

    @Test
    void recordAction_shouldHandleNewActionTypes() {
        metricsService.recordAction("newAction");
        assertEquals(1, metricsService.getActionCounts().get("newAction"));
    }

    @Test
    void getAllMetrics_shouldReturnAllCounters() {
        metricsService.recordQueryLatency(100);
        metricsService.recordPluginInstall();
        metricsService.recordPluginUninstall();
        metricsService.recordPluginUpdate();
        metricsService.recordPluginError();
        metricsService.recordAction("test");

        var all = metricsService.getAllMetrics();

        assertNotNull(all.get("queryLatency"));
        assertTrue(all.get("queryLatency") instanceof Map);
        assertEquals(1, ((Map<String, Object>) all.get("queryLatency")).get("total"));

        assertEquals(1, all.get("pluginInstalls"));
        assertEquals(1, all.get("pluginUninstalls"));
        assertEquals(1, all.get("pluginUpdates"));
        assertEquals(1, all.get("pluginErrors"));
        assertNotNull(all.get("actionCounts"));
    }

    @Test
    void reset_shouldClearAllCounters() {
        metricsService.recordQueryLatency(50);
        metricsService.recordPluginInstall();
        metricsService.recordPluginUninstall();
        metricsService.recordPluginUpdate();
        metricsService.recordPluginError();
        metricsService.recordAction("test");

        metricsService.reset();

        assertEquals(0.0, metricsService.getAverageQueryLatencyMs());
        assertEquals(0, metricsService.getPluginInstalls());
        assertEquals(0, metricsService.getPluginUninstalls());
        assertEquals(0, metricsService.getPluginUpdates());
        assertEquals(0, metricsService.getPluginErrors());
        assertTrue(metricsService.getActionCounts().isEmpty());
    }

    @Test
    void allCounters_shouldStartAtZero() {
        assertEquals(0, metricsService.getPluginInstalls());
        assertEquals(0, metricsService.getPluginUninstalls());
        assertEquals(0, metricsService.getPluginUpdates());
        assertEquals(0, metricsService.getPluginErrors());
        assertTrue(metricsService.getActionCounts().isEmpty());
    }
}
