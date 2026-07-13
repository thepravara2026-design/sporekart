package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.AnalyticsAuditService;
import com.sporekart.ai.analytics.domain.GovernanceMetric;
import com.sporekart.ai.analytics.domain.MetricType;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceMetricEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceMetricRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricsAggregationServiceImplTest {

    @Mock private GovernanceMetricRepository repository;
    @Mock private AnalyticsAuditService auditService;
    private ObjectMapper objectMapper;
    private MetricsAggregationServiceImpl service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new MetricsAggregationServiceImpl(repository, auditService, objectMapper);
    }

    @Test
    void testRecordMetric() {
        var entity = createEntity();
        when(repository.save(any(GovernanceMetricEntity.class))).thenReturn(entity);

        GovernanceMetric metric = service.recordMetric("test-metric", "module1", MetricType.COUNT, 42.0, Map.of("env", "test"));

        assertNotNull(metric);
        assertEquals("test-metric", metric.name());
        verify(auditService).recordAudit(eq("METRIC_RECORDED"), eq("GovernanceMetric"), any(), isNull(), any(), eq("system"));
    }

    @Test
    void testGetAggregatedMetrics() {
        var entity = createEntity();
        when(repository.findByRecordedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(entity));

        Map<String, Object> result = service.getAggregatedMetrics("module1", "test-metric", Instant.now().minusSeconds(3600), Instant.now());

        assertNotNull(result);
        assertEquals("module1", result.get("module"));
        assertEquals("test-metric", result.get("metricName"));
    }

    @Test
    void testGetMetricsSummary() {
        var entity = createEntity();
        when(repository.findAll()).thenReturn(List.of(entity));

        Map<String, Object> summary = service.getMetricsSummary();

        assertNotNull(summary);
        assertTrue(summary.containsKey("module1"));
    }

    private GovernanceMetricEntity createEntity() {
        var entity = new GovernanceMetricEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("test-metric");
        entity.setModule("module1");
        entity.setType("COUNT");
        entity.setValue(42.0);
        entity.setLabels("{}");
        entity.setRecordedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        return entity;
    }
}
