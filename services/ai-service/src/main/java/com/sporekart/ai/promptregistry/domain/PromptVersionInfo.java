package com.sporekart.ai.promptregistry.domain;

import java.time.Instant;

public record PromptVersionInfo(
        int version,
        PromptStatus status,
        Instant createdAt,
        String changeSummary) {
}
