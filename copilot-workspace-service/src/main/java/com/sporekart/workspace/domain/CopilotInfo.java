package com.sporekart.workspace.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record CopilotInfo(
    String copilotId,
    String name,
    String type,
    String version,
    String baseUrl,
    boolean enabled,
    String status,
    List<String> capabilities,
    List<String> personas,
    Map<String, Object> metadata,
    int activeSessions,
    double avgLatencyMs,
    OffsetDateTime lastHealthCheck
) {
    public static final String TYPE_CUSTOMER = "CUSTOMER";
    public static final String TYPE_ADMIN = "ADMIN";
    public static final String TYPE_TRAINER = "TRAINER";
    public static final String TYPE_GROWER = "GROWER";
    public static final String TYPE_BI = "BI";
    public static final String TYPE_MARKETING = "MARKETING";
    public static final String TYPE_OPERATIONS = "OPERATIONS";
    public static final String TYPE_EXECUTIVE = "EXECUTIVE";
    public static final String TYPE_DEVELOPER = "DEVELOPER";

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_DEGRADED = "DEGRADED";
    public static final String STATUS_ERROR = "ERROR";
}
