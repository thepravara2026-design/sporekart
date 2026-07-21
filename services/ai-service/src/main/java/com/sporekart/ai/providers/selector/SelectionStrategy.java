package com.sporekart.ai.providers.selector;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.ProviderConfiguration;
import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.model.ProviderRequest;

import java.util.List;

public interface SelectionStrategy {
    List<AIProvider> apply(List<AIProvider> providers, ProviderRequest request);
    String name();
    int order();
    boolean supports(ProviderRequest request);
}
