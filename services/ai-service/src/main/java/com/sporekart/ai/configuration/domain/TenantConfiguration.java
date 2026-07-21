package com.sporekart.ai.configuration.domain;

import java.time.Instant;

public record TenantConfiguration(
    String tenantId,
    String tenantName,
    String tier,
    boolean enabled,
    Instant createdAt,
    Instant updatedAt,
    AIConfiguration ai,
    GatewayConfiguration gateway,
    RuntimeConfiguration runtime,
    SecurityConfiguration security,
    MonitoringConfiguration monitoring
) {
    public static TenantConfiguration defaults(String tenantId, String tenantName) {
        return new TenantConfiguration(
            tenantId, tenantName, "standard", true,
            Instant.now(), Instant.now(),
            AIConfiguration.defaults(),
            GatewayConfiguration.defaults(),
            RuntimeConfiguration.defaults(),
            SecurityConfiguration.defaults(),
            MonitoringConfiguration.defaults()
        );
    }
}
