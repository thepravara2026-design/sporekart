package com.sporekart.bi.copilot.dto;

import com.sporekart.bi.copilot.domain.ExecutiveSummary;
import com.sporekart.bi.copilot.domain.VisualizationConfig;

import java.time.OffsetDateTime;
import java.util.List;

public record DashboardResponse(
    ExecutiveSummary summary,
    List<VisualizationConfig> visualizations,
    OffsetDateTime generatedAt
) {}