package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;

public interface AIProvider {
    AiResponse generate(AiRequest request);
    boolean supports(String model);
    String getProviderName();
    ProviderCapabilities getCapabilities();
    boolean isAvailable();
}
