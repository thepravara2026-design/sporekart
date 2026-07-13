package com.sporekart.ai.analytics.api;

import com.sporekart.ai.analytics.domain.DashboardWidget;
import com.sporekart.ai.analytics.domain.GovernanceDashboard;
import java.util.List;
import java.util.UUID;

public interface DashboardService {
    GovernanceDashboard getDashboard(String name);
    GovernanceDashboard getDashboard(UUID id);
    GovernanceDashboard createDashboard(GovernanceDashboard dashboard);
    GovernanceDashboard updateDashboard(UUID id, GovernanceDashboard dashboard);
    List<GovernanceDashboard> getAllDashboards();
    void addWidget(UUID dashboardId, DashboardWidget widget);
    void removeWidget(UUID dashboardId, UUID widgetId);
}
