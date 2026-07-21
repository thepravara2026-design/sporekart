package com.sporekart.ai.providers.lifecycle.manager;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;

public interface LifecycleCoordinator {
    void coordinateTransition(String providerId, LifecycleState target);
    boolean isTransitionCoordinationRequired(LifecycleState from, LifecycleState to);
    void onBeforeTransition(String providerId, LifecycleState from, LifecycleState to);
    void onAfterTransition(String providerId, LifecycleState from, LifecycleState to);
}
