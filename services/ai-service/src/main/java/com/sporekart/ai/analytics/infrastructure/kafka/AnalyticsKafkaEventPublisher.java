package com.sporekart.ai.analytics.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Profile("!test")
@Slf4j
@Component
public class AnalyticsKafkaEventPublisher {

    private static final String TOPIC = "analytics-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AnalyticsKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishGovernanceMetricsUpdated(String module, int metricCount) {
        var payload = basePayload("GovernanceMetricsUpdated");
        payload.put("module", module);
        payload.put("metricCount", metricCount);
        publish(payload, module);
    }

    public void publishDashboardUpdated(UUID dashboardId, String action) {
        var payload = basePayload("DashboardUpdated");
        payload.put("dashboardId", dashboardId.toString());
        payload.put("action", action);
        publish(payload, dashboardId.toString());
    }

    public void publishReportGenerated(UUID reportId, String reportType) {
        var payload = basePayload("ReportGenerated");
        payload.put("reportId", reportId.toString());
        payload.put("reportType", reportType);
        publish(payload, reportId.toString());
    }

    public void publishReportExported(UUID exportId, UUID reportId, String format) {
        var payload = basePayload("ReportExported");
        payload.put("exportId", exportId.toString());
        payload.put("reportId", reportId.toString());
        payload.put("format", format);
        publish(payload, exportId.toString());
    }

    public void publishKPICalculated(String name, String module, String status) {
        var payload = basePayload("KPICalculated");
        payload.put("name", name);
        payload.put("module", module);
        payload.put("status", status);
        publish(payload, name + ":" + module);
    }

    public void publishTrendCalculated(String name, String module, String direction) {
        var payload = basePayload("TrendCalculated");
        payload.put("name", name);
        payload.put("module", module);
        payload.put("direction", direction);
        publish(payload, name + ":" + module);
    }

    public void publishAnalyticsSnapshotCreated(UUID snapshotId, String name) {
        var payload = basePayload("AnalyticsSnapshotCreated");
        payload.put("snapshotId", snapshotId.toString());
        payload.put("name", name);
        publish(payload, snapshotId.toString());
    }

    private Map<String, Object> basePayload(String eventType) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", eventType);
        payload.put("timestamp", Instant.now().toString());
        return payload;
    }

    private void publish(Map<String, Object> payload, String key) {
        try {
            var message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(TOPIC, key, message);
            log.debug("Published {} event to {}", payload.get("eventType"), TOPIC);
        } catch (Exception e) {
            log.warn("Failed to publish {} event: {}", payload.get("eventType"), e.getMessage());
        }
    }
}
