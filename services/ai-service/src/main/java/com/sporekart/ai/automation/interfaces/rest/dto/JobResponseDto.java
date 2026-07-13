package com.sporekart.ai.automation.interfaces.rest.dto;

public record JobResponseDto(
    String id,
    String type,
    String name,
    String status,
    int retryCount,
    int maxRetries,
    String createdAt
) {}
