package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.ProviderFailoverStrategy;
import com.sporekart.ai.core.domain.AiProviderType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderFailoverImpl implements ProviderFailoverStrategy {
    private static final int MAX_FAILOVER_ATTEMPTS = 2;
    private static final long FAILOVER_DELAY_MS = 1000;
    private static final List<AiProviderType> FAILOVER_ORDER = Arrays.asList(
            AiProviderType.GEMINI, AiProviderType.OPENAI, AiProviderType.CLAUDE,
            AiProviderType.AZURE_OPENAI, AiProviderType.MOCK);

    @Override
    public Optional<AiProviderType> determineFailover(AiProviderType failedProvider, String module) {
        return FAILOVER_ORDER.stream()
                .filter(p -> p != failedProvider)
                .findFirst();
    }

    @Override
    public int getMaxFailoverAttempts() {
        return MAX_FAILOVER_ATTEMPTS;
    }

    @Override
    public long getFailoverDelayMs() {
        return FAILOVER_DELAY_MS;
    }

    public Optional<AiProviderType> determineFailover(AiProviderType failedProvider, String module,
            List<AiProviderType> excludedProviders) {
        return FAILOVER_ORDER.stream()
                .filter(p -> p != failedProvider)
                .filter(p -> !excludedProviders.contains(p))
                .findFirst();
    }
}
