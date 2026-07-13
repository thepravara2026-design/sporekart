package com.sporekart.ai.admin.api;

import com.sporekart.ai.admin.domain.MaintenanceWindow;

import java.time.Instant;
import java.util.UUID;

public interface MaintenanceModeService {
    MaintenanceWindow scheduleMaintenance(String title, String description, Instant scheduledStart, Instant scheduledEnd, UUID scheduledBy);
    MaintenanceWindow startMaintenance(UUID id);
    MaintenanceWindow endMaintenance(UUID id);
    MaintenanceWindow getMaintenanceStatus();
    boolean isInMaintenance();
}
