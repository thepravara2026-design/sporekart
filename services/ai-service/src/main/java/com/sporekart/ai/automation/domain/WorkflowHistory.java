package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.UUID;

public record WorkflowHistory(
    UUID id,
    UUID executionId,
    String step,
    String action,
    String status,
    String message,
    Instant timestamp
) {}
