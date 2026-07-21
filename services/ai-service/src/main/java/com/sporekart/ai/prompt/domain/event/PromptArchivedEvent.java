package com.sporekart.ai.prompt.domain.event;

import java.time.Instant;

public record PromptArchivedEvent(
    String promptId,
    String performedBy,
    String reason,
    Instant occurredOn
) implements PromptDomainEvent {
    public PromptArchivedEvent {
        occurredOn = occurredOn == null ? Instant.now() : occurredOn;
    }

    @Override
    public String eventType() { return "PROMPT_ARCHIVED"; }
}
