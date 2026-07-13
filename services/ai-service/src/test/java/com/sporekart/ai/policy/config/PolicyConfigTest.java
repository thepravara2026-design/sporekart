package com.sporekart.ai.policy.config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PolicyConfigTest {
    @Test void testDefaultValues() {
        PolicyConfig config = new PolicyConfig();
        assertTrue(config.isEnabled());
        assertEquals("ALLOW", config.getDefaultDecision());
        assertEquals("DENY_OVERRIDES", config.getDefaultConflictStrategy());
    }
    @Test void testCacheConfig() {
        PolicyConfig config = new PolicyConfig();
        assertEquals(300, config.getCache().getRegistryTtlSeconds());
        assertEquals(600, config.getCache().getCompiledTtlSeconds());
        assertEquals(180, config.getCache().getEvaluationTtlSeconds());
    }
    @Test void testKafkaConfig() {
        PolicyConfig config = new PolicyConfig();
        assertEquals("policy-events", config.getKafka().getTopic());
    }
    @Test void testSetters() {
        PolicyConfig config = new PolicyConfig();
        config.setEnabled(false);
        assertFalse(config.isEnabled());
    }
}
