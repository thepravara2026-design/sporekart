package com.sporekart.operations.copilot.domain;

import java.util.List;

public record LogisticsRoute(
    String routeId,
    String origin,
    String destination,
    String zone,
    double distanceKm,
    double estimatedTransitDays,
    double averageCost,
    List<String> availableCouriers,
    String recommendedCourier,
    double reliabilityScore
) {}
