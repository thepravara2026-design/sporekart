package com.sporekart.alert.application.service;

import com.sporekart.alert.application.engine.*;
import com.sporekart.alert.config.AlertConfig;
import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import com.sporekart.alert.infrastructure.cache.AlertCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertIntelligenceServiceTest {

    @Mock private AlertRepositoryPort repository;
    @Mock private AlertEngine alertEngine;
    @Mock private RiskEngine riskEngine;
    @Mock private AnomalyEngine anomalyEngine;
    @Mock private TimelineEngine timelineEngine;
    @Mock private AlertRegistryService registryService;
    @Mock private AlertCacheService cacheService;
    @Mock private AlertTelemetryService telemetry;
    private AlertConfig config;
    private AlertIntelligenceService service;

    @BeforeEach
    void setUp() {
        config = new AlertConfig();
        config.setEngine(new AlertConfig.EngineConfig());
        config.setRisk(new AlertConfig.RiskConfig());
        config.setAnomaly(new AlertConfig.AnomalyConfig());
        config.setTimeline(new AlertConfig.TimelineConfig());
        config.setCache(new AlertConfig.CacheConfig());
        config.getEngine().setEnabled(true);
        config.getRisk().setEnabled(true);
        config.getAnomaly().setEnabled(true);
        config.getTimeline().setEnabled(true);
        config.getCache().setEnabled(true);
        service = new AlertIntelligenceService(repository, alertEngine, riskEngine,
                anomalyEngine, timelineEngine, registryService, cacheService, telemetry, config);
    }

    @Test
    void listAlertsShouldDelegateToRegistry() {
        when(registryService.getAllAlerts()).thenReturn(List.of());
        assertTrue(service.listAlerts().isEmpty());
    }

    @Test
    void generateAllAlertsShouldDelegateToEngine() {
        var mockAlert = Alert.create("Test", "Desc",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inv", "Impact", "Resolution", Map.of());
        when(alertEngine.generateAllAlerts()).thenReturn(List.of(mockAlert));
        var result = service.generateAllAlerts();
        assertEquals(1, result.size());
        verify(telemetry, times(2)).recordAlertGenerated(anyString(), anyLong());
    }

    @Test
    void acknowledgeAlertShouldUpdateStatus() {
        var alert = Alert.create("Test", "Desc",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inv", "Impact", "Resolution", Map.of());
        when(repository.findAlertById(alert.id())).thenReturn(Optional.of(alert));
        when(repository.saveAlert(any())).thenAnswer(i -> i.getArgument(0));
        var ack = service.acknowledgeAlert(alert.id());
        assertEquals(AlertStatus.ACKNOWLEDGED, ack.status());
    }

    @Test
    void resolveAlertShouldUpdateStatus() {
        var alert = Alert.create("Test", "Desc",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inv", "Impact", "Resolution", Map.of());
        when(repository.findAlertById(alert.id())).thenReturn(Optional.of(alert));
        when(repository.saveAlert(any())).thenAnswer(i -> i.getArgument(0));
        var resolved = service.resolveAlert(alert.id());
        assertEquals(AlertStatus.RESOLVED, resolved.status());
    }

    @Test
    void acknowledgeAlertShouldThrowWhenNotFound() {
        when(repository.findAlertById("missing")).thenReturn(Optional.empty());
        assertThrows(java.util.NoSuchElementException.class, () -> service.acknowledgeAlert("missing"));
    }

    @Test
    void healthShouldReturnStatusMap() {
        when(repository.findAllAlerts()).thenReturn(List.of());
        when(repository.findAllRisks()).thenReturn(List.of());
        when(repository.findAllAnomalies()).thenReturn(List.of());
        when(repository.findAllTimelineEvents()).thenReturn(List.of());
        var health = service.health();
        assertEquals("UP", health.get("status"));
        assertEquals("alert-intelligence-service", health.get("service"));
    }

    @Test
    void getAlertHistoryShouldReturnCounts() {
        var open = Alert.create("Open", "", AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3, "dom", "Impact", "Resolution", Map.of());
        var ack = Alert.create("Ack", "", AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3, "dom", "Impact", "Resolution", Map.of()).acknowledge();
        var res = Alert.create("Res", "", AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3, "dom", "Impact", "Resolution", Map.of()).resolve();
        when(repository.findAllAlerts()).thenReturn(List.of(open, ack, res));
        var history = service.getAlertHistory();
        assertEquals(3L, history.get("totalAlerts"));
        assertEquals(1L, history.get("open"));
        assertEquals(1L, history.get("acknowledged"));
        assertEquals(1L, history.get("resolved"));
    }
}
