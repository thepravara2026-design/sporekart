package com.sporekart.marketing.copilot.domain;

import java.util.List;

public record BrandCheckResult(
    String contentId,
    String contentPreview,
    double consistencyScore,
    List<BrandViolation> violations,
    List<String> suggestions,
    boolean approved
) {
    public record BrandViolation(String element, String expected, String actual, String severity) {}
}
