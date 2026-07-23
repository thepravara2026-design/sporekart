package com.sporekart.executive.copilot.domain;

import java.util.List;

public record MarketIntelligence(
    String segment,
    double marketSize,
    double growthRate,
    List<CompetitorInfo> competitors,
    List<String> trends,
    List<String> opportunities
) {
    public record CompetitorInfo(String name, String marketShare, String strength, String weakness) {}
}
