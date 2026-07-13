package com.sporekart.ai.analytics.config;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AnalyticsConfigTest {

    @Test
    void testDefaultValues() {
        AnalyticsConfig config = new AnalyticsConfig();

        assertTrue(config.isEnabled());
        assertEquals(List.of("JSON", "CSV"), config.getExport().getSupportedFormats());
        assertEquals(10485760, config.getExport().getMaxFileSize());
    }

    @Test
    void testCacheConfig() {
        AnalyticsConfig config = new AnalyticsConfig();

        assertEquals(300, config.getCache().getDashboardTtl());
        assertEquals(120, config.getCache().getMetricsTtl());
        assertEquals(300, config.getCache().getKpisTtl());
        assertEquals(300, config.getCache().getReportsTtl());
        assertEquals(120, config.getCache().getStatsTtl());
    }

    @Test
    void testKafkaConfig() {
        AnalyticsConfig config = new AnalyticsConfig();

        assertEquals("analytics-events", config.getKafka().getTopic());
        assertEquals(3, config.getKafka().getPartitions());
        assertEquals(1, config.getKafka().getReplicationFactor());
    }

    @Test
    void testSetters() {
        AnalyticsConfig config = new AnalyticsConfig();

        config.setEnabled(false);
        assertFalse(config.isEnabled());

        config.getExport().setSupportedFormats(List.of("JSON", "CSV", "PDF"));
        assertEquals(3, config.getExport().getSupportedFormats().size());
    }
}
