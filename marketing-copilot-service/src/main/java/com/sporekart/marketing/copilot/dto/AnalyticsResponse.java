package com.sporekart.marketing.copilot.dto;

import java.util.List;
import java.util.Map;

public record AnalyticsResponse(
    String campaignId,
    Map<String, Object> metrics,
    Map<String, Double> channelBreakdown,
    List<String> insights,
    List<String> recommendations
) {}
