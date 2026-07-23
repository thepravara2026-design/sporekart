package com.sporekart.operations.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record SupplyChainRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String vendorId,
    String region
) {}
