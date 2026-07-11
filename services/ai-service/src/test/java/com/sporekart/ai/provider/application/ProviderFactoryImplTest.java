package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.domain.AiProviderType;
import com.sporekart.ai.provider.infrastructure.GeminiAdapter;
import com.sporekart.ai.provider.infrastructure.OpenAIAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderFactoryImplTest {

    private ProviderRegistryImpl registry;
    private ProviderFactoryImpl factory;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistryImpl();
        registry.register("GEMINI", new GeminiAdapter());
        registry.register("OPENAI", new OpenAIAdapter());
        factory = new ProviderFactoryImpl(registry);
    }

    @Test
    void shouldCreateProviderByType() {
        var provider = factory.createProvider(AiProviderType.GEMINI);
        assertThat(provider).isPresent();
        assertThat(provider.get().getProviderName()).isEqualTo("GEMINI");
    }

    @Test
    void shouldCreateProviderByString() {
        var provider = factory.createProvider("OPENAI");
        assertThat(provider).isPresent();
        assertThat(provider.get().getProviderName()).isEqualTo("OPENAI");
    }

    @Test
    void shouldReturnEmptyForUnknownProvider() {
        var provider = factory.createProvider(AiProviderType.LOCAL);
        assertThat(provider).isEmpty();
    }

    @Test
    void shouldGetOrCreateExistingProvider() {
        var provider = factory.getOrCreateProvider(AiProviderType.GEMINI);
        assertThat(provider.getProviderName()).isEqualTo("GEMINI");
    }
}
