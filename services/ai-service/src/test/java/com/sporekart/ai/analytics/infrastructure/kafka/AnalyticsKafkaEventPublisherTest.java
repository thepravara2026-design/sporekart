package com.sporekart.ai.analytics.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsKafkaEventPublisherTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;
    private AnalyticsKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        var objectMapper = new ObjectMapper();
        publisher = new AnalyticsKafkaEventPublisher(kafkaTemplate, objectMapper);
    }

    @Test
    void testPublishReportGenerated() {
        var reportId = UUID.randomUUID();

        publisher.publishReportGenerated(reportId, "COMPLIANCE_REPORT");

        verify(kafkaTemplate).send(eq("analytics-events"), eq(reportId.toString()), anyString());
    }

    @Test
    void testPublishReportExported() {
        var exportId = UUID.randomUUID();
        var reportId = UUID.randomUUID();

        publisher.publishReportExported(exportId, reportId, "JSON");

        verify(kafkaTemplate).send(eq("analytics-events"), eq(exportId.toString()), anyString());
    }

    @Test
    void testPublishDashboardUpdated() {
        var dashboardId = UUID.randomUUID();

        publisher.publishDashboardUpdated(dashboardId, "CREATED");

        verify(kafkaTemplate).send(eq("analytics-events"), eq(dashboardId.toString()), anyString());
    }

    @Test
    void testPublishKPICalculated() {
        publisher.publishKPICalculated("kpi1", "module1", "ON_TRACK");

        verify(kafkaTemplate).send(eq("analytics-events"), eq("kpi1:module1"), anyString());
    }

    @Test
    void testPublishTrendCalculated() {
        publisher.publishTrendCalculated("trend1", "module1", "UP");

        verify(kafkaTemplate).send(eq("analytics-events"), eq("trend1:module1"), anyString());
    }

    @Test
    void testPublishGovernanceMetricsUpdated() {
        publisher.publishGovernanceMetricsUpdated("module1", 42);

        verify(kafkaTemplate).send(eq("analytics-events"), eq("module1"), anyString());
    }

    @Test
    void testPublishAnalyticsSnapshotCreated() {
        var snapshotId = UUID.randomUUID();

        publisher.publishAnalyticsSnapshotCreated(snapshotId, "snapshot1");

        verify(kafkaTemplate).send(eq("analytics-events"), eq(snapshotId.toString()), anyString());
    }
}
