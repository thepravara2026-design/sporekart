package com.sporekart.admin.dto;

import com.sporekart.admin.domain.ForecastResult;

import java.util.List;

public record ForecastResponse(
    String metric,
    ForecastResult forecast,
    String confidenceLevel,
    List<String> recommendations
) {}
