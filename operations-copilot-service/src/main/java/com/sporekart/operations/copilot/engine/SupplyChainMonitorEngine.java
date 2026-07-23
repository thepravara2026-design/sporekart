package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.domain.SupplyChainAlert;
import com.sporekart.operations.copilot.domain.SupplyChainAlert.AlertCategory;
import com.sporekart.operations.copilot.domain.SupplyChainAlert.AlertSeverity;
import com.sporekart.operations.copilot.dto.SupplyChainResponse;
import com.sporekart.operations.copilot.dto.SupplyChainResponse.RiskItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SupplyChainMonitorEngine {

    private static final Logger log = LoggerFactory.getLogger(SupplyChainMonitorEngine.class);

    public SupplyChainResponse monitorSupplyChain(String vendorId, String region) {
        log.info("Monitoring supply chain for vendor: {} region: {}", vendorId, region);
        var healthScore = 78.5;
        var risks = List.of(
            new RiskItem("Supplier lead time increasing", "PROCUREMENT_DELAY", "HIGH", 7.5, vendorId != null ? vendorId : "VEN-001"),
            new RiskItem("Inventory shortage for spawn products", "INVENTORY_SHORTAGE", "CRITICAL", 9.0, "MUSH-001"),
            new RiskItem("Warehouse capacity nearing limit", "CAPACITY_ISSUE", "MEDIUM", 5.5, "WH-MAIN"),
            new RiskItem("Shipping delays in northern region", "SHIPPING_DELAY", "MEDIUM", 4.0, "Region-North")
        );
        var recommendations = List.of(
            "Expedite pending PO for MUSH-001 to avoid stockout",
            "Identify alternative suppliers for spawn category",
            "Review warehouse expansion plan for Q4",
            "Implement real-time tracking for northern shipments"
        );
        return new SupplyChainResponse("Supply chain health is MODERATE with 4 active risks requiring attention",
            healthScore, risks, recommendations);
    }

    public List<SupplyChainAlert> getActiveAlerts(String severity) {
        log.debug("Getting active supply chain alerts for severity: {}", severity);
        var allAlerts = List.of(
            new SupplyChainAlert("SCA-001", "Supplier Delay", "Vendor VEN-003 delayed shipment by 5 days",
                AlertCategory.SUPPLY_DELAY, AlertSeverity.HIGH, "VEN-003", 7.0,
                List.of("Contact vendor for updated ETA", "Activate backup supplier"), LocalDateTime.now().minusHours(6), false),
            new SupplyChainAlert("SCA-002", "Inventory Shortage", "MUSH-005 critically low",
                AlertCategory.INVENTORY_SHORTAGE, AlertSeverity.CRITICAL, "MUSH-005", 9.5,
                List.of("Create emergency PO", "Notify sales team"), LocalDateTime.now().minusHours(2), false),
            new SupplyChainAlert("SCA-003", "Warehouse Congestion", "Zone-A at 92% capacity",
                AlertCategory.WAREHOUSE_CONGESTION, AlertSeverity.MEDIUM, "WH-MAIN", 5.0,
                List.of("Review storage allocation", "Consider offsite storage"), LocalDateTime.now().minusDays(1), true)
        );
        if (severity != null && !severity.isBlank()) {
            return allAlerts.stream()
                .filter(a -> a.severity().name().equalsIgnoreCase(severity))
                .toList();
        }
        return allAlerts;
    }

    public Map<String, Object> detectAnomalies(String entityId) {
        log.debug("Detecting supply chain anomalies for: {}", entityId);
        var anomalies = new LinkedHashMap<String, Object>();
        anomalies.put("entityId", entityId);
        anomalies.put("anomaliesDetected", 3);
        anomalies.put("items", List.of(
            Map.of("type", "DEMAND_SPIKE", "description", "Unusual 200% demand spike for MUSH-001", "severity", "HIGH"),
            Map.of("type", "LEAD_TIME_DEVIATION", "description", "Vendor lead time 40% above average", "severity", "MEDIUM"),
            Map.of("type", "INVENTORY_DISCREPANCY", "description", "System vs physical count mismatch for 5 items", "severity", "LOW")
        ));
        return anomalies;
    }

    public Map<String, Object> calculateSupplyChainHealth() {
        log.debug("Calculating overall supply chain health");
        var health = new LinkedHashMap<String, Object>();
        health.put("overallHealthScore", 78.5);
        health.put("inventoryHealth", 82.0);
        health.put("procurementHealth", 75.0);
        health.put("logisticsHealth", 80.0);
        health.put("vendorHealth", 77.0);
        health.put("riskLevel", "MODERATE");
        health.put("recommendations", List.of("Focus on procurement reliability improvement", "Review vendor scorecards quarterly"));
        return health;
    }

    public List<String> getVendorRiskAssessment(String vendorId) {
        log.debug("Getting vendor risk assessment for: {}", vendorId);
        return List.of(
            "Financial stability: STABLE",
            "Quality compliance: 94%",
            "On-time delivery: 87%",
            "Price competitiveness: ABOVE AVERAGE",
            "Overall risk: LOW",
            "Recommendation: Maintain preferred vendor status with quarterly review"
        );
    }
}
