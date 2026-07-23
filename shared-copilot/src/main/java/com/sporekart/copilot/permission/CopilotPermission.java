package com.sporekart.copilot.permission;

import java.util.Objects;

public record CopilotPermission(
    String resource,
    String action,
    boolean granted
) {
    public CopilotPermission {
        Objects.requireNonNull(resource, "resource must not be null");
        Objects.requireNonNull(action, "action must not be null");
        if (resource.isBlank()) throw new IllegalArgumentException("resource must not be blank");
        if (action.isBlank()) throw new IllegalArgumentException("action must not be blank");
    }
}
