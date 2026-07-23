package com.sporekart.executive.copilot.dto;

import java.util.List;

public record MarketResponse(
    String segment,
    double marketSize,
    double growthRate,
    List<CompetitorItem> competitors,
    List<String> trends,
    List<String> opportunities
) {
    public record CompetitorItem(String name, String share, String strength, String weakness) {}
}
