package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiProviderType;
import java.util.Optional;

public interface ProviderFailoverStrategy {
    Optional<AiProviderType> determineFailover(AiProviderType failedProvider, String module);
    int getMaxFailoverAttempts();
    long getFailoverDelayMs();
}
