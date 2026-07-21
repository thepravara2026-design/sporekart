package com.sporekart.ai.providers.lifecycle.validation;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;

import java.util.List;

public interface LifecycleValidator {
    boolean validateTransition(String providerId, LifecycleState from, LifecycleState to);
    List<String> getValidationErrors(String providerId);
    boolean isStateConsistent(String providerId);
    boolean canProceed(String providerId, LifecycleState target);
}
