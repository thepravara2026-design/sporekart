package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.ProductAnalytics;
import com.sporekart.bi.copilot.domain.ProductAnalytics.ProductPerformance;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(ProductAnalyticsEngine.class);

    private static final String[] CATEGORIES = {"Mushroom Products", "Training", "Equipment", "Substrates", "Services"};
    private static final String[][] PRODUCTS_BY_CATEGORY = {
        {
            "Fresh Oyster Mushroom", "Dried Oyster Mushroom", "Oyster Spawn", "Oyster Mushroom Pickle",
            "Milky Mushroom Fresh", "Milky Mushroom Dried", "Milky Spawn",
            "Button Mushroom Fresh", "Button Mushroom Dried", "Button Spawn",
            "Shiitake Fresh", "Shiitake Dried", "Shiitake Spawn",
            "Paddy Straw Mushroom", "Paddy Straw Spawn",
            "Mushroom Cultivation Kit", "Advanced Grow Kit", "Starter Kit"
        },
        {
            "Mushroom Cultivation 101", "Advanced Oyster Farming", "Commercial Farming Program",
            "Disease Management Course", "Spawn Production Workshop"
        },
        {
            "Mist Sprayer", "Humidity Controller", "Shelving Unit", "Grow Bags (100)",
            "Thermometer", "pH Meter", "Sterilizer"
        },
        {
            "Wheat Straw Substrate", "Paddy Straw Substrate", "Sawdust Substrate",
            "Composted Substrate", "Coco Peat Blocks"
        },
        {
            "Consulting Service", "Farm Setup Service", "Quality Testing", "Packaging Service"
        }
    };
    private static final double[] COST_RATIOS = {0.55, 0.35, 0.60, 0.45, 0.30};

    private final List<ProductSnapshot> history = new ArrayList<>();

    public ProductAnalyticsEngine() {
        generateSeedData();
    }

    record ProductSnapshot(
        YearMonth period,
        Map<String, ProductRecord> products,
        double totalRevenue, int totalUnits,
        List<String> searchTrends, List<String> wishlistTrends
    ) {}

    record ProductRecord(String name, String category, double revenue, int unitsSold, double cost, double growth, double profitMargin) {}

    private void generateSeedData() {
        var rand = new Random(42);
        YearMonth start = YearMonth.now().minusMonths(11);

        var allProductNames = Arrays.stream(PRODUCTS_BY_CATEGORY).flatMap(Arrays::stream).toArray(String[]::new);

        for (int i = 0; i < 12; i++) {
            YearMonth ym = start.plusMonths(i);
            double seasonalFactor = getSeasonalFactor(ym.getMonthValue());
            var products = new LinkedHashMap<String, ProductRecord>();
            double totalRev = 0;
            int totalU = 0;

            for (int catIdx = 0; catIdx < CATEGORIES.length; catIdx++) {
                String[] prods = PRODUCTS_BY_CATEGORY[catIdx];
                double catRevenue = (200_000 + rand.nextDouble() * 300_000) * seasonalFactor * getCategoryFactor(catIdx);
                double[] weights = new double[prods.length];
                double wSum = 0;
                for (int j = 0; j < prods.length; j++) {
                    weights[j] = rand.nextDouble() * 10 + 1;
                    wSum += weights[j];
                }
                double remaining = catRevenue;
                for (int j = 0; j < prods.length - 1; j++) {
                    double rev = Math.round(catRevenue * (weights[j] / wSum) * 100.0) / 100.0;
                    int units = 5 + rand.nextInt(Math.max(1, (int)(rev / 500)));
                    double cost = rev * COST_RATIOS[catIdx] * (0.85 + rand.nextDouble() * 0.3);
                    double growth = -15 + rand.nextDouble() * 35;
                    double margin = rev > 0 ? Math.round(((rev - cost) / rev) * 10000.0) / 100.0 : 0;
                    products.put(prods[j], new ProductRecord(prods[j], CATEGORIES[catIdx], rev, units, cost, growth, margin));
                    totalRev += rev;
                    totalU += units;
                    remaining -= rev;
                }
                double rev = Math.round(remaining * 100.0) / 100.0;
                int units = Math.max(1, (int)(rev / 600));
                double cost = rev * COST_RATIOS[catIdx] * (0.85 + rand.nextDouble() * 0.3);
                double growth = -15 + rand.nextDouble() * 35;
                double margin = rev > 0 ? Math.round(((rev - cost) / rev) * 10000.0) / 100.0 : 0;
                products.put(prods[prods.length - 1], new ProductRecord(prods[prods.length - 1], CATEGORIES[catIdx],
                    rev, units, cost, growth, margin));
                totalRev += rev;
                totalU += units;
            }

            int trendCount = 3 + rand.nextInt(5);
            var searchTrends = new ArrayList<String>();
            var shuffled = new ArrayList<>(Arrays.asList(allProductNames));
            Collections.shuffle(shuffled, rand);
            for (int t = 0; t < Math.min(trendCount, shuffled.size()); t++) searchTrends.add(shuffled.get(t));

            var wishlistTrends = new ArrayList<String>();
            Collections.shuffle(shuffled, rand);
            for (int t = 0; t < Math.min(trendCount, shuffled.size()); t++) wishlistTrends.add(shuffled.get(t));

            history.add(new ProductSnapshot(ym, products, Math.round(totalRev * 100.0) / 100.0, totalU, searchTrends, wishlistTrends));
        }
        log.info("Generated {} months of product analytics seed data with {} products", history.size(),
            Arrays.stream(PRODUCTS_BY_CATEGORY).mapToInt(a -> a.length).sum());
    }

    private double getSeasonalFactor(int month) {
        return switch (month) {
            case 10, 11, 12, 1, 2 -> 1.15;
            case 6, 7, 8, 9 -> 1.05;
            default -> 0.88;
        };
    }

    private double getCategoryFactor(int catIdx) {
        return switch (catIdx) {
            case 0 -> 0.40;
            case 1 -> 0.25;
            case 2 -> 0.20;
            case 3 -> 0.10;
            case 4 -> 0.05;
            default -> 0.10;
        };
    }

    private ProductSnapshot resolvePeriod(String period) {
        if ("current".equalsIgnoreCase(period)) return history.getLast();
        if (period != null && period.length() == 7) {
            YearMonth ym = YearMonth.parse(period, DateTimeFormatter.ofPattern("yyyy-MM"));
            return history.stream().filter(h -> h.period().equals(ym)).findFirst().orElse(history.getLast());
        }
        return history.getLast();
    }

    public ProductAnalytics getProductSummary(String period) {
        var snap = resolvePeriod(period);
        return new ProductAnalytics(
            period, getTopProducts(10, period), getWorstProducts(10, period),
            getFastMovers(10), getSlowMovers(10),
            getCategoryPerformance(period), getConversionRate(period),
            snap.searchTrends(), snap.wishlistTrends()
        );
    }

    public List<ProductPerformance> getTopProducts(int limit, String period) {
        var snap = resolvePeriod(period);
        return snap.products().values().stream()
            .sorted((a, b) -> Double.compare(b.revenue(), a.revenue()))
            .limit(limit)
            .map(this::toPerformance)
            .toList();
    }

    public List<ProductPerformance> getWorstProducts(int limit, String period) {
        var snap = resolvePeriod(period);
        return snap.products().values().stream()
            .sorted(Comparator.comparingDouble(ProductRecord::revenue))
            .limit(limit)
            .map(this::toPerformance)
            .toList();
    }

    public List<ProductPerformance> getFastMovers(int limit) {
        var snap = history.getLast();
        return snap.products().values().stream()
            .filter(p -> p.growth() > 5)
            .sorted((a, b) -> Double.compare(b.growth(), a.growth()))
            .limit(limit)
            .map(this::toPerformance)
            .toList();
    }

    public List<ProductPerformance> getSlowMovers(int limit) {
        var snap = history.getLast();
        return snap.products().values().stream()
            .filter(p -> p.unitsSold() < 20 || p.growth() < -10)
            .sorted(Comparator.comparingDouble(ProductRecord::unitsSold))
            .limit(limit)
            .map(this::toPerformance)
            .toList();
    }

    public Map<String, Object> getCategoryPerformance(String period) {
        var snap = resolvePeriod(period);
        var map = new LinkedHashMap<String, Object>();
        for (String cat : CATEGORIES) {
            var catProds = snap.products().values().stream()
                .filter(p -> p.category().equals(cat)).toList();
            var catMap = new LinkedHashMap<String, Object>();
            double rev = catProds.stream().mapToDouble(ProductRecord::revenue).sum();
            int units = catProds.stream().mapToInt(ProductRecord::unitsSold).sum();
            double cost = catProds.stream().mapToDouble(ProductRecord::cost).sum();
            catMap.put("revenue", Math.round(rev * 100.0) / 100.0);
            catMap.put("unitsSold", units);
            catMap.put("avgMargin", rev > 0 ? Math.round(((rev - cost) / rev) * 10000.0) / 100.0 : 0);
            catMap.put("productCount", catProds.size());
            map.put(cat, catMap);
        }
        return map;
    }

    public double getConversionRate(String period) {
        return Math.round((1.5 + new Random(period.hashCode()).nextDouble() * 3.5) * 100.0) / 100.0;
    }

    public List<String> getSearchTrends(int limit) {
        var snap = history.getLast();
        return snap.searchTrends().stream().limit(limit).toList();
    }

    public List<String> getWishlistTrends(int limit) {
        var snap = history.getLast();
        return snap.wishlistTrends().stream().limit(limit).toList();
    }

    public Map<String, Object> getProductProfitability(String period) {
        var snap = resolvePeriod(period);
        var map = new LinkedHashMap<String, Object>();
        for (var entry : snap.products().entrySet()) {
            var prod = entry.getValue();
            var pm = new LinkedHashMap<String, Object>();
            pm.put("revenue", prod.revenue());
            pm.put("cost", Math.round(prod.cost() * 100.0) / 100.0);
            pm.put("profit", Math.round((prod.revenue() - prod.cost()) * 100.0) / 100.0);
            pm.put("margin", prod.profitMargin());
            pm.put("unitsSold", prod.unitsSold());
            map.put(entry.getKey(), pm);
        }
        return map;
    }

    public List<TrendDataPoint> getProductGrowth(String productId, int months) {
        int m = Math.min(months, history.size());
        return history.subList(history.size() - m, history.size()).stream()
            .map(s -> {
                var prod = s.products().get(productId);
                double value = prod != null ? prod.growth() : 0;
                return new TrendDataPoint(s.period(), value, productId);
            })
            .collect(Collectors.toList());
    }

    private ProductPerformance toPerformance(ProductRecord r) {
        return new ProductPerformance(r.name(), r.name(), r.category(), r.revenue(), r.unitsSold(), r.growth(), r.profitMargin());
    }
}
