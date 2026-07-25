package com.sporekart.alert.application.sdk;

import com.sporekart.alert.application.engine.AlertEngine;
import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertRuntimeTest {

    @Mock private AlertEngine alertEngine;
    @Mock private AlertRepositoryPort repository;
    private AlertRuntime runtime;

    @BeforeEach
    void setUp() { runtime = new AlertRuntime(alertEngine, repository); }

    @Test
    void generateAlertsForCategoryShouldDelegate() {
        var alert = Alert.create("Test", "Desc",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inv", "Impact", "Resolution", Map.of());
        when(alertEngine.generateAlertsForCategory(AlertCategory.OPERATIONAL)).thenReturn(List.of(alert));
        when(repository.saveAlert(any())).thenReturn(alert);
        var result = runtime.generateAlertsForCategory(AlertCategory.OPERATIONAL);
        assertEquals(1, result.size());
        verify(repository).saveAlert(any());
    }

    @Test
    void acknowledgeAlertShouldDelegate() {
        var alert = Alert.create("Test", "Desc",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inv", "Impact", "Resolution", Map.of());
        when(repository.findAlertById(alert.id())).thenReturn(Optional.of(alert));
        when(repository.saveAlert(any())).thenAnswer(i -> i.getArgument(0));
        var result = runtime.acknowledgeAlert(alert.id());
        assertEquals(AlertStatus.ACKNOWLEDGED, result.status());
    }

    @Test
    void resolveAlertShouldDelegate() {
        var alert = Alert.create("Test", "Desc",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inv", "Impact", "Resolution", Map.of());
        when(repository.findAlertById(alert.id())).thenReturn(Optional.of(alert));
        when(repository.saveAlert(any())).thenAnswer(i -> i.getArgument(0));
        var result = runtime.resolveAlert(alert.id());
        assertEquals(AlertStatus.RESOLVED, result.status());
    }
}
