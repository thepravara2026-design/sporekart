package com.sporekart.ai.providerregistry.interfaces.rest.dto;

import java.util.List;

public class ProviderDiscoveryResponseDto {

    private ProviderRegistryResponseDto recommendedProvider;
    private List<ProviderRegistryResponseDto> fallbackChain;

    public ProviderDiscoveryResponseDto() {}

    public ProviderDiscoveryResponseDto(ProviderRegistryResponseDto recommendedProvider,
                                        List<ProviderRegistryResponseDto> fallbackChain) {
        this.recommendedProvider = recommendedProvider;
        this.fallbackChain = fallbackChain;
    }

    public ProviderRegistryResponseDto getRecommendedProvider() {
        return recommendedProvider;
    }

    public void setRecommendedProvider(ProviderRegistryResponseDto recommendedProvider) {
        this.recommendedProvider = recommendedProvider;
    }

    public List<ProviderRegistryResponseDto> getFallbackChain() {
        return fallbackChain;
    }

    public void setFallbackChain(List<ProviderRegistryResponseDto> fallbackChain) {
        this.fallbackChain = fallbackChain;
    }
}
