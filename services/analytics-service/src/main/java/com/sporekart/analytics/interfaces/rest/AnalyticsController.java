package com.sporekart.analytics.interfaces.rest;

import com.sporekart.analytics.application.service.AnalyticsService;
import com.sporekart.analytics.domain.model.DashboardWidget;
import com.sporekart.analytics.domain.model.ReportRequest;
import com.sporekart.analytics.domain.model.SeoMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(analyticsService.getDashboard());
    }

    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> sales() {
        return ResponseEntity.ok(Map.of("revenueTrend", "up", "topSellingProducts", List.of("SporeMix")));
    }

    @GetMapping("/customers")
    public ResponseEntity<Map<String, Object>> customers() {
        return ResponseEntity.ok(Map.of("retention", 78.5, "repeatPurchases", 34));
    }

    @GetMapping("/inventory")
    public ResponseEntity<Map<String, Object>> inventory() {
        return ResponseEntity.ok(Map.of("lowStock", 11, "outOfStock", 2));
    }

    @GetMapping("/payments")
    public ResponseEntity<Map<String, Object>> payments() {
        return ResponseEntity.ok(Map.of("successRate", 98.4, "refundRate", 1.2));
    }

    @GetMapping("/shipments")
    public ResponseEntity<Map<String, Object>> shipments() {
        return ResponseEntity.ok(Map.of("delayed", 4, "successRate", 96.8));
    }

    @GetMapping("/trainings")
    public ResponseEntity<Map<String, Object>> trainings() {
        return ResponseEntity.ok(Map.of("enrollmentTrend", "up", "completionRate", 92));
    }

    @GetMapping("/growers")
    public ResponseEntity<Map<String, Object>> growers() {
        return ResponseEntity.ok(Map.of("activeGrowers", 128, "certificationRate", 87));
    }

    @GetMapping("/widgets")
    public ResponseEntity<List<DashboardWidget>> widgets() {
        return ResponseEntity.ok(analyticsService.getWidgets());
    }

    @PostMapping("/widgets")
    public ResponseEntity<DashboardWidget> createWidget(@RequestBody CreateWidgetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(analyticsService.createWidget(request.name(), request.metric()));
    }

    public record CreateWidgetRequest(String name, String metric) {
    }
}
