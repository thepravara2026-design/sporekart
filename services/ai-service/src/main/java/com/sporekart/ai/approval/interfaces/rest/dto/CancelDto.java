package com.sporekart.ai.approval.interfaces.rest.dto;

public record CancelDto(
    String userId,
    String reason
) {}
