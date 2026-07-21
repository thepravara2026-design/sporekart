package com.sporekart.ai.providers.lifecycle;

import com.sporekart.ai.providers.ProviderConfiguration;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record LifecycleEvent(
    String providerId,
    ProviderLifecycleStage fromStage,
    ProviderLifecycleStage toStage,
    String reason,
    boolean success,
    String errorMessage,
    Instant timestamp,
    Map<String, Object> details
) {
    public static LifecycleEvent transition(String providerId, ProviderLifecycleStage from,
            ProviderLifecycleStage to, String reason) {
        return new LifecycleEvent(providerId, from, to, reason, true, null, Instant.now(), Map.of());
    }

    public static LifecycleEvent failed(String providerId, ProviderLifecycleStage from,
            ProviderLifecycleStage to, String error) {
        return new LifecycleEvent(providerId, from, to, null, false, error, Instant.now(), Map.of());
    }
}
