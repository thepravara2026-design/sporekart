package com.sporekart.ai.compliance.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ComplianceConfigTest {

    @Test
    void testDefaultValues() {
        ComplianceConfig config = new ComplianceConfig();
        assertTrue(config.isEnabled());
        assertEquals("INTERNAL_AI_GOVERNANCE", config.getDefaultFramework());
        assertTrue(config.isDefaultEvidenceVerification());
    }

    @Test
    void testCacheConfig() {
        ComplianceConfig config = new ComplianceConfig();
        assertEquals(300, config.getCache().getRulesTtl());
        assertEquals(300, config.getCache().getFrameworksTtl());
        assertEquals(180, config.getCache().getValidationTtl());
        assertEquals(300, config.getCache().getReportsTtl());
        assertEquals(120, config.getCache().getStatsTtl());
    }

    @Test
    void testKafkaConfig() {
        ComplianceConfig config = new ComplianceConfig();
        assertEquals("compliance-events", config.getKafka().getTopic());
        assertEquals(3, config.getKafka().getPartitions());
        assertEquals(1, config.getKafka().getReplicationFactor());
    }

    @Test
    void testSetters() {
        ComplianceConfig config = new ComplianceConfig();
        config.setEnabled(false);
        config.setDefaultFramework("GDPR");
        config.setDefaultEvidenceVerification(false);

        assertFalse(config.isEnabled());
        assertEquals("GDPR", config.getDefaultFramework());
        assertFalse(config.isDefaultEvidenceVerification());
    }

    @Test
    void testCacheConfigSetters() {
        ComplianceConfig config = new ComplianceConfig();
        config.getCache().setRulesTtl(600);
        config.getCache().setFrameworksTtl(600);
        config.getCache().setValidationTtl(360);
        config.getCache().setReportsTtl(600);
        config.getCache().setStatsTtl(240);

        assertEquals(600, config.getCache().getRulesTtl());
        assertEquals(600, config.getCache().getFrameworksTtl());
        assertEquals(360, config.getCache().getValidationTtl());
        assertEquals(600, config.getCache().getReportsTtl());
        assertEquals(240, config.getCache().getStatsTtl());
    }

    @Test
    void testKafkaConfigSetters() {
        ComplianceConfig config = new ComplianceConfig();
        config.getKafka().setTopic("custom-topic");
        config.getKafka().setPartitions(5);
        config.getKafka().setReplicationFactor(3);

        assertEquals("custom-topic", config.getKafka().getTopic());
        assertEquals(5, config.getKafka().getPartitions());
        assertEquals(3, config.getKafka().getReplicationFactor());
    }
}
