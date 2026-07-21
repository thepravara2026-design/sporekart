package com.sporekart.ai.prompt.domain.event;

import java.time.Instant;

public record PromptPublishedEvent(
    String promptId,
    String version,
    String performedBy,
    Instant occurredOn
) implements PromptDomainEvent {
    public PromptPublishedEvent {
        occurredOn = occurredOn == null ? Instant.now() : occurredOn;
    }

    @Override
    public String eventType() { return "PROMPT_PUBLISHED"; }
}
