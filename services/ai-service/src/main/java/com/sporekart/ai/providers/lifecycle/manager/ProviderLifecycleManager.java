package com.sporekart.ai.providers.lifecycle.manager;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;
import com.sporekart.ai.providers.lifecycle.state.StateTransition;

import java.util.List;
import java.util.Optional;

public interface ProviderLifecycleManager {
    boolean transition(String providerId, LifecycleState target);
    Optional<LifecycleState> getState(String providerId);
    List<StateTransition> getHistory(String providerId);
    List<LifecycleState> getAllowedTransitions(String providerId);
    boolean canTransition(String providerId, LifecycleState target);
    void reset(String providerId);
}
