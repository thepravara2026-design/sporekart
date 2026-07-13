package com.sporekart.ai.decision.interfaces.rest.dto;
public record HistoryEntryDto(String id, String fromStatus, String toStatus, String triggeredBy, String reason, String timestamp) {}
