package com.sporekart.operations.copilot.domain;

import java.util.List;
import java.util.Map;

public record OperationsDashboard(
    Map<String, Object> inventorySummary,
    Map<String, Object> orderSummary,
    Map<String, Object> warehouseSummary,
    Map<String, Object> logisticsSummary,
    Map<String, Object> kpiSummary,
    List<OperationsAlert> activeAlerts,
    List<DecisionRecommendation> recommendations
) {}
