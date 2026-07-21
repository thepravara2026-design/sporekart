package com.sporekart.ai.memory.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record MemoryEntryRequest(
    @NotBlank String agentId,
    String sessionId,
    String userId,
    @NotBlank String content,
    @NotBlank String type,
    int importance,
    Map<String, String> metadata,
    Integer ttlDays
) {}
