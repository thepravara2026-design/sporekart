package com.sporekart.operations.copilot.dto;

import java.util.List;
import java.util.Map;

public record OrderOperationsResponse(
    Map<String, Integer> orderSummary,
    List<String> bottlenecks,
    List<String> recommendations,
    double fulfillmentRate
) {}
