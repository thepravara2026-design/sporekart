package com.sporekart.ai.provider.capability;

import com.sporekart.ai.provider.implementations.MockAIProvider;
import com.sporekart.ai.provider.registry.ProviderRegistryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityMatrixTest {
    private ProviderRegistryImpl registry;
    private CapabilityMatrix matrix;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistryImpl();
        registry.register(new MockAIProvider("openai-1", "OPENAI"));
        registry.register(new MockAIProvider("gemini-1", "GEMINI"));
        registry.register(new MockAIProvider("claude-1", "CLAUDE"));
        registry.register(new MockAIProvider("ollama-1", "OLLAMA"));
        registry.register(new MockAIProvider("groq-1", "GROQ"));
        matrix = new CapabilityMatrix(registry);
    }

    @Test
    void shouldFindProvidersWithCapability() {
        var providers = matrix.findProvidersWithCapability("streaming");
        assertFalse(providers.isEmpty());
        assertTrue(providers.contains("openai-1"));
    }

    @Test
    void shouldReturnEmptyForUnknownCapability() {
        var providers = matrix.findProvidersWithCapability("telepathy");
        assertTrue(providers.isEmpty());
    }

    @Test
    void shouldFindBestProviderForCapability() {
        var provider = matrix.findBestProviderFor("streaming");
        assertTrue(provider.isPresent());
    }

    @Test
    void shouldReturnEmptyForNoProviderWithCapability() {
        var provider = matrix.findBestProviderFor("telepathy");
        assertTrue(provider.isEmpty());
    }

    @Test
    void shouldReturnAllCapabilities() {
        var caps = matrix.getAllCapabilities();
        assertFalse(caps.isEmpty());
        assertTrue(caps.contains("streaming"));
        assertTrue(caps.contains("chat"));
        assertTrue(caps.contains("completion"));
    }

    @Test
    void shouldCheckProviderSupportsCapability() {
        assertTrue(matrix.providerSupports("openai-1", "streaming"));
    }

    @Test
    void shouldReturnFalseForUnsupportedCapability() {
        assertFalse(matrix.providerSupports("openai-1", "telepathy"));
    }

    @Test
    void shouldReturnProviderCapabilityMap() {
        var map = matrix.getProviderCapabilityMap();
        assertFalse(map.isEmpty());
        assertTrue(map.containsKey("openai-1"));
        assertTrue(map.containsKey("gemini-1"));
    }

    @Test
    void shouldGetProviderCapabilities() {
        var caps = matrix.getProviderCapabilities("openai-1");
        assertFalse(caps.isEmpty());
    }

    @Test
    void shouldBuildAndRebuildIndex() {
        matrix.rebuildIndex();
        var caps = matrix.getAllCapabilities();
        assertFalse(caps.isEmpty());
    }

    @Test
    void shouldRefreshSingleProvider() {
        matrix.refreshProvider("openai-1");
        assertTrue(matrix.providerSupports("openai-1", "streaming"));
    }

    @Test
    void shouldFindSupportingProviders() {
        var providers = matrix.findProvidersSupporting("streaming");
        assertFalse(providers.isEmpty());
    }

    @Test
    void shouldHaveModelIndex() {
        assertTrue(matrix.providerSupports("openai-1", "model:gpt-4"));
    }

    @Test
    void shouldHandleEmptyRegistry() {
        var emptyMatrix = new CapabilityMatrix(new ProviderRegistryImpl());
        assertTrue(emptyMatrix.getAllCapabilities().isEmpty());
    }
}
