package com.sporekart.ai.provider.models;

import java.util.List;
import java.util.Set;

public record ProviderCapabilities(
        String providerName,
        Set<String> supportedModels,
        Set<String> supportedModalities,
        int maxTokens,
        boolean streamingSupported,
        boolean visionSupported,
        boolean functionCallingSupported,
        boolean jsonModeSupported,
        boolean embeddingsSupported,
        boolean imageGenerationSupported,
        boolean audioSupported,
        boolean videoSupported,
        boolean reasoningSupported,
        List<String> capabilities) {

    public boolean supportsStreaming() { return streamingSupported; }
    public boolean supportsVision() { return visionSupported; }
    public boolean supportsFunctionCalling() { return functionCallingSupported; }
    public boolean supportsJsonMode() { return jsonModeSupported; }
    public boolean supportsEmbeddings() { return embeddingsSupported; }
    public boolean supportsImageGeneration() { return imageGenerationSupported; }
    public boolean supportsAudio() { return audioSupported; }
    public boolean supportsVideo() { return videoSupported; }
    public boolean supportsReasoning() { return reasoningSupported; }

    public boolean supportsCapability(String capability) {
        return capabilities != null && capabilities.stream().anyMatch(c -> c.equalsIgnoreCase(capability));
    }

    public boolean supportsModel(String model) {
        return supportedModels != null && supportedModels.stream().anyMatch(m -> m.equalsIgnoreCase(model));
    }
}
