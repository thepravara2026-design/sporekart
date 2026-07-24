package com.sporekart.analytics.infrastructure.persistence;

import com.sporekart.analytics.domain.model.DashboardWidget;
import com.sporekart.analytics.domain.model.ReportRequest;
import com.sporekart.analytics.domain.model.SeoMetadata;
import com.sporekart.analytics.domain.repository.AnalyticsRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAnalyticsRepository implements AnalyticsRepositoryPort {
    private final Map<String, DashboardWidget> widgets = new ConcurrentHashMap<>();
    private final Map<String, ReportRequest> reports = new ConcurrentHashMap<>();
    private final Map<String, SeoMetadata> seoMetadata = new ConcurrentHashMap<>();

    @Override
    public DashboardWidget saveWidget(DashboardWidget widget) {
        widgets.put(widget.getId(), widget);
        return widget;
    }

    @Override
    public List<DashboardWidget> findAllWidgets() {
        return new ArrayList<>(widgets.values());
    }

    @Override
    public ReportRequest saveReport(ReportRequest report) {
        reports.put(report.getId(), report);
        return report;
    }

    @Override
    public List<ReportRequest> findAllReports() {
        return new ArrayList<>(reports.values());
    }

    @Override
    public SeoMetadata saveSeoMetadata(SeoMetadata metadata) {
        seoMetadata.put(metadata.getId(), metadata);
        return metadata;
    }

    @Override
    public List<SeoMetadata> findAllSeoMetadata() {
        return new ArrayList<>(seoMetadata.values());
    }

    @Override
    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalWidgets", widgets.size());
        dashboard.put("totalReports", reports.size());
        dashboard.put("totalSeoEntries", seoMetadata.size());
        return dashboard;
    }

    @Override
    public Map<String, Object> getSalesMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalRevenue", 0.0);
        metrics.put("orderCount", 0);
        metrics.put("averageOrderValue", 0.0);
        metrics.put("period", "current");
        return metrics;
    }

    @Override
    public Map<String, Object> getCustomerMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalCustomers", 0);
        metrics.put("newCustomers", 0);
        metrics.put("activeCustomers", 0);
        metrics.put("churnRate", 0.0);
        return metrics;
    }

    @Override
    public Map<String, Object> getInventoryMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalProducts", 0);
        metrics.put("lowStockItems", 0);
        metrics.put("outOfStockItems", 0);
        metrics.put("inventoryValue", 0.0);
        return metrics;
    }

    @Override
    public Map<String, Object> getPaymentMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalPayments", 0);
        metrics.put("successfulPayments", 0);
        metrics.put("failedPayments", 0);
        metrics.put("paymentSuccessRate", 0.0);
        metrics.put("totalAmount", 0.0);
        return metrics;
    }

    @Override
    public Map<String, Object> getShipmentMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalShipments", 0);
        metrics.put("deliveredShipments", 0);
        metrics.put("inTransitShipments", 0);
        metrics.put("onTimeDeliveryRate", 0.0);
        return metrics;
    }

    @Override
    public Map<String, Object> getTrainingMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalTrainings", 0);
        metrics.put("completedTrainings", 0);
        metrics.put("enrolledParticipants", 0);
        metrics.put("certificationRate", 0.0);
        return metrics;
    }

    @Override
    public Map<String, Object> getGrowerMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalGrowers", 0);
        metrics.put("activeGrowers", 0);
        metrics.put("totalProduceValue", 0.0);
        metrics.put("averageRating", 0.0);
        return metrics;
    }
}
