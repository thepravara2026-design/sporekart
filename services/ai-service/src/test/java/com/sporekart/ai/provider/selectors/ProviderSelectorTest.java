package com.sporekart.ai.provider.selectors;

import com.sporekart.ai.provider.implementations.MockAIProvider;
import com.sporekart.ai.provider.registry.ProviderRegistryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProviderSelectorTest {
    private ProviderSelector selector;
    private ProviderRegistryImpl registry;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistryImpl();
        registry.register(new MockAIProvider("openai-1", "OPENAI"));
        registry.register(new MockAIProvider("gemini-1", "GEMINI"));
        registry.register(new MockAIProvider("claude-1", "CLAUDE"));
        selector = new ProviderSelector(registry);
    }

    @Test
    void shouldSelectFirstAvailable() {
        var result = selector.select("first-available", SelectionContext.forModule("chat"));
        assertTrue(result.isPresent());
    }

    @Test
    void shouldSelectCheapest() {
        var result = selector.select("cheapest", SelectionContext.forModule("chat"));
        assertTrue(result.isPresent());
        assertEquals("gemini-1", result.get().providerId());
    }

    @Test
    void shouldSelectPreferredProvider() {
        var context = SelectionContext.forModule("chat").withPreferredProvider("OPENAI");
        var result = selector.select("preferred", context);
        assertTrue(result.isPresent());
        assertEquals("openai-1", result.get().providerId());
    }

    @Test
    void shouldSelectRandom() {
        var result = selector.select("random", SelectionContext.forModule("chat"));
        assertTrue(result.isPresent());
    }

    @Test
    void shouldSelectRoundRobin() {
        var first = selector.select("round-robin", SelectionContext.forModule("chat"));
        var second = selector.select("round-robin", SelectionContext.forModule("chat"));
        var third = selector.select("round-robin", SelectionContext.forModule("chat"));
        assertTrue(first.isPresent());
        assertTrue(second.isPresent());
        assertTrue(third.isPresent());
    }

    @Test
    void shouldSelectByPreferredInDefault() {
        var context = SelectionContext.forModule("chat").withPreferredProvider("OPENAI");
        var result = selector.select(context);
        assertTrue(result.isPresent());
        assertEquals("openai-1", result.get().providerId());
    }

    @Test
    void shouldSelectFallback() {
        var primary = new MockAIProvider("primary-1", "PRIMARY");
        registry.register(primary);
        var fallback = selector.selectFallback(primary, SelectionContext.forModule("chat"));
        assertTrue(fallback.isPresent());
        assertNotEquals("primary-1", fallback.get().providerId());
    }

    @Test
    void shouldSelectWeighted() {
        var result = selector.select("weighted", SelectionContext.forModule("chat"));
        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyForNoAvailableProviders() {
        selector = new ProviderSelector(new ProviderRegistryImpl());
        var result = selector.select("first-available", SelectionContext.forModule("chat"));
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRegisterCustomStrategy() {
        selector.registerStrategy(new SelectionStrategy() {
            @Override
            public String name() { return "custom"; }
            @Override
            public Optional<AIProvider> select(java.util.List<AIProvider> providers, SelectionContext context) {
                return providers.stream().findFirst();
            }
        });
        var result = selector.select("custom", SelectionContext.forModule("chat"));
        assertTrue(result.isPresent());
    }

    @Test
    void shouldFallbackToFirstAvailableForUnknownStrategy() {
        var result = selector.select("nonexistent", SelectionContext.forModule("chat"));
        assertTrue(result.isPresent());
    }

    @Test
    void shouldListRegisteredStrategies() {
        var strategies = selector.getRegisteredStrategies();
        assertFalse(strategies.isEmpty());
        assertTrue(strategies.stream().anyMatch(s -> s.name().equals("first-available")));
        assertTrue(strategies.stream().anyMatch(s -> s.name().equals("random")));
        assertTrue(strategies.stream().anyMatch(s -> s.name().equals("round-robin")));
        assertTrue(strategies.stream().anyMatch(s -> s.name().equals("cheapest")));
        assertTrue(strategies.stream().anyMatch(s -> s.name().equals("weighted")));
        assertTrue(strategies.stream().anyMatch(s -> s.name().equals("preferred")));
        assertTrue(strategies.stream().anyMatch(s -> s.name().equals("manual-override")));
    }
}
