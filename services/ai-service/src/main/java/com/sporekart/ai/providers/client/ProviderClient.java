package com.sporekart.ai.providers.client;

import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.model.ProviderResponse;

import java.util.concurrent.CompletableFuture;

public interface ProviderClient {
    String clientId();
    String providerId();
    ProviderResponse execute(ProviderRequest request);
    CompletableFuture<ProviderResponse> executeAsync(ProviderRequest request);
    ProviderResponse executeStream(ProviderRequest request);
    boolean isConnected();
    void connect();
    void disconnect();
}
