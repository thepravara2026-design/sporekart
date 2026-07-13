package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.MaintenanceModeService;
import com.sporekart.ai.admin.domain.MaintenanceStatus;
import com.sporekart.ai.admin.domain.MaintenanceWindow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class MaintenanceModeServiceImpl implements MaintenanceModeService {

    private final ConcurrentHashMap<UUID, MaintenanceWindow> windows = new ConcurrentHashMap<>();

    @Override
    public MaintenanceWindow scheduleMaintenance(String title, String description, Instant scheduledStart, Instant scheduledEnd, UUID scheduledBy) {
        var window = new MaintenanceWindow(
                UUID.randomUUID(),
                title,
                description,
                MaintenanceStatus.SCHEDULED,
                scheduledStart,
                scheduledEnd,
                null,
                null,
                scheduledBy
        );
        windows.put(window.id(), window);
        log.info("Maintenance '{}' scheduled from {} to {}", title, scheduledStart, scheduledEnd);
        return window;
    }

    @Override
    public MaintenanceWindow startMaintenance(UUID id) {
        var window = windows.get(id);
        if (window == null) {
            throw new IllegalArgumentException("No maintenance window found: " + id);
        }
        var updated = new MaintenanceWindow(
                window.id(), window.title(), window.description(),
                MaintenanceStatus.IN_PROGRESS,
                window.scheduledStart(), window.scheduledEnd(),
                Instant.now(), null, window.scheduledBy()
        );
        windows.put(id, updated);
        log.info("Maintenance '{}' started", window.title());
        return updated;
    }

    @Override
    public MaintenanceWindow endMaintenance(UUID id) {
        var window = windows.get(id);
        if (window == null) {
            throw new IllegalArgumentException("No maintenance window found: " + id);
        }
        var updated = new MaintenanceWindow(
                window.id(), window.title(), window.description(),
                MaintenanceStatus.COMPLETED,
                window.scheduledStart(), window.scheduledEnd(),
                window.actualStart(), Instant.now(), window.scheduledBy()
        );
        windows.put(id, updated);
        log.info("Maintenance '{}' completed", window.title());
        return updated;
    }

    @Override
    public MaintenanceWindow getMaintenanceStatus() {
        return windows.values().stream()
                .filter(w -> w.status() == MaintenanceStatus.SCHEDULED || w.status() == MaintenanceStatus.IN_PROGRESS)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isInMaintenance() {
        return windows.values().stream()
                .anyMatch(w -> w.status() == MaintenanceStatus.IN_PROGRESS);
    }
}
