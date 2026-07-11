package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.domain.AiProviderType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProviderFailoverImplTest {

    private final ProviderFailoverImpl failover = new ProviderFailoverImpl();

    @Test
    void shouldDetermineFailover() {
        var fallback = failover.determineFailover(AiProviderType.GEMINI, "chat");
        assertThat(fallback).isPresent();
        assertThat(fallback.get()).isNotEqualTo(AiProviderType.GEMINI);
    }

    @Test
    void shouldRespectMaxFailoverAttempts() {
        assertThat(failover.getMaxFailoverAttempts()).isEqualTo(2);
    }

    @Test
    void shouldRespectFailoverDelay() {
        assertThat(failover.getFailoverDelayMs()).isEqualTo(1000);
    }

    @Test
    void shouldExcludeFailedProvider() {
        var fallback = failover.determineFailover(AiProviderType.MOCK, "chat",
                java.util.List.of(AiProviderType.GEMINI, AiProviderType.OPENAI));
        assertThat(fallback).isPresent();
        assertThat(fallback.get()).isEqualTo(AiProviderType.CLAUDE);
    }
}
