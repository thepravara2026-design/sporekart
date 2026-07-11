package com.sporekart.ai.provider.api;

import com.sporekart.ai.core.domain.AiModuleStatus;

public interface ProviderHealthIndicator {
    AiModuleStatus checkHealth(String providerType);
}
