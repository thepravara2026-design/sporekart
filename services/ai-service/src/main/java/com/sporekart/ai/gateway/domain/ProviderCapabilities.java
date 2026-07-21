package com.sporekart.ai.gateway.domain;

import java.util.List;

public record ProviderCapabilities(
    boolean streaming,
    boolean functionCalling,
    boolean embeddings,
    boolean vision,
    boolean audio,
    boolean fineTuning,
    List<String> supportedModels,
    int maxTokens,
    List<String> regions
) {}
