package com.sporekart.customer.copilot.dto;

import java.util.Map;

public record ContextResponse(
    String sessionId,
    String pageContext,
    Map<String, Object> userInfo,
    Map<String, Object> cartInfo,
    Map<String, Object> recentActivity
) {
    public ContextResponse {
        if (sessionId == null) {
            sessionId = "";
        }
        if (pageContext == null) {
            pageContext = "";
        }
        if (userInfo == null) {
            userInfo = Map.of();
        }
        if (cartInfo == null) {
            cartInfo = Map.of();
        }
        if (recentActivity == null) {
            recentActivity = Map.of();
        }
    }
}
