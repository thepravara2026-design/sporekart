package com.sporekart.ai.core.api;

import java.util.List;

public interface ProviderHealthService {
    ProviderHealth checkHealth(String providerName);
    List<ProviderHealth> checkAllProviders();
    boolean isProviderHealthy(String providerName);
    void markHealthChanged(String providerName, boolean healthy, String details);
}
