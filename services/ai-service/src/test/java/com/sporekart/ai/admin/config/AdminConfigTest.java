package com.sporekart.ai.admin.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminConfigTest {

    @Test
    void testDefaultEnabled() {
        var config = new AdminConfig();
        assertTrue(config.isEnabled());
    }

    @Test
    void testCacheConfigDefaults() {
        var config = new AdminConfig();
        assertEquals(300, config.getCache().getConfigTtl());
        assertEquals(300, config.getCache().getFlagsTtl());
        assertEquals(600, config.getCache().getEnvTtl());
        assertEquals(600, config.getCache().getSnapshotTtl());
        assertEquals(300, config.getCache().getMetadataTtl());
    }

    @Test
    void testKafkaConfigDefaults() {
        var config = new AdminConfig();
        assertEquals("admin-events", config.getKafka().getTopic());
        assertEquals(3, config.getKafka().getPartitions());
        assertEquals(1, config.getKafka().getReplicationFactor());
    }

    @Test
    void testSetEnabled() {
        var config = new AdminConfig();
        config.setEnabled(false);
        assertFalse(config.isEnabled());
    }

    @Test
    void testSetCacheConfig() {
        var config = new AdminConfig();
        config.getCache().setConfigTtl(600);
        assertEquals(600, config.getCache().getConfigTtl());
    }

    @Test
    void testSetKafkaTopic() {
        var config = new AdminConfig();
        config.getKafka().setTopic("custom-events");
        assertEquals("custom-events", config.getKafka().getTopic());
    }
}
