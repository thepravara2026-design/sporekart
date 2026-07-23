package com.sporekart.bi.copilot.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import com.sporekart.bi.copilot.domain.BusinessInsight;
import com.sporekart.bi.copilot.domain.DashboardWidget;

public record DashboardResponse(
    String dashboardId,
    String name,
    List<DashboardWidget> widgets,
    Map<String, Object> summaryData,
    List<BusinessInsight> insights,
    OffsetDateTime generatedAt
) {}
