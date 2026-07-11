package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiModule;

public interface RateLimitContract {
    boolean tryAcquire(AiModule module, String userId);
    int getRemainingQuota(AiModule module, String userId);
    long getResetTimeSeconds(AiModule module, String userId);
}
