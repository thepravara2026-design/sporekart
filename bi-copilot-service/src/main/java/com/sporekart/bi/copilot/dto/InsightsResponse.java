package com.sporekart.bi.copilot.dto;

import java.util.List;

import com.sporekart.bi.copilot.domain.BusinessInsight;

public record InsightsResponse(
    List<BusinessInsight> insights,
    int total,
    String category,
    String period
) {}
