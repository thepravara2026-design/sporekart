package com.sporekart.bi.copilot.engine;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sporekart.bi.copilot.domain.RevenueMetrics;
import com.sporekart.bi.copilot.domain.TrendDataPoint;

@Component
public class RevenueAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(RevenueAnalyticsEngine.class);
    private static final Random RANDOM = new Random(42);
    private static final DateTimeFormatter PERIOD_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final List<String> CATEGORIES = List.of("Oyster", "Shiitake", "Button", "King Trumpet", "Enoki", "Lion's Mane", "Maitake", "Reishi");
    private static final List<String> REGIONS = List.of("North", "South", "East", "West", "Central");
    private static final List<String> CHANNELS = List.of("Online", "Retail", "Wholesale", "Training");

    private static final double[] MONTHLY_REVENUES = {
        250000, 275000, 290000, 310000, 335000, 360000,
        390000, 420000, 450000, 480000, 530000, 580000
    };

    private static final double[] MONTHLY_ORDERS = {
        1250, 1375, 1450, 1550, 1675, 1800,
        1950, 2100, 2250, 2400, 2650, 2900
    };

    private static final double[] CUSTOMER_COUNTS = {
        800, 850, 880, 920, 960, 1000,
        1050, 1100, 1150, 1200, 1280, 1350
    };

    private final List<SeedMonth> seedData = generateSeedData();

    public RevenueAnalyticsEngine() {
        log.info("RevenueAnalyticsEngine initialized with {} months of seed data", seedData.size());
    }

    public RevenueMetrics getRevenueSummary(String period) {
        SeedMonth sm = resolvePeriod(period);
        if (sm == null) return emptyMetrics(period);

        SeedMonth prev = findPreviousPeriod(sm.monthIndex);

        double totalRevenue = sm.totalRevenue;
        double totalOrders = sm.totalOrders;
        double aov = totalOrders > 0 ? totalRevenue / totalOrders : 0;
        double rpc = sm.activeCustomers > 0 ? totalRevenue / sm.activeCustomers : 0;
        double prevRevenue = prev != null ? prev.totalRevenue : 0;
        double growth = prevRevenue > 0 ? (totalRevenue - prevRevenue) / prevRevenue * 100 : 0;

        return new RevenueMetrics(
            UUID.randomUUID().toString(),
            period,
            sm.periodStart,
            sm.periodEnd,
            totalRevenue,
            totalOrders,
            aov,
            rpc,
            sm.revenueByProduct,
            sm.revenueByCategory,
            sm.revenueByRegion,
            sm.revenueByChannel,
            growth,
            prevRevenue,
            OffsetDateTime.now()
        );
    }

    public Map<String, Double> getRevenueByProduct(String period, String category) {
        SeedMonth sm = resolvePeriod(period);
        if (sm == null) return Map.of();

        return sm.revenueByProduct.entrySet().stream()
            .filter(e -> category == null || category.isBlank() || e.getKey().toLowerCase().contains(category.toLowerCase()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    public Map<String, Double> getRevenueByRegion(String period) {
        SeedMonth sm = resolvePeriod(period);
        return sm != null ? sm.revenueByRegion : Map.of();
    }

    public Map<String, Double> getRevenueByChannel(String period) {
        SeedMonth sm = resolvePeriod(period);
        return sm != null ? sm.revenueByChannel : Map.of();
    }

    public List<TrendDataPoint> getRevenueTrend(int months) {
        int count = Math.min(months, seedData.size());
        List<SeedMonth> slice = seedData.subList(seedData.size() - count, seedData.size());
        List<TrendDataPoint> trends = new ArrayList<>();

        for (int i = 0; i < slice.size(); i++) {
            SeedMonth sm = slice.get(i);
            double ma = calculateSimpleMovingAverage(slice, i, 3);
            double seasonal = calculateSeasonalFactor(seedData, sm.monthIndex % 12);
            double trendLine = sm.monthIndex * 29000 + 225000;

            trends.add(new TrendDataPoint(
                UUID.randomUUID().toString(),
                "revenue",
                sm.periodLabel,
                sm.totalRevenue,
                ma,
                seasonal,
                trendLine,
                sm.totalRevenue - trendLine,
                sm.totalRevenue >= trendLine ? "up" : "down",
                i > 0 ? (sm.totalRevenue - slice.get(i - 1).totalRevenue) / slice.get(i - 1).totalRevenue * 100 : 0,
                OffsetDateTime.now()
            ));
        }
        return trends;
    }

    public double getAverageOrderValue(String period) {
        SeedMonth sm = resolvePeriod(period);
        if (sm == null || sm.totalOrders == 0) return 0;
        return sm.totalRevenue / sm.totalOrders;
    }

    public double getRevenuePerCustomer(String period) {
        SeedMonth sm = resolvePeriod(period);
        if (sm == null || sm.activeCustomers == 0) return 0;
        return sm.totalRevenue / sm.activeCustomers;
    }

    public double getGrowthRate(String currentPeriod, String previousPeriod) {
        SeedMonth current = resolvePeriod(currentPeriod);
        SeedMonth previous = resolvePeriod(previousPeriod);
        if (current == null || previous == null || previous.totalRevenue == 0) return 0;
        return (current.totalRevenue - previous.totalRevenue) / previous.totalRevenue * 100;
    }

    public List<Map<String, Object>> getTopProducts(int limit, String period) {
        SeedMonth sm = resolvePeriod(period);
        if (sm == null) return List.of();

        return sm.revenueByProduct.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(limit)
            .map(e -> {
                Map<String, Object> entry = new LinkedHashMap<>();
                entry.put("product", e.getKey());
                entry.put("revenue", e.getValue());
                entry.put("quantity", Math.round(e.getValue() / 200.0));
                return entry;
            })
            .collect(Collectors.toList());
    }

    private SeedMonth resolvePeriod(String period) {
        if (period == null || period.isBlank()) return seedData.getLast();
        for (SeedMonth sm : seedData) {
            if (sm.periodLabel.equals(period)) return sm;
        }
        return null;
    }

    private SeedMonth findPreviousPeriod(int currentIndex) {
        if (currentIndex <= 0) return null;
        return seedData.get(currentIndex - 1);
    }

    private RevenueMetrics emptyMetrics(String period) {
        return new RevenueMetrics(UUID.randomUUID().toString(), period, "", "", 0, 0, 0, 0,
            Map.of(), Map.of(), Map.of(), Map.of(), 0, 0, OffsetDateTime.now());
    }

    private double calculateSimpleMovingAverage(List<SeedMonth> data, int index, int window) {
        int start = Math.max(0, index - window + 1);
        double sum = 0;
        int count = 0;
        for (int i = start; i <= index; i++) {
            sum += data.get(i).totalRevenue;
            count++;
        }
        return count > 0 ? sum / count : 0;
    }

    private double calculateSeasonalFactor(List<SeedMonth> fullData, int monthIndex) {
        double sum = 0;
        int count = 0;
        for (SeedMonth sm : fullData) {
            if (sm.monthIndex % 12 == monthIndex) {
                sum += sm.totalRevenue;
                count++;
            }
        }
        double avg = count > 0 ? sum / count : 0;
        double overallAvg = fullData.stream().mapToDouble(s -> s.totalRevenue).average().orElse(1);
        return overallAvg > 0 ? avg / overallAvg : 1;
    }

    private List<SeedMonth> generateSeedData() {
        List<SeedMonth> data = new ArrayList<>();
        LocalDate base = LocalDate.of(2025, 1, 1);

        for (int i = 0; i < 12; i++) {
            LocalDate monthStart = base.plusMonths(i);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            String label = monthStart.format(PERIOD_FMT);
            double totalRevenue = MONTHLY_REVENUES[i] + RANDOM.nextDouble() * 20000 - 10000;
            double totalOrders = MONTHLY_ORDERS[i] + RANDOM.nextInt(200) - 100;
            double activeCust = CUSTOMER_COUNTS[i] + RANDOM.nextInt(100) - 50;

            Map<String, Double> revByProduct = new LinkedHashMap<>();
            double remaining = totalRevenue;
            for (int p = 0; p < CATEGORIES.size(); p++) {
                double share = p < CATEGORIES.size() - 1
                    ? remaining * (0.08 + RANDOM.nextDouble() * 0.06)
                    : remaining;
                revByProduct.put(CATEGORIES.get(p), Math.round(share * 100) / 100.0);
                remaining -= share;
            }

            Map<String, Double> revByCategory = new LinkedHashMap<>();
            for (int c = 0; c < Math.min(4, CATEGORIES.size()); c++) {
                revByCategory.put(CATEGORIES.get(c), revByProduct.getOrDefault(CATEGORIES.get(c), 0.0));
            }

            Map<String, Double> revByRegion = new LinkedHashMap<>();
            remaining = totalRevenue;
            for (int r = 0; r < REGIONS.size(); r++) {
                double share = r < REGIONS.size() - 1
                    ? remaining * (0.15 + RANDOM.nextDouble() * 0.08)
                    : remaining;
                revByRegion.put(REGIONS.get(r), Math.round(share * 100) / 100.0);
                remaining -= share;
            }

            Map<String, Double> revByChannel = new LinkedHashMap<>();
            double online = totalRevenue * (0.30 + RANDOM.nextDouble() * 0.05);
            double retail = totalRevenue * (0.25 + RANDOM.nextDouble() * 0.05);
            double wholesale = totalRevenue * (0.20 + RANDOM.nextDouble() * 0.05);
            double training = totalRevenue - online - retail - wholesale;
            revByChannel.put("Online", Math.round(online * 100) / 100.0);
            revByChannel.put("Retail", Math.round(retail * 100) / 100.0);
            revByChannel.put("Wholesale", Math.round(wholesale * 100) / 100.0);
            revByChannel.put("Training", Math.round(training * 100) / 100.0);

            data.add(new SeedMonth(
                i, label, monthStart.toString(), monthEnd.toString(),
                totalRevenue, totalOrders, activeCust,
                revByProduct, revByCategory, revByRegion, revByChannel
            ));
        }
        return data;
    }

    private record SeedMonth(
        int monthIndex,
        String periodLabel,
        String periodStart,
        String periodEnd,
        double totalRevenue,
        double totalOrders,
        double activeCustomers,
        Map<String, Double> revenueByProduct,
        Map<String, Double> revenueByCategory,
        Map<String, Double> revenueByRegion,
        Map<String, Double> revenueByChannel
    ) {}
}
