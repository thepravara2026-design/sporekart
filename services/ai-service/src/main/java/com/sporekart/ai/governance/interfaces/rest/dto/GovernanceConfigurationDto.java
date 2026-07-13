package com.sporekart.ai.governance.interfaces.rest.dto;

import java.util.Map;

public record GovernanceConfigurationDto(
    String key,
    String value,
    String description,
    String scope,
    String mode,
    boolean active,
    int version
) {}
