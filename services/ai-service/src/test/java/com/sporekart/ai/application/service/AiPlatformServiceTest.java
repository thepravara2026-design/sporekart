package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.ProviderType;
import com.sporekart.ai.domain.model.ai.AiProvider;
import com.sporekart.ai.infrastructure.provider.MockAiProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AiPlatformServiceTest {

    @Test
    void shouldInstantiateMockProviderAndResolveProviderType() {
        MockAiProvider provider = new MockAiProvider();
        AiProvider aiProvider = new AiProvider(
                "provider-1",
                ProviderType.MOCK,
                "mock-provider",
                true,
                "{}");

        assertThat(provider.supports(aiProvider.getProviderType())).isTrue();
        assertThat(aiProvider.getProviderType()).isEqualTo(ProviderType.MOCK);
    }
}
