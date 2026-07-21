package com.sporekart.ai.providers.maintenance;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MaintenanceManager {
    void startMaintenance(String providerId, String reason);
    void startMaintenance(String providerId, String reason, Instant scheduledEnd);
    void endMaintenance(String providerId);
    boolean isInMaintenance(String providerId);
    Optional<MaintenanceWindow> getCurrentWindow(String providerId);
    List<MaintenanceWindow> getMaintenanceHistory(String providerId);
    List<String> getProvidersInMaintenance();
}
