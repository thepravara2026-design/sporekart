package com.sporekart.operations.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record ProcurementRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String vendorId,
    String sku,
    String urgency
) {}
