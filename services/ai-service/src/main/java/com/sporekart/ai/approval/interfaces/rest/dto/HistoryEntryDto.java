package com.sporekart.ai.approval.interfaces.rest.dto;

public record HistoryEntryDto(
    String id,
    String decision,
    String comment,
    String reviewerId,
    String timestamp
) {}
