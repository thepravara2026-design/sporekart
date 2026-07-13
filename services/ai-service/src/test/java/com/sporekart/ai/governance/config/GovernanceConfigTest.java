package com.sporekart.ai.governance.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GovernanceConfigTest {

    @Test
    void testDefaultValues() {
        GovernanceConfig config = new GovernanceConfig();
        assertTrue(config.isEnabled());
        assertEquals("DEVELOPMENT", config.getMode());
        assertEquals("ALLOW", config.getDefaultDecision());
    }

    @Test
    void testCacheConfig() {
        GovernanceConfig config = new GovernanceConfig();
        assertEquals(300, config.getCache().getConfigTtlSeconds());
        assertEquals(300, config.getCache().getRegistryTtlSeconds());
        assertEquals(60, config.getCache().getHealthTtlSeconds());
        assertEquals(120, config.getCache().getMetricsTtlSeconds());
        assertEquals(180, config.getCache().getValidationTtlSeconds());
    }

    @Test
    void testKafkaConfig() {
        GovernanceConfig config = new GovernanceConfig();
        assertEquals("governance-events", config.getKafka().getTopic());
        assertEquals(3, config.getKafka().getPartitions());
    }

    @Test
    void testSetters() {
        GovernanceConfig config = new GovernanceConfig();
        config.setEnabled(false);
        config.setMode("PRODUCTION");
        assertFalse(config.isEnabled());
        assertEquals("PRODUCTION", config.getMode());
    }
}
