package com.sporekart.ai.analytics.application;

import com.sporekart.ai.analytics.api.TrendAnalysisService;
import com.sporekart.ai.analytics.domain.GovernanceTrend;
import com.sporekart.ai.analytics.domain.TrendDirection;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceMetricEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrendAnalysisServiceImpl implements TrendAnalysisService {

    private final GovernanceMetricRepository metricRepository;
    private final Map<String, GovernanceTrend> trendStore = new ConcurrentHashMap<>();

    @Override
    public GovernanceTrend calculateTrend(String name, String module, List<Double> dataPoints, List<String> timestamps) {
        var direction = TrendDirection.STABLE;
        var changePercent = 0.0;

        if (dataPoints.size() >= 2) {
            var first = dataPoints.get(0);
            var last = dataPoints.get(dataPoints.size() - 1);
            if (first != 0) {
                changePercent = ((last - first) / Math.abs(first)) * 100.0;
            }
            if (changePercent > 5.0) {
                direction = TrendDirection.UP;
            } else if (changePercent < -5.0) {
                direction = TrendDirection.DOWN;
            }
        }

        var trend = new GovernanceTrend(
                UUID.randomUUID(),
                name,
                module,
                List.copyOf(dataPoints),
                timestamps.stream().map(Instant::parse).toList(),
                direction,
                changePercent,
                Instant.now()
        );

        trendStore.put(name + ":" + module, trend);
        log.info("Calculated trend '{}' for module '{}': direction={}, change={}%", name, module, direction, changePercent);
        return trend;
    }

    @Override
    public GovernanceTrend getTrend(String name, String module) {
        return trendStore.get(name + ":" + module);
    }

    @Override
    public List<GovernanceTrend> getTrendsByModule(String module) {
        return trendStore.values().stream()
                .filter(t -> t.module().equals(module))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<GovernanceTrend>> getAllTrends() {
        return trendStore.values().stream()
                .collect(Collectors.groupingBy(GovernanceTrend::module, Collectors.toList()));
    }
}
