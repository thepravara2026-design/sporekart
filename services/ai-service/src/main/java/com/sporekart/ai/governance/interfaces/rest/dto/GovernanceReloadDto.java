package com.sporekart.ai.governance.interfaces.rest.dto;

public record GovernanceReloadDto(
    boolean success,
    String message,
    long timestamp
) {}
