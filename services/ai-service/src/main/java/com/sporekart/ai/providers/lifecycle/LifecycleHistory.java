package com.sporekart.ai.providers.lifecycle;

import com.sporekart.ai.providers.lifecycle.state.StateTransition;

import java.time.Instant;
import java.util.List;

public interface LifecycleHistory {
    void recordTransition(StateTransition transition);
    List<StateTransition> getHistory(String providerId);
    List<StateTransition> getHistoryByTimeRange(String providerId, Instant from, Instant to);
    List<StateTransition> getRecentTransitions(int limit);
    void clearHistory(String providerId);
}
