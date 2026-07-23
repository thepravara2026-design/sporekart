package com.sporekart.marketing.copilot.dto;

import java.util.List;

public record MarketingBriefResponse(
    String briefId,
    String title,
    String objective,
    String targetAudience,
    String keyMessage,
    List<String> channels,
    List<String> deliverables,
    String timeline,
    List<String> competitorSuggestions,
    String successMetrics
) {}
