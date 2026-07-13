package com.sporekart.ai.decision.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecisionConfigTest {

    private DecisionConfig config;

    @BeforeEach
    void setUp() {
        config = new DecisionConfig();
    }

    @Test
    void defaultEnabledIsTrue() {
        assertTrue(config.isEnabled());
    }

    @Test
    void defaultActionIsAllow() {
        assertEquals("ALLOW", config.getDefaultAction());
    }

    @Test
    void defaultConflictStrategyIsDenyOverrides() {
        assertEquals("DENY_OVERRIDES", config.getDefaultConflictStrategy());
    }

    @Test
    void cacheConfigDefaults() {
        DecisionConfig.CacheConfig cache = config.getCache();
        assertEquals(300, cache.getResultTtlSeconds());
        assertEquals(300, cache.getMetadataTtlSeconds());
        assertEquals(300, cache.getRegistryTtlSeconds());
        assertEquals(120, cache.getStatisticsTtlSeconds());
        assertEquals(300, cache.getExplanationTtlSeconds());
    }

    @Test
    void kafkaConfigDefaults() {
        DecisionConfig.KafkaConfig kafka = config.getKafka();
        assertEquals("decision-events", kafka.getTopic());
        assertEquals(3, kafka.getPartitions());
        assertEquals((short) 1, kafka.getReplicationFactor());
    }
}
