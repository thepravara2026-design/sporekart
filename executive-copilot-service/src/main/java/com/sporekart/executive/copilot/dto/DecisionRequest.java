package com.sporekart.executive.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record DecisionRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String category,
    Double budget,
    String timeframe
) {}
