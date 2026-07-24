package com.sporekart.analytics.infrastructure.persistence;

import com.sporekart.analytics.domain.model.DashboardWidget;
import com.sporekart.analytics.domain.model.ReportRequest;
import com.sporekart.analytics.domain.model.SeoMetadata;
import com.sporekart.analytics.domain.repository.AnalyticsRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@Repository
@Transactional
public class JpaAnalyticsRepositoryAdapter implements AnalyticsRepositoryPort {

    private final WidgetJpaRepository widgetJpaRepository;
    private final ReportJpaRepository reportJpaRepository;
    private final SeoJpaRepository seoJpaRepository;

    public JpaAnalyticsRepositoryAdapter(WidgetJpaRepository widgetJpaRepository,
                                         ReportJpaRepository reportJpaRepository,
                                         SeoJpaRepository seoJpaRepository) {
        this.widgetJpaRepository = widgetJpaRepository;
        this.reportJpaRepository = reportJpaRepository;
        this.seoJpaRepository = seoJpaRepository;
    }

    @Override
    public DashboardWidget saveWidget(DashboardWidget widget) {
        return widgetJpaRepository.save(DashboardWidgetEntity.fromDomain(widget)).toDomain();
    }

    @Override
    public List<DashboardWidget> findAllWidgets() {
        return widgetJpaRepository.findAll().stream().map(DashboardWidgetEntity::toDomain).toList();
    }

    @Override
    public ReportRequest saveReport(ReportRequest report) {
        return reportJpaRepository.save(ReportRequestEntity.fromDomain(report)).toDomain();
    }

    @Override
    public List<ReportRequest> findAllReports() {
        return reportJpaRepository.findAll().stream().map(ReportRequestEntity::toDomain).toList();
    }

    @Override
    public SeoMetadata saveSeoMetadata(SeoMetadata metadata) {
        return seoJpaRepository.save(SeoMetadataEntity.fromDomain(metadata)).toDomain();
    }

    @Override
    public List<SeoMetadata> findAllSeoMetadata() {
        return seoJpaRepository.findAll().stream().map(SeoMetadataEntity::toDomain).toList();
    }

    @Override
    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalWidgets", widgetJpaRepository.count());
        dashboard.put("totalReports", reportJpaRepository.count());
        dashboard.put("totalSeoEntries", seoJpaRepository.count());
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
