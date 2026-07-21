package com.sporekart.ai.providers.health;

import java.util.List;

public interface HealthAggregator {
    HealthStatus aggregate(List<HealthResponse> responses);
    double aggregateHealthScore(List<HealthResponse> responses);
    HealthStatus weightedAggregate(List<HealthResponse> responses, List<Integer> weights);
}
