package com.sporekart.ai.gateway.domain;

import java.net.URI;

public record ProviderEndpoint(
    String providerId,
    String model,
    URI baseUrl,
    URI inferenceUrl,
    URI streamUrl,
    String apiVersion,
    String region
) {}
