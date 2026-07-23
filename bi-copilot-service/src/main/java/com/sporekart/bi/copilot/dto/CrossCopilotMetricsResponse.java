package com.sporekart.bi.copilot.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import com.sporekart.bi.copilot.domain.CrossCopilotMetric;

public record CrossCopilotMetricsResponse(
    List<CrossCopilotMetric> metrics,
    OffsetDateTime timestamp,
    Map<String, Object> comparison
) {}
