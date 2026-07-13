package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import com.sporekart.ai.risk.domain.RiskLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RiskConfigurationServiceImplTest {

    private RiskConfigurationServiceImpl configService;

    @BeforeEach
    void setUp() {
        configService = new RiskConfigurationServiceImpl();
    }

    @Test
    void getConfigShouldReturnDefaultValues() {
        assertTrue((Boolean) configService.getConfig("enabled"));
        assertEquals(50.0, (Double) configService.getConfig("defaultRiskScore"));
        assertEquals(75.0, (Double) configService.getConfig("defaultTrustScore"));
        assertEquals(70.0, (Double) configService.getConfig("defaultConfidence"));
    }

    @Test
    void setConfigShouldUpdateValue() {
        configService.setConfig("enabled", false);
        assertFalse((Boolean) configService.getConfig("enabled"));
    }

    @Test
    void setConfigShouldStoreAnyValue() {
        configService.setConfig("customKey", "customValue");
        assertEquals("customValue", configService.getConfig("customKey"));
    }

    @Test
    void getThresholdShouldReturnDefaultThresholds() {
        var low = configService.getThreshold(RiskLevel.LOW);
        assertNotNull(low);
        assertEquals(0, low.minScore());
        assertEquals(20, low.maxScore());
        assertEquals("proceed", low.action());

        var critical = configService.getThreshold(RiskLevel.CRITICAL);
        assertNotNull(critical);
        assertEquals(71, critical.minScore());
        assertEquals(100, critical.maxScore());
        assertEquals("block", critical.action());
    }

    @Test
    void getThresholdShouldReturnMediumThreshold() {
        var medium = configService.getThreshold(RiskLevel.MEDIUM);
        assertNotNull(medium);
        assertEquals(21, medium.minScore());
        assertEquals(40, medium.maxScore());
        assertEquals("retry", medium.action());
    }

    @Test
    void getThresholdShouldReturnHighThreshold() {
        var high = configService.getThreshold(RiskLevel.HIGH);
        assertNotNull(high);
        assertEquals(41, high.minScore());
        assertEquals(70, high.maxScore());
        assertEquals("require_approval", high.action());
    }

    @Test
    void setThresholdShouldUpdateExisting() {
        configService.setThreshold(RiskLevel.HIGH, 50, 80, "custom_action");
        var threshold = configService.getThreshold(RiskLevel.HIGH);
        assertEquals(50, threshold.minScore());
        assertEquals(80, threshold.maxScore());
        assertEquals("custom_action", threshold.action());
    }

    @Test
    void setThresholdShouldAddNew() {
        configService.setThreshold(RiskLevel.CUSTOM, 0, 100, "custom");
        var threshold = configService.getThreshold(RiskLevel.CUSTOM);
        assertNotNull(threshold);
        assertEquals(0, threshold.minScore());
        assertEquals(100, threshold.maxScore());
        assertEquals("custom", threshold.action());
    }

    @Test
    void isFeatureEnabledShouldReturnTrueForEnabled() {
        configService.setConfig("enabled", true);
        assertTrue(configService.isFeatureEnabled("enabled"));
    }

    @Test
    void isFeatureEnabledShouldReturnFalseForDisabled() {
        configService.setConfig("enabled", false);
        assertFalse(configService.isFeatureEnabled("enabled"));
    }

    @Test
    void isFeatureEnabledShouldReturnFalseForUnknown() {
        assertFalse(configService.isFeatureEnabled("unknown_feature"));
    }

    @Test
    void getAllConfigsShouldIncludeAll() {
        var all = configService.getAllConfigs();
        assertTrue(all.containsKey("enabled"));
        assertTrue(all.containsKey("defaultRiskScore"));
        assertTrue(all.containsKey("defaultTrustScore"));
        assertTrue(all.containsKey("defaultConfidence"));
        assertTrue(all.containsKey("threshold.low"));
        assertTrue(all.containsKey("threshold.medium"));
        assertTrue(all.containsKey("threshold.high"));
        assertTrue(all.containsKey("threshold.critical"));
    }

    @Test
    void reloadConfigShouldResetToDefaults() {
        configService.setConfig("enabled", false);
        configService.setThreshold(RiskLevel.LOW, 10, 30, "modified");
        configService.reloadConfig();

        assertTrue((Boolean) configService.getConfig("enabled"));
        assertEquals(0, configService.getThreshold(RiskLevel.LOW).minScore());
    }
}
