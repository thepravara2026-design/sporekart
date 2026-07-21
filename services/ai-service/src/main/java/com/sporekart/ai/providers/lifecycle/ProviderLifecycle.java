package com.sporekart.ai.providers.lifecycle;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.ProviderStatus;

public interface ProviderLifecycle {
    void onRegister(AIProvider provider);
    void onValidate(AIProvider provider);
    void onInitialize(AIProvider provider);
    void onActivate(AIProvider provider);
    void onDeactivate(AIProvider provider);
    void onRemove(AIProvider provider);
    void onStatusChange(AIProvider provider, ProviderStatus oldStatus, ProviderStatus newStatus);
    ProviderLifecycleStage currentStage(String providerId);
}
