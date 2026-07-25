package com.sporekart.alert.application.engine;

import com.sporekart.alert.config.AlertConfig;
import com.sporekart.alert.domain.model.AlertCategory;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertEngineTest {

    @Mock private AlertRepositoryPort repository;
    private AlertEngine engine;

    @BeforeEach
    void setUp() {
        when(repository.saveAlert(any())).thenAnswer(i -> i.getArgument(0));
        var config = new AlertConfig();
        var engineConfig = new AlertConfig.EngineConfig();
        engineConfig.setEnabled(true);
        config.setEngine(engineConfig);
        engine = new AlertEngine(config, repository);
    }

    @Test
    void generateAllAlertsShouldReturnAlertsForAllCategories() {
        var alerts = engine.generateAllAlerts();
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());
        assertTrue(alerts.size() >= 9);
    }

    @Test
    void generateAlertsForCategoryShouldReturnAlertsForGivenCategory() {
        var alerts = engine.generateAlertsForCategory(AlertCategory.SECURITY);
        assertNotNull(alerts);
        alerts.forEach(a -> assertEquals(AlertCategory.SECURITY, a.category()));
    }


}
