package com.sporekart.alert.domain.repository;

import com.sporekart.alert.domain.model.*;

import java.util.List;
import java.util.Optional;

public interface AlertRepositoryPort {
    List<Alert> findAllAlerts();
    Optional<Alert> findAlertById(String id);
    List<Alert> findAlertsBySeverity(AlertSeverity severity);
    List<Alert> findAlertsByCategory(AlertCategory category);
    List<Alert> findAlertsByDomain(String domain);
    List<Alert> findAlertsByStatus(AlertStatus status);
    Alert saveAlert(Alert alert);

    List<BusinessRisk> findAllRisks();
    Optional<BusinessRisk> findRiskById(String id);
    List<BusinessRisk> findRisksBySeverity(RiskSeverity severity);
    List<BusinessRisk> findRisksByCategory(RiskCategory category);
    BusinessRisk saveRisk(BusinessRisk risk);

    List<Anomaly> findAllAnomalies();
    Optional<Anomaly> findAnomalyById(String id);
    List<Anomaly> findAnomaliesByDomain(String domain);
    List<Anomaly> findAnomaliesByType(AnomalyType type);
    Anomaly saveAnomaly(Anomaly anomaly);

    List<TimelineEvent> findAllTimelineEvents();
    Optional<TimelineEvent> findTimelineEventById(String id);
    List<TimelineEvent> findTimelineEventsByType(TimelineEventType type);
    TimelineEvent saveTimelineEvent(TimelineEvent event);

    List<AlertCache> findAllCacheEntries();
    Optional<AlertCache> findCacheByKey(String cacheKey);
    AlertCache saveCache(AlertCache cache);
    void deleteCache(String cacheKey);
    void clearCache();
}
