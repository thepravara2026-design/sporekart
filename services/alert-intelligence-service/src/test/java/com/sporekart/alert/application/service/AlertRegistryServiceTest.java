package com.sporekart.alert.application.service;

import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertRegistryServiceTest {

    @Mock private AlertRepositoryPort repository;
    private AlertRegistryService service;

    @BeforeEach
    void setUp() { service = new AlertRegistryService(repository); }

    @Test
    void getAllAlertsShouldDelegateToRepository() {
        when(repository.findAllAlerts()).thenReturn(List.of());
        assertTrue(service.getAllAlerts().isEmpty());
        verify(repository).findAllAlerts();
    }

    @Test
    void getAlertByIdShouldDelegateToRepository() {
        var alert = Alert.create("Test", "Desc",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inv", "Impact", "Resolution", java.util.Map.of());
        when(repository.findAlertById(alert.id())).thenReturn(Optional.of(alert));
        assertTrue(service.getAlertById(alert.id()).isPresent());
        verify(repository).findAlertById(alert.id());
    }

    @Test
    void registerAlertShouldDelegateToRepository() {
        var alert = Alert.create("Test", "Desc",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inv", "Impact", "Resolution", java.util.Map.of());
        when(repository.saveAlert(alert)).thenReturn(alert);
        assertEquals(alert, service.registerAlert(alert));
        verify(repository).saveAlert(alert);
    }
}
