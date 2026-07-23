package com.sporekart.bi.copilot.dto;

import com.sporekart.bi.copilot.domain.BusinessInsight;

import java.util.List;

public record InsightsResponse(
    List<BusinessInsight> insights,
    int total,
    int critical,
    int important,
    int info
) {}