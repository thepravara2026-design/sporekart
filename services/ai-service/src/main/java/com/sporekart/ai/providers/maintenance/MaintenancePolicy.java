package com.sporekart.ai.providers.maintenance;

import java.time.Duration;

public interface MaintenancePolicy {
    Duration getMaxMaintenanceDuration(String providerId);
    boolean isScheduledMaintenanceAllowed(String providerId);
    boolean isEmergencyMaintenanceAllowed(String providerId);
    boolean requiresApproval(String providerId);
    int getMinNoticePeriodMinutes(String providerId);
}
