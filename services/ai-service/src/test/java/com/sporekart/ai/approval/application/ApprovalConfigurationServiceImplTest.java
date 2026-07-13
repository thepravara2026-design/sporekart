package com.sporekart.ai.approval.application;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApprovalConfigurationServiceImplTest {

    private ApprovalConfigurationServiceImpl configService;

    @BeforeEach
    void setUp() {
        configService = new ApprovalConfigurationServiceImpl();
    }

    @Test
    void shouldGetConfigWhenNotSet() {
        var result = configService.getConfig("nonexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSetAndGetConfig() {
        configService.setConfig("feature.x", "true", "Enable feature X");
        var result = configService.getConfig("feature.x");
        assertTrue(result.isPresent());
        assertEquals("true", result.get());
    }

    @Test
    void shouldGetAllConfigs() {
        configService.setConfig("a", "1", "desc a");
        configService.setConfig("b", "2", "desc b");
        assertEquals(2, configService.getAllConfigs().size());
    }

    @Test
    void isFeatureEnabledShouldReturnTrueWhenTrue() {
        configService.setConfig("feature.x", "true", "desc");
        assertTrue(configService.isFeatureEnabled("feature.x"));
    }

    @Test
    void isFeatureEnabledShouldReturnFalseWhenFalse() {
        configService.setConfig("feature.x", "false", "desc");
        assertFalse(configService.isFeatureEnabled("feature.x"));
    }

    @Test
    void isFeatureEnabledShouldReturnFalseWhenNotSet() {
        assertFalse(configService.isFeatureEnabled("nonexistent"));
    }
}
