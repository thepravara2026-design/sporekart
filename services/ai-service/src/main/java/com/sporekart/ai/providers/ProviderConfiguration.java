package com.sporekart.ai.providers;

import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.model.ProviderResponse;

public interface ProviderConfiguration {
    String providerId();
    String model();
    String endpoint();
    int timeoutMs();
    int maxRetries();
    java.util.Map<String, String> options();
    <T> T get(String key, Class<T> type);
}
