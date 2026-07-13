package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.UUID;

public record AdminSession(
    UUID id,
    UUID userId,
    String token,
    Instant createdAt,
    Instant expiresAt,
    boolean active
) {}
