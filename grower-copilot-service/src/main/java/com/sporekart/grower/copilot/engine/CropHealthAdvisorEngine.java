package com.sporekart.grower.copilot.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CropHealthAdvisorEngine {

    private static final Logger log = LoggerFactory.getLogger(CropHealthAdvisorEngine.class);

    private static final Map<String, Map<String, double[]>> OPTIMAL_RANGES = new HashMap<>();

    static {
        var oyster = Map.of(
            "temperature", new double[]{22.0, 28.0},
            "humidity", new double[]{75.0, 85.0},
            "co2", new double[]{400.0, 800.0},
            "light", new double[]{500.0, 2000.0},
            "ph", new double[]{6.0, 7.5},
            "moisture", new double[]{65.0, 70.0}
        );
        var button = Map.of(
            "temperature", new double[]{22.0, 26.0},
            "humidity", new double[]{80.0, 90.0},
            "co2", new double[]{600.0, 1200.0},
            "light", new double[]{200.0, 500.0},
            "ph", new double[]{7.0, 8.0},
            "moisture", new double[]{68.0, 72.0}
        );
        var shiitake = Map.of(
            "temperature", new double[]{18.0, 24.0},
            "humidity", new double[]{80.0, 90.0},
            "co2", new double[]{400.0, 1000.0},
            "light", new double[]{1000.0, 3000.0},
            "ph", new double[]{5.0, 6.5},
            "moisture", new double[]{60.0, 65.0}
        );
        var milky = Map.of(
            "temperature", new double[]{30.0, 35.0},
            "humidity", new double[]{80.0, 85.0},
            "co2", new double[]{400.0, 800.0},
            "light", new double[]{500.0, 1500.0},
            "ph", new double[]{7.0, 8.0},
            "moisture", new double[]{65.0, 70.0}
        );
        var paddyStraw = Map.of(
            "temperature", new double[]{32.0, 38.0},
            "humidity", new double[]{85.0, 90.0},
            "co2", new double[]{400.0, 600.0},
            "light", new double[]{1000.0, 3000.0},
            "ph", new double[]{6.0, 7.5},
            "moisture", new double[]{70.0, 75.0}
        );

        OPTIMAL_RANGES.put("oyster", oyster);
        OPTIMAL_RANGES.put("button", button);
        OPTIMAL_RANGES.put("shiitake", shiitake);
        OPTIMAL_RANGES.put("milky", milky);
        OPTIMAL_RANGES.put("paddy straw", paddyStraw);
    }

    public Map<String, Object> assessCropHealth(
        final String speciesName,
        final String stage,
        final Map<String, Object> conditions
    ) {
        log.debug("Assessing crop health for {} at stage '{}'", speciesName, stage);
        final Map<String, Object> result = new LinkedHashMap<>();
        final int healthScore = calculateHealthScore(speciesName, conditions);
        final List<String> issues = new ArrayList<>();
        final List<String> recommendations = new ArrayList<>();

        final var ranges = getOptimalRanges(speciesName);
        if (ranges != null) {
            for (final var entry : conditions.entrySet()) {
                final String param = entry.getKey().toLowerCase();
                final double value = toDouble(entry.getValue());
                final double[] range = ranges.get(param);
                if (range != null) {
                    if (value < range[0]) {
                        issues.add(param + " is too low (" + value + "). Optimal range: " + range[0] + "-" + range[1]);
                        recommendations.add("Increase " + param + " to " + range[0] + "-" + range[1]);
                    } else if (value > range[1]) {
                        issues.add(param + " is too high (" + value + "). Optimal range: " + range[0] + "-" + range[1]);
                        recommendations.add("Reduce " + param + " to " + range[0] + "-" + range[1]);
                    }
                }
            }
        }

        result.put("species", speciesName);
        result.put("stage", stage);
        result.put("healthScore", healthScore);
        result.put("issues", issues);
        result.put("recommendations", recommendations);
        result.put("status", healthScore >= 70 ? "GOOD" : healthScore >= 40 ? "MODERATE" : "CRITICAL");
        return result;
    }

    public List<Map<String, String>> getHealthChecklist(final String stage) {
        log.debug("Getting health checklist for stage: '{}'", stage);
        final List<Map<String, String>> checklist = new ArrayList<>();
        final String s = stage.toLowerCase();

        if ("spawning".equals(s) || "inoculation".equals(s)) {
            checklist.add(Map.of("item", "Spawn quality check", "status", "PENDING", "description", "Verify spawn lot purity and vitality >90%"));
            checklist.add(Map.of("item", "Substrate sterility", "status", "PENDING", "description", "Confirm autoclave reached 121°C for full duration"));
            checklist.add(Map.of("item", "Aseptic technique", "status", "PENDING", "description", "Verify sterile gown, gloves, mask, ethanol spray"));
            checklist.add(Map.of("item", "Bag sealing", "status", "PENDING", "description", "Check filter patch integrity and heat seal quality"));
            checklist.add(Map.of("item", "Labeling", "status", "PENDING", "description", "Date, species, spawn lot number, batch ID"));
        } else if ("incubation".equals(s) || "spawn run".equals(s)) {
            checklist.add(Map.of("item", "Temperature monitoring", "status", "PENDING", "description", "Maintain 25±2°C; check thrice daily"));
            checklist.add(Map.of("item", "Contamination check", "status", "PENDING", "description", "Daily visual: look for green/black/brown spots"));
            checklist.add(Map.of("item", "CO2 levels", "status", "PENDING", "description", "Ensure CO2 <1000 ppm; ventilate if needed"));
            checklist.add(Map.of("item", "Mycelial growth rate", "status", "PENDING", "description", "Expected 5-8 mm/day; measure weekly"));
            checklist.add(Map.of("item", "Moisture retention", "status", "PENDING", "description", "Check bag weight loss; mist if <60% moisture"));
        } else if ("fruiting".equals(s) || "cropping".equals(s)) {
            checklist.add(Map.of("item", "Humidity control", "status", "PENDING", "description", "Maintain 80-90% RH; avoid free water on caps"));
            checklist.add(Map.of("item", "Light exposure", "status", "PENDING", "description", "500-2000 lux for 8-12h/day for proper development"));
            checklist.add(Map.of("item", "Air exchange", "status", "PENDING", "description", "4-6 air changes per hour to prevent CO2 buildup"));
            checklist.add(Map.of("item", "Pest monitoring", "status", "PENDING", "description", "Check yellow sticky traps; inspect caps for damage"));
            checklist.add(Map.of("item", "Harvest timing", "status", "PENDING", "description", "Monitor cap development; harvest at optimal maturity"));
        } else {
            checklist.add(Map.of("item", "General health check", "status", "PENDING", "description", "Inspect all growing parameters"));
            checklist.add(Map.of("item", "Pest and disease scouting", "status", "PENDING", "description", "Look for any signs of infestation or infection"));
            checklist.add(Map.of("item", "Environmental audit", "status", "PENDING", "description", "Verify all climate control systems functioning"));
        }

        return checklist;
    }

    public Map<String, Object> monitorGrowthParameters(
        final String speciesName,
        final Map<String, Object> currentParams
    ) {
        log.debug("Monitoring growth parameters for {}", speciesName);
        final Map<String, Object> deviations = new LinkedHashMap<>();
        final var ranges = getOptimalRanges(speciesName);

        if (ranges == null) {
            deviations.put("error", "Unknown species: " + speciesName);
            return deviations;
        }

        for (final var entry : currentParams.entrySet()) {
            final String param = entry.getKey().toLowerCase();
            final double value = toDouble(entry.getValue());
            final double[] range = ranges.get(param);
            if (range != null) {
                final Map<String, Object> analysis = new LinkedHashMap<>();
                analysis.put("currentValue", value);
                analysis.put("optimalMin", range[0]);
                analysis.put("optimalMax", range[1]);
                if (value < range[0]) {
                    analysis.put("status", "BELOW_OPTIMAL");
                    analysis.put("deviationPct", Math.round(((range[0] - value) / range[0]) * 100));
                } else if (value > range[1]) {
                    analysis.put("status", "ABOVE_OPTIMAL");
                    analysis.put("deviationPct", Math.round(((value - range[1]) / range[1]) * 100));
                } else {
                    analysis.put("status", "OPTIMAL");
                    analysis.put("deviationPct", 0);
                }
                deviations.put(param, analysis);
            } else {
                deviations.put(param, Map.of("currentValue", value, "status", "UNKNOWN_PARAMETER"));
            }
        }

        return deviations;
    }

    public Map<String, Object> recommendCorrectiveAction(
        final String speciesName,
        final String parameter,
        final double currentValue,
        final double optimalValue
    ) {
        log.debug("Recommending corrective action for {} parameter '{}': {} vs optimal {}", speciesName, parameter, currentValue, optimalValue);
        final Map<String, Object> recommendation = new LinkedHashMap<>();
        recommendation.put("species", speciesName);
        recommendation.put("parameter", parameter);
        recommendation.put("currentValue", currentValue);
        recommendation.put("optimalValue", optimalValue);
        recommendation.put("deviation", currentValue - optimalValue);

        final String param = parameter.toLowerCase();
        final double diff = currentValue - optimalValue;

        if (Math.abs(diff) < optimalValue * 0.05) {
            recommendation.put("action", "NO_ACTION");
            recommendation.put("advice", "Parameter is within acceptable range (5% tolerance). No action needed.");
            return recommendation;
        }

        if (param.contains("temperature") || param.equals("temp")) {
            if (diff > 0) {
                recommendation.put("action", "REDUCE_TEMPERATURE");
                recommendation.put("advice", "Activate cooling system or increase ventilation. Target: reduce by " + String.format("%.1f", Math.abs(diff)) + "°C. Consider evaporative cooling pads, AC, or cool water misting.");
            } else {
                recommendation.put("action", "INCREASE_TEMPERATURE");
                recommendation.put("advice", "Activate heating or reduce ventilation. Target: increase by " + String.format("%.1f", Math.abs(diff)) + "°C. Consider room heaters or heat mats for shelves.");
            }
        } else if (param.contains("humidity") || param.equals("rh")) {
            if (diff > 0) {
                recommendation.put("action", "REDUCE_HUMIDITY");
                recommendation.put("advice", "Increase ventilation and reduce misting frequency. Check for water pooling. Use dehumidifier if necessary. Target: reduce by " + String.format("%.1f", Math.abs(diff)) + "%");
            } else {
                recommendation.put("action", "INCREASE_HUMIDITY");
                recommendation.put("advice", "Increase misting frequency, install humidifier, reduce ventilation. Check floor wetting. Target: increase by " + String.format("%.1f", Math.abs(diff)) + "%");
            }
        } else if (param.contains("co2")) {
            if (diff > 0) {
                recommendation.put("action", "INCREASE_VENTILATION");
                recommendation.put("advice", "Increase air exchange rate. Install exhaust fans. CO2 levels >1000 ppm can cause stunted growth and elongated stipes.");
            } else {
                recommendation.put("action", "REDUCE_VENTILATION");
                recommendation.put("advice", "Reduce air exchange to conserve humidity and temperature. Very low CO2 is uncommon in growing rooms.");
            }
        } else if (param.contains("moisture") || param.contains("water")) {
            if (diff > 0) {
                recommendation.put("action", "REDUCE_WATERING");
                recommendation.put("advice", "Reduce irrigation frequency. Ensure drainage is adequate. Over-saturation promotes bacterial growth and anaerobic conditions.");
            } else {
                recommendation.put("action", "INCREASE_WATERING");
                recommendation.put("advice", "Increase misting or spraying. Substrate moisture below optimal reduces yield. Target moisture: 65-72% depending on species.");
            }
        } else if (param.contains("ph")) {
            if (diff > 0) {
                recommendation.put("action", "LOWER_PH");
                recommendation.put("advice", "Add gypsum or sulfur to casing. Check water pH. Mushrooms generally prefer neutral pH (6.0-8.0 depending on species).");
            } else {
                recommendation.put("action", "RAISE_PH");
                recommendation.put("advice", "Add calcium carbonate (CaCO3) to casing or substrate. Lime water can be used for adjustment.");
            }
        } else if (param.contains("light")) {
            if (diff > 0) {
                recommendation.put("action", "REDUCE_LIGHT");
                recommendation.put("advice", "Shade netting, reduce light hours, or use dimmer controls. Excessive light causes cap discoloration.");
            } else {
                recommendation.put("action", "INCREASE_LIGHT");
                recommendation.put("advice", "Add grow lights (cool white LEDs). Increase photoperiod to 8-12 hours. Adequate light improves cap color and shape.");
            }
        } else {
            recommendation.put("action", "CONSULT_SPECIALIST");
            recommendation.put("advice", "Unknown parameter. Consult SporeKart agronomist for specific guidance on " + parameter);
        }

        return recommendation;
    }

    public List<Map<String, String>> getPreventiveMeasures(final String stage, final String region) {
        log.debug("Getting preventive measures for stage '{}' in region '{}'", stage, region);
        final List<Map<String, String>> measures = new ArrayList<>();
        final String s = stage.toLowerCase();

        if ("spawning".equals(s) || "inoculation".equals(s)) {
            measures.add(Map.of("measure", "Quarantine new spawn batches", "frequency", "Each batch", "priority", "HIGH"));
            measures.add(Map.of("measure", "Surface disinfect work area with 70% ethanol", "frequency", "Before each session", "priority", "HIGH"));
            measures.add(Map.of("measure", "Verify autoclave sterilization logs", "frequency", "Each cycle", "priority", "HIGH"));
            measures.add(Map.of("measure", "Change HEPA filters in inoculation room", "frequency", "Monthly", "priority", "MEDIUM"));
        } else if ("incubation".equals(s) || "spawn run".equals(s)) {
            measures.add(Map.of("measure", "Daily contamination scouting", "frequency", "Daily", "priority", "HIGH"));
            measures.add(Map.of("measure", "Calibrate temperature sensors", "frequency", "Weekly", "priority", "MEDIUM"));
            measures.add(Map.of("measure", "Apply neem-based repellent around room perimeter", "frequency", "Bi-weekly", "priority", "LOW"));
            if (region != null && (region.toLowerCase().contains("karnataka") || region.toLowerCase().contains("tamil"))) {
                measures.add(Map.of("measure", "Additional dehumidifier in monsoon months", "frequency", "Jun-Nov", "priority", "HIGH"));
            }
        } else if ("fruiting".equals(s) || "cropping".equals(s)) {
            measures.add(Map.of("measure", "Install yellow sticky traps for sciarid fly monitoring", "frequency", "Weekly", "priority", "HIGH"));
            measures.add(Map.of("measure", "Apply 150 ppm chlorinated water spray for bacterial control", "frequency", "Alternate days", "priority", "MEDIUM"));
            measures.add(Map.of("measure", "Monitor and record cap development daily", "frequency", "Daily", "priority", "MEDIUM"));
            measures.add(Map.of("measure", "Clean and disinfect harvesting tools", "frequency", "Each use", "priority", "HIGH"));
            measures.add(Map.of("measure", "Apply foot dips (5% formalin) at entry", "frequency", "Daily replacement", "priority", "HIGH"));
        } else {
            measures.add(Map.of("measure", "General sanitation: mop floors with 2% phenol", "frequency", "Daily", "priority", "HIGH"));
            measures.add(Map.of("measure", "Fumigation: 5% formalin + 1% KMnO4, 1 ml/m3", "frequency", "Between cycles", "priority", "HIGH"));
            measures.add(Map.of("measure", "Inspect cooling pads and ventilation ducts", "frequency", "Weekly", "priority", "MEDIUM"));
        }

        if ("high".equalsIgnoreCase(getRegionRiskLevel(region))) {
            measures.add(Map.of("measure", "Enhanced biosecurity: double airlock + UV sterilization", "frequency", "Continuous", "priority", "HIGH"));
        }

        return measures;
    }

    public int calculateHealthScore(final String speciesName, final Map<String, Object> parameters) {
        log.debug("Calculating health score for {}", speciesName);
        final var ranges = getOptimalRanges(speciesName);
        if (ranges == null || parameters.isEmpty()) return 50;

        double totalScore = 0;
        int count = 0;

        for (final var entry : parameters.entrySet()) {
            final String param = entry.getKey().toLowerCase();
            final double value = toDouble(entry.getValue());
            final double[] range = ranges.get(param);
            if (range != null) {
                final double mid = (range[0] + range[1]) / 2;
                final double halfRange = (range[1] - range[0]) / 2;
                final double distance = Math.abs(value - mid);
                final double score;
                if (distance <= halfRange) {
                    score = 100 - (distance / halfRange) * 30;
                } else {
                    score = Math.max(0, 70 - ((distance - halfRange) / mid) * 70);
                }
                totalScore += score;
                count++;
            }
        }

        return count > 0 ? (int) Math.round(totalScore / count) : 50;
    }

    public Map<String, Object> getCropHealthReport(final String speciesName, final String batchId) {
        log.debug("Generating crop health report for {} batch {}", speciesName, batchId);
        final Map<String, Object> report = new LinkedHashMap<>();
        report.put("species", speciesName);
        report.put("batchId", batchId);
        report.put("reportDate", java.time.LocalDate.now().toString());
        report.put("healthStatus", "Report generation requires current growing parameters. Call assessCropHealth() with conditions.");
        report.put("checklist", getHealthChecklist("general"));
        report.put("recommendations", List.of(
            "Maintain daily monitoring logs",
            "Follow the health checklist above",
            "Review environmental control systems",
            "Schedule regular pest and disease scouting",
            "Document any abnormalities for traceability"
        ));
        report.put("nextScheduledReview", java.time.LocalDate.now().plusDays(7).toString());
        return report;
    }

    private Map<String, double[]> getOptimalRanges(final String speciesName) {
        if (speciesName == null) return null;
        final String key = speciesName.toLowerCase().trim();
        for (final var entry : OPTIMAL_RANGES.entrySet()) {
            if (key.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private double toDouble(final Object value) {
        if (value instanceof Number n) return n.doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            log.warn("Cannot convert {} to double", value);
            return 0;
        }
    }

    private String getRegionRiskLevel(final String region) {
        if (region == null) return "MODERATE";
        return switch (region.toLowerCase()) {
            case "karnataka", "tamil nadu", "west bengal", "kerala" -> "HIGH";
            case "himachal pradesh", "jammu and kashmir", "uttarakhand" -> "LOW";
            default -> "MODERATE";
        };
    }
}
