package com.sporekart.ai.providerregistry.domain;

import java.util.List;

public record FallbackChain(
        List<ProviderRegistryEntry> chain
) {}
