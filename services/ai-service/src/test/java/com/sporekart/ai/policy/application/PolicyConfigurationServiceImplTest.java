package com.sporekart.ai.policy.application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PolicyConfigurationServiceImplTest {
    private PolicyConfigurationServiceImpl configService;

    @BeforeEach void setUp() { configService = new PolicyConfigurationServiceImpl(); }

    @Test void testSetAndGet() {
        configService.setConfig("key1", "value1", "desc");
        assertTrue(configService.getConfig("key1").isPresent());
        assertEquals("value1", configService.getConfig("key1").get());
    }

    @Test void testGetConfig_NotFound() { assertTrue(configService.getConfig("nonexistent").isEmpty()); }

    @Test void testGetAllConfigs() { assertEquals(0, configService.getAllConfigs().size()); }

    @Test void testIsFeatureEnabled() { assertTrue(configService.isFeatureEnabled("test")); }
}
