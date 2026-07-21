package com.sporekart.ai.prompt.domain.event;

import java.time.Instant;

public record PromptCreatedEvent(
    String promptId,
    String name,
    String createdBy,
    Instant occurredOn
) implements PromptDomainEvent {
    public PromptCreatedEvent {
        occurredOn = occurredOn == null ? Instant.now() : occurredOn;
    }

    @Override
    public String eventType() { return "PROMPT_CREATED"; }
}
