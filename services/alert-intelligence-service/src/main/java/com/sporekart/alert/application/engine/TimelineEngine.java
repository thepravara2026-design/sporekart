package com.sporekart.alert.application.engine;

import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimelineEngine {

    private final AlertRepositoryPort repository;

    public TimelineEngine(AlertRepositoryPort repository) { this.repository = repository; }

    public List<TimelineEvent> generateTimeline() {
        List<TimelineEvent> events = new ArrayList<>();
        events.addAll(generateBusinessEvents());
        events.addAll(generateAlertEvents());
        events.addAll(generateRiskEvents());
        events.addAll(generatePlatformEvents());
        events.addAll(generateAiEvents());
        events.addAll(generateWorkflowEvents());
        events.addAll(generateTrainingEvents());
        events.addAll(generateInventoryEvents());
        events.addAll(generateMarketplaceEvents());
        return events;
    }

    public List<TimelineEvent> generateEventsForType(TimelineEventType type) {
        return switch (type) {
            case BUSINESS -> generateBusinessEvents();
            case ALERT -> generateAlertEvents();
            case RISK -> generateRiskEvents();
            case PLATFORM -> generatePlatformEvents();
            case AI -> generateAiEvents();
            case WORKFLOW -> generateWorkflowEvents();
            case TRAINING -> generateTrainingEvents();
            case INVENTORY -> generateInventoryEvents();
            case MARKETPLACE -> generateMarketplaceEvents();
        };
    }

    private List<TimelineEvent> generateBusinessEvents() {
        return List.of(event(TimelineEventType.BUSINESS, "REVENUE", "REVENUE",
                "Revenue Target Update", "Revenue on track to exceed Q3 target by 8%",
                AlertSeverity.INFO, "analytics-engine", Map.of("projectedVsTarget", 108.0, "quarter", "Q3")));
    }

    private List<TimelineEvent> generateAlertEvents() {
        return List.of(event(TimelineEventType.ALERT, "ALERTS", "PLATFORM",
                "Alert Batch Processed", "Batch of 5 new alerts generated from anomaly detection",
                AlertSeverity.INFO, "alert-engine", Map.of("alertCount", 5, "batchId", UUID.randomUUID().toString())));
    }

    private List<TimelineEvent> generateRiskEvents() {
        return List.of(event(TimelineEventType.RISK, "REVENUE", "REVENUE",
                "Risk Score Update", "Revenue risk score adjusted to 74 (previously 78)",
                AlertSeverity.MEDIUM, "risk-engine", Map.of("previousScore", 78, "newScore", 74, "change", -4)));
    }

    private List<TimelineEvent> generatePlatformEvents() {
        return List.of(event(TimelineEventType.PLATFORM, "SYSTEM", "PLATFORM",
                "Platform Deployment", "New release v3.2.1 deployed to production",
                AlertSeverity.INFO, "devops", Map.of("version", "v3.2.1", "deploymentTime", "2026-07-25T10:30:00Z")));
    }

    private List<TimelineEvent> generateAiEvents() {
        return List.of(event(TimelineEventType.AI, "AI_PLATFORM", "AI_PLATFORM",
                "Model Retraining Complete", "Recommendation model v4.2 retrained with latest data",
                AlertSeverity.INFO, "ai-platform", Map.of("model", "recommendation-v4.2", "accuracy", 95.3)));
    }

    private List<TimelineEvent> generateWorkflowEvents() {
        return List.of(event(TimelineEventType.WORKFLOW, "WORKFLOW", "WORKFLOW",
                "Workflow Optimization", "Order fulfillment workflow optimized — 12% throughput improvement",
                AlertSeverity.INFO, "workflow-engine", Map.of("improvement", 12.0, "workflowType", "ORDER_FULFILLMENT")));
    }

    private List<TimelineEvent> generateTrainingEvents() {
        return List.of(event(TimelineEventType.TRAINING, "TRAINING", "TRAINING",
                "Course Published", "New advanced cultivation course published with 12 modules",
                AlertSeverity.INFO, "training-platform", Map.of("courseName", "Advanced Cultivation", "modules", 12)));
    }

    private List<TimelineEvent> generateInventoryEvents() {
        return List.of(event(TimelineEventType.INVENTORY, "INVENTORY", "INVENTORY",
                "Inventory Reorder Triggered", "Automated reorder for 5 low-stock SKUs",
                AlertSeverity.INFO, "inventory-system", Map.of("skus", 5, "orderValue", 45000)));
    }

    private List<TimelineEvent> generateMarketplaceEvents() {
        return List.of(event(TimelineEventType.MARKETPLACE, "MARKETPLACE", "MARKETPLACE",
                "Marketplace Feature Launch", "New product comparison feature launched",
                AlertSeverity.INFO, "marketplace-team", Map.of("feature", "product-comparison", "impact", "HIGH")));
    }

    private TimelineEvent event(TimelineEventType type, String category, String domain,
                                 String title, String desc, AlertSeverity severity,
                                 String source, Map<String, Object> metadata) {
        TimelineEvent e = TimelineEvent.create(type, category, domain, title, desc, severity, source, metadata);
        return repository.saveTimelineEvent(e);
    }
}
