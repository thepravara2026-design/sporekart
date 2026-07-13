package com.sporekart.ai.approval.config;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApprovalConfigTest {

    private ApprovalConfig config;

    @BeforeEach
    void setUp() {
        config = new ApprovalConfig();
    }

    @Test
    void defaultEnabledIsTrue() {
        assertTrue(config.isEnabled());
    }

    @Test
    void defaultSlaMinutesIs60() {
        assertEquals(60, config.getDefaultSlaMinutes());
    }

    @Test
    void defaultMaxLevelsIs3() {
        assertEquals(3, config.getMaxLevels());
    }

    @Test
    void shouldAllowSettingEnabled() {
        config.setEnabled(false);
        assertFalse(config.isEnabled());
    }

    @Test
    void shouldAllowSettingSlaMinutes() {
        config.setDefaultSlaMinutes(120);
        assertEquals(120, config.getDefaultSlaMinutes());
    }

    @Test
    void shouldAllowSettingMaxLevels() {
        config.setMaxLevels(5);
        assertEquals(5, config.getMaxLevels());
    }

    @Test
    void cacheConfigHasDefaults() {
        var cache = config.getCache();
        assertEquals(60, cache.getPendingTtl());
        assertEquals(120, cache.getAssignmentTtl());
        assertEquals(300, cache.getConfigTtl());
        assertEquals(300, cache.getWorkflowTtl());
        assertEquals(120, cache.getStatsTtl());
    }

    @Test
    void kafkaConfigHasDefaults() {
        var kafka = config.getKafka();
        assertEquals("approval-events", kafka.getTopic());
        assertEquals(3, kafka.getPartitions());
        assertEquals(1, kafka.getReplicationFactor());
    }
}
