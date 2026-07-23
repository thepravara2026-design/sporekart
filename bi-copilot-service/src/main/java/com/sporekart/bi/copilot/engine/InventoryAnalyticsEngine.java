package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.InventoryAnalytics;
import com.sporekart.bi.copilot.domain.InventoryAnalytics.InventoryItem;
import com.sporekart.bi.copilot.domain.InventoryAnalytics.RestockingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InventoryAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(InventoryAnalyticsEngine.class);

    private static final String[] CATEGORIES = {"Mushroom Products", "Training Materials", "Equipment", "Substrates", "Services"};
    private static final String[][] ITEMS_BY_CATEGORY = {
        {
            "Fresh Oyster Mushroom", "Dried Oyster Mushroom", "Oyster Spawn", "Oyster Mushroom Pickle",
            "Milky Mushroom Fresh", "Milky Mushroom Dried", "Milky Spawn",
            "Button Mushroom Fresh", "Button Mushroom Dried", "Button Spawn",
            "Shiitake Fresh", "Shiitake Dried", "Shiitake Spawn",
            "Paddy Straw Mushroom", "Paddy Straw Spawn",
            "Mushroom Cultivation Kit", "Advanced Grow Kit", "Starter Kit"
        },
        {"Course Manuals", "Workbooks", "Certification Kits", "Training Videos", "Lab Supplies"},
        {
            "Mist Sprayer", "Humidity Controller", "Shelving Unit", "Grow Bags (100)",
            "Thermometer", "pH Meter", "Sterilizer"
        },
        {
            "Wheat Straw Substrate", "Paddy Straw Substrate", "Sawdust Substrate",
            "Composted Substrate", "Coco Peat Blocks"
        },
        {"Consulting Tools", "Testing Kits", "Packaging Material", "Labels"}
    };

    private final List<InventorySnapshot> history = new ArrayList<>();
    private final Map<String, ProductInventory> currentInventory = new LinkedHashMap<>();

    public InventoryAnalyticsEngine() {
        generateSeedData();
    }

    record ProductInventory(
        String productId, String name, String category,
        int currentStock, int reorderLevel, int soldLastMonth,
        double turnoverDays, double unitPrice, int leadTimeDays,
        int safetyStock, int maxStock
    ) {}

    record InventorySnapshot(YearMonth period, Map<String, Integer> salesVelocity) {}

    private void generateSeedData() {
        var rand = new Random(42);
        YearMonth start = YearMonth.now().minusMonths(11);

        int idx = 0;
        for (int catIdx = 0; catIdx < ITEMS_BY_CATEGORY.length; catIdx++) {
            for (String name : ITEMS_BY_CATEGORY[catIdx]) {
                String id = "PROD" + String.format("%03d", ++idx);
                int baseStock = 50 + rand.nextInt(500);
                int reorder = (int) (baseStock * (0.1 + rand.nextDouble() * 0.2));
                int maxStock = baseStock * 3;
                int safety = reorder / 2;
                double unitPrice = switch (catIdx) {
                    case 0 -> 150 + rand.nextDouble() * 850;
                    case 1 -> 500 + rand.nextDouble() * 2500;
                    case 2 -> 300 + rand.nextDouble() * 5000;
                    case 3 -> 100 + rand.nextDouble() * 400;
                    default -> 200 + rand.nextDouble() * 3000;
                };
                int leadTime = 3 + rand.nextInt(12);
                currentInventory.put(name, new ProductInventory(id, name, CATEGORIES[catIdx],
                    baseStock, reorder, 0, 0, Math.round(unitPrice * 100.0) / 100.0, leadTime, safety, maxStock));
            }
        }

        for (int i = 0; i < 12; i++) {
            YearMonth ym = start.plusMonths(i);
            var velocity = new LinkedHashMap<String, Integer>();
            for (var entry : currentInventory.entrySet()) {
                String name = entry.getKey();
                var inv = entry.getValue();
                int sold = 2 + rand.nextInt(Math.max(1, inv.currentStock() / 3));
                velocity.put(name, sold);
            }
            history.add(new InventorySnapshot(ym, velocity));
        }

        updateCurrentStock();
        log.info("Generated inventory data for {} products across {} months", currentInventory.size(), history.size());
    }

    private void updateCurrentStock() {
        for (var entry : currentInventory.entrySet()) {
            String name = entry.getKey();
            var inv = entry.getValue();
            int totalSold = history.stream()
                .flatMap(s -> s.salesVelocity().entrySet().stream())
                .filter(e -> e.getKey().equals(name))
                .mapToInt(Map.Entry::getValue)
                .sum();
            int avgMonthly = totalSold / Math.max(history.size(), 1);
            double turnoverDays = avgMonthly > 0 ? Math.round((double) inv.currentStock() / avgMonthly * 30.0 * 100.0) / 100.0 : 999;

            int lastSold = history.getLast().salesVelocity().getOrDefault(name, 0);

            int adjustedStock = inv.currentStock() - lastSold;
            if (adjustedStock < 0) adjustedStock = 2 + new Random(name.hashCode()).nextInt(20);
            int reorder = inv.reorderLevel();
            if (adjustedStock < reorder) adjustedStock = reorder + new Random(name.hashCode()).nextInt(50);

            currentInventory.put(name, new ProductInventory(
                inv.productId(), inv.name(), inv.category(),
                adjustedStock, reorder, lastSold,
                turnoverDays, inv.unitPrice(), inv.leadTimeDays(),
                inv.safetyStock(), inv.maxStock()
            ));
        }
    }

    private YearMonth resolveYearMonth(String period) {
        if (period == null || "current".equalsIgnoreCase(period)) return YearMonth.now();
        try { return YearMonth.parse(period, DateTimeFormatter.ofPattern("yyyy-MM")); }
        catch (Exception e) { return YearMonth.now(); }
    }

    public InventoryAnalytics getInventorySummary(String period) {
        var lowStock = getLowStockItems();
        var deadStock = getDeadStockItems();
        var fastMoving = getFastMovingItems(10);
        var slowMoving = getSlowMovingItems(10);
        int totalStock = currentInventory.values().stream().mapToInt(ProductInventory::currentStock).sum();
        double turnover = getInventoryTurnover(period);
        var restock = getRestockingPriority();
        double risk = getInventoryRisk();

        return new InventoryAnalytics(
            period, totalStock, lowStock.size(), deadStock.size(),
            fastMoving, slowMoving, turnover, restock, risk
        );
    }

    public List<InventoryItem> getLowStockItems() {
        return currentInventory.values().stream()
            .filter(inv -> inv.currentStock() <= inv.reorderLevel())
            .map(inv -> new InventoryItem(inv.productId(), inv.name(), inv.currentStock(),
                inv.reorderLevel(), inv.soldLastMonth(), inv.turnoverDays()))
            .sorted(Comparator.comparingInt(InventoryItem::currentStock))
            .toList();
    }

    public List<InventoryItem> getDeadStockItems() {
        return currentInventory.values().stream()
            .filter(inv -> inv.soldLastMonth() == 0 || inv.turnoverDays() > 180)
            .map(inv -> new InventoryItem(inv.productId(), inv.name(), inv.currentStock(),
                inv.reorderLevel(), inv.soldLastMonth(), inv.turnoverDays()))
            .toList();
    }

    public List<InventoryItem> getFastMovingItems(int limit) {
        return currentInventory.values().stream()
            .filter(inv -> inv.turnoverDays() < 30 && inv.soldLastMonth() > 10)
            .sorted(Comparator.comparingDouble(ProductInventory::turnoverDays))
            .limit(limit)
            .map(inv -> new InventoryItem(inv.productId(), inv.name(), inv.currentStock(),
                inv.reorderLevel(), inv.soldLastMonth(), inv.turnoverDays()))
            .toList();
    }

    public List<InventoryItem> getSlowMovingItems(int limit) {
        return currentInventory.values().stream()
            .filter(inv -> inv.turnoverDays() > 90 || inv.soldLastMonth() < 5)
            .sorted((a, b) -> Double.compare(b.turnoverDays(), a.turnoverDays()))
            .limit(limit)
            .map(inv -> new InventoryItem(inv.productId(), inv.name(), inv.currentStock(),
                inv.reorderLevel(), inv.soldLastMonth(), inv.turnoverDays()))
            .toList();
    }

    public double getInventoryTurnover(String period) {
        YearMonth ym = resolveYearMonth(period);
        var snap = history.stream()
            .filter(h -> h.period().equals(ym))
            .findFirst().orElse(history.getLast());
        int totalSold = snap.salesVelocity().values().stream().mapToInt(Integer::intValue).sum();
        int totalStock = currentInventory.values().stream().mapToInt(ProductInventory::currentStock).sum();
        if (totalStock == 0) return 0;
        return Math.round((double) totalSold / totalStock * 100.0) / 100.0;
    }

    public List<RestockingItem> getRestockingPriority() {
        return currentInventory.values().stream()
            .filter(inv -> inv.currentStock() <= inv.reorderLevel() + inv.safetyStock())
            .map(inv -> {
                int deficit = inv.reorderLevel() + inv.safetyStock() - inv.currentStock();
                int orderQty = Math.max(deficit + inv.soldLastMonth() * inv.leadTimeDays() / 30, inv.reorderLevel());
                String urgency;
                if (inv.currentStock() <= inv.safetyStock()) urgency = "Critical";
                else if (inv.currentStock() <= inv.reorderLevel()) urgency = "High";
                else urgency = "Medium";
                return new RestockingItem(inv.productId(), inv.name(), inv.currentStock(), orderQty, urgency);
            })
            .sorted((a, b) -> {
                var urgencyOrder = Map.of("Critical", 0, "High", 1, "Medium", 2);
                return Integer.compare(
                    urgencyOrder.getOrDefault(a.urgency(), 3),
                    urgencyOrder.getOrDefault(b.urgency(), 3)
                );
            })
            .toList();
    }

    public double getInventoryRisk() {
        long lowStockCount = currentInventory.values().stream()
            .filter(inv -> inv.currentStock() <= inv.reorderLevel()).count();
        long deadCount = currentInventory.values().stream()
            .filter(inv -> inv.soldLastMonth() == 0 || inv.turnoverDays() > 180).count();
        int total = currentInventory.size();
        if (total == 0) return 0;
        double lowRisk = (double) lowStockCount / total;
        double deadRisk = (double) deadCount / total;
        double turnoverRisk = currentInventory.values().stream()
            .mapToDouble(inv -> inv.turnoverDays() > 90 ? 0.3 : 0)
            .average().orElse(0);
        return Math.round((lowRisk * 0.4 + deadRisk * 0.35 + turnoverRisk * 0.25) * 10000.0) / 100.0;
    }

    public List<TrendDataPoint> getStockForecast(String productId, int weeks) {
        var inv = currentInventory.values().stream()
            .filter(i -> i.productId().equals(productId) || i.name().equalsIgnoreCase(productId))
            .findFirst().orElse(null);
        if (inv == null) return List.of();

        int monthlyVelocity = Math.max(inv.soldLastMonth(), 1);
        double weeklyVelocity = monthlyVelocity / 4.0;
        var points = new ArrayList<TrendDataPoint>();
        double projected = inv.currentStock();
        var start = YearMonth.now();

        points.add(new TrendDataPoint(start, projected, inv.name() + " (Current)"));
        for (int w = 1; w <= weeks; w++) {
            projected -= weeklyVelocity;
            if (projected < 0) projected = 0;
            points.add(new TrendDataPoint(start, Math.round(projected * 100.0) / 100.0,
                inv.name() + " (Week " + w + ")"));
        }
        return points;
    }

    public Map<String, Object> getInventoryByCategory() {
        var map = new LinkedHashMap<String, Object>();
        for (String cat : CATEGORIES) {
            var catItems = currentInventory.values().stream()
                .filter(i -> i.category().equals(cat)).toList();
            var catMap = new LinkedHashMap<String, Object>();
            catMap.put("itemCount", catItems.size());
            catMap.put("totalStock", catItems.stream().mapToInt(ProductInventory::currentStock).sum());
            catMap.put("totalValue", Math.round(catItems.stream()
                .mapToDouble(i -> i.currentStock() * i.unitPrice()).sum() * 100.0) / 100.0);
            catMap.put("avgTurnoverDays", Math.round(catItems.stream()
                .mapToDouble(ProductInventory::turnoverDays).average().orElse(0) * 100.0) / 100.0);
            map.put(cat, catMap);
        }
        return map;
    }

    record TrendDataPoint(YearMonth period, double value, String label) {}
}
