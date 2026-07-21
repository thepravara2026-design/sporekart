package com.sporekart.ai.providers.lifecycle.policy;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;

import java.time.Instant;
import java.util.Map;

public record LifecycleContext(
    String providerId,
    LifecycleState currentState,
    LifecycleState targetState,
    String trigger,
    String reason,
    Map<String, Object> attributes,
    Instant timestamp
) {}
