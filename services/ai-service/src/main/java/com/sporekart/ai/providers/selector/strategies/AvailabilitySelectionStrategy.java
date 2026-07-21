package com.sporekart.ai.providers.selector.strategies;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.selector.SelectionStrategy;

import java.util.List;

public class AvailabilitySelectionStrategy implements SelectionStrategy {
    @Override
    public List<AIProvider> apply(List<AIProvider> providers, ProviderRequest request) {
        return providers.stream()
            .filter(AIProvider::isAvailable)
            .toList();
    }

    @Override
    public String name() { return "availability"; }

    @Override
    public int order() { return 3; }

    @Override
    public boolean supports(ProviderRequest request) { return true; }
}
