package com.sporekart.ai.analytics.engine;

import com.sporekart.ai.analytics.domain.GovernanceMetric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MetricsAggregator {

    public Map<String, Long> aggregateByModule(List<GovernanceMetric> metrics) {
        return metrics.stream()
                .collect(Collectors.groupingBy(
                        GovernanceMetric::module,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    public Map<String, Long> aggregateByName(List<GovernanceMetric> metrics) {
        return metrics.stream()
                .collect(Collectors.groupingBy(
                        GovernanceMetric::name,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    public List<GovernanceMetric> aggregateByTimeRange(List<GovernanceMetric> metrics, Instant from, Instant to) {
        return metrics.stream()
                .filter(m -> !m.recordedAt().isBefore(from) && !m.recordedAt().isAfter(to))
                .toList();
    }

    public double computeAverage(List<GovernanceMetric> metrics) {
        return metrics.stream()
                .mapToDouble(GovernanceMetric::value)
                .average()
                .orElse(0.0);
    }

    public double computeSum(List<GovernanceMetric> metrics) {
        return metrics.stream()
                .mapToDouble(GovernanceMetric::value)
                .sum();
    }

    public double computeRate(List<GovernanceMetric> metrics) {
        if (metrics.isEmpty()) {
            return 0.0;
        }
        var sorted = metrics.stream()
                .sorted((a, b) -> a.recordedAt().compareTo(b.recordedAt()))
                .toList();
        var first = sorted.get(0);
        var last = sorted.get(sorted.size() - 1);
        var durationSeconds = java.time.Duration.between(first.recordedAt(), last.recordedAt()).getSeconds();
        if (durationSeconds <= 0) {
            return sorted.size();
        }
        return (double) sorted.size() / durationSeconds;
    }
}
