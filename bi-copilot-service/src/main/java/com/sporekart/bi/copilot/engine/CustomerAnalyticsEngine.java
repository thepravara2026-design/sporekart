package com.sporekart.bi.copilot.engine;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sporekart.bi.copilot.domain.CustomerAnalytics;
import com.sporekart.bi.copilot.domain.CustomerSegment;
import com.sporekart.bi.copilot.domain.TrendDataPoint;

@Component
public class CustomerAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(CustomerAnalyticsEngine.class);
    private static final Random RANDOM = new Random(101);
    private static final DateTimeFormatter PERIOD_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final String[] SEGMENT_NAMES = {"New Growers", "Active Enthusiasts", "Commercial Farmers", "Enterprise Buyers"};
    private static final double[] SEGMENT_PERCENTS = {0.40, 0.30, 0.20, 0.10};
    private static final double[][] SEGMENT_REVENUE_RANGES = {{0, 15000}, {15000, 50000}, {50000, 200000}, {200000, 500000}};

    private static final int BASE_CUSTOMERS = 1000;
    private static final double[] MONTHLY_NEW_CUSTOMERS = {45, 52, 48, 60, 58, 72, 68, 85, 80, 92, 95, 110};
    private static final double[] MONTHLY_CHURN_RATES = {0.035, 0.032, 0.030, 0.028, 0.025, 0.022, 0.020, 0.018, 0.017, 0.015, 0.014, 0.012};

    private final List<SeedCustomerMonth> seedData = generateSeedData();

    public CustomerAnalyticsEngine() {
        log.info("CustomerAnalyticsEngine initialized with {} months of seed data", seedData.size());
    }

    public CustomerAnalytics getCustomerSummary(String period) {
        SeedCustomerMonth sm = resolvePeriod(period);
        if (sm == null) return emptyAnalytics(period);

        return new CustomerAnalytics(
            UUID.randomUUID().toString(),
            period,
            sm.totalCustomers,
            sm.newCustomers,
            sm.churnedCustomers,
            sm.churnRate * 100,
            calculateCLV(sm),
            450 + RANDOM.nextInt(200),
            (1 - sm.churnRate) * 100,
            sm.customersBySegment,
            sm.revenueBySegment,
            4.0 + RANDOM.nextDouble() * 0.8,
            sm.activeCustomers,
            sm.customersByRegion
        );
    }

    public List<CustomerSegment> getCustomerSegments() {
        SeedCustomerMonth latest = seedData.getLast();
        List<CustomerSegment> segments = new ArrayList<>();

        for (int i = 0; i < SEGMENT_NAMES.length; i++) {
            String name = SEGMENT_NAMES[i];
            int count = latest.customersBySegment.getOrDefault(name, 0);
            double revenue = latest.revenueBySegment.getOrDefault(name, 0.0);
            double avgRev = count > 0 ? revenue / count : 0;
            double churn = 0.08 - (i * 0.015) + RANDOM.nextDouble() * 0.02;

            segments.add(new CustomerSegment(
                UUID.randomUUID().toString(),
                name,
                descriptionForSegment(name),
                count,
                revenue,
                avgRev,
                churn * 100,
                characteristicsForSegment(name),
                strategiesForSegment(name)
            ));
        }
        return segments;
    }

    public List<TrendDataPoint> getCustomerAcquisitionTrend(int months) {
        int count = Math.min(months, seedData.size());
        List<SeedCustomerMonth> slice = seedData.subList(seedData.size() - count, seedData.size());
        List<TrendDataPoint> trends = new ArrayList<>();

        for (int i = 0; i < slice.size(); i++) {
            SeedCustomerMonth sm = slice.get(i);
            double ma = i > 0
                ? (sm.newCustomers + slice.get(i - 1).newCustomers) / 2.0
                : sm.newCustomers;

            trends.add(new TrendDataPoint(
                UUID.randomUUID().toString(),
                "acquisition",
                sm.periodLabel,
                sm.newCustomers,
                ma,
                1.0,
                sm.newCustomers,
                0,
                sm.newCustomers >= ma ? "up" : "down",
                i > 0 ? (sm.newCustomers - slice.get(i - 1).newCustomers) / slice.get(i - 1).newCustomers * 100 : 0,
                OffsetDateTime.now()
            ));
        }
        return trends;
    }

    public Map<String, Object> getChurnAnalysis(String period) {
        SeedCustomerMonth sm = resolvePeriod(period);
        if (sm == null) return Map.of();

        Map<String, Object> analysis = new LinkedHashMap<>();
        analysis.put("period", period);
        analysis.put("churnRate", sm.churnRate * 100);
        analysis.put("churnedCustomers", sm.churnedCustomers);
        analysis.put("totalCustomers", sm.totalCustomers);
        analysis.put("retentionRate", (1 - sm.churnRate) * 100);

        Map<String, Double> churnBySegment = new LinkedHashMap<>();
        for (int i = 0; i < SEGMENT_NAMES.length; i++) {
            churnBySegment.put(SEGMENT_NAMES[i], (0.08 - i * 0.015 + RANDOM.nextDouble() * 0.01) * 100);
        }
        analysis.put("churnBySegment", churnBySegment);

        List<String> reasons = List.of("Price sensitivity", "Quality concerns", "Competition", "Seasonal decline", "Logistics issues");
        analysis.put("topChurnReasons", reasons);

        return analysis;
    }

    public double getCustomerLifetimeValue(String period) {
        SeedCustomerMonth sm = resolvePeriod(period);
        if (sm == null) return 0;
        return calculateCLV(sm);
    }

    public List<CustomerSegment> getCustomerSegmentation() {
        return getCustomerSegments();
    }

    public int getActiveCustomerCount() {
        return seedData.getLast().activeCustomers;
    }

    public double getRetentionRate(String period) {
        SeedCustomerMonth sm = resolvePeriod(period);
        if (sm == null) return 0;
        return (1 - sm.churnRate) * 100;
    }

    public List<TrendDataPoint> getCustomerSatisfactionTrend(int months) {
        int count = Math.min(months, seedData.size());
        List<SeedCustomerMonth> slice = seedData.subList(seedData.size() - count, seedData.size());
        List<TrendDataPoint> trends = new ArrayList<>();

        for (int i = 0; i < slice.size(); i++) {
            SeedCustomerMonth sm = slice.get(i);
            double satisfaction = 4.0 + RANDOM.nextDouble() * 0.8;
            double ma = i > 0
                ? (satisfaction + 4.0 + RANDOM.nextDouble() * 0.8) / 2.0
                : satisfaction;

            trends.add(new TrendDataPoint(
                UUID.randomUUID().toString(),
                "satisfaction",
                sm.periodLabel,
                satisfaction,
                ma,
                1.0,
                4.4,
                satisfaction - 4.4,
                satisfaction >= ma ? "up" : "down",
                0,
                OffsetDateTime.now()
            ));
        }
        return trends;
    }

    private double calculateCLV(SeedCustomerMonth sm) {
        double avgRevenue = sm.totalCustomers > 0
            ? sm.revenueBySegment.values().stream().mapToDouble(Double::doubleValue).sum() / sm.totalCustomers
            : 0;
        double churn = sm.churnRate;
        double retention = 1 - churn;
        if (retention <= 0 || retention >= 1) return avgRevenue;
        return avgRevenue / (1 - retention);
    }

    private SeedCustomerMonth resolvePeriod(String period) {
        if (period == null || period.isBlank()) return seedData.getLast();
        for (SeedCustomerMonth sm : seedData) {
            if (sm.periodLabel.equals(period)) return sm;
        }
        return null;
    }

    private CustomerAnalytics emptyAnalytics(String period) {
        return new CustomerAnalytics(UUID.randomUUID().toString(), period, 0, 0, 0, 0, 0, 0, 0,
            Map.of(), Map.of(), 0, 0, Map.of());
    }

    private List<SeedCustomerMonth> generateSeedData() {
        List<SeedCustomerMonth> data = new ArrayList<>();
        LocalDate base = LocalDate.of(2025, 1, 1);
        int runningCustomers = BASE_CUSTOMERS;

        for (int i = 0; i < 12; i++) {
            LocalDate monthStart = base.plusMonths(i);
            String label = monthStart.format(PERIOD_FMT);
            double churnRate = MONTHLY_CHURN_RATES[i] + RANDOM.nextDouble() * 0.003;
            int newCust = (int) (MONTHLY_NEW_CUSTOMERS[i] + RANDOM.nextInt(15) - 5);
            int churned = (int) (runningCustomers * churnRate);
            int active = runningCustomers - churned + newCust;
            runningCustomers = active;

            Map<String, Integer> custBySegment = new LinkedHashMap<>();
            Map<String, Double> revBySegment = new LinkedHashMap<>();
            int totalSeg = 0;
            for (int s = 0; s < SEGMENT_NAMES.length; s++) {
                int segCount = (int) (active * SEGMENT_PERCENTS[s]);
                custBySegment.put(SEGMENT_NAMES[s], segCount);
                double low = SEGMENT_REVENUE_RANGES[s][0];
                double high = SEGMENT_REVENUE_RANGES[s][1];
                double segRev = segCount * (low + (high - low) * (0.3 + RANDOM.nextDouble() * 0.4));
                revBySegment.put(SEGMENT_NAMES[s], Math.round(segRev * 100) / 100.0);
                totalSeg += segCount;
            }

            Map<String, Integer> custByRegion = new LinkedHashMap<>();
            String[] regions = {"North", "South", "East", "West", "Central"};
            int regRemaining = active;
            for (int r = 0; r < regions.length; r++) {
                int regCount = r < regions.length - 1
                    ? (int) (active * (0.15 + RANDOM.nextDouble() * 0.06))
                    : regRemaining;
                custByRegion.put(regions[r], regCount);
                regRemaining -= regCount;
            }

            data.add(new SeedCustomerMonth(
                i, label, active, newCust, churned, churnRate,
                custBySegment, revBySegment, custByRegion
            ));
        }
        return data;
    }

    private static String descriptionForSegment(String name) {
        return switch (name) {
            case "New Growers" -> "Hobbyists and small-scale growers starting their mushroom cultivation journey";
            case "Active Enthusiasts" -> "Regular growers with established setups, purchasing supplies monthly";
            case "Commercial Farmers" -> "Professional mushroom farmers operating at commercial scale";
            case "Enterprise Buyers" -> "Large agricultural enterprises and institutional buyers";
            default -> "";
        };
    }

    private static List<String> characteristicsForSegment(String name) {
        return switch (name) {
            case "New Growers" -> List.of("Price sensitive", "High education needs", "Low order value", "High growth potential");
            case "Active Enthusiasts" -> List.of("Regular purchasers", "Brand loyal", "Mid-order value", "Referral drivers");
            case "Commercial Farmers" -> List.of("Volume buyers", "Bulk orders", "Negotiate pricing", "Long-term contracts");
            case "Enterprise Buyers" -> List.of("High value contracts", "Quality focused", "Strategic partners", "Low churn");
            default -> List.of();
        };
    }

    private static List<String> strategiesForSegment(String name) {
        return switch (name) {
            case "New Growers" -> List.of("Free starter kits", "Beginner workshops", "Bundle discounts", "Educational content");
            case "Active Enthusiasts" -> List.of("Loyalty program", "Premium subscriptions", "Referral rewards", "Early access");
            case "Commercial Farmers" -> List.of("Volume discounts", "Dedicated account manager", "Bulk shipping", "Custom formulations");
            case "Enterprise Buyers" -> List.of("Strategic partnership", "Custom SLAs", "Dedicated support", "Revenue sharing");
            default -> List.of();
        };
    }

    private record SeedCustomerMonth(
        int monthIndex,
        String periodLabel,
        int totalCustomers,
        int newCustomers,
        int churnedCustomers,
        double churnRate,
        Map<String, Integer> customersBySegment,
        Map<String, Double> revenueBySegment,
        Map<String, Integer> customersByRegion
    ) {}
}
