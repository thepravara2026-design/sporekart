package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record AEOGEORequest(
    @NotBlank String query,
    @NotBlank String content,
    String locale
) {}
