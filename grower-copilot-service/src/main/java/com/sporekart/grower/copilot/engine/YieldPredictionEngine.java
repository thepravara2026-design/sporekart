package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.YieldPrediction;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class YieldPredictionEngine {

    private static final Logger log = LoggerFactory.getLogger(YieldPredictionEngine.class);

    private final Map<String, SpeciesData> speciesDatabase = new HashMap<>();

    public record YieldRequest(
        String speciesName,
        String substrate,
        double areaSquareMeters,
        int numberOfBags,
        double temperatureCelsius,
        double humidityPercent,
        String season,
        String region,
        Map<String, Object> additionalParams
    ) {}

    public record YieldResponse(
        String predictionId,
        YieldPrediction prediction,
        List<String> recommendations,
        String confidenceLevel
    ) {}

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

    @PostConstruct
    void seedSpeciesData() {
        log.info("Seeding species yield data");
        speciesDatabase.put("oyster", new SpeciesData("Oyster", 2.0, 3.0, 4, 45, 24.0, 85.0, 200.0, 0.45));
        speciesDatabase.put("button", new SpeciesData("Button", 4.0, 5.0, 3, 60, 22.0, 80.0, 300.0, 0.45));
        speciesDatabase.put("milky", new SpeciesData("Milky", 2.0, 3.0, 3, 55, 25.0, 82.0, 250.0, 0.45));
        speciesDatabase.put("shiitake", new SpeciesData("Shiitake", 1.5, 2.0, 3, 70, 20.0, 80.0, 350.0, 0.40));
        speciesDatabase.put("paddystraw", new SpeciesData("Paddy Straw", 2.0, 3.0, 2, 55, 32.0, 85.0, 180.0, 0.50));
        log.info("Species yield data seeded with {} entries", speciesDatabase.size());
    }

    public YieldResponse predictYield(YieldRequest request) {
        log.debug("Predicting yield for species={}, area={}, bags={}", request.speciesName(), request.areaSquareMeters(), request.numberOfBags());
        YieldPrediction prediction = predictYield(
            request.speciesName(), request.substrate(), request.areaSquareMeters(),
            request.numberOfBags(), request.additionalParams() != null ? request.additionalParams() : Map.of()
        );
        List<String> recommendations = generateRecommendations(prediction);
        String confidence = prediction.confidenceLevel() >= 80 ? "HIGH" : prediction.confidenceLevel() >= 60 ? "MEDIUM" : "LOW";
        return new YieldResponse(UUID.randomUUID().toString(), prediction, recommendations, confidence);
    }

    public YieldPrediction predictYield(String speciesName, String substrate, double area, int numberOfBags, Map<String, Object> params) {
        log.debug("Predicting yield for species={}, substrate={}, area={}, bags={}", speciesName, substrate, area, numberOfBags);
        SpeciesData species = findSpecies(speciesName);
        double baseYieldPerBag = (species.spawnToYieldRatioMin + species.spawnToYieldRatioMax) / 2.0;
        double expectedYield = baseYieldPerBag * numberOfBags;

        double temp = getDoubleParam(params, "temperatureCelsius", species.optimalTempCelsius);
        double humidity = getDoubleParam(params, "humidityPercent", species.optimalHumidityPercent);
        double efficiency = calculateEfficiency(expectedYield, area, numberOfBags);
        double tempDeviation = Math.abs(temp - species.optimalTempCelsius);
        double humidityDeviation = Math.abs(humidity - species.optimalHumidityPercent);
        double environmentalFactor = 1.0 - (tempDeviation * 0.02) - (humidityDeviation * 0.005);
        environmentalFactor = Math.max(0.3, Math.min(1.0, environmentalFactor));
        double adjustedYield = expectedYield * environmentalFactor;
        double revenue = estimateRevenue(adjustedYield, speciesName);
        double cost = calculateProductionCost(area, numberOfBags, substrate);
        double profitMargin = calculateProfitMargin(revenue, cost);
        String season = getStringParam(params, "season", "general");
        String region = getStringParam(params, "region", "default");
        double riskScore = calculateRiskScore(speciesName, season, region);
        Map<String, String> harvestWindow = getHarvestWindow(speciesName, substrate);
        int confidence = (int) (environmentalFactor * 100);
        String recommendations = String.format(
            "Optimal temp: %.1f°C, Optimal humidity: %.0f%%. Adjust environment to maximize yield. " +
            "Expected harvest: %s to %s. Risk score: %.2f.",
            species.optimalTempCelsius, species.optimalHumidityPercent,
            harvestWindow.get("start"), harvestWindow.get("end"), riskScore
        );
        return new YieldPrediction(
            UUID.randomUUID().toString(), species.name, substrate, area, numberOfBags,
            Math.round(adjustedYield * 100.0) / 100.0,
            Math.round(efficiency * 100.0) / 100.0,
            Math.round(estimateRevenue(adjustedYield, speciesName) * 100.0) / 100.0,
            Math.round(calculateProductionCost(area, numberOfBags, substrate) * 100.0) / 100.0,
            Math.round(calculateProfitMargin(estimateRevenue(adjustedYield, speciesName), calculateProductionCost(area, numberOfBags, substrate)) * 100.0) / 100.0,
            Math.round(riskScore * 100.0) / 100.0,
            harvestWindow.get("start"), harvestWindow.get("end"),
            confidence, recommendations
        );
    }

    public Map<String, Object> getYieldHistory(String speciesName) {
        log.debug("Fetching yield history for species: {}", speciesName);
        Map<String, Object> history = new LinkedHashMap<>();
        history.put("species", speciesName);
        history.put("averageYieldPerBag", "2.5 kg");
        history.put("bestYieldPerBag", "3.8 kg");
        history.put("worstYieldPerBag", "1.2 kg");
        history.put("totalCyclesCompleted", 12);
        history.put("successRate", "85%");
        return history;
    }

    public double calculateEfficiency(double expectedYield, double area, int bags) {
        log.debug("Calculating efficiency: yield={}, area={}, bags={}", expectedYield, area, bags);
        if (area <= 0 || bags <= 0) return 0.0;
        double yieldPerSqM = expectedYield / area;
        double yieldPerBag = expectedYield / bags;
        double efficiency = ((yieldPerSqM / 10.0) * 0.5 + (yieldPerBag / 3.0) * 0.5) * 100;
        return Math.min(100, Math.max(0, efficiency));
    }

    public double estimateRevenue(double yieldKg, String speciesName) {
        log.debug("Estimating revenue for yield={}kg, species={}", yieldKg, speciesName);
        SpeciesData species = findSpecies(speciesName);
        return yieldKg * species.pricePerKg;
    }

    public double calculateProductionCost(double area, int bags, String substrateType) {
        log.debug("Calculating production cost: area={}, bags={}, substrate={}", area, bags, substrateType);
        double substrateCost = switch (substrateType.toLowerCase()) {
            case "straw" -> 15.0;
            case "sawdust" -> 25.0;
            case "compost" -> 35.0;
            case "cotton waste" -> 20.0;
            default -> 20.0;
        };
        double laborCost = area * 50.0;
        double materialCost = bags * substrateCost;
        double overheadCost = area * 30.0;
        return materialCost + laborCost + overheadCost;
    }

    public double calculateProfitMargin(double revenue, double cost) {
        log.debug("Calculating profit margin: revenue={}, cost={}", revenue, cost);
        if (revenue <= 0) return 0.0;
        return ((revenue - cost) / revenue) * 100.0;
    }

    public double calculateRiskScore(String speciesName, String season, String region) {
        log.debug("Calculating risk score for species={}, season={}, region={}", speciesName, season, region);
        SpeciesData species = findSpecies(speciesName);
        double baseRisk = 0.3;
        double seasonRisk = switch (season.toLowerCase()) {
            case "summer" -> 0.2;
            case "monsoon" -> 0.35;
            case "winter" -> 0.15;
            case "spring" -> 0.1;
            default -> 0.2;
        };
        double regionRisk = switch (region.toLowerCase()) {
            case "north" -> 0.15;
            case "south" -> 0.2;
            case "east" -> 0.25;
            case "west" -> 0.15;
            case "northeast" -> 0.3;
            default -> 0.2;
        };
        double speciesRisk = 1.0 - (species.spawnToYieldRatioMax / 5.0);
        return Math.min(1.0, Math.max(0.0, baseRisk + seasonRisk + regionRisk + speciesRisk));
    }

    public Map<String, String> getHarvestWindow(String speciesName, String substrate) {
        log.debug("Getting harvest window for species={}, substrate={}", speciesName, substrate);
        SpeciesData species = findSpecies(speciesName);
        int duration = species.cycleDurationDays;
        Map<String, String> window = new LinkedHashMap<>();
        window.put("start", (duration - 7) + " days after spawning");
        window.put("end", duration + " days after spawning");
        window.put("durationDays", String.valueOf(duration));
        window.put("cyclesPerYear", String.valueOf(species.cyclesPerYear));
        return window;
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

    private double getDoubleParam(Map<String, Object> params, String key, double defaultValue) {
        if (params != null && params.containsKey(key)) {
            Object val = params.get(key);
            if (val instanceof Number n) return n.doubleValue();
        }
        return defaultValue;
    }

    private String getStringParam(Map<String, Object> params, String key, String defaultValue) {
        if (params != null && params.containsKey(key)) {
            Object val = params.get(key);
            if (val instanceof String s) return s;
        }
        return defaultValue;
    }

    private List<String> generateRecommendations(YieldPrediction prediction) {
        List<String> recs = new java.util.ArrayList<>();
        if (prediction.riskScore() > 0.6) {
            recs.add("High risk detected. Consider delaying planting or choosing a more resilient species.");
        }
        if (prediction.yieldEfficiencyPercent() < 50) {
            recs.add("Efficiency is low. Review environmental controls and substrate quality.");
        }
        if (prediction.profitMargin() < 20) {
            recs.add("Profit margin is below 20%. Review cost structure and pricing strategy.");
        }
        recs.add("Monitor temperature and humidity closely during the growth cycle.");
        recs.add("Harvest at optimal time: " + prediction.harvestWindowStart() + " to " + prediction.harvestWindowEnd());
        return recs;
    }
}
