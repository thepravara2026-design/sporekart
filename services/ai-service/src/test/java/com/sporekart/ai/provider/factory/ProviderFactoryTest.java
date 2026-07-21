package com.sporekart.ai.provider.factory;

import com.sporekart.ai.provider.configuration.EnvironmentConfigProvider;
import com.sporekart.ai.provider.implementations.MockAIProvider;
import com.sporekart.ai.provider.registry.ProviderRegistryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProviderFactoryTest {
    private ProviderFactory factory;
    private ProviderRegistryImpl registry;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistryImpl();
        var configProvider = new EnvironmentConfigProvider();
        factory = new ProviderFactory(registry, configProvider);
    }

    @Test
    void shouldCreateProviderFromRegistry() {
        var provider = new MockAIProvider("test-1", "TEST");
        registry.register(provider);
        var created = factory.createProvider("TEST");
        assertTrue(created.isPresent());
        assertEquals("test-1", created.get().providerId());
    }

    @Test
    void shouldReturnEmptyForUnknownProvider() {
        var created = factory.createProvider("UNKNOWN");
        assertTrue(created.isEmpty());
    }

    @Test
    void shouldGetConfiguration() {
        var config = factory.getConfiguration("OPENAI");
        assertNotNull(config);
        assertEquals("OPENAI", config.providerType());
    }

    @Test
    void shouldCheckIfProviderEnabled() {
        assertFalse(factory.isProviderEnabled("OPENAI"));
    }

    @Test
    void shouldRefreshConfiguration() {
        factory.refreshConfiguration("OPENAI");
        var config = factory.getConfiguration("OPENAI");
        assertNotNull(config);
    }

    @Test
    void shouldGetOrCreateExistingProvider() {
        var provider = new MockAIProvider("test-1", "TEST");
        registry.register(provider);
        var result = factory.getOrCreateProvider("TEST");
        assertTrue(result.isPresent());
        assertEquals("test-1", result.get().providerId());
    }

    @Test
    void shouldReturnEmptyForDisabledProvider() {
        var result = factory.getOrCreateProvider("NONEXISTENT");
        assertTrue(result.isEmpty());
    }
}
