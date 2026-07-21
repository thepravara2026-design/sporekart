package com.sporekart.ai.providers.model;

import java.util.Map;

public record ProviderCredentials(
    String apiKey,
    String apiSecret,
    String accessToken,
    String clientId,
    String clientSecret,
    String tenantId,
    Map<String, String> additionalCredentials
) {
    public boolean hasCredentials() {
        return apiKey != null || apiSecret != null || accessToken != null;
    }
}
