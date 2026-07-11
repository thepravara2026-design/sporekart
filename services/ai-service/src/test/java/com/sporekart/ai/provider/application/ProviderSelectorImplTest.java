package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.domain.AiProviderType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderSelectorImplTest {

    private final ProviderSelectorImpl selector = new ProviderSelectorImpl(null);

    @Test
    void shouldReturnMockWhenNoFlags() {
        var selected = selector.select("chat", null);
        assertThat(selected).isEqualTo(AiProviderType.MOCK);
    }

    @Test
    void shouldReturnPreferredProviderWhenAvailable() {
        var selected = selector.select("chat", "MOCK");
        assertThat(selected).isEqualTo(AiProviderType.MOCK);
    }

    @Test
    void shouldReturnFallback() {
        var fallback = selector.selectFallback("chat", AiProviderType.MOCK);
        assertThat(fallback).isPresent();
    }
}
