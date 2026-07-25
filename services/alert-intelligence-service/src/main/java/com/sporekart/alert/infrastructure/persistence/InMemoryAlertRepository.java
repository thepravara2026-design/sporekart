package com.sporekart.alert.infrastructure.persistence;

import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryAlertRepository implements AlertRepositoryPort {

    private final Map<String, Alert> alerts = new ConcurrentHashMap<>();
    private final Map<String, BusinessRisk> risks = new ConcurrentHashMap<>();
    private final Map<String, Anomaly> anomalies = new ConcurrentHashMap<>();
    private final Map<String, TimelineEvent> timeline = new ConcurrentHashMap<>();
    private final Map<String, AlertCache> cache = new ConcurrentHashMap<>();

    @Override public List<Alert> findAllAlerts() { return List.copyOf(alerts.values()); }
    @Override public Optional<Alert> findAlertById(String id) { return Optional.ofNullable(alerts.get(id)); }
    @Override public List<Alert> findAlertsBySeverity(AlertSeverity s) {
        return alerts.values().stream().filter(a -> a.severity() == s).collect(Collectors.toUnmodifiableList()); }
    @Override public List<Alert> findAlertsByCategory(AlertCategory c) {
        return alerts.values().stream().filter(a -> a.category() == c).collect(Collectors.toUnmodifiableList()); }
    @Override public List<Alert> findAlertsByDomain(String d) {
        return alerts.values().stream().filter(a -> a.domain().equals(d)).collect(Collectors.toUnmodifiableList()); }
    @Override public List<Alert> findAlertsByStatus(AlertStatus s) {
        return alerts.values().stream().filter(a -> a.status() == s).collect(Collectors.toUnmodifiableList()); }
    @Override public synchronized Alert saveAlert(Alert a) { alerts.put(a.id(), a); return a; }

    @Override public List<BusinessRisk> findAllRisks() { return List.copyOf(risks.values()); }
    @Override public Optional<BusinessRisk> findRiskById(String id) { return Optional.ofNullable(risks.get(id)); }
    @Override public List<BusinessRisk> findRisksBySeverity(RiskSeverity s) {
        return risks.values().stream().filter(r -> r.severity() == s).collect(Collectors.toUnmodifiableList()); }
    @Override public List<BusinessRisk> findRisksByCategory(RiskCategory c) {
        return risks.values().stream().filter(r -> r.category() == c).collect(Collectors.toUnmodifiableList()); }
    @Override public synchronized BusinessRisk saveRisk(BusinessRisk r) { risks.put(r.id(), r); return r; }

    @Override public List<Anomaly> findAllAnomalies() { return List.copyOf(anomalies.values()); }
    @Override public Optional<Anomaly> findAnomalyById(String id) { return Optional.ofNullable(anomalies.get(id)); }
    @Override public List<Anomaly> findAnomaliesByDomain(String d) {
        return anomalies.values().stream().filter(a -> a.domain().equals(d)).collect(Collectors.toUnmodifiableList()); }
    @Override public List<Anomaly> findAnomaliesByType(AnomalyType t) {
        return anomalies.values().stream().filter(a -> a.type() == t).collect(Collectors.toUnmodifiableList()); }
    @Override public synchronized Anomaly saveAnomaly(Anomaly a) { anomalies.put(a.id(), a); return a; }

    @Override public List<TimelineEvent> findAllTimelineEvents() { return List.copyOf(timeline.values()); }
    @Override public Optional<TimelineEvent> findTimelineEventById(String id) { return Optional.ofNullable(timeline.get(id)); }
    @Override public List<TimelineEvent> findTimelineEventsByType(TimelineEventType t) {
        return timeline.values().stream().filter(e -> e.eventType() == t).collect(Collectors.toUnmodifiableList()); }
    @Override public synchronized TimelineEvent saveTimelineEvent(TimelineEvent e) { timeline.put(e.id(), e); return e; }

    @Override public List<AlertCache> findAllCacheEntries() { return List.copyOf(cache.values()); }
    @Override public Optional<AlertCache> findCacheByKey(String key) { return Optional.ofNullable(cache.get(key)); }
    @Override public synchronized AlertCache saveCache(AlertCache c) { cache.put(c.cacheKey(), c); return c; }
    @Override public synchronized void deleteCache(String key) { cache.remove(key); }
    @Override public synchronized void clearCache() { cache.clear(); }
}
