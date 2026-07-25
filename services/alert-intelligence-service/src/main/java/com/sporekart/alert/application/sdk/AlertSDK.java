package com.sporekart.alert.application.sdk;

import com.sporekart.alert.domain.model.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AlertSDK {
    List<Alert> getAllAlerts();
    Optional<Alert> getAlertById(String id);
    List<Alert> getAlertsBySeverity(AlertSeverity severity);
    List<Alert> getAlertsByCategory(AlertCategory category);
    List<Alert> getAlertsByDomain(String domain);
    List<Alert> getAlertsByStatus(AlertStatus status);
    Alert acknowledgeAlert(String id);
    Alert resolveAlert(String id);
    List<Alert> generateAllAlerts();
    List<Alert> generateAlertsForCategory(AlertCategory category);
    List<BusinessRisk> getAllRisks();
    Optional<BusinessRisk> getRiskById(String id);
    List<BusinessRisk> getRisksBySeverity(RiskSeverity severity);
    List<BusinessRisk> getRisksByCategory(RiskCategory category);
    Map<String, Object> getRiskSummary();
    List<BusinessRisk> generateAllRisks();
    List<Anomaly> getAllAnomalies();
    Optional<Anomaly> getAnomalyById(String id);
    List<Anomaly> getAnomaliesByDomain(String domain);
    List<Anomaly> getAnomaliesByType(AnomalyType type);
    List<Anomaly> detectAllAnomalies();
    List<TimelineEvent> getAllTimelineEvents();
    Optional<TimelineEvent> getTimelineEventById(String id);
    List<TimelineEvent> getTimelineEventsByType(TimelineEventType type);
    List<TimelineEvent> generateTimeline();
    Map<String, Object> getAlertHistory();
    Map<String, Object> getCacheInfo();
    void clearCache();
    Map<String, Object> getTelemetry();
    Map<String, Object> health();
}
