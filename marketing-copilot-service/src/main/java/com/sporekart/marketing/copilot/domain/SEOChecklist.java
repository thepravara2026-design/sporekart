package com.sporekart.marketing.copilot.domain;

import java.util.List;

public record SEOChecklist(
    String url,
    double score,
    List<SEOIssue> critical,
    List<SEOIssue> warnings,
    List<SEOIssue> passed,
    List<String> recommendations
) {
    public record SEOIssue(String field, String message, String severity) {}
}
