package com.sporekart.ai.providers.lifecycle.state;

import java.time.Instant;

public record StateTransition(
    String providerId,
    LifecycleState from,
    LifecycleState to,
    String reason,
    boolean success,
    String error,
    Instant timestamp
) {
    public static StateTransition success(String providerId, LifecycleState from, LifecycleState to, String reason) {
        return new StateTransition(providerId, from, to, reason, true, null, Instant.now());
    }

    public static StateTransition failed(String providerId, LifecycleState from, LifecycleState to, String error) {
        return new StateTransition(providerId, from, to, null, false, error, Instant.now());
    }
}
