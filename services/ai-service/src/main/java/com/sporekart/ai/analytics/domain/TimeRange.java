package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.UUID;

public record TimeRange(
    UUID id,
    Instant from,
    Instant to,
    String period
) {}
