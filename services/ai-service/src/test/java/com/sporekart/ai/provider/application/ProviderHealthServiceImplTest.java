package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.ProviderHealth;
import com.sporekart.ai.core.api.ProviderHealthService;
import com.sporekart.ai.provider.infrastructure.GeminiAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderHealthServiceImplTest {

    private ProviderRegistryImpl registry;
    private ProviderHealthService healthService;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistryImpl();
        registry.register("GEMINI", new GeminiAdapter());
        healthService = new ProviderHealthServiceImpl(registry);
    }

    @Test
    void shouldReturnHealthyForUnknownProvider() {
        ProviderHealth health = healthService.checkHealth("GEMINI");
        assertThat(health.healthy()).isTrue();
    }

    @Test
    void shouldMarkHealthChanged() {
        healthService.markHealthChanged("GEMINI", false, "API error");
        ProviderHealth health = healthService.checkHealth("GEMINI");
        assertThat(health.healthy()).isFalse();
        assertThat(health.details()).contains("API error");
    }

    @Test
    void shouldCheckProviderHealthy() {
        healthService.markHealthChanged("GEMINI", true, "Operational");
        assertThat(healthService.isProviderHealthy("GEMINI")).isTrue();
    }

    @Test
    void shouldDetectUnhealthyProvider() {
        healthService.markHealthChanged("GEMINI", false, "Down");
        assertThat(healthService.isProviderHealthy("GEMINI")).isFalse();
    }

    @Test
    void shouldReturnHealthyForUncheckedProvider() {
        assertThat(healthService.isProviderHealthy("OPENAI")).isTrue();
    }

    @Test
    void shouldCheckAllProviders() {
        registry.register("OPENAI", new com.sporekart.ai.provider.infrastructure.OpenAIAdapter());
        var healthList = healthService.checkAllProviders();
        assertThat(healthList).isNotEmpty();
    }
}
