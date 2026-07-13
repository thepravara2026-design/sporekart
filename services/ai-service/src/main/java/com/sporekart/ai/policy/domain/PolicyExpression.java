package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record PolicyExpression(
    String expression, Map<String, Object> bindings,
    String language, boolean compiled, OffsetDateTime compiledAt) {}
