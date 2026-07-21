package com.sporekart.ai.providers.metrics;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MetricsRegistry {
    void register(String providerId, MetricsModel model);
    void update(String providerId, MetricsModel model);
    Optional<MetricsModel> getMetrics(String providerId);
    List<MetricsModel> getAllMetrics();
    Map<String, MetricsModel> getMetricsSnapshot();
    void clear(String providerId);
}
