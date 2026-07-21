package com.sporekart.ai.providers.selector.strategies;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.selector.SelectionStrategy;

import java.util.List;

public class RoundRobinSelectionStrategy implements SelectionStrategy {
    private int counter = 0;

    @Override
    public List<AIProvider> apply(List<AIProvider> providers, ProviderRequest request) {
        if (providers.isEmpty()) return providers;
        int index = counter++ % providers.size();
        return List.of(providers.get(index));
    }

    @Override
    public String name() { return "round-robin"; }

    @Override
    public int order() { return 7; }

    @Override
    public boolean supports(ProviderRequest request) { return true; }
}
