package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiModule;
import com.sporekart.ai.core.domain.AiProviderType;

public interface AiAuthorization {
    boolean isAuthorized(String userId, AiModule module);
    boolean isAuthorizedForProvider(String userId, AiProviderType provider);
    boolean isRateLimitAllowed(String userId, AiModule module);
}
