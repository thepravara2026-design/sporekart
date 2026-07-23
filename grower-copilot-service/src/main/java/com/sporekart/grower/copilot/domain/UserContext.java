package com.sporekart.grower.copilot.domain;

import java.util.Map;

public record UserContext(
    String userId,
    String sessionId,
    String growerId,
    Map<String, Object> attributes
) {}
