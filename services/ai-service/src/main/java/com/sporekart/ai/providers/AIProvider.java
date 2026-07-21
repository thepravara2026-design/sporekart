package com.sporekart.ai.providers;

import com.sporekart.ai.providers.metadata.ProviderMetadata;
import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.model.ProviderResponse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface AIProvider {
    String providerId();
    String providerName();
    ProviderMetadata metadata();
    List<ProviderCapability> capabilities();
    boolean supports(String capabilityName);
    ProviderResponse execute(ProviderRequest request);
    CompletableFuture<ProviderResponse> executeAsync(ProviderRequest request);
    boolean isAvailable();
    int priority();
    Optional<String> version();
}
