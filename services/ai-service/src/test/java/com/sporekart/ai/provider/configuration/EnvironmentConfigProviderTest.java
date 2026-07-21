package com.sporekart.ai.provider.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentConfigProviderTest {
    private final EnvironmentConfigProvider configProvider = new EnvironmentConfigProvider();

    @Test
    void shouldLoadConfigWithDefaults() {
        var config = configProvider.loadConfig("OPENAI");
        assertNotNull(config);
        assertEquals("OPENAI", config.providerType());
        assertFalse(config.enabled());
        assertEquals(30000, config.timeoutMs());
        assertEquals(3, config.maxRetries());
    }

    @Test
    void shouldLoadConfigForAnyProviderType() {
        var config = configProvider.loadConfig("GEMINI");
        assertNotNull(config);
        assertEquals("GEMINI", config.providerType());
    }

    @Test
    void shouldRefreshConfig() {
        configProvider.loadConfig("OPENAI");
        configProvider.refreshConfig("OPENAI");
        var config = configProvider.loadConfig("OPENAI");
        assertNotNull(config);
    }

    @Test
    void shouldRefreshAll() {
        configProvider.loadConfig("OPENAI");
        configProvider.loadConfig("GEMINI");
        configProvider.refreshAll();
        var config = configProvider.loadConfig("OPENAI");
        assertNotNull(config);
    }

    @Test
    void shouldReturnConfigWithProviderId() {
        var config = configProvider.loadConfig("OPENAI");
        assertEquals("openai-provider", config.providerId());
    }

    @Test
    void shouldHandleCaseInsensitivity() {
        var config = configProvider.loadConfig("openai");
        assertNotNull(config);
        assertEquals("OPENAI", config.providerType());
    }
}
