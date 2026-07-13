package com.sporekart.ai.governance.interfaces.rest.dto;

import java.net.URI;
import java.util.Map;

public record GovernanceErrorDto(
    String type,
    String title,
    int status,
    String detail,
    URI instance,
    Map<String, Object> extensions
) {
    public static GovernanceErrorDto of(int status, String title, String detail) {
        return new GovernanceErrorDto(
            "about:blank",
            title,
            status,
            detail,
            null,
            Map.of()
        );
    }
}
