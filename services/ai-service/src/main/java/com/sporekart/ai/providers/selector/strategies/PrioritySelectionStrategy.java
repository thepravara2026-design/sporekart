package com.sporekart.ai.providers.selector.strategies;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.selector.SelectionStrategy;

import java.util.Comparator;
import java.util.List;

public class PrioritySelectionStrategy implements SelectionStrategy {
    @Override
    public List<AIProvider> apply(List<AIProvider> providers, ProviderRequest request) {
        return providers.stream()
            .sorted(Comparator.comparingInt(AIProvider::priority))
            .toList();
    }

    @Override
    public String name() { return "priority"; }

    @Override
    public int order() { return 1; }

    @Override
    public boolean supports(ProviderRequest request) { return true; }
}
