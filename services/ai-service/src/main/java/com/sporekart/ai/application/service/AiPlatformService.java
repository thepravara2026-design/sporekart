package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.ProviderType;
import com.sporekart.ai.domain.model.ai.AiProvider;
import com.sporekart.ai.domain.port.AiProviderPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AiPlatformService {
    private final List<AiProviderPort> providers;
    private final AiEventPublisher aiEventPublisher;
    private final AiCacheService aiCacheService;

    public AiPlatformService(List<AiProviderPort> providers, AiEventPublisher aiEventPublisher,
            AiCacheService aiCacheService) {
        this.providers = providers;
        this.aiEventPublisher = aiEventPublisher;
        this.aiCacheService = aiCacheService;
    }

    public String handlePrompt(String prompt, ProviderType providerType) {
        String cached = aiCacheService.get(providerType.name() + ":" + prompt);
        if (cached != null) {
            return cached;
        }

        AiProvider provider = new AiProvider("default", providerType, providerType.name(), true, "{}");
        String response = providers.stream()
                .filter(candidate -> candidate.supports(providerType))
                .findFirst()
                .map(candidate -> candidate.generateResponse(prompt, provider))
                .orElseThrow(() -> new IllegalArgumentException("No provider registered for " + providerType));

        aiCacheService.put(providerType.name() + ":" + prompt, response);
        aiEventPublisher.publish("AIRequestProcessed", Map.of("provider", providerType.name(), "prompt", prompt));
        return response;
    }
}

