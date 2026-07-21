package com.sporekart.ai.provider.models;

public record ProviderCostInfo(
        String providerName,
        double costPerInputToken,
        double costPerOutputToken,
        String currency,
        PricingTier pricingTier) {

    public enum PricingTier {
        FREE,
        LOW,
        MEDIUM,
        HIGH,
        CUSTOM
    }

    public double estimateCost(int inputTokens, int outputTokens) {
        return (inputTokens * costPerInputToken) + (outputTokens * costPerOutputToken);
    }
}
