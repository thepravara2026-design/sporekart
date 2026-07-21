package com.sporekart.ai.provider.interfaces;

import com.sporekart.ai.provider.models.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface AIProvider {
    String providerId();
    String providerName();
    String providerVersion();

    ProviderResponse generateCompletion(ProviderRequest request);
    ProviderResponse generateChat(ProviderRequest request);
    ProviderResponse generateEmbeddings(ProviderRequest request);
    ProviderResponse generateStreaming(ProviderRequest request);

    CompletableFuture<ProviderResponse> generateCompletionAsync(ProviderRequest request);
    CompletableFuture<ProviderResponse> generateChatAsync(ProviderRequest request);

    ProviderHealth checkHealth();
    ProviderCapabilities getCapabilities();

    List<String> supportedModels();
    List<String> supportedModalities();
    int maxTokens();
    ProviderCostInfo costInfo();
    ProviderRateLimits rateLimits();

    boolean isAvailable();
    Optional<String> version();
}
