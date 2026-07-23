package com.sporekart.operations.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record LogisticsRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String origin,
    String destination,
    String pincode,
    Double weight
) {}
