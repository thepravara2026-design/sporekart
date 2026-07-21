package com.sporekart.ai.providers.model;

import java.util.Map;

public record ProviderEndpoint(
    String baseUrl,
    String inferenceUrl,
    String streamUrl,
    String embeddingUrl,
    String visionUrl,
    String audioUrl,
    String imageUrl,
    String healthUrl,
    String apiVersion,
    Map<String, String> customEndpoints
) {}
