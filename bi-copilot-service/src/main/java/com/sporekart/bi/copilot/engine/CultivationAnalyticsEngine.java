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

import com.sporekart.bi.copilot.domain.CultivationAnalytics;
import com.sporekart.bi.copilot.domain.TrendDataPoint;

@Component
public class CultivationAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(CultivationAnalyticsEngine.class);
    private static final Random RANDOM = new Random(303);
    private static final DateTimeFormatter PERIOD_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final List<String> SPECIES = List.of(
        "Oyster", "Shiitake", "Button", "King Trumpet", "Enoki"
    );

    private static final List<String> REGIONS = List.of("North", "South", "East", "West", "Central");
    private static final List<String> DISEASES = List.of("Green Mold", "Cobweb Mold", "Bacterial Blotch", "Trichoderma", "Verticillium");

    private static final double[] YIELD_BY_SPECIES = {3200, 2800, 3500, 2200, 1800};
    private static final double[] CYCLE_DAYS = {28, 42, 35, 38, 30};
    private static final double[] CONTAMINATION_RATES = {0.04, 0.06, 0.08, 0.05, 0.07};

    private final List<SeedCultivationMonth> seedData = generateSeedData();

    public CultivationAnalyticsEngine() {
        log.info("CultivationAnalyticsEngine initialized with {} months of seed data", seedData.size());
    }

    public CultivationAnalytics getCultivationSummary(String period) {
        SeedCultivationMonth sm = resolvePeriod(period);
        if (sm == null) return emptyAnalytics(period);

        return new CultivationAnalytics(
            UUID.randomUUID().toString(),
            period,
            sm.totalYieldKg,
            sm.totalYieldKg / Math.max(1, sm.totalGrowers),
            sm.yieldBySpecies,
            sm.yieldByRegion,
            sm.averageCycleDays,
            sm.contaminationRate * 100,
            sm.diseaseIncidence,
            sm.totalGrowers,
            sm.revenueByRegion,
            4.2 + RANDOM.nextDouble() * 0.6
        );
    }

    public Map<String, Double> getYieldBySpecies(String period) {
        SeedCultivationMonth sm = resolvePeriod(period);
        return sm != null ? sm.yieldBySpecies : Map.of();
    }

    public Map<String, Double> getYieldByRegion(String period) {
        SeedCultivationMonth sm = resolvePeriod(period);
        return sm != null ? sm.yieldByRegion : Map.of();
    }

    public double getAverageCycleTime(String species) {
        if (species == null || species.isBlank()) {
            return seedData.stream().mapToDouble(s -> s.averageCycleDays).average().orElse(0);
        }
        for (int i = 0; i < SPECIES.size(); i++) {
            if (SPECIES.get(i).equalsIgnoreCase(species)) {
                return CYCLE_DAYS[i] + RANDOM.nextDouble() * 4 - 2;
            }
        }
        return 0;
    }

    public List<TrendDataPoint> getContaminationRateTrend(int months) {
        int count = Math.min(months, seedData.size());
        List<SeedCultivationMonth> slice = seedData.subList(seedData.size() - count, seedData.size());
        List<TrendDataPoint> trends = new ArrayList<>();

        for (int i = 0; i < slice.size(); i++) {
            SeedCultivationMonth sm = slice.get(i);
            double ma = i > 0
                ? (sm.contaminationRate + slice.get(i - 1).contaminationRate) / 2.0
                : sm.contaminationRate;

            trends.add(new TrendDataPoint(
                UUID.randomUUID().toString(),
                "contamination_rate",
                sm.periodLabel,
                sm.contaminationRate * 100,
                ma * 100,
                1.0,
                0.060 * 100,
                (sm.contaminationRate - 0.060) * 100,
                sm.contaminationRate <= 0.060 ? "down" : "up",
                i > 0 ? (sm.contaminationRate - slice.get(i - 1).contaminationRate) / slice.get(i - 1).contaminationRate * 100 : 0,
                OffsetDateTime.now()
            ));
        }
        return trends;
    }

    public Map<String, Double> getDiseaseIncidenceRate() {
        Map<String, Double> incidence = new LinkedHashMap<>();
        for (String disease : DISEASES) {
            incidence.put(disease, Math.round((0.02 + RANDOM.nextDouble() * 0.08) * 10000) / 100.0);
        }
        return incidence;
    }

    public double getGrowerSatisfaction() {
        return Math.round((4.0 + RANDOM.nextDouble() * 0.8) * 100) / 100.0;
    }

    public List<Map<String, Object>> getTopGrowers(int limit) {
        List<Map<String, Object>> growers = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            Map<String, Object> grower = new LinkedHashMap<>();
            grower.put("growerId", "GRW-" + (1000 + i));
            grower.put("name", "Grower " + (i + 1));
            grower.put("totalYieldKg", Math.round((100 + RANDOM.nextDouble() * 900) * 100) / 100.0);
            grower.put("revenue", Math.round((50000 + RANDOM.nextDouble() * 500000) * 100) / 100.0);
            grower.put("satisfactionScore", Math.round((3.5 + RANDOM.nextDouble() * 1.5) * 100) / 100.0);
            grower.put("contaminationRate", Math.round((0.02 + RANDOM.nextDouble() * 0.10) * 10000) / 100.0);
            grower.put("region", REGIONS.get(RANDOM.nextInt(REGIONS.size())));
            growers.add(grower);
        }
        growers.sort((a, b) -> Double.compare(
            (Double) b.get("revenue"),
            (Double) a.get("revenue")
        ));
        return growers;
    }

    private SeedCultivationMonth resolvePeriod(String period) {
        if (period == null || period.isBlank()) return seedData.getLast();
        for (SeedCultivationMonth sm : seedData) {
            if (sm.periodLabel.equals(period)) return sm;
        }
        return null;
    }

    private CultivationAnalytics emptyAnalytics(String period) {
        return new CultivationAnalytics(
            UUID.randomUUID().toString(), period, 0, 0, Map.of(), Map.of(), 0, 0, 0, 0, Map.of(), 0
        );
    }

    private List<SeedCultivationMonth> generateSeedData() {
        List<SeedCultivationMonth> data = new ArrayList<>();
        LocalDate base = LocalDate.of(2025, 1, 1);

        for (int i = 0; i < 12; i++) {
            LocalDate monthStart = base.plusMonths(i);
            String label = monthStart.format(PERIOD_FMT);

            Map<String, Double> yieldBySpecies = new LinkedHashMap<>();
            for (int s = 0; s < SPECIES.size(); s++) {
                double yield = YIELD_BY_SPECIES[s] + RANDOM.nextDouble() * 400 - 200
                    + (i * 20) + RANDOM.nextDouble() * 50;
                yieldBySpecies.put(SPECIES.get(s), Math.round(yield * 100) / 100.0);
            }

            double totalYield = yieldBySpecies.values().stream().mapToDouble(Double::doubleValue).sum();
            int growers = 50 + (i * 3) + RANDOM.nextInt(5);

            Map<String, Double> yieldByRegion = new LinkedHashMap<>();
            double remaining = totalYield;
            for (int r = 0; r < REGIONS.size(); r++) {
                double share = r < REGIONS.size() - 1
                    ? remaining * (0.15 + RANDOM.nextDouble() * 0.08)
                    : remaining;
                yieldByRegion.put(REGIONS.get(r), Math.round(share * 100) / 100.0);
                remaining -= share;
            }

            double avgCycle = 34.0 + RANDOM.nextDouble() * 6;
            double contaminationRate = 0.04 + RANDOM.nextDouble() * 0.06 - (i * 0.001);
            contaminationRate = Math.max(0.01, contaminationRate);
            double diseaseIncidence = 0.03 + RANDOM.nextDouble() * 0.05;

            Map<String, Double> revByRegion = new LinkedHashMap<>();
            for (int r = 0; r < REGIONS.size(); r++) {
                revByRegion.put(REGIONS.get(r),
                    Math.round(yieldByRegion.get(REGIONS.get(r)) * (250 + RANDOM.nextDouble() * 100) * 100) / 100.0);
            }

            data.add(new SeedCultivationMonth(
                i, label, totalYield, yieldBySpecies, yieldByRegion,
                avgCycle, contaminationRate, diseaseIncidence, growers, revByRegion
            ));
        }
        return data;
    }

    private record SeedCultivationMonth(
        int monthIndex,
        String periodLabel,
        double totalYieldKg,
        Map<String, Double> yieldBySpecies,
        Map<String, Double> yieldByRegion,
        double averageCycleDays,
        double contaminationRate,
        double diseaseIncidence,
        int totalGrowers,
        Map<String, Double> revenueByRegion
    ) {}
}
