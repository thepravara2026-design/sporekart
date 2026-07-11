package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.ProviderType;
import com.sporekart.ai.domain.model.ai.AiProvider;
import com.sporekart.ai.domain.port.AiProviderPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiGatewayService {
    private final List<AiProviderPort> providers;

    public AiGatewayService(List<AiProviderPort> providers) {
        this.providers = providers;
    }

    public String route(String prompt, ProviderType providerType) {
        AiProvider provider = new AiProvider("default", providerType, providerType.name(), true, "{}");
        return providers.stream()
                .filter(candidate -> candidate.supports(providerType))
                .findFirst()
                .map(candidate -> candidate.generateResponse(prompt, provider))
                .orElseGet(() -> fallback(prompt, providerType));
    }

    private String fallback(String prompt, ProviderType providerType) {
        return "fallback-response:" + providerType + ":" + prompt;
    }
}

