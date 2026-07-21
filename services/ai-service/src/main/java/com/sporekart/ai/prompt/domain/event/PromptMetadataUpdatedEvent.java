package com.sporekart.ai.prompt.domain.event;

import java.time.Instant;

public record PromptMetadataUpdatedEvent(
    String promptId,
    String performedBy,
    Instant occurredOn
) implements PromptDomainEvent {
    public PromptMetadataUpdatedEvent {
        occurredOn = occurredOn == null ? Instant.now() : occurredOn;
    }

    @Override
    public String eventType() { return "PROMPT_METADATA_UPDATED"; }
}
