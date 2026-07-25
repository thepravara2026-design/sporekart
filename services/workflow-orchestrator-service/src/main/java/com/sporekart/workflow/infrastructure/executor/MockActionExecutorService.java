package com.sporekart.workflow.infrastructure.executor;

import com.sporekart.workflow.domain.model.MockAction;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MockActionExecutorService {
    private final Random random = new Random();

    public MockAction executeAction(String name, String actionType, String description, Map<String, Object> input) {
        if (random.nextDouble() < 0.05) {
            return MockAction.failed(name, actionType, "Simulated execution failure for " + name,
                input == null ? Map.of() : input);
        }
        return MockAction.create(name, actionType, description, input);
    }

    public List<MockAction> executeActions(List<Map<String, Object>> actions) {
        var results = new ArrayList<MockAction>();
        for (var action : actions) {
            var name = (String) action.getOrDefault("name", "unknown");
            var actionType = (String) action.getOrDefault("type", "execution");
            var description = (String) action.getOrDefault("description", "");
            var input = (Map<String, Object>) action.getOrDefault("input", Map.of());
            results.add(executeAction(name, actionType, description, input));
        }
        return List.copyOf(results);
    }

    public List<MockAction> executeRollback(List<MockAction> executedActions) {
        var rollbacks = new ArrayList<MockAction>();
        for (var action : executedActions.reversed()) {
            rollbacks.add(MockAction.create(
                "ROLLBACK_" + action.name(),
                "rollback",
                "Rolling back: " + action.name(),
                Map.of("originalActionId", action.id(), "originalStatus", action.status())
            ));
        }
        return List.copyOf(rollbacks);
    }
}
