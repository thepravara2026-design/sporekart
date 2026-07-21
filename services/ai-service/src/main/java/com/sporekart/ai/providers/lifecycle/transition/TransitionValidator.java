package com.sporekart.ai.providers.lifecycle.transition;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;

public interface TransitionValidator {
    boolean validate(String providerId, LifecycleState from, LifecycleState to);
    String getValidationError(String providerId, LifecycleState from, LifecycleState to);
    boolean isTransitionSafe(String providerId, LifecycleState from, LifecycleState to);
}
