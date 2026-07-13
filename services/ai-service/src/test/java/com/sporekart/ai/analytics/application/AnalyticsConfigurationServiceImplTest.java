package com.sporekart.ai.analytics.application;

import com.sporekart.ai.analytics.api.AnalyticsConfigurationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnalyticsConfigurationServiceImplTest {

    private AnalyticsConfigurationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AnalyticsConfigurationServiceImpl();
    }

    @Test
    void testGetConfig() {
        assertTrue((Boolean) service.getConfig("analytics.enabled"));
        assertEquals(90, service.getConfig("analytics.retention.days"));
    }

    @Test
    void testSetConfig() {
        service.setConfig("analytics.retention.days", 180);
        assertEquals(180, service.getConfig("analytics.retention.days"));
    }

    @Test
    void testGetAllConfigs() {
        var configs = service.getAllConfigs();
        assertNotNull(configs);
        assertTrue(configs.containsKey("analytics.enabled"));
        assertTrue(configs.containsKey("analytics.retention.days"));
        assertTrue(configs.containsKey("analytics.cache.enabled"));
        assertTrue(configs.containsKey("analytics.reports.maxExportSize"));
    }

    @Test
    void testIsFeatureEnabled() {
        assertTrue(service.isFeatureEnabled("dashboard"));
        assertTrue(service.isFeatureEnabled("reporting"));
        assertTrue(service.isFeatureEnabled("trends"));
        assertTrue(service.isFeatureEnabled("kpi"));
        assertTrue(service.isFeatureEnabled("export"));
        assertFalse(service.isFeatureEnabled("nonexistent"));
    }

    @Test
    void testReloadConfig() {
        assertDoesNotThrow(() -> service.reloadConfig());
    }
}
