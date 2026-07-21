package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.ProviderStatus;

import java.util.List;
import java.util.Map;

public interface ProviderLifecycleManager {
    void transition(String providerId, ProviderStatus from, ProviderStatus to);
    boolean canTransition(String providerId, ProviderStatus to);
    ProviderStatus getCurrentStatus(String providerId);
    List<ProviderStatus> getAllowedTransitions(String providerId);
    Map<String, ProviderStatus> getStatusMap();
}
