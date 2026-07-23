package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.RevenueAnalytics;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RevenueAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(RevenueAnalyticsEngine.class);

    private static final String[] CATEGORIES = {"Mushroom Products", "Training", "Equipment", "Substrates", "Services"};
    private static final double[] CATEGORY_WEIGHTS = {0.40, 0.25, 0.20, 0.10, 0.05};
    private static final String[] PRODUCTS = {
        "Fresh Oyster Mushroom", "Dried Oyster Mushroom", "Oyster Spawn", "Oyster Mushroom Pickle",
        "Milky Mushroom Fresh", "Milky Mushroom Dried", "Milky Spawn",
        "Button Mushroom Fresh", "Button Mushroom Dried", "Button Spawn",
        "Shiitake Fresh", "Shiitake Dried", "Shiitake Spawn",
        "Paddy Straw Mushroom", "Paddy Straw Spawn",
        "Mushroom Cultivation Kit", "Advanced Grow Kit", "Starter Kit",
        "Wheat Straw Substrate", "Paddy Straw Substrate", "Sawdust Substrate", "Composted Substrate", "Coco Peat Blocks",
        "Mist Sprayer", "Humidity Controller", "Shelving Unit", "Grow Bags (100)", "Thermometer", "pH Meter", "Sterilizer",
        "Mushroom Cultivation 101", "Advanced Oyster Farming", "Commercial Farming Program", "Disease Management Course", "Spawn Production Workshop",
        "Consulting Service", "Farm Setup Service", "Quality Testing", "Packaging Service"
    };
    private static final String[] CATEGORY_PRODUCTS = {
        "Fresh Oyster Mushroom,Dried Oyster Mushroom,Oyster Spawn,Oyster Mushroom Pickle,Milky Mushroom Fresh,Milky Mushroom Dried,Milky Spawn,Button Mushroom Fresh,Button Mushroom Dried,Button Spawn,Shiitake Fresh,Shiitake Dried,Shiitake Spawn,Paddy Straw Mushroom,Paddy Straw Spawn,Mushroom Cultivation Kit,Advanced Grow Kit,Starter Kit",
        "Mushroom Cultivation 101,Advanced Oyster Farming,Commercial Farming Program,Disease Management Course,Spawn Production Workshop",
        "Mist Sprayer,Humidity Controller,Shelving Unit,Grow Bags (100),Thermometer,pH Meter,Sterilizer",
        "Wheat Straw Substrate,Paddy Straw Substrate,Sawdust Substrate,Composted Substrate,Coco Peat Blocks",
        "Consulting Service,Farm Setup Service,Quality Testing,Packaging Service"
    };
    private static final String[] REGIONS = {"Maharashtra", "Karnataka", "Tamil Nadu", "Punjab", "Himachal"};
    private static final double[] REGION_WEIGHTS = {0.30, 0.22, 0.18, 0.17, 0.13};
    private static final String[] CHANNELS = {"Online Direct", "Online Marketplace", "Retail", "Wholesale", "Training Center"};
    private static final double[] CHANNEL_WEIGHTS = {0.25, 0.20, 0.20, 0.20, 0.15};
    private static final String[] SEGMENTS = {"Home Growers", "Hobbyists", "Commercial Farmers", "Enterprise Buyers"};
    private static final double[] SEGMENT_WEIGHTS = {0.15, 0.20, 0.40, 0.25};

    private final List<MonthlySnapshot> history = new ArrayList<>();

    public RevenueAnalyticsEngine() {
        generateSeedData();
    }

    private record MonthlySnapshot(
        YearMonth period, double grossRevenue, double netRevenue, double refundAmount,
        double averageOrderValue, int orderCount, int refundCount,
        Map<String, Double> byCategory, Map<String, Double> byProduct,
        Map<String, Double> byRegion, Map<String, Double> bySegment,
        Map<String, Double> byChannel, Map<String, Double> byTraining
    ) {}

    private void generateSeedData() {
        var rand = new Random(42);
        YearMonth start = YearMonth.now().minusMonths(23);

        for (int i = 0; i < 24; i++) {
            YearMonth ym = start.plusMonths(i);

            double seasonalFactor = getSeasonalFactor(ym.getMonthValue());
            double baseRevenue = 200_000 + rand.nextDouble() * 450_000;
            double gross = Math.round(baseRevenue * seasonalFactor * 100.0) / 100.0;

            double refundPct = 0.01 + rand.nextDouble() * 0.04;
            double refunds = Math.round(gross * refundPct * 100.0) / 100.0;
            double net = Math.round((gross - refunds) * 100.0) / 100.0;

            int orderCount = (int) (40 + rand.nextInt(60));
            double aov = Math.round((gross / orderCount) * 100.0) / 100.0;
            int refundCount = (int) (orderCount * refundPct);

            var byCategory = distribute(gross, CATEGORIES, CATEGORY_WEIGHTS, rand);
            var byProduct = distributeProducts(gross, rand);
            var byRegion = distribute(gross, REGIONS, REGION_WEIGHTS, rand);
            var bySegment = distribute(gross, SEGMENTS, SEGMENT_WEIGHTS, rand);
            var byChannel = distribute(gross, CHANNELS, CHANNEL_WEIGHTS, rand);

            var byTraining = new HashMap<String, Double>();
            byTraining.put("Mushroom Cultivation 101", byCategory.get("Training") * 0.30);
            byTraining.put("Advanced Oyster Farming", byCategory.get("Training") * 0.20);
            byTraining.put("Commercial Farming Program", byCategory.get("Training") * 0.25);
            byTraining.put("Disease Management Course", byCategory.get("Training") * 0.15);
            byTraining.put("Spawn Production Workshop", byCategory.get("Training") * 0.10);

            history.add(new MonthlySnapshot(ym, gross, net, refunds, aov, orderCount, refundCount,
                byCategory, byProduct, byRegion, bySegment, byChannel, byTraining));
        }
        log.info("Generated {} months of revenue seed data", history.size());
    }

    private double getSeasonalFactor(int month) {
        return switch (month) {
            case 10, 11, 12, 1, 2 -> 1.0 + 0.15 * Math.sin((month - 10) * Math.PI / 5);
            case 6, 7, 8, 9 -> 1.0 + 0.05 * Math.sin((month - 6) * Math.PI / 3);
            default -> 0.85;
        };
    }

    private Map<String, Double> distribute(double total, String[] keys, double[] weights, Random rand) {
        var result = new LinkedHashMap<String, Double>();
        double remaining = total;
        double weightSum = 0;
        for (int i = 0; i < keys.length - 1; i++) {
            double variation = 0.9 + rand.nextDouble() * 0.2;
            double amount = Math.round(total * weights[i] * variation * 100.0) / 100.0;
            result.put(keys[i], amount);
            remaining -= amount;
            weightSum += weights[i];
        }
        result.put(keys[keys.length - 1], Math.round(remaining * 100.0) / 100.0);
        return result;
    }

    private Map<String, Double> distributeProducts(double total, Random rand) {
        var result = new LinkedHashMap<String, Double>();
        var catProducts = Arrays.asList(CATEGORY_PRODUCTS);
        var catAmounts = distribute(total, CATEGORIES, CATEGORY_WEIGHTS, rand);
        for (int catIdx = 0; catIdx < CATEGORIES.length; catIdx++) {
            String[] prods = catProducts.get(catIdx).split(",");
            double catTotal = catAmounts.get(CATEGORIES[catIdx]);
            double[] pWeights = new double[prods.length];
            double wSum = 0;
            for (int j = 0; j < prods.length; j++) {
                pWeights[j] = rand.nextDouble() * 10 + 1;
                wSum += pWeights[j];
            }
            double remaining = catTotal;
            for (int j = 0; j < prods.length - 1; j++) {
                double amount = Math.round(catTotal * (pWeights[j] / wSum) * 100.0) / 100.0;
                result.put(prods[j], amount);
                remaining -= amount;
            }
            result.put(prods[prods.length - 1], Math.round(remaining * 100.0) / 100.0);
        }
        return result;
    }

    private MonthlySnapshot resolvePeriod(String period) {
        if ("current".equalsIgnoreCase(period)) {
            return history.getLast();
        }
        if (period != null && period.length() == 7) {
            YearMonth ym = YearMonth.parse(period, DateTimeFormatter.ofPattern("yyyy-MM"));
            return history.stream().filter(h -> h.period.equals(ym)).findFirst().orElse(history.getLast());
        }
        return history.getLast();
    }

    private List<MonthlySnapshot> resolveRange(String period) {
        if ("current".equalsIgnoreCase(period)) {
            return List.of(history.getLast());
        }
        if ("all".equalsIgnoreCase(period)) {
            return history;
        }
        if (period != null && period.length() == 7) {
            YearMonth ym = YearMonth.parse(period, DateTimeFormatter.ofPattern("yyyy-MM"));
            return history.stream().filter(h -> h.period.equals(ym)).toList();
        }
        return history;
    }

    public RevenueAnalytics getRevenueSummary(String period) {
        var snaps = resolveRange(period);
        if (snaps.isEmpty()) snaps = List.of(history.getLast());
        var snap = snaps.getLast();

        double prevRevenue = history.size() > 1 ? history.get(history.indexOf(snap) - 1).grossRevenue() : snap.grossRevenue();
        double growth = prevRevenue > 0 ? Math.round(((snap.grossRevenue() - prevRevenue) / prevRevenue) * 10000.0) / 100.0 : 0;

        return new RevenueAnalytics(
            snap.period().toString(), snap.grossRevenue(), snap.netRevenue(), snap.refundAmount(),
            snap.averageOrderValue(), growth, snap.byCategory(), snap.byProduct(),
            snap.byRegion(), snap.bySegment(), snap.byChannel(), snap.byTraining(),
            prevRevenue, snap.orderCount(), snap.refundCount()
        );
    }

    public Map<String, Object> getRevenueByCategory(String period) {
        var snap = resolvePeriod(period);
        var result = new LinkedHashMap<String, Object>();
        double prevTotal = getPreviousRevenue(snap);
        for (var entry : snap.byCategory().entrySet()) {
            var catMap = new LinkedHashMap<String, Object>();
            String prevPeriod = snap.period().minusMonths(1).toString();
            double prevAmount = history.stream()
                .filter(h -> h.period.equals(snap.period().minusMonths(1)))
                .findFirst()
                .map(h -> h.byCategory().getOrDefault(entry.getKey(), 0.0))
                .orElse(0.0);
            double momChange = prevAmount > 0 ? Math.round(((entry.getValue() - prevAmount) / prevAmount) * 10000.0) / 100.0 : 0;
            catMap.put("revenue", entry.getValue());
            catMap.put("momChange", momChange);
            catMap.put("percentage", Math.round((entry.getValue() / snap.grossRevenue()) * 10000.0) / 100.0);
            result.put(entry.getKey(), catMap);
        }
        return result;
    }

    public Map<String, Double> getRevenueByProduct(String period, String category) {
        var snap = resolvePeriod(period);
        if (category == null || category.isBlank()) return snap.byProduct();
        var filtered = new LinkedHashMap<String, Double>();
        int catIdx = -1;
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equalsIgnoreCase(category)) { catIdx = i; break; }
        }
        if (catIdx < 0) return snap.byProduct();
        String[] catProds = CATEGORY_PRODUCTS[catIdx].split(",");
        for (String p : catProds) {
            if (snap.byProduct().containsKey(p)) filtered.put(p, snap.byProduct().get(p));
        }
        return filtered;
    }

    public Map<String, Object> getRevenueByRegion(String period) {
        var snap = resolvePeriod(period);
        var result = new LinkedHashMap<String, Object>();
        for (var entry : snap.byRegion().entrySet()) {
            var regMap = new LinkedHashMap<String, Object>();
            double prevAmount = history.stream()
                .filter(h -> h.period.equals(snap.period().minusMonths(1)))
                .findFirst()
                .map(h -> h.byRegion().getOrDefault(entry.getKey(), 0.0))
                .orElse(0.0);
            double prevYearAmount = history.stream()
                .filter(h -> h.period.equals(snap.period().minusMonths(12)))
                .findFirst()
                .map(h -> h.byRegion().getOrDefault(entry.getKey(), 0.0))
                .orElse(0.0);
            double momGrowth = prevAmount > 0 ? Math.round(((entry.getValue() - prevAmount) / prevAmount) * 10000.0) / 100.0 : 0;
            double yoyGrowth = prevYearAmount > 0 ? Math.round(((entry.getValue() - prevYearAmount) / prevYearAmount) * 10000.0) / 100.0 : 0;
            regMap.put("revenue", entry.getValue());
            regMap.put("momGrowth", momGrowth);
            regMap.put("yoyGrowth", yoyGrowth);
            regMap.put("percentage", Math.round((entry.getValue() / snap.grossRevenue()) * 10000.0) / 100.0);
            result.put(entry.getKey(), regMap);
        }
        return result;
    }

    public Map<String, Double> getRevenueBySegment(String period) {
        return resolvePeriod(period).bySegment();
    }

    public Map<String, Double> getRevenueByChannel(String period) {
        return resolvePeriod(period).byChannel();
    }

    public Map<String, Double> getRevenueByTraining(String period) {
        return resolvePeriod(period).byTraining();
    }

    public double getRevenueGrowth(String currentPeriod, String previousPeriod) {
        var curr = resolvePeriod(currentPeriod);
        var prev = resolvePeriod(previousPeriod);
        if (prev.grossRevenue() == 0) return 0;
        return Math.round(((curr.grossRevenue() - prev.grossRevenue()) / prev.grossRevenue()) * 10000.0) / 100.0;
    }

    public double getAverageOrderValue(String period) {
        return resolvePeriod(period).averageOrderValue();
    }

    public Map<String, Object> getGrossVsNetRevenue(String period) {
        var snap = resolvePeriod(period);
        var map = new LinkedHashMap<String, Object>();
        map.put("grossRevenue", snap.grossRevenue());
        map.put("netRevenue", snap.netRevenue());
        map.put("refundAmount", snap.refundAmount());
        map.put("refundRate", Math.round((snap.refundAmount() / snap.grossRevenue()) * 10000.0) / 100.0);
        map.put("netMargin", Math.round((snap.netRevenue() / snap.grossRevenue()) * 10000.0) / 100.0);
        return map;
    }

    public Map<String, Object> getRefundAnalysis(String period) {
        var snap = resolvePeriod(period);
        var map = new LinkedHashMap<String, Object>();
        map.put("totalRefunds", snap.refundAmount());
        map.put("refundCount", snap.refundCount());
        map.put("refundRate", Math.round((snap.refundAmount() / snap.grossRevenue()) * 10000.0) / 100.0);
        map.put("avgRefundPerOrder", Math.round((snap.refundAmount() / Math.max(snap.refundCount(), 1)) * 100.0) / 100.0);
        map.put("orderCount", snap.orderCount());

        var byCategoryRefunds = new LinkedHashMap<String, Object>();
        for (var entry : snap.byCategory().entrySet()) {
            double catRefundRate = 0.01 + new Random(entry.getKey().hashCode()).nextDouble() * 0.03;
            byCategoryRefunds.put(entry.getKey(), Math.round(entry.getValue() * catRefundRate * 100.0) / 100.0);
        }
        map.put("byCategory", byCategoryRefunds);
        return map;
    }

    public List<TrendDataPoint> getRevenueTrend(int months) {
        int m = Math.min(months, history.size());
        return history.subList(history.size() - m, history.size()).stream()
            .map(s -> new TrendDataPoint(s.period(), s.grossRevenue(), "Revenue"))
            .collect(Collectors.toList());
    }

    public Map<String, Object> comparePeriods(String period1, String period2) {
        var snap1 = resolvePeriod(period1);
        var snap2 = resolvePeriod(period2);
        var map = new LinkedHashMap<String, Object>();
        map.put("period1", snap1.period().toString());
        map.put("period2", snap2.period().toString());
        map.put("revenue1", snap1.grossRevenue());
        map.put("revenue2", snap2.grossRevenue());
        map.put("difference", Math.round((snap1.grossRevenue() - snap2.grossRevenue()) * 100.0) / 100.0);
        map.put("growth", snap2.grossRevenue() > 0
            ? Math.round(((snap1.grossRevenue() - snap2.grossRevenue()) / snap2.grossRevenue()) * 10000.0) / 100.0
            : 0);
        map.put("orders1", snap1.orderCount());
        map.put("orders2", snap2.orderCount());
        map.put("aov1", snap1.averageOrderValue());
        map.put("aov2", snap2.averageOrderValue());
        map.put("byCategory1", snap1.byCategory());
        map.put("byCategory2", snap2.byCategory());
        map.put("byRegion1", snap1.byRegion());
        map.put("byRegion2", snap2.byRegion());
        return map;
    }

    private double getPreviousRevenue(MonthlySnapshot snap) {
        int idx = history.indexOf(snap);
        if (idx > 0) return history.get(idx - 1).grossRevenue();
        return snap.grossRevenue();
    }
}
