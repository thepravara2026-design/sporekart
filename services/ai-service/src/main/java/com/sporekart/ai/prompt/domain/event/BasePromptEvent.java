package com.sporekart.ai.prompt.domain.event;

import java.time.Instant;

public abstract record BasePromptEvent(
    String promptId,
    String eventType,
    Instant occurredOn,
    String performedBy
) implements PromptDomainEvent {
    public BasePromptEvent {
        occurredOn = occurredOn == null ? Instant.now() : occurredOn;
    }
}
