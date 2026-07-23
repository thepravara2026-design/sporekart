package com.sporekart.marketing.copilot.domain;

import java.util.List;

public record MarketingBrief(
    String id,
    String title,
    String objective,
    String targetAudience,
    String keyMessage,
    List<String> channels,
    List<String> deliverables,
    double budget,
    String timeline,
    String brandGuidelines,
    List<String> competitors,
    String successMetrics
) {}
