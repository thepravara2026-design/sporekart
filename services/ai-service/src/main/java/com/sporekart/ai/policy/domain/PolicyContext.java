package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record PolicyContext(
    UUID id, UUID requestId, String module, String action,
    PolicyScope scope, Map<String, Object> resource,
    Map<String, Object> subject, Map<String, Object> environment,
    List<String> roles, OffsetDateTime timestamp) {}
