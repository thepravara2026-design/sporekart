package com.sporekart.admin.service;

import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdminContextService {

    private static final Logger log = LoggerFactory.getLogger(AdminContextService.class);

    private final Map<SessionId, SessionContext> sessionContexts = new ConcurrentHashMap<>();

    public UserContext buildUserContext(String userId, String userName, String email) {
        return UserContext.builder()
            .userId(userId)
            .userName(userName)
            .email(email)
            .roles(List.of("ADMIN", "SUPER_ADMIN"))
            .workspaceId("admin-workspace")
            .organizationId("sporekart-org")
            .tenantId("sporekart-enterprise")
            .deviceType("web")
            .language("en")
            .timeZone("UTC")
            .featureFlags(Map.of(
                "streamingEnabled", true,
                "advancedAnalytics", true,
                "forecastingEnabled", true,
                "alertsEnabled", true
            ))
            .build();
    }

    public void initSession(SessionId sessionId, UserContext userContext) {
        sessionContexts.put(sessionId, new SessionContext(userContext, new ArrayList<>()));
        log.debug("Session context initialized for session {}", sessionId);
    }

    public void trackQuery(SessionId sessionId, String query) {
        SessionContext ctx = sessionContexts.get(sessionId);
        if (ctx != null) {
            ctx.recentQueries().add(query);
            if (ctx.recentQueries().size() > 50) {
                ctx.recentQueries().remove(0);
            }
        }
    }

    public void endSession(SessionId sessionId) {
        sessionContexts.remove(sessionId);
        log.debug("Session context removed for session {}", sessionId);
    }

    public UserContext getUserContext(SessionId sessionId) {
        SessionContext ctx = sessionContexts.get(sessionId);
        return ctx != null ? ctx.userContext() : buildUserContext("system", "System", "system@sporekart.com");
    }

    public List<String> getRecentQueries(SessionId sessionId) {
        SessionContext ctx = sessionContexts.get(sessionId);
        return ctx != null ? List.copyOf(ctx.recentQueries()) : List.of();
    }

    public Map<String, Object> getPreferredMetrics(SessionId sessionId) {
        return Map.of(
            "dashboard", List.of("totalRevenue", "totalOrders", "activeUsers", "growthRate"),
            "defaultPeriod", "LAST_30_DAYS",
            "comparison", "PREVIOUS_PERIOD"
        );
    }

    private record SessionContext(UserContext userContext, List<String> recentQueries) {}
}
