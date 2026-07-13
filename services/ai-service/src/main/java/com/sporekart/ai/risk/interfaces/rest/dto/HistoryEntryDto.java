package com.sporekart.ai.risk.interfaces.rest.dto;

public record HistoryEntryDto(
    String id,
    String assessmentId,
    String eventType,
    String description,
    String timestamp
) {}
