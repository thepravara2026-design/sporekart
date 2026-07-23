package com.sporekart.copilot.capability;

import com.sporekart.copilot.domain.CopilotType;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public record Capability(
    String id,
    String name,
    String description,
    CopilotType[] requiredTypes,
    Map<String, Object> inputSchema,
    Map<String, Object> outputSchema
) {
    public Capability {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(description, "description must not be null");
        if (id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        requiredTypes = requiredTypes == null ? new CopilotType[0] : requiredTypes.clone();
        inputSchema = inputSchema == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(inputSchema));
        outputSchema = outputSchema == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(outputSchema));
    }
}
