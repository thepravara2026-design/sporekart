package com.sporekart.ai.automation.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AutomationConfigTest {

    @Test
    void shouldHaveDefaultEnabled() {
        var config = new AutomationConfig();
        assertTrue(config.isEnabled());
    }

    @Test
    void shouldHaveDefaultSchedulerConfig() {
        var scheduler = new AutomationConfig.SchedulerConfig();
        assertTrue(scheduler.isEnabled());
        assertEquals(60000, scheduler.getPollIntervalMs());
    }

    @Test
    void shouldHaveDefaultRetryConfig() {
        var retry = new AutomationConfig.RetryConfig();
        assertEquals(3, retry.getDefaultMaxRetries());
        assertEquals(1000, retry.getDefaultBackoffMs());
        assertEquals(2.0, retry.getDefaultBackoffMultiplier(), 0.001);
    }

    @Test
    void shouldHaveDefaultCacheConfig() {
        var cache = new AutomationConfig.CacheConfig();
        assertEquals(300, cache.getWorkflowTtl());
        assertEquals(300, cache.getSchedulerTtl());
        assertEquals(600, cache.getLifecycleTtl());
        assertEquals(300, cache.getConfigTtl());
        assertEquals(120, cache.getStatsTtl());
    }

    @Test
    void shouldHaveDefaultKafkaConfig() {
        var kafka = new AutomationConfig.KafkaConfig();
        assertEquals("automation-events", kafka.getTopic());
        assertEquals(3, kafka.getPartitions());
        assertEquals(1, kafka.getReplicationFactor());
    }

    @Test
    void shouldAllowCustomEnabled() {
        var config = new AutomationConfig();
        config.setEnabled(false);
        assertFalse(config.isEnabled());
    }
}
