package com.sporekart.ai.compliance.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ComplianceConfigurationServiceImplTest {

    private ComplianceConfigurationServiceImpl configService;

    @BeforeEach
    void setUp() {
        configService = new ComplianceConfigurationServiceImpl();
    }

    @Test
    void testGetConfigReturnsEmptyWhenNotSet() {
        Optional<String> result = configService.getConfig("nonexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSetAndGetConfig() {
        configService.setConfig("key1", "value1");
        Optional<String> result = configService.getConfig("key1");
        assertTrue(result.isPresent());
        assertEquals("value1", result.get());
    }

    @Test
    void testSetConfigOverwritesExisting() {
        configService.setConfig("key1", "old");
        configService.setConfig("key1", "new");
        assertEquals("new", configService.getConfig("key1").get());
    }

    @Test
    void testGetAllConfigs() {
        configService.setConfig("a", "1");
        configService.setConfig("b", "2");
        Map<String, String> all = configService.getAllConfigs();

        assertEquals(2, all.size());
        assertEquals("1", all.get("a"));
        assertEquals("2", all.get("b"));
    }

    @Test
    void testGetAllConfigsReturnsCopy() {
        configService.setConfig("key", "value");
        Map<String, String> all = configService.getAllConfigs();
        assertThrows(UnsupportedOperationException.class, () -> all.put("new", "val"));
    }

    @Test
    void testIsFeatureEnabledReturnsTrueByDefault() {
        assertTrue(configService.isFeatureEnabled("someFeature"));
    }

    @Test
    void testIsFeatureEnabledReturnsTrueWhenSetToTrue() {
        configService.setConfig("featureX", "true");
        assertTrue(configService.isFeatureEnabled("featureX"));
    }

    @Test
    void testIsFeatureEnabledReturnsFalseWhenSetToFalse() {
        configService.setConfig("featureX", "false");
        assertFalse(configService.isFeatureEnabled("featureX"));
    }

    @Test
    void testReloadConfigDoesNotThrow() {
        assertDoesNotThrow(() -> configService.reloadConfig());
    }
}
