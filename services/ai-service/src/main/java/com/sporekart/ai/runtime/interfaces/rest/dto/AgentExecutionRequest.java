package com.sporekart.ai.runtime.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record AgentExecutionRequest(
    @NotBlank String input,
    String sessionId,
    String userId,
    Map<String, String> metadata
) {}
