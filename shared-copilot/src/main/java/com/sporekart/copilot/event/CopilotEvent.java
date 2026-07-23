package com.sporekart.copilot.event;

import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record CopilotEvent(
    String id,
    String type,
    CopilotType copilotType,
    SessionId sessionId,
    Map<String, Object> payload,
    OffsetDateTime timestamp
) {
    public CopilotEvent {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(copilotType, "copilotType must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
        if (type.isBlank()) throw new IllegalArgumentException("type must not be blank");
        payload = payload == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(payload));
    }

    public static CopilotEvent of(String type, CopilotType copilotType, SessionId sessionId) {
        return new CopilotEvent(
            UUID.randomUUID().toString(),
            type,
            copilotType,
            sessionId,
            Collections.emptyMap(),
            OffsetDateTime.now()
        );
    }

    public static CopilotEvent of(String type, CopilotType copilotType, SessionId sessionId, Map<String, Object> payload) {
        return new CopilotEvent(
            UUID.randomUUID().toString(),
            type,
            copilotType,
            sessionId,
            payload,
            OffsetDateTime.now()
        );
    }
}
