package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.AnalyticsAuditService;
import com.sporekart.ai.analytics.api.MetricsAggregationService;
import com.sporekart.ai.analytics.domain.GovernanceMetric;
import com.sporekart.ai.analytics.domain.MetricType;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceMetricEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsAggregationServiceImpl implements MetricsAggregationService {

    private final GovernanceMetricRepository repository;
    private final AnalyticsAuditService auditService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public GovernanceMetric recordMetric(String name, String module, MetricType type, double value, Map<String, String> labels) {
        var entity = new GovernanceMetricEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(name);
        entity.setModule(module);
        entity.setType(type.name());
        entity.setValue(value);
        try {
            entity.setLabels(objectMapper.writeValueAsString(labels != null ? labels : Map.of()));
        } catch (Exception e) {
            entity.setLabels("{}");
        }
        entity.setRecordedAt(java.time.OffsetDateTime.now());
        entity.setCreatedAt(java.time.OffsetDateTime.now());

        var saved = repository.save(entity);
        auditService.recordAudit("METRIC_RECORDED", "GovernanceMetric", saved.getId(),
                null, Map.of("name", name, "module", module, "type", type.name(), "value", value), "system");
        log.debug("Recorded metric {}: {}={} for module '{}'", saved.getId(), name, value, module);
        return toDomain(saved);
    }

    @Override
    public Map<String, Object> getAggregatedMetrics(String module, String metricName, Instant from, Instant to) {
        var fromLdt = LocalDateTime.ofInstant(from, ZoneId.systemDefault());
        var toLdt = LocalDateTime.ofInstant(to, ZoneId.systemDefault());

        var allBetween = repository.findByRecordedAtBetween(fromLdt, toLdt);
        var filtered = allBetween.stream()
                .filter(m -> m.getModule().equals(module) && m.getName().equals(metricName))
                .toList();

        var result = new HashMap<String, Object>();
        result.put("module", module);
        result.put("metricName", metricName);
        result.put("count", filtered.size());
        result.put("sum", filtered.stream().mapToDouble(GovernanceMetricEntity::getValue).sum());
        result.put("average", filtered.isEmpty() ? 0.0 : filtered.stream().mapToDouble(GovernanceMetricEntity::getValue).average().orElse(0.0));
        result.put("from", from.toString());
        result.put("to", to.toString());
        return result;
    }

    @Override
    public List<GovernanceMetric> getMetricsByModule(String module) {
        return repository.findByModule(module).stream().map(this::toDomain).toList();
    }

    @Override
    public Map<String, Object> getMetricsSummary() {
        var all = repository.findAll();
        var summary = new HashMap<String, Object>();
        all.stream()
                .collect(Collectors.groupingBy(GovernanceMetricEntity::getModule, Collectors.counting()))
                .forEach((module, count) -> summary.put(module, count));
        return summary;
    }

    @SuppressWarnings("unchecked")
    private GovernanceMetric toDomain(GovernanceMetricEntity entity) {
        Map<String, Object> labelsMap = Map.of();
        try {
            labelsMap = objectMapper.readValue(entity.getLabels(), HashMap.class);
        } catch (Exception e) {
            log.warn("Failed to deserialize labels for metric {}: {}", entity.getId(), e.getMessage());
        }
        return new GovernanceMetric(
                entity.getId(),
                entity.getName(),
                entity.getModule(),
                MetricType.valueOf(entity.getType()),
                entity.getValue(),
                labelsMap,
                entity.getRecordedAt().toInstant()
        );
    }
}
