package com.sporekart.ai.prompt.domain.event;

import java.time.Instant;

public record PromptUpdatedEvent(
    String promptId,
    String performedBy,
    Instant occurredOn
) implements PromptDomainEvent {
    public PromptUpdatedEvent {
        occurredOn = occurredOn == null ? Instant.now() : occurredOn;
    }

    @Override
    public String eventType() { return "PROMPT_UPDATED"; }
}
