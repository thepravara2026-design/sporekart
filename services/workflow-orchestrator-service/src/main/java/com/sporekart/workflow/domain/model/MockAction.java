package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record MockAction(
    String id,
    String name,
    String actionType,
    String status,
    String description,
    Map<String, Object> input,
    Map<String, Object> output,
    Instant executedAt
) {
    public static MockAction create(String name, String actionType, String description, Map<String, Object> input) {
        return new MockAction(
            UUID.randomUUID().toString(),
            name, actionType, "EXECUTED", description,
            input == null ? Map.of() : Map.copyOf(input),
            Map.of("simulated", true, "result", "success"),
            Instant.now()
        );
    }

    public static MockAction failed(String name, String actionType, String description, Map<String, Object> input) {
        return new MockAction(
            UUID.randomUUID().toString(),
            name, actionType, "FAILED", description,
            input == null ? Map.of() : Map.copyOf(input),
            Map.of("simulated", true, "result", "failure", "error", "Simulated failure"),
            Instant.now()
        );
    }
}
