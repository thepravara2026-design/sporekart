package com.sporekart.ai.prompt.domain.event;

import java.time.Instant;

public record PromptExecutionPolicyChangedEvent(
    String promptId,
    String performedBy,
    Instant occurredOn
) implements PromptDomainEvent {
    public PromptExecutionPolicyChangedEvent {
        occurredOn = occurredOn == null ? Instant.now() : occurredOn;
    }

    @Override
    public String eventType() { return "PROMPT_EXECUTION_POLICY_CHANGED"; }
}
