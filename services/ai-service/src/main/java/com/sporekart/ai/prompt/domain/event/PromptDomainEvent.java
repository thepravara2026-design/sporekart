package com.sporekart.ai.prompt.domain.event;

import java.time.Instant;

public interface PromptDomainEvent {
    String promptId();
    String eventType();
    Instant occurredOn();
}
