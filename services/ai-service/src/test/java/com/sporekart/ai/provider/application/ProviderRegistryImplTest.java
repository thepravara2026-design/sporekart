package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.ProviderHealth;
import com.sporekart.ai.provider.infrastructure.GeminiAdapter;
import com.sporekart.ai.provider.infrastructure.OpenAIAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderRegistryImplTest {

    private ProviderRegistryImpl registry;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistryImpl();
        registry.register("GEMINI", new GeminiAdapter());
        registry.register("OPENAI", new OpenAIAdapter());
    }

    @Test
    void shouldRegisterProvider() {
        assertThat(registry.isRegistered("GEMINI")).isTrue();
        assertThat(registry.isRegistered("OPENAI")).isTrue();
    }

    @Test
    void shouldFindByType() {
        var port = registry.findByType("GEMINI");
        assertThat(port).isPresent();
        assertThat(port.get().isAvailable()).isTrue();
    }

    @Test
    void shouldReturnAllProviders() {
        List<?> all = registry.all();
        assertThat(all).hasSize(2);
    }

    @Test
    void shouldUnregisterProvider() {
        registry.unregister("GEMINI");
        assertThat(registry.isRegistered("GEMINI")).isFalse();
    }

    @Test
    void shouldFindAIProvider() {
        var aiProvider = registry.findAIProvider("GEMINI");
        assertThat(aiProvider).isPresent();
        assertThat(aiProvider.get().getProviderName()).isEqualTo("GEMINI");
    }

    @Test
    void shouldReturnEmptyForUnknownProvider() {
        var port = registry.findByType("UNKNOWN");
        assertThat(port).isEmpty();
    }
}
