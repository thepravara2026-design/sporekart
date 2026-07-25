package com.sporekart.alert.domain.model;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class AlertTest {

    @Test
    void createShouldReturnAlertWithGivenValues() {
        var alert = Alert.create("Test Alert", "Description",
                AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P3,
                "inventory", "Impact", "Resolution", Map.of("key", "value"));
        assertNotNull(alert.id());
        assertEquals("Test Alert", alert.title());
        assertEquals(AlertCategory.OPERATIONAL, alert.category());
        assertEquals(AlertSeverity.MEDIUM, alert.severity());
        assertEquals(AlertPriority.P3, alert.priority());
        assertEquals(AlertStatus.OPEN, alert.status());
    }

}
