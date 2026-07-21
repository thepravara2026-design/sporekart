package com.sporekart.ai.providers.selector.strategies;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.selector.SelectionStrategy;

import java.util.List;

public class WeightedSelectionStrategy implements SelectionStrategy {
    private final java.util.Map<String, Integer> weights;

    public WeightedSelectionStrategy(java.util.Map<String, Integer> weights) {
        this.weights = weights;
    }

    @Override
    public List<AIProvider> apply(List<AIProvider> providers, ProviderRequest request) {
        return providers.stream()
            .sorted((a, b) -> {
                int wa = weights.getOrDefault(a.providerId(), 1);
                int wb = weights.getOrDefault(b.providerId(), 1);
                return Integer.compare(wb, wa);
            })
            .toList();
    }

    @Override
    public String name() { return "weighted"; }

    @Override
    public int order() { return 6; }

    @Override
    public boolean supports(ProviderRequest request) { return !weights.isEmpty(); }
}
