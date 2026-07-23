package com.sporekart.admin.engine;

import com.sporekart.admin.domain.OperationalAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlertEngineTest {

    private AlertEngine engine;

    @BeforeEach
    void setUp() {
        engine = new AlertEngine();
    }

    @Test
    void getActiveAlertsShouldReturnAlerts() {
        List<OperationalAlert> alerts = engine.getActiveAlerts();

        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());
    }

    @Test
    void checkInventoryAlertsShouldIdentifyLowStock() {
        List<OperationalAlert> alerts = engine.checkInventoryAlerts();

        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());
        boolean hasLowStockAlert = alerts.stream()
            .anyMatch(a -> a.title().toLowerCase().contains("stock") || a.title().toLowerCase().contains("inventory"));
        assertTrue(hasLowStockAlert);
    }

    @Test
    void checkRevenueAlertsShouldDetectDrops() {
        List<OperationalAlert> alerts = engine.checkRevenueAlerts();

        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());
        boolean hasRevenueAlert = alerts.stream()
            .anyMatch(a -> a.metric().toLowerCase().contains("revenue"));
        assertTrue(hasRevenueAlert);
    }

    @Test
    void alertsHaveProperSeverityLevels() {
        List<OperationalAlert> alerts = engine.getActiveAlerts();

        for (OperationalAlert alert : alerts) {
            assertNotNull(alert.type());
            assertTrue(alert.type() == OperationalAlert.AlertType.INFO ||
                       alert.type() == OperationalAlert.AlertType.WARNING ||
                       alert.type() == OperationalAlert.AlertType.CRITICAL);
        }
    }

    @Test
    void eachAlertHasSuggestedAction() {
        List<OperationalAlert> alerts = engine.getActiveAlerts();

        for (OperationalAlert alert : alerts) {
            assertNotNull(alert.suggestedAction());
            assertFalse(alert.suggestedAction().isBlank());
        }
    }
}
