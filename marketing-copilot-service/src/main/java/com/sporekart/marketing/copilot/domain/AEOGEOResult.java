package com.sporekart.marketing.copilot.domain;

import java.util.List;

public record AEOGEOResult(
    String query,
    double answerAppearanceScore,
    double generativeSnippetScore,
    List<String> featuredSnippetOpportunities,
    List<String> peopleAlsoAsk,
    List<String> schemaTypes,
    String optimizedSnippet,
    List<String> optimizationTips,
    String locale
) {}
