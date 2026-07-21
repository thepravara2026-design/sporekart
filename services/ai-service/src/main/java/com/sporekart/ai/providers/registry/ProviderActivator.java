package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.AIProvider;

import java.util.List;
import java.util.Optional;

public interface ProviderActivator {
    boolean activate(String providerId);
    boolean deactivate(String providerId);
    boolean isActive(String providerId);
    List<AIProvider> activateAll();
    void deactivateAll();
    Optional<String> getActivationError(String providerId);
}
