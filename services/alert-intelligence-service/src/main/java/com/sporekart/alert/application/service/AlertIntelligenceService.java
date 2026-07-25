package com.sporekart.alert.application.service;

import com.sporekart.alert.config.AlertConfig;
import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import com.sporekart.alert.application.engine.*;
import com.sporekart.alert.infrastructure.cache.AlertCacheService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlertIntelligenceService {

    private final AlertRepositoryPort repository;
    private final AlertEngine alertEngine;
    private final RiskEngine riskEngine;
    private final AnomalyEngine anomalyEngine;
    private final TimelineEngine timelineEngine;
    private final AlertRegistryService registryService;
    private final AlertCacheService cacheService;
    private final AlertTelemetryService telemetry;
    private final AlertConfig config;

    public AlertIntelligenceService(AlertRepositoryPort repository, AlertEngine alertEngine,
                                     RiskEngine riskEngine, AnomalyEngine anomalyEngine,
                                     TimelineEngine timelineEngine, AlertRegistryService registryService,
                                     AlertCacheService cacheService, AlertTelemetryService telemetry,
                                     AlertConfig config) {
        this.repository = repository; this.alertEngine = alertEngine; this.riskEngine = riskEngine;
        this.anomalyEngine = anomalyEngine; this.timelineEngine = timelineEngine;
        this.registryService = registryService; this.cacheService = cacheService;
        this.telemetry = telemetry; this.config = config;
    }

    public List<Alert> listAlerts() { return registryService.getAllAlerts(); }
    public Optional<Alert> getAlert(String id) { return registryService.getAlertById(id); }
    public List<Alert> getAlertsBySeverity(AlertSeverity severity) { return registryService.getAlertsBySeverity(severity); }
    public List<Alert> getAlertsByCategory(AlertCategory category) { return registryService.getAlertsByCategory(category); }
    public List<Alert> getAlertsByDomain(String domain) { return registryService.getAlertsByDomain(domain); }
    public List<Alert> getAlertsByStatus(AlertStatus status) { return registryService.getAlertsByStatus(status); }

    public List<Alert> generateAllAlerts() {
        long start = System.currentTimeMillis();
        List<Alert> generated = alertEngine.generateAllAlerts();
        generated.forEach(a -> telemetry.recordAlertGenerated(a.domain(), 0));
        telemetry.recordAlertGenerated("ALL", System.currentTimeMillis() - start);
        return generated;
    }

    public List<Alert> generateAlertsForCategory(AlertCategory category) {
        long start = System.currentTimeMillis();
        List<Alert> generated = alertEngine.generateAlertsForCategory(category);
        generated.forEach(a -> telemetry.recordAlertGenerated(a.domain(), System.currentTimeMillis() - start));
        return generated;
    }

    public Alert acknowledgeAlert(String id) {
        return repository.findAlertById(id)
                .map(alert -> repository.saveAlert(alert.acknowledge()))
                .orElseThrow(() -> new NoSuchElementException("Alert not found: " + id));
    }

    public Alert resolveAlert(String id) {
        return repository.findAlertById(id)
                .map(alert -> repository.saveAlert(alert.resolve()))
                .orElseThrow(() -> new NoSuchElementException("Alert not found: " + id));
    }

    public List<BusinessRisk> listRisks() { return registryService.getAllRisks(); }
    public Optional<BusinessRisk> getRisk(String id) { return registryService.getRiskById(id); }
    public List<BusinessRisk> getRisksBySeverity(RiskSeverity severity) { return registryService.getRisksBySeverity(severity); }
    public List<BusinessRisk> getRisksByCategory(RiskCategory category) { return registryService.getRisksByCategory(category); }
    public Map<String, Object> getRiskSummary() { return riskEngine.getRiskSummary(); }

    public List<BusinessRisk> generateAllRisks() {
        telemetry.recordRiskEvaluation();
        return riskEngine.generateAllRisks();
    }

    public List<Anomaly> listAnomalies() { return registryService.getAllAnomalies(); }
    public Optional<Anomaly> getAnomaly(String id) { return registryService.getAnomalyById(id); }
    public List<Anomaly> getAnomaliesByDomain(String domain) { return registryService.getAnomaliesByDomain(domain); }
    public List<Anomaly> getAnomaliesByType(AnomalyType type) { return registryService.getAnomaliesByType(type); }

    public List<Anomaly> detectAllAnomalies() {
        telemetry.recordAnomalyDetection("ALL");
        return anomalyEngine.detectAllAnomalies();
    }

    public List<TimelineEvent> listTimelineEvents() { return registryService.getAllTimelineEvents(); }
    public Optional<TimelineEvent> getTimelineEvent(String id) { return registryService.getTimelineEventById(id); }
    public List<TimelineEvent> getTimelineEventsByType(TimelineEventType type) { return registryService.getTimelineEventsByType(type); }

    public List<TimelineEvent> generateTimeline() { return timelineEngine.generateTimeline(); }

    public Map<String, Object> getAlertHistory() {
        List<Alert> all = repository.findAllAlerts();
        long total = all.size();
        long open = all.stream().filter(a -> a.status() == AlertStatus.OPEN).count();
        long ack = all.stream().filter(a -> a.status() == AlertStatus.ACKNOWLEDGED).count();
        long resolved = all.stream().filter(a -> a.status() == AlertStatus.RESOLVED).count();
        long escalated = all.stream().filter(a -> a.status() == AlertStatus.ESCALATED).count();
        return Map.of("totalAlerts", total, "open", open, "acknowledged", ack,
                "resolved", resolved, "escalated", escalated);
    }

    public Map<String, Object> getCacheInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("enabled", cacheService.isEnabled());
        info.put("activeEntries", cacheService.getActiveEntryCount());
        info.put("totalEntries", cacheService.getAllEntries().size());
        info.put("entries", cacheService.getAllEntries().stream()
                .map(c -> Map.of("cacheKey", c.cacheKey(), "cacheType", c.cacheType(),
                        "expired", c.isExpired(), "hitCount", c.hitCount()))
                .toList());
        return info;
    }

    public void clearCache() { cacheService.clear(); }

    public Map<String, Object> getTelemetry() { return telemetry.getMetrics(); }

    public Map<String, Object> health() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("status", "UP");
        info.put("service", "alert-intelligence-service");
        info.put("version", "0.1.0-SNAPSHOT");
        info.put("alertEngine", config.getEngine().isEnabled() ? "enabled" : "disabled");
        info.put("riskEngine", config.getRisk().isEnabled() ? "enabled" : "disabled");
        info.put("anomalyEngine", config.getAnomaly().isEnabled() ? "enabled" : "disabled");
        info.put("timelineEngine", config.getTimeline().isEnabled() ? "enabled" : "disabled");
        info.put("cache", config.getCache().isEnabled() ? "enabled" : "disabled");
        info.put("totalAlerts", repository.findAllAlerts().size());
        info.put("totalRisks", repository.findAllRisks().size());
        info.put("totalAnomalies", repository.findAllAnomalies().size());
        info.put("totalTimelineEvents", repository.findAllTimelineEvents().size());
        return info;
    }
}
