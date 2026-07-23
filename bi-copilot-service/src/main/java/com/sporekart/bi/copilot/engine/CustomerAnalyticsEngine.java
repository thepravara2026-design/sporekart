package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.CustomerAnalytics;
import com.sporekart.bi.copilot.domain.CustomerAnalytics.TopCustomer;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomerAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(CustomerAnalyticsEngine.class);

    private static final String[] SEGMENTS = {"Home Growers", "Hobbyists", "Commercial Farmers", "Enterprise Buyers"};
    private static final double[] SEGMENT_WEIGHTS = {0.35, 0.25, 0.25, 0.15};
    private static final double[] SEGMENT_RANGES = {5000, 20000, 100000, 500000};
    private static final String[] NAMES = {
        "Amit Sharma", "Priya Patel", "Rahul Verma", "Sneha Reddy", "Vikram Singh",
        "Anita Desai", "Rajesh Kumar", "Deepa Nair", "Suresh Iyer", "Kavita Joshi",
        "Manoj Tiwari", "Neha Gupta", "Arun Kapoor", "Pooja Mehta", "Sunil Rao",
        "Lata Krishnan", "Vijay Chauhan", "Rekha Agarwal", "Anand Menon", "Shweta Pandey"
    };
    private static final String[] REGIONS = {"Maharashtra", "Karnataka", "Tamil Nadu", "Punjab", "Himachal"};
    private static final double[] REGION_WEIGHTS = {0.28, 0.24, 0.20, 0.16, 0.12};

    private final List<Customer> customers = new ArrayList<>();
    private static final int TOTAL_CUSTOMERS = 5000;

    public CustomerAnalyticsEngine() {
        generateSeedData();
    }

    record Customer(
        String customerId, String name, String segment, String region,
        YearMonth firstPurchase, YearMonth lastPurchase, int orderCount,
        double totalSpent, boolean active
    ) {}

    private void generateSeedData() {
        var rand = new Random(42);
        YearMonth start = YearMonth.now().minusMonths(12);
        YearMonth current = YearMonth.now();

        for (int i = 0; i < TOTAL_CUSTOMERS; i++) {
            String id = "CUST" + String.format("%05d", i + 1);
            String name = NAMES[rand.nextInt(NAMES.length)] + " " + i;
            double segmentRand = rand.nextDouble();
            String segment;
            if (segmentRand < 0.35) segment = "Home Growers";
            else if (segmentRand < 0.60) segment = "Hobbyists";
            else if (segmentRand < 0.85) segment = "Commercial Farmers";
            else segment = "Enterprise Buyers";

            String region = REGIONS[weightedIndex(rand, REGION_WEIGHTS)];

            int signupMonth = rand.nextInt(12);
            YearMonth firstPurchase = start.plusMonths(signupMonth);

            int activeMonths = 1 + rand.nextInt(Math.max(1, 12 - signupMonth));
            YearMonth lastPurchase = firstPurchase.plusMonths(activeMonths - 1);
            if (lastPurchase.isAfter(current)) lastPurchase = current;

            int orderCount = 1 + rand.nextInt(Math.min(24, activeMonths * 2));
            double avgOrderValue = getSegmentAvgOrderValue(segment);
            double totalSpent = Math.round(orderCount * avgOrderValue * (0.7 + rand.nextDouble() * 0.6) * 100.0) / 100.0;

            boolean active = lastPurchase.isAfter(current.minusMonths(3));

            customers.add(new Customer(id, name, segment, region, firstPurchase, lastPurchase, orderCount, totalSpent, active));
        }
        log.info("Generated {} customer records", customers.size());
    }

    private double getSegmentAvgOrderValue(String segment) {
        return switch (segment) {
            case "Home Growers" -> 1500;
            case "Hobbyists" -> 5000;
            case "Commercial Farmers" -> 25000;
            case "Enterprise Buyers" -> 75000;
            default -> 2000;
        };
    }

    private int weightedIndex(Random rand, double[] weights) {
        double r = rand.nextDouble();
        double cumulative = 0;
        for (int i = 0; i < weights.length; i++) {
            cumulative += weights[i];
            if (r < cumulative) return i;
        }
        return weights.length - 1;
    }

    public CustomerAnalytics getCustomerSummary(String period) {
        var periodCustomers = filterByPeriod(period);
        var all = filterByPeriod(period);
        int total = all.size();
        int newC = (int) all.stream().filter(c -> c.firstPurchase().equals(resolveYearMonth(period))).count();
        int returning = (int) all.stream().filter(c -> c.orderCount() > 1).count();
        int churned = (int) all.stream().filter(c -> !c.active()).count();
        double retention = total > 0 ? Math.round((double) returning / total * 10000.0) / 100.0 : 0;
        double churn = total > 0 ? Math.round((double) churned / total * 10000.0) / 100.0 : 0;
        double clv = Math.round(customers.stream().mapToDouble(Customer::totalSpent).average().orElse(0) * 100.0) / 100.0;
        double repeatRate = total > 0 ? Math.round((double) returning / total * 10000.0) / 100.0 : 0;
        int inactive = (int) all.stream().filter(c -> !c.active()).count();

        var topCustomers = getTopCustomers(10);
        var bySegment = new LinkedHashMap<String, Integer>();
        var revenueBySegment = new LinkedHashMap<String, Double>();
        for (String seg : SEGMENTS) {
            var segCusts = all.stream().filter(c -> c.segment().equals(seg)).toList();
            bySegment.put(seg, segCusts.size());
            revenueBySegment.put(seg, Math.round(segCusts.stream().mapToDouble(Customer::totalSpent).sum() * 100.0) / 100.0);
        }

        return new CustomerAnalytics(
            period, total, newC, returning, churned, retention, churn, clv, repeatRate,
            inactive, topCustomers, bySegment, revenueBySegment
        );
    }

    public List<TrendDataPoint> getNewCustomers(int months) {
        YearMonth end = YearMonth.now();
        YearMonth start = end.minusMonths(months - 1);
        var result = new ArrayList<TrendDataPoint>();
        for (YearMonth ym = start; !ym.isAfter(end); ym = ym.plusMonths(1)) {
            final YearMonth m = ym;
            long count = customers.stream().filter(c -> c.firstPurchase().equals(m)).count();
            result.add(new TrendDataPoint(ym, count, "New Customers"));
        }
        return result;
    }

    public int getReturningCustomers(String period) {
        return (int) filterByPeriod(period).stream().filter(c -> c.orderCount() > 1).count();
    }

    public double getRetentionRate(String period) {
        var all = filterByPeriod(period);
        if (all.isEmpty()) return 0;
        long returning = all.stream().filter(c -> c.orderCount() > 1).count();
        return Math.round((double) returning / all.size() * 10000.0) / 100.0;
    }

    public double getChurnRate(String period) {
        var all = filterByPeriod(period);
        if (all.isEmpty()) return 0;
        long churned = all.stream().filter(c -> !c.active()).count();
        return Math.round((double) churned / all.size() * 10000.0) / 100.0;
    }

    public double getCustomerLifetimeValue() {
        return Math.round(customers.stream().mapToDouble(Customer::totalSpent).average().orElse(0) * 100.0) / 100.0;
    }

    public double getRepeatPurchaseRate(String period) {
        return getRetentionRate(period);
    }

    public int getInactiveCustomers(String period) {
        return (int) filterByPeriod(period).stream().filter(c -> !c.active()).count();
    }

    public List<TopCustomer> getTopCustomers(int limit) {
        return customers.stream()
            .sorted((a, b) -> Double.compare(b.totalSpent(), a.totalSpent()))
            .limit(limit)
            .map(c -> new TopCustomer(c.customerId(), c.name(), c.totalSpent(), c.orderCount()))
            .toList();
    }

    public Map<String, Object> getCustomerSegments() {
        var map = new LinkedHashMap<String, Object>();
        for (String seg : SEGMENTS) {
            var segCusts = customers.stream().filter(c -> c.segment().equals(seg)).toList();
            var segMap = new LinkedHashMap<String, Object>();
            segMap.put("count", segCusts.size());
            segMap.put("avgSpent", Math.round(segCusts.stream().mapToDouble(Customer::totalSpent).average().orElse(0) * 100.0) / 100.0);
            segMap.put("activePct", Math.round((double) segCusts.stream().filter(Customer::active).count() / segCusts.size() * 10000.0) / 100.0);
            map.put(seg, segMap);
        }
        return map;
    }

    public Map<String, Double> getRevenueBySegment(String period) {
        var all = filterByPeriod(period);
        var map = new LinkedHashMap<String, Double>();
        for (String seg : SEGMENTS) {
            double rev = all.stream().filter(c -> c.segment().equals(seg)).mapToDouble(Customer::totalSpent).sum();
            map.put(seg, Math.round(rev * 100.0) / 100.0);
        }
        return map;
    }

    public List<TrendDataPoint> getCustomerTrend(int months) {
        YearMonth end = YearMonth.now();
        YearMonth start = end.minusMonths(months - 1);
        var result = new ArrayList<TrendDataPoint>();
        for (YearMonth ym = start; !ym.isAfter(end); ym = ym.plusMonths(1)) {
            final YearMonth m = ym;
            long count = customers.stream().filter(c -> !c.firstPurchase().isAfter(m)).count();
            result.add(new TrendDataPoint(ym, count, "Total Customers"));
        }
        return result;
    }

    public Map<String, Object> getBuyingPatterns(String segmentId) {
        var segCusts = customers.stream()
            .filter(c -> c.segment().equalsIgnoreCase(segmentId))
            .toList();
        var map = new LinkedHashMap<String, Object>();
        map.put("segment", segmentId);
        map.put("customerCount", segCusts.size());
        map.put("avgOrderCount", Math.round(segCusts.stream().mapToInt(Customer::orderCount).average().orElse(0) * 100.0) / 100.0);
        map.put("avgOrderValue", Math.round(segCusts.stream().mapToDouble(Customer::totalSpent).sum() /
            Math.max(segCusts.stream().mapToInt(Customer::orderCount).sum(), 1) * 100.0) / 100.0);
        map.put("avgLifespanMonths", Math.round(segCusts.stream()
            .mapToLong(c -> java.time.temporal.ChronoUnit.MONTHS.between(c.firstPurchase(), c.lastPurchase()))
            .average().orElse(0) * 100.0) / 100.0);
        map.put("activePct", Math.round((double) segCusts.stream().filter(Customer::active).count() / segCusts.size() * 10000.0) / 100.0);
        return map;
    }

    private List<Customer> filterByPeriod(String period) {
        YearMonth ym = resolveYearMonth(period);
        return customers.stream()
            .filter(c -> c.firstPurchase().equals(ym) || c.lastPurchase().equals(ym) ||
                (!c.firstPurchase().isAfter(ym) && !c.lastPurchase().isBefore(ym)))
            .toList();
    }

    private YearMonth resolveYearMonth(String period) {
        if (period == null || "current".equalsIgnoreCase(period)) return YearMonth.now();
        try { return YearMonth.parse(period, DateTimeFormatter.ofPattern("yyyy-MM")); }
        catch (Exception e) { return YearMonth.now(); }
    }
}
