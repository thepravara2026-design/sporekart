package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiProviderType;
import java.util.Optional;

public interface ProviderSelector {
    AiProviderType select(String module, String preferredProvider);
    Optional<AiProviderType> selectFallback(String module, AiProviderType failedProvider);
}
