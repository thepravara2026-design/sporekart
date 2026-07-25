package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.WorkflowState;

import java.util.*;

public class WorkflowStateMachine {
    private static final Map<WorkflowState, Set<WorkflowState>> TRANSITIONS = new EnumMap<>(WorkflowState.class);

    static {
        TRANSITIONS.put(WorkflowState.CREATED, Set.of(WorkflowState.QUEUED, WorkflowState.CANCELLED));
        TRANSITIONS.put(WorkflowState.QUEUED, Set.of(WorkflowState.PENDING, WorkflowState.CANCELLED));
        TRANSITIONS.put(WorkflowState.PENDING, Set.of(WorkflowState.RUNNING, WorkflowState.CANCELLED, WorkflowState.FAILED));
        TRANSITIONS.put(WorkflowState.RUNNING, Set.of(WorkflowState.PAUSED, WorkflowState.WAITING_APPROVAL, WorkflowState.COMPLETED, WorkflowState.FAILED, WorkflowState.RETRYING, WorkflowState.CANCELLED));
        TRANSITIONS.put(WorkflowState.PAUSED, Set.of(WorkflowState.RUNNING, WorkflowState.CANCELLED));
        TRANSITIONS.put(WorkflowState.WAITING_APPROVAL, Set.of(WorkflowState.RUNNING, WorkflowState.CANCELLED, WorkflowState.FAILED));
        TRANSITIONS.put(WorkflowState.RETRYING, Set.of(WorkflowState.RUNNING, WorkflowState.FAILED, WorkflowState.CANCELLED));
        TRANSITIONS.put(WorkflowState.COMPLETED, Set.of(WorkflowState.ARCHIVED));
        TRANSITIONS.put(WorkflowState.FAILED, Set.of(WorkflowState.RETRYING, WorkflowState.ARCHIVED));
        TRANSITIONS.put(WorkflowState.CANCELLED, Set.of(WorkflowState.ARCHIVED));
        TRANSITIONS.put(WorkflowState.ARCHIVED, Set.of());
    }

    public boolean isValidTransition(WorkflowState from, WorkflowState to) {
        var allowed = TRANSITIONS.get(from);
        return allowed != null && allowed.contains(to);
    }

    public Set<WorkflowState> getAllowedTransitions(WorkflowState from) {
        return TRANSITIONS.getOrDefault(from, Set.of());
    }

    public WorkflowState transition(WorkflowState from, WorkflowState to) {
        if (!isValidTransition(from, to)) {
            throw new IllegalStateException("Invalid transition from " + from + " to " + to);
        }
        return to;
    }

    public List<WorkflowState> getTransitionPath(WorkflowState from, WorkflowState to) {
        if (from == to) return List.of(from);
        var visited = new HashSet<WorkflowState>();
        var queue = new ArrayDeque<List<WorkflowState>>();
        queue.add(List.of(from));
        visited.add(from);

        while (!queue.isEmpty()) {
            var path = queue.poll();
            var current = path.getLast();
            for (var next : TRANSITIONS.getOrDefault(current, Set.of())) {
                if (next == to) {
                    var result = new ArrayList<>(path);
                    result.add(next);
                    return List.copyOf(result);
                }
                if (visited.add(next)) {
                    var newPath = new ArrayList<>(path);
                    newPath.add(next);
                    queue.add(newPath);
                }
            }
        }
        return List.of();
    }

    public boolean isTerminal(WorkflowState state) {
        return state == WorkflowState.COMPLETED || state == WorkflowState.CANCELLED || state == WorkflowState.ARCHIVED;
    }

    public boolean isActive(WorkflowState state) {
        return state != WorkflowState.COMPLETED && state != WorkflowState.FAILED &&
               state != WorkflowState.CANCELLED && state != WorkflowState.ARCHIVED;
    }
}
