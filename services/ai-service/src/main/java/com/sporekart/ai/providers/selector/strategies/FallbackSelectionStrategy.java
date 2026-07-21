package com.sporekart.ai.providers.selector.strategies;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.selector.SelectionStrategy;

import java.util.List;

public class FallbackSelectionStrategy implements SelectionStrategy {
    private final List<String> fallbackOrder;

    public FallbackSelectionStrategy(List<String> fallbackOrder) {
        this.fallbackOrder = fallbackOrder;
    }

    @Override
    public List<AIProvider> apply(List<AIProvider> providers, ProviderRequest request) {
        return fallbackOrder.stream()
            .map(id -> providers.stream().filter(p -> p.providerId().equals(id)).findFirst())
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .toList();
    }

    @Override
    public String name() { return "fallback"; }

    @Override
    public int order() { return 10; }

    @Override
    public boolean supports(ProviderRequest request) { return true; }
}
