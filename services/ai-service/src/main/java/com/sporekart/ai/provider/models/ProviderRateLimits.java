package com.sporekart.ai.provider.models;

public record ProviderRateLimits(
        String providerName,
        int requestsPerMinute,
        int tokensPerMinute,
        int requestsPerDay,
        int maxConcurrentRequests,
        boolean unlimited) {

    public static ProviderRateLimits unlimited(String providerName) {
        return new ProviderRateLimits(providerName, Integer.MAX_VALUE, Integer.MAX_VALUE,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true);
    }

    public static ProviderRateLimits of(String providerName, int rpm, int tpm) {
        return new ProviderRateLimits(providerName, rpm, tpm, rpm * 1440, 100, false);
    }
}
