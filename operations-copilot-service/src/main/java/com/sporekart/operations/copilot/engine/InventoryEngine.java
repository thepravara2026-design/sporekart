package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.domain.*;
import com.sporekart.operations.copilot.domain.InventoryItem.InventoryStatus;
import com.sporekart.operations.copilot.dto.InventoryAlertResponse;
import com.sporekart.operations.copilot.dto.InventoryAlertResponse.AlertItem;
import com.sporekart.operations.copilot.dto.InventoryStatusResponse;
import com.sporekart.operations.copilot.dto.InventoryStatusResponse.InventoryItemResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryEngine {

    private static final Logger log = LoggerFactory.getLogger(InventoryEngine.class);

    public InventoryStatusResponse getInventoryStatus(String warehouseId, String category) {
        log.info("Getting inventory status for warehouse: {} category: {}", warehouseId, category);
        var items = generateSampleItems(warehouseId, category);
        var inStock = (int) items.stream().filter(i -> i.status() == InventoryStatus.IN_STOCK).count();
        var lowStock = (int) items.stream().filter(i -> i.status() == InventoryStatus.LOW_STOCK).count();
        var critical = (int) items.stream().filter(i -> i.status() == InventoryStatus.CRITICAL).count();
        var outOfStock = (int) items.stream().filter(i -> i.status() == InventoryStatus.OUT_OF_STOCK).count();
        var totalValue = items.stream().mapToDouble(InventoryItem::inventoryValue).sum();
        var turnover = 6.5;
        var itemResponses = items.stream().map(i -> new InventoryItemResponse(i.sku(), i.productName(),
            i.currentStock(), i.reorderPoint(), i.status().name(), i.warehouseId())).toList();
        return new InventoryStatusResponse(items.size(), inStock, lowStock, critical, outOfStock, totalValue, turnover, itemResponses);
    }

    public InventoryAlertResponse getAlerts(String warehouseId) {
        log.info("Getting inventory alerts for warehouse: {}", warehouseId);
        var alerts = new ArrayList<AlertItem>();
        alerts.add(new AlertItem("a1", "MUSH-001", "Oyster Mushroom Spawn", "LOW_STOCK", "WARNING", "Stock level below reorder point"));
        alerts.add(new AlertItem("a2", "MUSH-005", "Shiitake Grow Kit", "CRITICAL_STOCK", "CRITICAL", "Only 5 units remaining"));
        alerts.add(new AlertItem("a3", "MUSH-010", "Button Mushroom Compost", "OUT_OF_STOCK", "BLOCKER", "Completely out of stock"));
        alerts.add(new AlertItem("a4", "SUB-003", "Coco Coir Substrate", "LOW_STOCK", "INFO", "120 days in inventory, consider promotion"));
        return new InventoryAlertResponse(alerts.size(), 1, 2, alerts);
    }

    public InventoryItem getItemDetail(String sku) {
        log.debug("Getting inventory detail for SKU: {}", sku);
        return new InventoryItem(sku, "Product " + sku, "General", 50, 5, 45, 20, 10, 7,
            15.0, 750.0, "WH-MAIN", "ZONE-A", "BIN-A1", LocalDateTime.now().minusDays(10),
            LocalDateTime.now().minusDays(30), InventoryStatus.IN_STOCK);
    }

    public List<InventoryItem> getLowStockItems(String warehouseId) {
        log.info("Getting low stock items for warehouse: {}", warehouseId);
        return generateSampleItems(warehouseId, null).stream()
            .filter(i -> i.status() == InventoryStatus.LOW_STOCK || i.status() == InventoryStatus.CRITICAL)
            .toList();
    }

    public Map<String, Object> calculateInventoryRisk() {
        log.debug("Calculating inventory risk");
        var risk = new LinkedHashMap<String, Object>();
        risk.put("overallRiskScore", 72.5);
        risk.put("riskLevel", "MODERATE");
        risk.put("highRiskItems", 12);
        risk.put("moderateRiskItems", 28);
        risk.put("lowRiskItems", 60);
        risk.put("topRisks", List.of("Seasonal demand spike for spawn kits", "Supplier lead time variability", "Storage capacity constraints"));
        return risk;
    }

    public List<String> generateRestockSuggestions(String warehouseId) {
        log.debug("Generating restock suggestions for warehouse: {}", warehouseId);
        return List.of(
            "Restock Shiitake Grow Kit (SKU: MUSH-005) - Critical stock",
            "Reorder Oyster Mushroom Spawn (SKU: MUSH-001) - Below reorder point",
            "Increase safety stock for Button Mushroom Compost - High demand season approaching",
            "Consider alternative supplier for Coco Coir - Current lead time too high",
            "Review dead stock items for potential discount clearance"
        );
    }

    public double calculateSafetyStock(int avgDailyUsage, int leadTimeDays, int serviceLevel) {
        var zScore = switch (serviceLevel) {
            case 90 -> 1.28;
            case 95 -> 1.65;
            case 99 -> 2.33;
            default -> 1.0;
        };
        var demandVariability = avgDailyUsage * 0.3;
        return Math.round((avgDailyUsage * leadTimeDays) + (zScore * demandVariability * Math.sqrt(leadTimeDays)));
    }

    public int calculateReorderPoint(int avgDailyUsage, int leadTimeDays, int safetyStock) {
        return (avgDailyUsage * leadTimeDays) + safetyStock;
    }

    private List<InventoryItem> generateSampleItems(String warehouseId, String category) {
        var wid = warehouseId != null ? warehouseId : "WH-MAIN";
        return List.of(
            new InventoryItem("MUSH-001", "Oyster Mushroom Spawn", "Spawn", 25, 5, 20, 30, 15, 7, 12.0, 300.0, wid, "ZONE-A", "BIN-A1", LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(15), InventoryStatus.LOW_STOCK),
            new InventoryItem("MUSH-002", "Shiitake Mushroom Spawn", "Spawn", 120, 10, 110, 20, 15, 7, 15.0, 1800.0, wid, "ZONE-A", "BIN-A2", LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(10), InventoryStatus.IN_STOCK),
            new InventoryItem("MUSH-005", "Shiitake Grow Kit", "Kits", 5, 0, 5, 25, 10, 5, 25.0, 125.0, wid, "ZONE-B", "BIN-B1", LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(5), InventoryStatus.CRITICAL),
            new InventoryItem("MUSH-010", "Button Mushroom Compost", "Substrate", 0, 0, 0, 50, 20, 10, 8.0, 0.0, wid, "ZONE-C", "BIN-C1", LocalDateTime.now().minusDays(60), LocalDateTime.now().minusDays(30), InventoryStatus.OUT_OF_STOCK),
            new InventoryItem("SUB-003", "Coco Coir Substrate", "Substrate", 500, 20, 480, 200, 100, 14, 5.0, 2500.0, wid, "ZONE-C", "BIN-C2", LocalDateTime.now().minusDays(120), LocalDateTime.now().minusDays(90), InventoryStatus.IN_STOCK)
        ).stream().filter(i -> category == null || i.category().equalsIgnoreCase(category)).collect(Collectors.toList());
    }
}
