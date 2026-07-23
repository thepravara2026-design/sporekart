package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.BusinessPlan;
import com.sporekart.grower.copilot.domain.BusinessPlan.ProductionPlanner;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class BusinessPlanningEngine {

    private static final Logger log = LoggerFactory.getLogger(BusinessPlanningEngine.class);

    private final Map<String, MarketData> marketDatabase = new java.util.HashMap<>();

    private final Map<String, SpeciesData> speciesDatabase = new java.util.HashMap<>();

    private record SpeciesData(
        String name,
        double spawnToYieldRatioMin,
        double spawnToYieldRatioMax,
        int cyclesPerYear,
        int cycleDurationDays,
        double optimalTempCelsius,
        double optimalHumidityPercent,
        double pricePerKg,
        double productionCostRatio
    ) {}

    public record PlanningRequest(
        String speciesName,
        String substrateType,
        double farmArea,
        int numberOfBags,
        int plannedCycles,
        String region,
        String marketType,
        String storageDuration,
        String transportDistance
    ) {}

    public record PlanningResponse(
        String planId,
        BusinessPlan businessPlan,
        List<String> warnings,
        String feasibilityRating
    ) {}

    private record MarketData(
        String speciesName,
        double priceMinPerKg,
        double priceMaxPerKg,
        double productionCostRatio,
        List<String> marketInsights,
        Map<Integer, Double> seasonalDemand,
        List<String> packagingOptions,
        String storageAdvice,
        String transportAdvice
    ) {}

    @PostConstruct
    void seedMarketData() {
        log.info("Seeding market data");
        speciesDatabase.put("oyster", new SpeciesData("Oyster", 2.0, 3.0, 4, 45, 24.0, 85.0, 200.0, 0.45));
        speciesDatabase.put("button", new SpeciesData("Button", 4.0, 5.0, 3, 60, 22.0, 80.0, 300.0, 0.45));
        speciesDatabase.put("milky", new SpeciesData("Milky", 2.0, 3.0, 3, 55, 25.0, 82.0, 250.0, 0.45));
        speciesDatabase.put("shiitake", new SpeciesData("Shiitake", 1.5, 2.0, 3, 70, 20.0, 80.0, 350.0, 0.40));
        speciesDatabase.put("paddystraw", new SpeciesData("Paddy Straw", 2.0, 3.0, 2, 55, 32.0, 85.0, 180.0, 0.50));

        Map<Integer, Double> oysterDemand = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) oysterDemand.put(m, (m >= 10 && m <= 12) ? 1.3 : (m >= 6 && m <= 8) ? 0.7 : 1.0);
        marketDatabase.put("oyster", new MarketData("Oyster", 150.0, 300.0, 0.45,
            List.of("Growing demand in urban markets", "Premium pricing for organic oyster mushrooms", "Export potential to Middle East", "High demand in hotel and restaurant sector"),
            oysterDemand,
            List.of("200g punnets for retail", "1kg trays for wholesale", "Vacuum packs for export", "Fresh-cut in polybags"),
            "Store at 2-4°C with 85-90% humidity. Shelf life: 5-7 days refrigerated. Do not wash before storage.",
            "Refrigerated van at 2-4°C. Maximum transit time 24 hours. Use perforated packaging for airflow."
        ));
        Map<Integer, Double> buttonDemand = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) buttonDemand.put(m, (m >= 10 && m <= 2) ? 1.4 : (m >= 6 && m <= 8) ? 0.6 : 1.0);
        marketDatabase.put("button", new MarketData("Button", 200.0, 400.0, 0.45,
            List.of("Highest demand in Indian market", "Preferred in hotels and restaurants", "Export potential to Gulf countries", "Growing demand for canned mushrooms"),
            buttonDemand,
            List.of("250g punnets for retail", "1kg trays for wholesale", "Canned mushrooms for export", "Sliced mushrooms for food service"),
            "Store at 2-4°C with 85-90% humidity. Shelf life: 7-10 days. Do not seal in plastic without perforation.",
            "Refrigerated transport at 2-4°C. Use ventilated crates. Maximum transit 48 hours."
        ));
        Map<Integer, Double> milkyDemand = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) milkyDemand.put(m, (m >= 9 && m <= 12) ? 1.2 : (m >= 5 && m <= 7) ? 0.8 : 1.0);
        marketDatabase.put("milky", new MarketData("Milky", 200.0, 350.0, 0.45,
            List.of("Growing popularity in South India", "Good demand in local markets", "Premium pricing for fresh milky mushrooms", "Limited competition in many regions"),
            milkyDemand,
            List.of("200g punnets", "500g trays", "1kg bulk packs"),
            "Store at 2-4°C with 85-90% humidity. Shelf life: 5-7 days. Handle gently to avoid bruising.",
            "Refrigerated transport at 2-4°C. Maximum transit 24 hours. Use cushioned packaging."
        ));
        Map<Integer, Double> shiitakeDemand = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) shiitakeDemand.put(m, (m >= 10 && m <= 12) ? 1.5 : (m >= 5 && m <= 7) ? 0.7 : 1.0);
        marketDatabase.put("shiitake", new MarketData("Shiitake", 250.0, 500.0, 0.40,
            List.of("Premium market segment", "High demand in upscale restaurants", "Export potential to Europe and USA", "Growing health food market"),
            shiitakeDemand,
            List.of("100g premium packs", "250g retail punnets", "500g wholesale trays", "Dried shiitake packs"),
            "Store at 2-4°C with 85-90% humidity. Shelf life: 7-10 days. Dried shiitake stores up to 6 months in airtight containers.",
            "Refrigerated transport at 2-4°C. Use cushioned packaging. Maximum transit 36 hours for fresh, 7 days for dried."
        ));
        Map<Integer, Double> paddyDemand = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) paddyDemand.put(m, (m >= 3 && m <= 6) ? 1.3 : (m >= 9 && m <= 11) ? 0.7 : 1.0);
        marketDatabase.put("paddystraw", new MarketData("Paddy Straw", 150.0, 250.0, 0.50,
            List.of("Popular in rural markets", "Low production cost", "Short cultivation cycle", "Good for small-scale farmers"),
            paddyDemand,
            List.of("Fresh bunches for local market", "Dried mushrooms for storage", "Value-added mushroom powder"),
            "Store at 4-8°C. Shelf life: 3-5 days fresh. Dry at 45°C for long-term storage.",
            "Ambient transport for short distances. Refrigerated for long distance. Maximum transit 12 hours fresh."
        ));
        log.info("Market data seeded with {} entries", marketDatabase.size());
    }

    public PlanningResponse createBusinessPlan(PlanningRequest request) {
        log.debug("Creating business plan for species={}, area={}, cycles={}", request.speciesName(), request.farmArea(), request.plannedCycles());
        MarketData market = findMarketData(request.speciesName());
        double investment = estimateInvestment(request.farmArea(), request.speciesName(), request.substrateType()).values().stream()
            .mapToDouble(v -> ((Number) v).doubleValue()).sum();
        double expectedYield = estimateYieldForPlan(request);
        double revenue = expectedYield * ((market.priceMinPerKg + market.priceMaxPerKg) / 2.0);
        double roi = calculateROI(investment, revenue);
        int cycleDays = getCycleDays(request.speciesName());
        double breakEven = investment / (revenue / cycleDays);
        List<ProductionPlanner> plans = generateProductionPlan(request.speciesName(), request.plannedCycles(), request.farmArea());
        List<String> insights = getMarketInsights(request.speciesName(), request.region());
        List<String> seasonalDemand = List.of(
            "Peak demand: " + getPeakDemandMonths(request.speciesName()),
            "Off-peak: " + getOffPeakDemandMonths(request.speciesName())
        );
        List<String> pricing = getPricingSuggestions(request.speciesName(), request.region());
        String packaging = getPackagingAdvice(request.speciesName(), request.marketType());
        String storage = getStorageAdvice(request.speciesName(), request.storageDuration());
        String transport = getTransportationAdvice(request.speciesName(), request.transportDistance());
        BusinessPlan plan = new BusinessPlan(
            UUID.randomUUID().toString(), request.speciesName(), investment,
            Math.round(revenue * 100.0) / 100.0, Math.round(roi * 100.0) / 100.0,
            cycleDays, Math.round(breakEven * 100.0) / 100.0,
            plans, insights, seasonalDemand, pricing, packaging, storage, transport
        );
        List<String> warnings = new ArrayList<>();
        if (roi < 15) warnings.add("ROI is below 15%. Consider optimizing costs or choosing a higher-value species.");
        if (request.farmArea() < 50) warnings.add("Small farm area may limit profitability. Consider cooperative models.");
        String feasibility = roi >= 30 ? "HIGH" : roi >= 15 ? "MEDIUM" : "LOW";
        return new PlanningResponse(UUID.randomUUID().toString(), plan, warnings, feasibility);
    }

    public Map<String, Object> estimateInvestment(double farmArea, String speciesName, String substrateType) {
        log.debug("Estimating investment for area={}, species={}, substrate={}", farmArea, speciesName, substrateType);
        Map<String, Object> costs = new LinkedHashMap<>();
        double landPrepCost = farmArea * 100.0;
        double infrastructureCost = farmArea * 500.0;
        double substrateCost = switch (substrateType.toLowerCase()) {
            case "straw" -> farmArea * 200.0;
            case "sawdust" -> farmArea * 300.0;
            case "compost" -> farmArea * 400.0;
            case "cotton waste" -> farmArea * 250.0;
            default -> farmArea * 250.0;
        };
        double spawnCost = farmArea * 150.0;
        double equipmentCost = farmArea * 200.0;
        double laborSetupCost = farmArea * 100.0;
        double miscCost = farmArea * 50.0;
        double total = landPrepCost + infrastructureCost + substrateCost + spawnCost + equipmentCost + laborSetupCost + miscCost;
        costs.put("landPreparation", landPrepCost);
        costs.put("infrastructure", infrastructureCost);
        costs.put("substrate", substrateCost);
        costs.put("spawn", spawnCost);
        costs.put("equipment", equipmentCost);
        costs.put("laborSetup", laborSetupCost);
        costs.put("miscellaneous", miscCost);
        costs.put("totalInvestment", total);
        return costs;
    }

    public double calculateROI(double investment, double expectedRevenue) {
        log.debug("Calculating ROI: investment={}, expectedRevenue={}", investment, expectedRevenue);
        if (investment <= 0) return 0.0;
        return ((expectedRevenue - investment) / investment) * 100.0;
    }

    public List<String> getMarketInsights(String speciesName, String region) {
        log.debug("Fetching market insights for species={}, region={}", speciesName, region);
        MarketData data = findMarketData(speciesName);
        return data.marketInsights();
    }

    public Map<String, Object> getSeasonalDemand(String speciesName, int month) {
        log.debug("Fetching seasonal demand for species={}, month={}", speciesName, month);
        MarketData data = findMarketData(speciesName);
        double demandFactor = data.seasonalDemand().getOrDefault(month, 1.0);
        Map<String, Object> demand = new LinkedHashMap<>();
        demand.put("species", speciesName);
        demand.put("month", month);
        demand.put("demandFactor", demandFactor);
        demand.put("demandLevel", demandFactor >= 1.2 ? "HIGH" : demandFactor >= 0.8 ? "MEDIUM" : "LOW");
        demand.put("priceRange", String.format("₹%.0f-%.0f/kg", data.priceMinPerKg(), data.priceMaxPerKg()));
        return demand;
    }

    public List<String> getPricingSuggestions(String speciesName, String region) {
        log.debug("Fetching pricing suggestions for species={}, region={}", speciesName, region);
        MarketData data = findMarketData(speciesName);
        List<String> suggestions = new ArrayList<>();
        suggestions.add(String.format("Retail price range: ₹%.0f-%.0f per kg", data.priceMinPerKg(), data.priceMaxPerKg()));
        suggestions.add(String.format("Wholesale price: ₹%.0f-%.0f per kg", data.priceMinPerKg() * 0.8, data.priceMaxPerKg() * 0.85));
        suggestions.add("Premium pricing for organic certification: +20-30%");
        suggestions.add("Bulk discounts for regular buyers: 5-10% off");
        suggestions.add("Seasonal pricing: increase by 15-20% during peak demand months");
        return suggestions;
    }

    public String getPackagingAdvice(String speciesName, String marketType) {
        log.debug("Fetching packaging advice for species={}, market={}", speciesName, marketType);
        MarketData data = findMarketData(speciesName);
        if ("retail".equalsIgnoreCase(marketType)) {
            return "Use " + data.packagingOptions().get(0) + ". Ensure proper ventilation holes. Label with variety, weight, price, and harvest date.";
        }
        if ("wholesale".equalsIgnoreCase(marketType)) {
            return "Use " + data.packagingOptions().get(1) + ". Bulk packaging in ventilated crates. Label with batch number and grade.";
        }
        if ("export".equalsIgnoreCase(marketType)) {
            return "Use " + data.packagingOptions().get(2) + ". Vacuum seal for freshness. Include export documentation. Use food-grade packaging materials.";
        }
        return data.packagingOptions().get(0) + " recommended for general use.";
    }

    public String getStorageAdvice(String speciesName, String duration) {
        log.debug("Fetching storage advice for species={}, duration={}", speciesName, duration);
        MarketData data = findMarketData(speciesName);
        if ("short".equalsIgnoreCase(duration) || "short-term".equalsIgnoreCase(duration)) {
            return data.storageAdvice();
        }
        if ("medium".equalsIgnoreCase(duration) || "medium-term".equalsIgnoreCase(duration)) {
            return "Blanch and freeze at -18°C. Shelf life: 6-8 months. Alternatively, dry at 45-50°C for 6-8 hours.";
        }
        if ("long".equalsIgnoreCase(duration) || "long-term".equalsIgnoreCase(duration)) {
            return "Dry at 45-50°C until moisture content <10%. Store in airtight containers in cool, dark place. Shelf life: 12 months. Alternatively, can or pickle for extended storage.";
        }
        return data.storageAdvice();
    }

    public String getTransportationAdvice(String speciesName, String distance) {
        log.debug("Fetching transport advice for species={}, distance={}", speciesName, distance);
        MarketData data = findMarketData(speciesName);
        if ("short".equalsIgnoreCase(distance) || "local".equalsIgnoreCase(distance)) {
            return "Use ventilated crates or baskets. Avoid plastic bags. Transport in cool hours (early morning/evening). Maximum 2 hours transit.";
        }
        if ("medium".equalsIgnoreCase(distance) || "regional".equalsIgnoreCase(distance)) {
            return data.transportAdvice();
        }
        if ("long".equalsIgnoreCase(distance) || "national".equalsIgnoreCase(distance)) {
            return "Refrigerated truck at 2-4°C. Use perforated packaging. Vacuum seal for extended shelf life. Maximum 48 hours transit. Include temperature data loggers.";
        }
        return data.transportAdvice();
    }

    public List<ProductionPlanner> generateProductionPlan(String speciesName, int cycles, double area) {
        log.debug("Generating production plan for species={}, cycles={}, area={}", speciesName, cycles, area);
        List<ProductionPlanner> plans = new ArrayList<>();
        int cycleDays = getCycleDays(speciesName);
        int week = 1;
        for (int c = 1; c <= cycles; c++) {
            plans.add(new ProductionPlanner(week, "Cycle " + c + " - Substrate Preparation",
                List.of("Procure raw materials", "Pasteurize/sterilize substrate", "Cool substrate to spawning temperature"),
                List.of("Substrate material", "Lime for pH adjustment", "Water"),
                "Substrate ready for spawning"
            ));
            week += 1;
            plans.add(new ProductionPlanner(week, "Cycle " + c + " - Spawning",
                List.of("Mix spawn with substrate", "Fill grow bags/ beds", "Incubate at optimal temperature"),
                List.of("Spawn", "Grow bags/trays", "Temperature monitoring equipment"),
                "Spawn run initiated"
            ));
            week += 1;
            plans.add(new ProductionPlanner(week, "Cycle " + c + " - Spawn Run",
                List.of("Monitor temperature and humidity", "Check for contamination", "Maintain dark conditions"),
                List.of("Thermometer", "Hygrometer", "Sprayer for humidity"),
                "Mycelium fully colonized"
            ));
            week += 1;
            plans.add(new ProductionPlanner(week, "Cycle " + c + " - Casing & Pinhead Formation",
                List.of("Apply casing layer", "Maintain high humidity 85-90%", "Introduce fresh air", "Reduce temperature by 2-3°C"),
                List.of("Casing soil", "Sprayer", "Ventilation system"),
                "Pinheads visible"
            ));
            week += 1;
            plans.add(new ProductionPlanner(week, "Cycle " + c + " - Harvesting",
                List.of("Harvest at optimal maturity", "Grade and sort mushrooms", "Pack for market", "Clean growing area"),
                List.of("Harvesting knives", "Grading trays", "Packaging materials"),
                "Harvest complete. Yield recorded."
            ));
            week += 1;
        }
        return plans;
    }

    private double estimateYieldForPlan(PlanningRequest request) {
        SpeciesData species = findSpecies(request.speciesName());
        double baseYieldPerBag = (species.spawnToYieldRatioMin + species.spawnToYieldRatioMax) / 2.0;
        return baseYieldPerBag * request.numberOfBags() * request.plannedCycles();
    }

    private int getCycleDays(String speciesName) {
        SpeciesData species = findSpecies(speciesName);
        return species.cycleDurationDays();
    }

    private String getPeakDemandMonths(String speciesName) {
        return switch (speciesName.toLowerCase()) {
            case "oyster" -> "October to December";
            case "button" -> "October to February";
            case "milky" -> "September to December";
            case "shiitake" -> "October to December";
            case "paddy straw" -> "March to June";
            default -> "October to December";
        };
    }

    private String getOffPeakDemandMonths(String speciesName) {
        return switch (speciesName.toLowerCase()) {
            case "oyster" -> "June to August";
            case "button" -> "June to August";
            case "milky" -> "May to July";
            case "shiitake" -> "May to July";
            case "paddy straw" -> "September to November";
            default -> "June to August";
        };
    }

    private MarketData findMarketData(String speciesName) {
        String key = speciesName.toLowerCase().replaceAll("\\s+", "");
        MarketData data = marketDatabase.get(key);
        if (data == null) {
            log.warn("Market data not found for species: {}, using Oyster as default", speciesName);
            return marketDatabase.get("oyster");
        }
        return data;
    }

    private SpeciesData findSpecies(String speciesName) {
        String key = speciesName.toLowerCase().replaceAll("\\s+", "");
        SpeciesData data = speciesDatabase.get(key);
        if (data == null) {
            log.warn("Species not found: {}, using Oyster as default", speciesName);
            return speciesDatabase.get("oyster");
        }
        return data;
    }
}
