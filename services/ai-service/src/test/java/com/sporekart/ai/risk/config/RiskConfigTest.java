package com.sporekart.ai.risk.config;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RiskConfigTest {

    private RiskConfig config;

    @BeforeEach
    void setUp() {
        config = new RiskConfig();
    }

    @Test
    void shouldBeEnabledByDefault() {
        assertTrue(config.isEnabled());
    }

    @Test
    void shouldDefaultRiskScore50() {
        assertEquals(50.0, config.getScoring().getDefaultRiskScore());
    }

    @Test
    void shouldDefaultTrustScore75() {
        assertEquals(75.0, config.getTrust().getDefaultTrustScore());
    }

    @Test
    void shouldDefaultConfidence70() {
        assertEquals(70.0, config.getConfidence().getDefaultConfidence());
    }

    @Test
    void shouldAllowSettingEnabled() {
        config.setEnabled(false);
        assertFalse(config.isEnabled());
    }

    @Test
    void shouldAllowSettingScoringDefaults() {
        config.getScoring().setDefaultRiskScore(60.0);
        assertEquals(60.0, config.getScoring().getDefaultRiskScore());
    }

    @Test
    void shouldAllowSettingTrustDefaults() {
        config.getTrust().setDefaultTrustScore(85.0);
        assertEquals(85.0, config.getTrust().getDefaultTrustScore());
    }

    @Test
    void shouldAllowSettingConfidenceDefaults() {
        config.getConfidence().setDefaultConfidence(80.0);
        assertEquals(80.0, config.getConfidence().getDefaultConfidence());
    }

    @Test
    void shouldCacheDefaultScoresTtl300() {
        assertEquals(300, config.getCache().getScoresTtl());
    }

    @Test
    void shouldCacheDefaultTrustTtl300() {
        assertEquals(300, config.getCache().getTrustTtl());
    }

    @Test
    void shouldCacheDefaultConfidenceTtl300() {
        assertEquals(300, config.getCache().getConfidenceTtl());
    }

    @Test
    void shouldCacheDefaultThresholdsTtl600() {
        assertEquals(600, config.getCache().getThresholdsTtl());
    }

    @Test
    void shouldCacheDefaultMetadataTtl300() {
        assertEquals(300, config.getCache().getMetadataTtl());
    }

    @Test
    void shouldKafkaDefaultTopicRiskEvents() {
        assertEquals("risk-events", config.getKafka().getTopic());
    }

    @Test
    void shouldKafkaDefaultPartitions3() {
        assertEquals(3, config.getKafka().getPartitions());
    }

    @Test
    void shouldKafkaDefaultReplicationFactor1() {
        assertEquals(1, config.getKafka().getReplicationFactor());
    }

    @Test
    void shouldAllowSettingCacheTtl() {
        config.getCache().setScoresTtl(600);
        assertEquals(600, config.getCache().getScoresTtl());
    }

    @Test
    void shouldAllowSettingKafkaTopic() {
        config.getKafka().setTopic("custom-risk-events");
        assertEquals("custom-risk-events", config.getKafka().getTopic());
    }
}
