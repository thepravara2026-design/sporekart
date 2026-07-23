package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record SEORequest(
    @NotBlank String url,
    @NotBlank String targetKeyword,
    String locale,
    String contentType,
    String currentContent
) {}
