package com.sporekart.ai.governance.interfaces.rest.dto;

import java.util.Map;

public record GovernanceStatusDto(
    boolean operational,
    String mode,
    Map<String, Object> metrics
) {}
