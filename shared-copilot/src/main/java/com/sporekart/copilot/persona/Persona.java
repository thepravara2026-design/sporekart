package com.sporekart.copilot.persona;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record Persona(
    String name,
    String description,
    String role,
    String tone,
    String behavior,
    List<String> knowledgeScope,
    List<String> permissionScope,
    List<String> allowedTools,
    String systemPrompt,
    String responseStyle,
    List<String> escalationRules,
    Map<String, Object> safetyPolicies
) {
    public Persona {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(role, "role must not be null");
        Objects.requireNonNull(tone, "tone must not be null");
        Objects.requireNonNull(behavior, "behavior must not be null");
        knowledgeScope = knowledgeScope == null ? Collections.emptyList() : List.copyOf(knowledgeScope);
        permissionScope = permissionScope == null ? Collections.emptyList() : List.copyOf(permissionScope);
        allowedTools = allowedTools == null ? Collections.emptyList() : List.copyOf(allowedTools);
        systemPrompt = systemPrompt == null ? "" : systemPrompt;
        responseStyle = responseStyle == null ? "" : responseStyle;
        escalationRules = escalationRules == null ? Collections.emptyList() : List.copyOf(escalationRules);
        safetyPolicies = safetyPolicies == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(safetyPolicies));
    }
}
