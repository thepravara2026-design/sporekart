package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiRequest;

public interface ProviderValidator {
    boolean validateProvider(String providerName);
    boolean validateModel(String providerName, String model);
    boolean validateRequest(AiRequest request, String providerName);
    String getValidationError();
}
