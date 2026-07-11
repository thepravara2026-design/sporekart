package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiProviderType;
import java.util.List;
import java.util.Optional;

public interface ProviderResolver {
    Optional<AiProviderType> resolve(String module, String preferredProvider);
    List<AiProviderType> getAvailableProviders();
    boolean isProviderAvailable(AiProviderType providerType);
}
