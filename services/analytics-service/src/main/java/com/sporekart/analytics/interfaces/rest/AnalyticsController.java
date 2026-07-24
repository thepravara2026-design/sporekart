package com.sporekart.analytics.interfaces.rest;

import com.sporekart.analytics.application.service.AnalyticsService;
import com.sporekart.analytics.domain.model.DashboardWidget;
import com.sporekart.analytics.domain.model.ReportRequest;
import com.sporekart.analytics.domain.model.SeoMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(analyticsService.getDashboard());
    }

    @GetMapping("/sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sales() {
        return ResponseEntity.ok(analyticsService.getSalesMetrics());
    }

    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> customers() {
        return ResponseEntity.ok(analyticsService.getCustomerMetrics());
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> inventory() {
        return ResponseEntity.ok(analyticsService.getInventoryMetrics());
    }

    @GetMapping("/payments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> payments() {
        return ResponseEntity.ok(analyticsService.getPaymentMetrics());
    }

    @GetMapping("/shipments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> shipments() {
        return ResponseEntity.ok(analyticsService.getShipmentMetrics());
    }

    @GetMapping("/trainings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> trainings() {
        return ResponseEntity.ok(analyticsService.getTrainingMetrics());
    }

    @GetMapping("/growers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> growers() {
        return ResponseEntity.ok(analyticsService.getGrowerMetrics());
    }

    @GetMapping("/widgets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DashboardWidget>> widgets() {
        return ResponseEntity.ok(analyticsService.getWidgets());
    }

    @PostMapping("/widgets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardWidget> createWidget(@RequestBody CreateWidgetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(analyticsService.createWidget(request.name(), request.metric()));
    }

    public record CreateWidgetRequest(String name, String metric) {
    }
}
