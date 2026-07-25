package com.sporekart.alert.application.service;

import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertRegistryService {

    private final AlertRepositoryPort repository;

    public AlertRegistryService(AlertRepositoryPort repository) { this.repository = repository; }

    public List<Alert> getAllAlerts() { return repository.findAllAlerts(); }
    public Optional<Alert> getAlertById(String id) { return repository.findAlertById(id); }
    public List<Alert> getAlertsBySeverity(AlertSeverity severity) { return repository.findAlertsBySeverity(severity); }
    public List<Alert> getAlertsByCategory(AlertCategory category) { return repository.findAlertsByCategory(category); }
    public List<Alert> getAlertsByDomain(String domain) { return repository.findAlertsByDomain(domain); }
    public List<Alert> getAlertsByStatus(AlertStatus status) { return repository.findAlertsByStatus(status); }
    public Alert registerAlert(Alert alert) { return repository.saveAlert(alert); }

    public List<BusinessRisk> getAllRisks() { return repository.findAllRisks(); }
    public Optional<BusinessRisk> getRiskById(String id) { return repository.findRiskById(id); }
    public List<BusinessRisk> getRisksBySeverity(RiskSeverity severity) { return repository.findRisksBySeverity(severity); }
    public List<BusinessRisk> getRisksByCategory(RiskCategory category) { return repository.findRisksByCategory(category); }
    public BusinessRisk registerRisk(BusinessRisk risk) { return repository.saveRisk(risk); }

    public List<Anomaly> getAllAnomalies() { return repository.findAllAnomalies(); }
    public Optional<Anomaly> getAnomalyById(String id) { return repository.findAnomalyById(id); }
    public List<Anomaly> getAnomaliesByDomain(String domain) { return repository.findAnomaliesByDomain(domain); }
    public List<Anomaly> getAnomaliesByType(AnomalyType type) { return repository.findAnomaliesByType(type); }

    public List<TimelineEvent> getAllTimelineEvents() { return repository.findAllTimelineEvents(); }
    public Optional<TimelineEvent> getTimelineEventById(String id) { return repository.findTimelineEventById(id); }
    public List<TimelineEvent> getTimelineEventsByType(TimelineEventType type) { return repository.findTimelineEventsByType(type); }
    public TimelineEvent recordEvent(TimelineEvent event) { return repository.saveTimelineEvent(event); }
}
