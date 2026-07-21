package com.sporekart.ai.provider.selectors;

import com.sporekart.ai.provider.interfaces.AIProvider;

import java.util.List;
import java.util.Optional;

public interface SelectionStrategy {
    String name();
    Optional<AIProvider> select(List<AIProvider> providers, SelectionContext context);
}
