package com.sporekart.alert.infrastructure.persistence;

import com.sporekart.alert.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAlertRepositoryTest {

    private InMemoryAlertRepository repository;

    @BeforeEach
    void setUp() { repository = new InMemoryAlertRepository(); }

    private Alert createAlert(String title, AlertCategory cat, AlertSeverity sev, AlertStatus status) {
        return Alert.create(title, "Desc", cat, sev, AlertPriority.P3, "inventory", "Impact", "Resolution", Map.of());
    }

    private BusinessRisk createRisk(RiskCategory cat, RiskSeverity sev, String domain) {
        return BusinessRisk.create("Risk", "Desc", cat, sev, domain, "Impact", 0.5, 0.6, "Mitigate");
    }

    private Anomaly createAnomaly(AnomalyType type, String domain) {
        return Anomaly.create(type, domain, AlertSeverity.HIGH, "Anomaly detected", 100, 200, 100.0, 0.85);
    }

    private TimelineEvent createTimelineEvent(TimelineEventType type, String domain) {
        return TimelineEvent.create(type, domain, domain, "Event", "Event description",
                AlertSeverity.INFO, "source", Map.of());
    }

    @Test
    void saveAndFindAlert() {
        var alert = createAlert("Test1", AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertStatus.OPEN);
        repository.saveAlert(alert);
        assertTrue(repository.findAlertById(alert.id()).isPresent());
        assertEquals(1, repository.findAllAlerts().size());
    }

    @Test
    void findAlertsBySeverity() {
        repository.saveAlert(createAlert("A1", AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertStatus.OPEN));
        repository.saveAlert(createAlert("A2", AlertCategory.SECURITY, AlertSeverity.CRITICAL, AlertStatus.OPEN));
        repository.saveAlert(createAlert("A3", AlertCategory.PERFORMANCE, AlertSeverity.MEDIUM, AlertStatus.OPEN));
        assertEquals(2, repository.findAlertsBySeverity(AlertSeverity.MEDIUM).size());
        assertEquals(1, repository.findAlertsBySeverity(AlertSeverity.CRITICAL).size());
    }

    @Test
    void findAlertsByCategory() {
        repository.saveAlert(createAlert("A1", AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertStatus.OPEN));
        repository.saveAlert(createAlert("A2", AlertCategory.SECURITY, AlertSeverity.CRITICAL, AlertStatus.OPEN));
        assertEquals(1, repository.findAlertsByCategory(AlertCategory.SECURITY).size());
    }

    @Test
    void findAlertsByStatus() {
        repository.saveAlert(createAlert("A1", AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertStatus.OPEN));
        repository.saveAlert(createAlert("A2", AlertCategory.SECURITY, AlertSeverity.CRITICAL, AlertStatus.OPEN).acknowledge());
        assertEquals(1, repository.findAlertsByStatus(AlertStatus.ACKNOWLEDGED).size());
    }

    @Test
    void findAlertsByDomain() {
        var alert = Alert.create("Test", "Desc",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inventory", "Impact", "Resolution", Map.of());
        repository.saveAlert(alert);
        assertEquals(1, repository.findAlertsByDomain("inventory").size());
    }

    @Test
    void saveAndFindRisk() {
        var risk = createRisk(RiskCategory.OPERATIONAL, RiskSeverity.HIGH, "operations");
        repository.saveRisk(risk);
        assertTrue(repository.findRiskById(risk.id()).isPresent());
        assertEquals(1, repository.findAllRisks().size());
    }

    @Test
    void findRisksBySeverity() {
        repository.saveRisk(createRisk(RiskCategory.OPERATIONAL, RiskSeverity.HIGH, "ops"));
        repository.saveRisk(createRisk(RiskCategory.REVENUE, RiskSeverity.LOW, "finance"));
        assertEquals(1, repository.findRisksBySeverity(RiskSeverity.HIGH).size());
    }

    @Test
    void findRisksByCategory() {
        repository.saveRisk(createRisk(RiskCategory.OPERATIONAL, RiskSeverity.HIGH, "ops"));
        repository.saveRisk(createRisk(RiskCategory.REVENUE, RiskSeverity.MEDIUM, "finance"));
        assertEquals(1, repository.findRisksByCategory(RiskCategory.REVENUE).size());
    }

    @Test
    void saveAndFindAnomaly() {
        var anomaly = createAnomaly(AnomalyType.UNEXPECTED_GROWTH, "operations");
        repository.saveAnomaly(anomaly);
        assertTrue(repository.findAnomalyById(anomaly.id()).isPresent());
        assertEquals(1, repository.findAllAnomalies().size());
    }

    @Test
    void findAnomaliesByType() {
        repository.saveAnomaly(createAnomaly(AnomalyType.UNEXPECTED_GROWTH, "ops"));
        repository.saveAnomaly(createAnomaly(AnomalyType.OUTLIER, "sec"));
        assertEquals(1, repository.findAnomaliesByType(AnomalyType.OUTLIER).size());
    }

    @Test
    void findAnomaliesByDomain() {
        repository.saveAnomaly(createAnomaly(AnomalyType.UNEXPECTED_GROWTH, "ops"));
        assertEquals(1, repository.findAnomaliesByDomain("ops").size());
    }

    @Test
    void saveAndFindTimelineEvent() {
        var event = createTimelineEvent(TimelineEventType.ALERT, "platform");
        repository.saveTimelineEvent(event);
        assertTrue(repository.findTimelineEventById(event.id()).isPresent());
        assertEquals(1, repository.findAllTimelineEvents().size());
    }

    @Test
    void cacheOperations() {
        assertTrue(repository.findCacheByKey("key").isEmpty());
        var cache = AlertCache.create("key", "TYPE", "value", 60);
        repository.saveCache(cache);
        assertTrue(repository.findCacheByKey("key").isPresent());
        assertEquals("value", repository.findCacheByKey("key").get().cachedData());
        repository.deleteCache("key");
        assertTrue(repository.findCacheByKey("key").isEmpty());
        repository.saveCache(AlertCache.create("a", "TYPE", "1", 60));
        repository.saveCache(AlertCache.create("b", "TYPE", "2", 60));
        repository.clearCache();
        assertTrue(repository.findCacheByKey("a").isEmpty());
        assertTrue(repository.findCacheByKey("b").isEmpty());
    }
}
