package com.sporekart.ai.providers.registry.activation;

public interface ActivationLifecycle {
    ActivationState activate(String providerId);
    ActivationState deactivate(String providerId);
    ActivationState transition(String providerId, ActivationState target);
    boolean canTransition(ActivationState current, ActivationState target);
    ActivationState getState(String providerId);
}
