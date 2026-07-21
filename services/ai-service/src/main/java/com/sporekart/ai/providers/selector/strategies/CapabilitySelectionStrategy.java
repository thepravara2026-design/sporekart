package com.sporekart.ai.providers.selector.strategies;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.selector.SelectionStrategy;

import java.util.List;

public class CapabilitySelectionStrategy implements SelectionStrategy {
    @Override
    public List<AIProvider> apply(List<AIProvider> providers, ProviderRequest request) {
        return providers.stream()
            .filter(p -> p.supports(com.sporekart.ai.providers.capability.ProviderCapability.CHAT.name()))
            .toList();
    }

    @Override
    public String name() { return "capability"; }

    @Override
    public int order() { return 2; }

    @Override
    public boolean supports(ProviderRequest request) { return true; }
}
