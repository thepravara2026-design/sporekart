package com.sporekart.workspace.domain;

import java.time.OffsetDateTime;
import java.util.List;

public record SharedMemoryEntry(
    String entryId,
    String namespace,
    String key,
    String value,
    String type,
    List<String> tags,
    int ttlMinutes,
    OffsetDateTime createdAt,
    OffsetDateTime expiresAt
) {
    public static final String NAMESPACE_CONVERSATION = "CONVERSATION";
    public static final String NAMESPACE_KNOWLEDGE = "KNOWLEDGE";
    public static final String NAMESPACE_WORKSPACE = "WORKSPACE";
    public static final String NAMESPACE_COPILOT = "COPILOT";
    public static final String NAMESPACE_SESSION = "SESSION";
    public static final String NAMESPACE_USER_PREFERENCES = "USER_PREFERENCES";
}
