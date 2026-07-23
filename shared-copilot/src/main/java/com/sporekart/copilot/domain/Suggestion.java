package com.sporekart.copilot.domain;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public record Suggestion(
    String label,
    String action,
    Map<String, Object> payload
) {
    public Suggestion {
        Objects.requireNonNull(label, "label must not be null");
        Objects.requireNonNull(action, "action must not be null");
        payload = payload == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(payload));
    }

    public static Suggestion of(String label, String action) {
        return new Suggestion(label, action, Collections.emptyMap());
    }

    public static Suggestion of(String label, String action, Map<String, Object> payload) {
        return new Suggestion(label, action, payload);
    }
}
