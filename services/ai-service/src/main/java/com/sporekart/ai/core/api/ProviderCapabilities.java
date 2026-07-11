package com.sporekart.ai.core.api;

import java.util.List;
import java.util.Set;

public interface ProviderCapabilities {
    String getProviderName();
    Set<String> supportedModels();
    int getMaxTokens(String model);
    boolean supportsStreaming(String model);
    boolean supportsEmbedding(String model);
    boolean supportsVision(String model);
    boolean supportsModeration(String model);
    boolean supportsFunctionCalling(String model);
    List<String> getCapabilities();
}
