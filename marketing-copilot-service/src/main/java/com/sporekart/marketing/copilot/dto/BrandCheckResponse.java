package com.sporekart.marketing.copilot.dto;

import java.util.List;

public record BrandCheckResponse(
    double consistencyScore,
    List<String> violations,
    List<String> suggestions,
    boolean approved
) {}
