package com.sporekart.ai.providers.lifecycle.transition;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record LifecycleTransition(
    LifecycleState from,
    LifecycleState to,
    boolean allowed,
    String condition
) {
    private static final Map<LifecycleState, Set<LifecycleState>> TRANSITIONS = buildTransitions();

    private static Map<LifecycleState, Set<LifecycleState>> buildTransitions() {
        var map = new EnumMap<LifecycleState, Set<LifecycleState>>(LifecycleState.class);
        map.put(LifecycleState.UNKNOWN, Set.of(LifecycleState.REGISTERED, LifecycleState.DISCOVERED));
        map.put(LifecycleState.REGISTERED, Set.of(LifecycleState.VALIDATED, LifecycleState.REMOVED));
        map.put(LifecycleState.DISCOVERED, Set.of(LifecycleState.VALIDATED, LifecycleState.REMOVED));
        map.put(LifecycleState.VALIDATED, Set.of(LifecycleState.INITIALIZED, LifecycleState.FAILED));
        map.put(LifecycleState.INITIALIZED, Set.of(LifecycleState.READY, LifecycleState.FAILED));
        map.put(LifecycleState.READY, Set.of(LifecycleState.ACTIVE, LifecycleState.MAINTENANCE, LifecycleState.DEPRECATED));
        map.put(LifecycleState.ACTIVE, Set.of(LifecycleState.BUSY, LifecycleState.DEGRADED, LifecycleState.MAINTENANCE, LifecycleState.UNAVAILABLE, LifecycleState.DEPRECATED));
        map.put(LifecycleState.BUSY, Set.of(LifecycleState.ACTIVE, LifecycleState.DEGRADED));
        map.put(LifecycleState.DEGRADED, Set.of(LifecycleState.RECOVERING, LifecycleState.MAINTENANCE, LifecycleState.FAILED));
        map.put(LifecycleState.RECOVERING, Set.of(LifecycleState.ACTIVE, LifecycleState.READY, LifecycleState.FAILED));
        map.put(LifecycleState.MAINTENANCE, Set.of(LifecycleState.READY, LifecycleState.ACTIVE, LifecycleState.DEPRECATED));
        map.put(LifecycleState.UNAVAILABLE, Set.of(LifecycleState.RECOVERING, LifecycleState.ACTIVE, LifecycleState.FAILED));
        map.put(LifecycleState.FAILED, Set.of(LifecycleState.RECOVERING, LifecycleState.REMOVED));
        map.put(LifecycleState.DEPRECATED, Set.of(LifecycleState.REMOVED, LifecycleState.ACTIVE));
        map.put(LifecycleState.REMOVED, Set.of());
        return map;
    }

    public static boolean isAllowed(LifecycleState from, LifecycleState to) {
        var targets = TRANSITIONS.get(from);
        return targets != null && targets.contains(to);
    }

    public static Set<LifecycleState> getAllowedTransitions(LifecycleState state) {
        return TRANSITIONS.getOrDefault(state, new HashSet<>());
    }
}
