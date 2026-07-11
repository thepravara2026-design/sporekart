package com.sporekart.ai.provider.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.provider.domain.Provider;
import com.sporekart.ai.provider.domain.ProviderCapability;

import java.util.Set;

public interface ProviderPort {
    boolean supports(String providerType);
    AiResponse generate(AiRequest request, Provider provider);
    Set<ProviderCapability> capabilities();
    boolean isAvailable();
}
