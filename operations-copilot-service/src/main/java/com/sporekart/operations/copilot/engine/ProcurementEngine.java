package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.domain.*;
import com.sporekart.operations.copilot.dto.ProcurementRequest;
import com.sporekart.operations.copilot.dto.ProcurementResponse;
import com.sporekart.operations.copilot.dto.ProcurementResponse.VendorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProcurementEngine {

    private static final Logger log = LoggerFactory.getLogger(ProcurementEngine.class);

    public ProcurementResponse recommendProcurement(ProcurementRequest request) {
        log.info("Recommending procurement for: {} intent: {}", request.query(), request.intent());
        var vendors = getVendorSuggestions(request.sku());
        var totalCost = vendors.stream().mapToDouble(v -> v.unitPrice()).sum();
        var savings = totalCost * 0.12;
        var actionItems = List.of(
            "Create purchase order for " + (request.sku() != null ? request.sku() : "recommended items"),
            "Schedule delivery within lead time window",
            "Verify quality certificates before acceptance",
            "Update inventory system on receipt"
        );
        return new ProcurementResponse("Procurement plan for " + (request.query() != null ? request.query() : "mushroom supplies"),
            totalCost, savings, vendors, actionItems);
    }

    public List<VendorResponse> getVendorSuggestions(String sku) {
        log.debug("Getting vendor suggestions for SKU: {}", sku);
        return List.of(
            new VendorResponse("VEN-001", "Mushroom Supply Co.", 92.5, 1200.0, 5, "LOW"),
            new VendorResponse("VEN-002", "GrowKart Distributors", 88.0, 1150.0, 7, "LOW"),
            new VendorResponse("VEN-003", "Organic Spores Ltd.", 85.0, 1080.0, 10, "MEDIUM"),
            new VendorResponse("VEN-004", "AgriGlobal Supplies", 78.5, 1050.0, 14, "MEDIUM"),
            new VendorResponse("VEN-005", "QuickShip Traders", 72.0, 980.0, 3, "HIGH")
        );
    }

    public PurchaseOrder createPurchaseOrder(String vendorId, String sku, int quantity, String warehouseId) {
        log.info("Creating purchase order for vendor: {} sku: {} qty: {}", vendorId, sku, quantity);
        return new PurchaseOrder(UUID.randomUUID().toString(), vendorId, "Vendor " + vendorId,
            List.of(new PurchaseLineItem(sku, "Product " + sku, quantity, 100.0, quantity * 100.0, 0)),
            quantity * 100.0, PurchaseOrder.POStatus.DRAFT, LocalDate.now(),
            LocalDate.now().plusDays(7), null, warehouseId, "Created by Operations Copilot");
    }

    public double calculateEOQ(double annualDemand, double orderingCost, double holdingCost) {
        if (holdingCost <= 0) return 0;
        return Math.round(Math.sqrt((2 * annualDemand * orderingCost) / holdingCost));
    }

    public Map<String, Object> analyzeVendorRisk(String vendorId) {
        log.debug("Analyzing vendor risk for: {}", vendorId);
        var risk = new LinkedHashMap<String, Object>();
        risk.put("vendorId", vendorId);
        risk.put("overallRiskScore", 25.0);
        risk.put("riskLevel", "LOW");
        risk.put("deliveryRisk", 20.0);
        risk.put("qualityRisk", 15.0);
        risk.put("financialRisk", 10.0);
        risk.put("recommendation", "Preferred vendor - maintain current relationship");
        return risk;
    }

    public List<String> getProcurementTimeline(String urgency) {
        log.debug("Getting procurement timeline for urgency: {}", urgency);
        return switch (urgency.toLowerCase()) {
            case "emergency" -> List.of("Same day: Emergency PO creation", "24h: Express shipping", "48h: Expected delivery");
            case "expedited" -> List.of("Day 1: PO creation and approval", "Day 2-3: Processing", "Day 4-7: Delivery");
            case "standard" -> List.of("Week 1: PO creation and vendor confirmation", "Week 2: Production/processing", "Week 3: Shipping", "Week 4: Delivery and inspection");
            default -> List.of("Standard 30-day procurement cycle");
        };
    }
}
