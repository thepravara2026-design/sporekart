package com.sporekart.ai.governance.interfaces.rest.dto;

import java.util.Map;

public record GovernanceHealthDto(
    String status,
    String service,
    long timestamp,
    Map<String, Object> details
) {}
