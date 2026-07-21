package com.sporekart.ai.providers.selector;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.model.ProviderRequest;

import java.util.List;
import java.util.Optional;

public interface ProviderSelector {
    Optional<AIProvider> select(ProviderRequest request);
    Optional<AIProvider> select(String providerId);
    Optional<AIProvider> selectByCapability(ProviderCapability capability);
    List<AIProvider> selectAll(ProviderRequest request);
    List<AIProvider> rank(List<AIProvider> providers, ProviderRequest request);
    String strategyName();
}
