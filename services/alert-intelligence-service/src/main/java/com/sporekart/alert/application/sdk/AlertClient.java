package com.sporekart.alert.application.sdk;

import com.sporekart.alert.application.service.AlertIntelligenceService;
import com.sporekart.alert.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AlertClient implements AlertSDK {

    private final AlertIntelligenceService service;

    public AlertClient(AlertIntelligenceService service) { this.service = service; }

    @Override public List<Alert> getAllAlerts() { return service.listAlerts(); }
    @Override public Optional<Alert> getAlertById(String id) { return service.getAlert(id); }
    @Override public List<Alert> getAlertsBySeverity(AlertSeverity severity) { return service.getAlertsBySeverity(severity); }
    @Override public List<Alert> getAlertsByCategory(AlertCategory category) { return service.getAlertsByCategory(category); }
    @Override public List<Alert> getAlertsByDomain(String domain) { return service.getAlertsByDomain(domain); }
    @Override public List<Alert> getAlertsByStatus(AlertStatus status) { return service.getAlertsByStatus(status); }
    @Override public Alert acknowledgeAlert(String id) { return service.acknowledgeAlert(id); }
    @Override public Alert resolveAlert(String id) { return service.resolveAlert(id); }
    @Override public List<Alert> generateAllAlerts() { return service.generateAllAlerts(); }
    @Override public List<Alert> generateAlertsForCategory(AlertCategory category) { return service.generateAlertsForCategory(category); }
    @Override public List<BusinessRisk> getAllRisks() { return service.listRisks(); }
    @Override public Optional<BusinessRisk> getRiskById(String id) { return service.getRisk(id); }
    @Override public List<BusinessRisk> getRisksBySeverity(RiskSeverity severity) { return service.getRisksBySeverity(severity); }
    @Override public List<BusinessRisk> getRisksByCategory(RiskCategory category) { return service.getRisksByCategory(category); }
    @Override public Map<String, Object> getRiskSummary() { return service.getRiskSummary(); }
    @Override public List<BusinessRisk> generateAllRisks() { return service.generateAllRisks(); }
    @Override public List<Anomaly> getAllAnomalies() { return service.listAnomalies(); }
    @Override public Optional<Anomaly> getAnomalyById(String id) { return service.getAnomaly(id); }
    @Override public List<Anomaly> getAnomaliesByDomain(String domain) { return service.getAnomaliesByDomain(domain); }
    @Override public List<Anomaly> getAnomaliesByType(AnomalyType type) { return service.getAnomaliesByType(type); }
    @Override public List<Anomaly> detectAllAnomalies() { return service.detectAllAnomalies(); }
    @Override public List<TimelineEvent> getAllTimelineEvents() { return service.listTimelineEvents(); }
    @Override public Optional<TimelineEvent> getTimelineEventById(String id) { return service.getTimelineEvent(id); }
    @Override public List<TimelineEvent> getTimelineEventsByType(TimelineEventType type) { return service.getTimelineEventsByType(type); }
    @Override public List<TimelineEvent> generateTimeline() { return service.generateTimeline(); }
    @Override public Map<String, Object> getAlertHistory() { return service.getAlertHistory(); }
    @Override public Map<String, Object> getCacheInfo() { return service.getCacheInfo(); }
    @Override public void clearCache() { service.clearCache(); }
    @Override public Map<String, Object> getTelemetry() { return service.getTelemetry(); }
    @Override public Map<String, Object> health() { return service.health(); }
}
