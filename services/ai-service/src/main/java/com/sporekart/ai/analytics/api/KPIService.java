package com.sporekart.ai.analytics.api;

import com.sporekart.ai.analytics.domain.GovernanceKPI;
import com.sporekart.ai.analytics.domain.KpiStatus;
import java.util.List;
import java.util.Map;

public interface KPIService {
    GovernanceKPI calculateKPI(String name, String module, double currentValue, double targetValue, double threshold);
    List<GovernanceKPI> getAllKPIs();
    List<GovernanceKPI> getKPIsByModule(String module);
    KpiStatus evaluateKPIStatus(double currentValue, double targetValue, double threshold);
    Map<String, Object> getKPISummary();
}
