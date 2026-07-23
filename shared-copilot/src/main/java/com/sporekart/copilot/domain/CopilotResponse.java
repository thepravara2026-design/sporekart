package com.sporekart.copilot.domain;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record CopilotResponse(
    String message,
    CopilotType copilotType,
    Map<String, Object> context,
    boolean streaming,
    List<Suggestion> suggestions,
    OffsetDateTime timestamp
) {
    public CopilotResponse {
        Objects.requireNonNull(message, "message must not be null");
        Objects.requireNonNull(copilotType, "copilotType must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
        context = context == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(context));
        suggestions = suggestions == null ? Collections.emptyList() : List.copyOf(suggestions);
    }

    public static CopilotResponse of(String message, CopilotType copilotType) {
        return new CopilotResponse(
            message,
            copilotType,
            Collections.emptyMap(),
            false,
            Collections.emptyList(),
            OffsetDateTime.now()
        );
    }
}
