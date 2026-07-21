package com.sporekart.ai.prompt.domain.event;

import java.time.Instant;

public record PromptVersionCreatedEvent(
    String promptId,
    String version,
    String createdBy,
    Instant occurredOn
) implements PromptDomainEvent {
    public PromptVersionCreatedEvent {
        occurredOn = occurredOn == null ? Instant.now() : occurredOn;
    }

    @Override
    public String eventType() { return "PROMPT_VERSION_CREATED"; }
}
