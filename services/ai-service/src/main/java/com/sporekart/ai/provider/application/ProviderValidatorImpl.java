package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.ProviderValidator;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.provider.api.ProviderPort;
import org.springframework.stereotype.Service;

@Service
public class ProviderValidatorImpl implements ProviderValidator {
    private final ProviderRegistryImpl registry;
    private String lastValidationError;

    public ProviderValidatorImpl(ProviderRegistryImpl registry) {
        this.registry = registry;
    }

    @Override
    public boolean validateProvider(String providerName) {
        if (providerName == null || providerName.isBlank()) {
            lastValidationError = "Provider name must not be blank";
            return false;
        }
        if (!registry.isRegistered(providerName)) {
            lastValidationError = "Provider not registered: " + providerName;
            return false;
        }
        lastValidationError = null;
        return true;
    }

    @Override
    public boolean validateModel(String providerName, String model) {
        if (model == null || model.isBlank()) {
            lastValidationError = "Model must not be blank";
            return false;
        }
        lastValidationError = null;
        return true;
    }

    @Override
    public boolean validateRequest(AiRequest request, String providerName) {
        if (request == null) {
            lastValidationError = "Request must not be null";
            return false;
        }
        if (request.prompt() == null || request.prompt().isBlank()) {
            lastValidationError = "Prompt must not be blank";
            return false;
        }
        if (!validateProvider(providerName)) {
            return false;
        }
        lastValidationError = null;
        return true;
    }

    @Override
    public String getValidationError() {
        return lastValidationError;
    }
}
