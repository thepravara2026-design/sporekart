package com.sporekart.ai.conversation.domain;

import java.util.UUID;

public record ContextEntry(
    UUID sessionId,
    String source,
    String content,
    double weight
) {}
