package com.sporekart.ai.providers.lifecycle;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LifecycleRegistry {
    void register(String providerId, LifecycleState initialState);
    void updateState(String providerId, LifecycleState state);
    Optional<LifecycleState> getState(String providerId);
    List<String> getProvidersInState(LifecycleState state);
    Map<String, LifecycleState> getAllStates();
    boolean contains(String providerId);
    int size();
}
