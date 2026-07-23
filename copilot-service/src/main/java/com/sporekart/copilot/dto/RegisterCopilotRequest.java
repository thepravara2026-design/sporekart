package com.sporekart.copilot.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public record RegisterCopilotRequest(
    @NotBlank String name,
    @NotBlank String type,
    @NotBlank String version,
    String description,
    Map<String, Object> persona,
    List<String> capabilities
) {}
