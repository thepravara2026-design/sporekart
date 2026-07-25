package com.sporekart.alert.interfaces.rest;

import com.sporekart.alert.application.service.AlertIntelligenceService;
import com.sporekart.alert.domain.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertIntelligenceService service;

    public AlertController(AlertIntelligenceService service) { this.service = service; }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(service.health());
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return ResponseEntity.ok(service.listAlerts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable String id) {
        return service.getAlert(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = "severity")
    public ResponseEntity<List<Alert>> getAlertsBySeverity(@RequestParam AlertSeverity severity) {
        return ResponseEntity.ok(service.getAlertsBySeverity(severity));
    }

    @GetMapping(params = "category")
    public ResponseEntity<List<Alert>> getAlertsByCategory(@RequestParam AlertCategory category) {
        return ResponseEntity.ok(service.getAlertsByCategory(category));
    }

    @GetMapping(params = "domain")
    public ResponseEntity<List<Alert>> getAlertsByDomain(@RequestParam String domain) {
        return ResponseEntity.ok(service.getAlertsByDomain(domain));
    }

    @GetMapping(params = "status")
    public ResponseEntity<List<Alert>> getAlertsByStatus(@RequestParam AlertStatus status) {
        return ResponseEntity.ok(service.getAlertsByStatus(status));
    }

    @PostMapping("/generate")
    public ResponseEntity<List<Alert>> generateAlerts() {
        return ResponseEntity.ok(service.generateAllAlerts());
    }

    @PostMapping("/generate/{category}")
    public ResponseEntity<List<Alert>> generateAlertsForCategory(@PathVariable AlertCategory category) {
        return ResponseEntity.ok(service.generateAlertsForCategory(category));
    }

    @PostMapping("/{id}/acknowledge")
    public ResponseEntity<Alert> acknowledgeAlert(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.acknowledgeAlert(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Alert> resolveAlert(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.resolveAlert(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getAlertHistory() {
        return ResponseEntity.ok(service.getAlertHistory());
    }

    @GetMapping("/telemetry")
    public ResponseEntity<Map<String, Object>> getTelemetry() {
        return ResponseEntity.ok(service.getTelemetry());
    }

    @GetMapping("/cache")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        return ResponseEntity.ok(service.getCacheInfo());
    }

    @DeleteMapping("/cache")
    public ResponseEntity<Void> clearCache() {
        service.clearCache();
        return ResponseEntity.noContent().build();
    }

    // --- Risks ---

    @GetMapping("/risks")
    public ResponseEntity<List<BusinessRisk>> getAllRisks() {
        return ResponseEntity.ok(service.listRisks());
    }

    @GetMapping("/risks/{id}")
    public ResponseEntity<BusinessRisk> getRiskById(@PathVariable String id) {
        return service.getRisk(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/risks/summary")
    public ResponseEntity<Map<String, Object>> getRiskSummary() {
        return ResponseEntity.ok(service.getRiskSummary());
    }

    @PostMapping("/risks/generate")
    public ResponseEntity<List<BusinessRisk>> generateRisks() {
        return ResponseEntity.ok(service.generateAllRisks());
    }

    // --- Anomalies ---

    @GetMapping("/anomalies")
    public ResponseEntity<List<Anomaly>> getAllAnomalies() {
        return ResponseEntity.ok(service.listAnomalies());
    }

    @GetMapping("/anomalies/{id}")
    public ResponseEntity<Anomaly> getAnomalyById(@PathVariable String id) {
        return service.getAnomaly(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/anomalies/detect")
    public ResponseEntity<List<Anomaly>> detectAnomalies() {
        return ResponseEntity.ok(service.detectAllAnomalies());
    }

    // --- Timeline ---

    @GetMapping("/timeline")
    public ResponseEntity<List<TimelineEvent>> getTimeline() {
        return ResponseEntity.ok(service.listTimelineEvents());
    }

    @GetMapping("/timeline/{id}")
    public ResponseEntity<TimelineEvent> getTimelineEventById(@PathVariable String id) {
        return service.getTimelineEvent(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/timeline/generate")
    public ResponseEntity<List<TimelineEvent>> generateTimeline() {
        return ResponseEntity.ok(service.generateTimeline());
    }
}
