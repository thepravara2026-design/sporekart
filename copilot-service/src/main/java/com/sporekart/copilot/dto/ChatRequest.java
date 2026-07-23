package com.sporekart.copilot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;

public record ChatRequest(
    String sessionId,
    @NotBlank @Size(max = 4000) String message,
    Map<String, Object> context
) {}
