package com.sporekart.ai.governance.interfaces.rest.dto;

import java.util.List;
import java.util.Map;

public record GovernanceRequestDto(
    String module,
    String action,
    Map<String, Object> payload,
    Map<String, Object> metadata,
    String userId,
    List<String> roles
) {}
