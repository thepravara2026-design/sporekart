package com.sporekart.ai.runtime.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record AgentDefinitionRequest(
    @NotBlank String name,
    String description,
    @NotBlank String type,
    String systemPrompt,
    List<String> tools,
    Map<String, String> configuration,
    int maxIterations,
    int maxTokens,
    double temperature
) {}
