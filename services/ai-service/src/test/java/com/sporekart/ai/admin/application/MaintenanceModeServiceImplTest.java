package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.domain.MaintenanceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceModeServiceImplTest {

    private MaintenanceModeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MaintenanceModeServiceImpl();
    }

    @Test
    void testScheduleMaintenance() {
        var start = Instant.now().plusSeconds(3600);
        var end = Instant.now().plusSeconds(7200);
        var result = service.scheduleMaintenance("Release v2.0", "Scheduled release", start, end, UUID.randomUUID());

        assertNotNull(result);
        assertEquals("Release v2.0", result.title());
        assertEquals(MaintenanceStatus.SCHEDULED, result.status());
        assertEquals(start, result.scheduledStart());
        assertEquals(end, result.scheduledEnd());
    }

    @Test
    void testStartMaintenance() {
        var start = Instant.now().plusSeconds(3600);
        var end = Instant.now().plusSeconds(7200);
        var scheduled = service.scheduleMaintenance("Maintenance", "desc", start, end, UUID.randomUUID());

        var started = service.startMaintenance(scheduled.id());
        assertEquals(MaintenanceStatus.IN_PROGRESS, started.status());
        assertNotNull(started.actualStart());
    }

    @Test
    void testStartMaintenance_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> service.startMaintenance(UUID.randomUUID()));
    }

    @Test
    void testEndMaintenance() {
        var scheduled = service.scheduleMaintenance("Task", "desc",
                Instant.now().plusSeconds(3600), Instant.now().plusSeconds(7200), UUID.randomUUID());
        service.startMaintenance(scheduled.id());

        var ended = service.endMaintenance(scheduled.id());
        assertEquals(MaintenanceStatus.COMPLETED, ended.status());
        assertNotNull(ended.actualEnd());
    }

    @Test
    void testEndMaintenance_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> service.endMaintenance(UUID.randomUUID()));
    }

    @Test
    void testIsInMaintenance_FalseInitially() {
        assertFalse(service.isInMaintenance());
    }

    @Test
    void testIsInMaintenance_TrueWhenInProgress() {
        var scheduled = service.scheduleMaintenance("Test", "desc",
                Instant.now().plusSeconds(3600), Instant.now().plusSeconds(7200), UUID.randomUUID());
        service.startMaintenance(scheduled.id());
        assertTrue(service.isInMaintenance());
    }

    @Test
    void testIsInMaintenance_FalseAfterCompletion() {
        var scheduled = service.scheduleMaintenance("Test", "desc",
                Instant.now().plusSeconds(3600), Instant.now().plusSeconds(7200), UUID.randomUUID());
        service.startMaintenance(scheduled.id());
        service.endMaintenance(scheduled.id());
        assertFalse(service.isInMaintenance());
    }

    @Test
    void testGetMaintenanceStatus_ReturnsScheduledOrInProgress() {
        var scheduled = service.scheduleMaintenance("Active maintenance", "desc",
                Instant.now().plusSeconds(3600), Instant.now().plusSeconds(7200), UUID.randomUUID());
        var status = service.getMaintenanceStatus();
        assertNotNull(status);
        assertEquals(scheduled.id(), status.id());
    }

    @Test
    void testGetMaintenanceStatus_NullWhenNoneActive() {
        assertNull(service.getMaintenanceStatus());
    }
}
