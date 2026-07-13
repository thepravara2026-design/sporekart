package com.sporekart.ai.analytics.application;

import com.sporekart.ai.analytics.api.GovernanceAnalyticsService;
import com.sporekart.ai.analytics.api.KPIService;
import com.sporekart.ai.analytics.api.MetricsAggregationService;
import com.sporekart.ai.analytics.api.SnapshotService;
import com.sporekart.ai.analytics.api.TrendAnalysisService;
import com.sporekart.ai.analytics.domain.GovernanceSnapshot;
import com.sporekart.ai.analytics.domain.GovernanceSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GovernanceAnalyticsServiceImpl implements GovernanceAnalyticsService {

    private final MetricsAggregationService metricsAggregationService;
    private final KPIService kpiService;
    private final TrendAnalysisService trendAnalysisService;
    private final SnapshotService snapshotService;

    @Override
    public GovernanceSummary getSummary() {
        var metrics = metricsAggregationService.getMetricsSummary();
        var kpis = kpiService.getKPISummary();
        var trends = trendAnalysisService.getAllTrends();

        var metricsMap = new HashMap<String, Object>();
        metricsMap.put("aggregated", metrics);

        var kpisMap = new HashMap<String, Object>();
        kpisMap.put("summary", kpis);

        var trendsMap = new HashMap<String, Object>(trends);

        log.info("Generated governance summary with {} modules, {} KPI entries, {} trend groups",
                metrics.size(), kpis.size(), trends.size());

        return new GovernanceSummary(
                UUID.randomUUID(),
                "Governance Summary",
                metricsMap,
                kpisMap,
                trendsMap,
                Instant.now()
        );
    }

    @Override
    public GovernanceSummary getSummaryByModule(String module) {
        var metrics = metricsAggregationService.getMetricsByModule(module);
        var kpis = kpiService.getKPIsByModule(module);
        var trends = trendAnalysisService.getTrendsByModule(module);

        var metricsMap = new HashMap<String, Object>();
        metricsMap.put("metrics", metrics);
        metricsMap.put("count", metrics.size());

        var kpisMap = new HashMap<String, Object>();
        kpisMap.put("kpis", kpis);
        kpisMap.put("count", kpis.size());

        var trendsMap = new HashMap<String, Object>();
        trendsMap.put("trends", trends);
        trendsMap.put("count", trends.size());

        log.info("Generated governance summary for module '{}'", module);

        return new GovernanceSummary(
                UUID.randomUUID(),
                "Governance Summary - " + module,
                metricsMap,
                kpisMap,
                trendsMap,
                Instant.now()
        );
    }

    @Override
    public GovernanceSummary getSummaryByDateRange(Instant from, Instant to) {
        var metricsMap = new HashMap<String, Object>();
        metricsMap.put("range", Map.of("from", from.toString(), "to", to.toString()));

        var kpisMap = new HashMap<String, Object>();
        kpisMap.put("note", "KPIs are point-in-time and not filtered by date range");

        var trendsMap = new HashMap<String, Object>();
        trendsMap.put("note", "Trends are point-in-time and not filtered by date range");

        log.info("Generated governance summary for date range {} to {}", from, to);

        return new GovernanceSummary(
                UUID.randomUUID(),
                "Governance Summary - Date Range",
                metricsMap,
                kpisMap,
                trendsMap,
                Instant.now()
        );
    }

    @Override
    public GovernanceSnapshot captureSnapshot(String name) {
        var summary = getSummary();
        var data = new HashMap<String, Object>();
        data.put("summary", summary);
        data.put("moduleCount", summary.metrics().size());
        data.put("kpiCount", summary.kpis().size());
        data.put("trendGroups", summary.trends().size());

        log.info("Capturing governance snapshot '{}'", name);
        return snapshotService.createSnapshot(name, data);
    }
}
