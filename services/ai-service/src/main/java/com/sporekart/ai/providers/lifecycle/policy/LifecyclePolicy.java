package com.sporekart.ai.providers.lifecycle.policy;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;

import java.util.List;

public interface LifecyclePolicy {
    boolean isTransitionAllowed(LifecycleState from, LifecycleState to);
    List<LifecycleState> getAllowedTargets(LifecycleState current);
    boolean requiresValidation(LifecycleState from, LifecycleState to);
    boolean requiresApproval(LifecycleState from, LifecycleState to);
    int maxRetries(LifecycleState from, LifecycleState to);
}
