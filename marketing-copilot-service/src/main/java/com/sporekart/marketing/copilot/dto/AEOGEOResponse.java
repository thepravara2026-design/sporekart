package com.sporekart.marketing.copilot.dto;

import java.util.List;

public record AEOGEOResponse(
    String query,
    double answerAppearanceScore,
    double generativeSnippetScore,
    List<String> featuredSnippetOpportunities,
    List<String> peopleAlsoAsk,
    String optimizedSnippet,
    List<String> optimizationTips
) {}
