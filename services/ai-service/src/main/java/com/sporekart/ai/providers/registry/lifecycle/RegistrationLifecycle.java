package com.sporekart.ai.providers.registry.lifecycle;

public interface RegistrationLifecycle {
    RegistrationState submit(String providerId);
    RegistrationState validate(String providerId);
    RegistrationState approve(String providerId);
    RegistrationState reject(String providerId, String reason);
    RegistrationState cancel(String providerId);
    RegistrationState getState(String providerId);
    boolean canTransition(RegistrationState current, RegistrationState target);
}
