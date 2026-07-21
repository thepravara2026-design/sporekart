package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.registry.activation.ActivationState;

import java.util.List;

public interface ProviderActivationManager {
    boolean activate(String providerId);
    boolean deactivate(String providerId);
    boolean isActivated(String providerId);
    ActivationState getActivationState(String providerId);
    List<String> getActiveProviders();
    List<String> getInactiveProviders();
    void setMaintenanceMode(String providerId, boolean maintenance);
}
