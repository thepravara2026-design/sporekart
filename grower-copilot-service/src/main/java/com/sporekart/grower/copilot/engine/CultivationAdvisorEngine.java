package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.CultivationStage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CultivationAdvisorEngine {

    private static final Logger log = LoggerFactory.getLogger(CultivationAdvisorEngine.class);

    private final Map<String, List<CultivationStage>> speciesLifecycle = new ConcurrentHashMap<>();
    private final Map<String, String> speciesDescriptions = new ConcurrentHashMap<>();
    private final Map<String, Map<String, String>> speciesTroubleshooting = new ConcurrentHashMap<>();
    private final Map<String, Map<String, String>> speciesWatering = new ConcurrentHashMap<>();
    private final Map<String, Map<String, String>> speciesVentilation = new ConcurrentHashMap<>();
    private final Map<String, List<String>> speciesRelatedTopics = new ConcurrentHashMap<>();

    public CultivationAdvisorEngine() {
        log.info("Initializing CultivationAdvisorEngine");
    }

    @PostConstruct
    void init() {
        loadOysterData();
        loadMilkyData();
        loadButtonData();
        loadShiitakeData();
        loadPaddyStrawData();
        log.info("CultivationAdvisorEngine seeded with {} species", speciesLifecycle.size());
    }

    public Map<String, Object> getCultivationOverview(String speciesName) {
        String key = normalize(speciesName);
        List<CultivationStage> stages = speciesLifecycle.get(key);
        if (stages == null) return Map.of("error", "Species not found: " + speciesName);

        Map<String, Object> overview = new LinkedHashMap<>();
        overview.put("speciesName", speciesName);
        overview.put("description", speciesDescriptions.getOrDefault(key, ""));
        overview.put("totalDurationDays", stages.stream().mapToInt(CultivationStage::durationDays).sum());
        overview.put("numberOfStages", stages.size());
        overview.put("stages", stages.stream()
            .sorted(Comparator.comparingInt(CultivationStage::stageOrder))
            .map(s -> Map.of(
                "stageId", s.stageId(),
                "stageName", s.stageName(),
                "stageOrder", s.stageOrder(),
                "description", s.description(),
                "durationDays", s.durationDays()
            ))
            .toList());
        return overview;
    }

    public CultivationStage getStageGuidance(String speciesName, String stageName) {
        String key = normalize(speciesName);
        List<CultivationStage> stages = speciesLifecycle.get(key);
        if (stages == null) return null;
        return stages.stream()
            .filter(s -> s.stageName().equalsIgnoreCase(stageName.trim()))
            .findFirst()
            .orElse(null);
    }

    public Map<String, Object> getCurrentStageAdvice(String speciesName, String currentStage, Map<String, Object> conditions) {
        String key = normalize(speciesName);
        List<CultivationStage> stages = speciesLifecycle.get(key);
        if (stages == null) return Map.of("error", "Species not found");

        CultivationStage stage = stages.stream()
            .filter(s -> s.stageName().equalsIgnoreCase(currentStage.trim()))
            .findFirst().orElse(null);
        if (stage == null) return Map.of("error", "Stage not found: " + currentStage);

        Map<String, Object> advice = new LinkedHashMap<>();
        advice.put("currentStage", stage.stageName());
        advice.put("stageDescription", stage.description());
        advice.put("recommendedTemp", stage.optimalTempCelsius() + " C");
        advice.put("recommendedHumidity", stage.optimalHumidityPercent() + "%");
        advice.put("co2Level", stage.co2Ppm() + " ppm");
        advice.put("lightRequirement", stage.lightRequirement());
        advice.put("ventilation", stage.ventilationRequirement());
        advice.put("checkpoints", stage.checkpoints());
        advice.put("commonIssues", stage.commonIssues());

        if (conditions != null && !conditions.isEmpty()) {
            List<String> deviations = new ArrayList<>();
            if (conditions.containsKey("temp")) {
                double temp = ((Number) conditions.get("temp")).doubleValue();
                if (Math.abs(temp - stage.optimalTempCelsius()) > 3) {
                    deviations.add("Temperature deviation: " + temp + "C (optimal " + stage.optimalTempCelsius() + "C). Adjust heating/cooling.");
                }
            }
            if (conditions.containsKey("humidity")) {
                double hum = ((Number) conditions.get("humidity")).doubleValue();
                if (Math.abs(hum - stage.optimalHumidityPercent()) > 10) {
                    deviations.add("Humidity deviation: " + hum + "% (optimal " + stage.optimalHumidityPercent() + "%). Use humidifier/dehumidifier.");
                }
            }
            advice.put("deviations", deviations);
            advice.put("hasDeviations", !deviations.isEmpty());
        }

        advice.put("nextStages", stages.stream()
            .filter(s -> s.stageOrder() > stage.stageOrder())
            .sorted(Comparator.comparingInt(CultivationStage::stageOrder))
            .map(s -> Map.of("stageName", s.stageName(), "durationDays", s.durationDays()))
            .toList());

        return advice;
    }

    public Map<String, Object> answerCultivationQuestion(String question, String speciesName) {
        String key = normalize(speciesName);
        List<CultivationStage> stages = speciesLifecycle.get(key);
        if (stages == null) return Map.of("error", "Species not found");

        List<String> relatedTopics = speciesRelatedTopics.getOrDefault(key, List.of());
        List<String> citations = stages.stream()
            .map(s -> s.stageName() + ": " + s.description())
            .toList();

        Map<String, Object> answer = new LinkedHashMap<>();
        answer.put("question", question);
        answer.put("speciesName", speciesName);
        answer.put("answer", "Based on " + speciesName + " cultivation data: " + generateAnswer(question, stages));
        answer.put("citations", citations.size() > 3 ? citations.subList(0, 3) : citations);
        answer.put("relatedTopics", relatedTopics);
        return answer;
    }

    public List<String> getTroubleshootingTips(String speciesName, String issue) {
        String key = normalize(speciesName);
        Map<String, String> tipsMap = speciesTroubleshooting.get(key);
        if (tipsMap == null) return List.of("No troubleshooting data for " + speciesName);

        String issueKey = issue.toLowerCase().replaceAll("\\s+", "");
        List<String> results = new ArrayList<>();
        for (var entry : tipsMap.entrySet()) {
            if (entry.getKey().toLowerCase().contains(issueKey) || issueKey.contains(entry.getKey().toLowerCase())) {
                results.add(entry.getValue());
            }
        }
        return results.isEmpty()
            ? List.of("No specific tips found for '" + issue + "'. General advice: check temperature, humidity, and ventilation.")
            : results;
    }

    public Map<String, String> getOptimalConditions(String speciesName, String stage) {
        String key = normalize(speciesName);
        List<CultivationStage> stages = speciesLifecycle.get(key);
        if (stages == null) return Map.of("error", "Species not found");

        CultivationStage s = stages.stream()
            .filter(st -> st.stageName().equalsIgnoreCase(stage.trim()))
            .findFirst().orElse(null);
        if (s == null) return Map.of("error", "Stage not found");

        Map<String, String> conditions = new LinkedHashMap<>();
        conditions.put("temperature", s.optimalTempCelsius() + " C");
        conditions.put("humidity", s.optimalHumidityPercent() + "%");
        conditions.put("co2", s.co2Ppm() + " ppm");
        conditions.put("light", s.lightRequirement());
        conditions.put("ventilation", s.ventilationRequirement());
        return conditions;
    }

    public Map<String, Object> getWaterManagementAdvice(String speciesName, String stage) {
        String key = normalize(speciesName);
        Map<String, String> watering = speciesWatering.get(key);
        if (watering == null) return Map.of("error", "No watering data for " + speciesName);

        String advice = watering.getOrDefault(stage.toLowerCase(), watering.getOrDefault("general", "Maintain recommended humidity levels."));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("speciesName", speciesName);
        result.put("stage", stage);
        result.put("advice", advice);
        result.put("generalGuidelines", List.of(
            "Use clean, non-chlorinated water",
            "Avoid overwatering to prevent bacterial growth",
            "Maintain humidity through misting rather than direct watering where possible",
            "Monitor substrate moisture regularly"
        ));
        return result;
    }

    public Map<String, Object> getVentilationGuide(String speciesName, String stage) {
        String key = normalize(speciesName);
        Map<String, String> ventilation = speciesVentilation.get(key);
        if (ventilation == null) return Map.of("error", "No ventilation data for " + speciesName);

        String advice = ventilation.getOrDefault(stage.toLowerCase(), ventilation.getOrDefault("general", "Maintain adequate fresh air exchange."));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("speciesName", speciesName);
        result.put("stage", stage);
        result.put("recommendation", advice);
        result.put("generalGuidelines", List.of(
            "Avoid direct drafts on growing mushrooms",
            "Use slow-speed fans for gentle air movement",
            "Monitor CO2 levels; above 1200 ppm indicates insufficient ventilation",
            "Increase FAE during pinning and fruiting stages"
        ));
        return result;
    }

    private String generateAnswer(String question, List<CultivationStage> stages) {
        String q = question.toLowerCase();
        if (q.contains("duration") || q.contains("how long") || q.contains("time")) {
            int total = stages.stream().mapToInt(CultivationStage::durationDays).sum();
            return "The complete cultivation cycle takes approximately " + total + " days.";
        }
        if (q.contains("temperature") || q.contains("temp")) {
            CultivationStage s = stages.stream().findFirst().orElse(null);
            if (s != null) return "Optimal temperature range varies by stage. During " + s.stageName() + ", maintain " + s.optimalTempCelsius() + " C.";
        }
        if (q.contains("humidity")) {
            CultivationStage s = stages.stream().findFirst().orElse(null);
            if (s != null) return "Humidity requirement is " + s.optimalHumidityPercent() + "% during " + s.stageName() + ".";
        }
        return "Refer to the stage-specific guidance for detailed recommendations. The lifecycle includes " + stages.size() + " stages.";
    }

    private String normalize(String name) {
        return name.trim().toLowerCase().replaceAll("\\s+", "");
    }

    private void loadOysterData() {
        String key = "oyster";
        speciesDescriptions.put(key, "Oyster mushrooms (Pleurotus ostreatus) are one of the easiest gourmet mushrooms to cultivate. Fast-growing, high-yielding, and versatile across many substrates.");

        speciesLifecycle.put(key, List.of(
            new CultivationStage("oyster_spawn", "Spawn Run", 1,
                "Mycelium colonizes the substrate. Keep in dark, stable conditions.", 14,
                24, 85, "Dark (no light needed)", 800,
                "Minimal ventilation; CO2 buildup helps colonization",
                List.of("Slow colonization", "Contamination (green mold)", "Bacterial blotch"),
                List.of("Check for white mycelium growth daily", "Maintain stable temperature", "No direct light")),
            new CultivationStage("oyster_pin", "Pinning", 2,
                "Initiate fruiting by introducing fresh air, light, and temperature drop.", 5,
                20, 90, "12 hours indirect light/day", 600,
                "Increase fresh air exchange 4-6x/day",
                List.of("No pin formation", "Abnormal pin shapes", "Droopy pins"),
                List.of("Reduce temperature by 3-5C", "Introduce light cycle", "Increase FAE")),
            new CultivationStage("oyster_fruit", "Fruiting", 3,
                "Mushrooms develop from pins to full-sized harvest-ready fruiting bodies.", 7,
                18, 85, "12 hours indirect light/day", 500,
                "High FAE 6-8x/day; avoid CO2 buildup",
                List.of("Long thin stems (CO2 too high)", "Yellowing caps", "Cracking caps"),
                List.of("Monitor cap development", "Check for off-odors", "Maintain high humidity")),
            new CultivationStage("oyster_harvest", "Harvest", 4,
                "Harvest when caps flatten but before spores drop. Twist and pull whole clusters.", 2,
                16, 80, "Ambient light", 500,
                "Reduce FAE slightly to prevent drying",
                List.of("Spore drop (white dust)", "Over-mature mushrooms", "Pest infestation"),
                List.of("Harvest before edges curl up", "Store at 2-4C", "Use within 5-7 days"))
        ));

        speciesTroubleshooting.put(key, Map.ofEntries(
            Map.entry("slowcolonization", "Increase temperature by 1-2C and check moisture content (should be 65-70%)."),
            Map.entry("contamination", "Remove contaminated substrate immediately. Sterilize equipment. Reduce spawn inoculation time."),
            Map.entry("nopins", "Introduce cold shock (drop temp by 5C for 48h), increase light, and boost fresh air exchange."),
            Map.entry("longthin", "Increase fresh air exchange. CO2 levels above 800 ppm cause elongated stems."),
            Map.entry("yellowcaps", "Reduce light intensity or duration. Ensure humidity is above 80%."),
            Map.entry("cracking", "Increase humidity to 85-90%. Reduce direct airflow on mushrooms.")
        ));

        speciesWatering.put(key, Map.of(
            "spawnrun", "No direct watering needed during spawn run. Maintain humidity through substrate moisture.",
            "pinning", "Mist lightly 2-3 times daily to maintain 90% humidity. Avoid pooling water.",
            "fruiting", "Mist 3-4 times daily. Use fine mist to avoid bruising. Keep humidity at 85%.",
            "harvest", "Reduce misting to 1-2 times daily. Allow slight drying before harvest.",
            "general", "Oyster mushrooms require consistent high humidity. Use automated misting systems for best results."
        ));

        speciesVentilation.put(key, Map.of(
            "spawnrun", "Very minimal FAE. Maintain CO2 around 800 ppm. Seal bags but allow minimal gas exchange.",
            "pinning", "Increase FAE to 4-6 air exchanges per day. CO2 target: 600 ppm. Use slow fan.",
            "fruiting", "High FAE: 6-8 exchanges per day. Critical to keep CO2 below 600 ppm.",
            "harvest", "Moderate FAE: 3-4 exchanges per day. Reduce to prevent drying of mature mushrooms.",
            "general", "Oyster mushrooms are highly sensitive to CO2. Proper ventilation is critical for quality fruiting bodies."
        ));

        speciesRelatedTopics.put(key, List.of("substrate preparation", "humidity management", "pest control", "harvesting techniques", "second flush induction"));
    }

    private void loadMilkyData() {
        String key = "milky";
        speciesDescriptions.put(key, "Milky mushrooms (Calocybe indica) are tropical, heat-tolerant mushrooms ideal for Indian climates. Known for high yield, long shelf life, and white color.");

        speciesLifecycle.put(key, List.of(
            new CultivationStage("milky_spawn", "Spawn Run", 1,
                "Mycelium colonization of substrate. Keep in dark at 30-35C.", 18,
                32, 80, "Complete darkness", 1000,
                "Minimal ventilation; seal bags tightly",
                List.of("Slow growth", "Bacterial contamination", "Dry substrate"),
                List.of("Monitor temperature daily", "Check for uniform colonization", "No disturbance")),
            new CultivationStage("milky_casing", "Casing", 2,
                "Apply 3-4 cm sterilized casing layer (garden soil + sand). Maintain high humidity.", 7,
                30, 90, "Diffuse light 8 hours/day", 700,
                "Gentle FAE 2-3x/day",
                List.of("Cracking casing layer", "Delayed pinning", "Algae growth on casing"),
                List.of("Keep casing uniformly moist", "Avoid direct watering", "Monitor for weeds")),
            new CultivationStage("milky_pin", "Pinning", 3,
                "Pinhead initiation after casing. Requires temperature drop and light stimulus.", 6,
                28, 90, "12 hours indirect light/day", 600,
                "Moderate FAE 4-5x/day",
                List.of("No pin formation", "Abnormal pin clusters", "Pin abortion"),
                List.of("Maintain 90% humidity", "Ensure light penetration", "Avoid temperature spikes")),
            new CultivationStage("milky_fruit", "Fruiting", 4,
                "Mushrooms elongate and caps expand. Remove when stems reach 8-12 cm.", 10,
                28, 85, "12 hours indirect light/day", 500,
                "High FAE 5-6x/day",
                List.of("Bacterial soft rot", "Stem discoloration", "Small caps"),
                List.of("Harvest before caps open fully", "Check for off-odors", "Maintain cleanliness")),
            new CultivationStage("milky_harvest", "Harvest", 5,
                "Harvest by twisting gently at base. Milky mushrooms have excellent shelf life of 7-10 days.", 2,
                25, 80, "Ambient light", 500,
                "Reduce FAE to prevent drying",
                List.of("Bruising during harvest", "Post-harvest browning", "Storage rot"),
                List.of("Cool immediately to 4C", "Pack in perforated bags", "Avoid stacking"))
        ));

        speciesTroubleshooting.put(key, Map.ofEntries(
            Map.entry("slowgrowth", "Increase temperature to 32-35C. Milky requires warm conditions for optimal growth."),
            Map.entry("nopins", "Ensure temperature drop of 3-5C. Increase light exposure to 12 hours daily."),
            Map.entry("bacterialrot", "Reduce humidity to 80%. Improve air circulation. Remove affected mushrooms."),
            Map.entry("smallcaps", "Increase FAE. Ensure adequate light. Check for nutrient deficiency in substrate."),
            Map.entry("browning", "Harvest earlier. Reduce temperature during storage. Handle gently."),
            Map.entry("casingcracking", "Mist casing layer more frequently. Ensure casing is 3-4 cm thick.")
        ));

        speciesWatering.put(key, Map.of(
            "spawnrun", "No watering during spawn run. Substrate should be at 60% moisture initially.",
            "casing", "Mist casing layer daily to keep it moist. Never let it dry out.",
            "pinning", "Mist 3-4 times daily. Maintain 90% humidity through fine misting.",
            "fruiting", "Mist 2-3 times daily. Reduce if bacterial issues appear.",
            "harvest", "Stop misting 24 hours before harvest. Keep humidity above 75%.",
            "general", "Milky mushrooms need consistent moisture. Use well water or RO water."
        ));

        speciesVentilation.put(key, Map.of(
            "spawnrun", "Minimal ventilation. CO2 can rise to 1000 ppm during colonization.",
            "casing", "Introduce gentle air exchange 2-3 times daily. CO2 target: 700 ppm.",
            "pinning", "Increase FAE to 4-5 exchanges daily. CO2 target: below 600 ppm.",
            "fruiting", "High FAE 5-6 exchanges daily. Vital for proper stem development.",
            "harvest", "Moderate FAE. Reduce to maintain humidity in the growing room.",
            "general", "Milky mushrooms tolerate higher CO2 than Oyster but still need good ventilation."
        ));

        speciesRelatedTopics.put(key, List.of("casing soil preparation", "tropical mushroom cultivation", "substrate for milky", "harvest indicators", "market preparation"));
    }

    private void loadButtonData() {
        String key = "button";
        speciesDescriptions.put(key, "Button mushrooms (Agaricus bisporus) are the most commercially cultivated mushrooms worldwide. Require compost-based substrate and precise environmental control.");

        speciesLifecycle.put(key, List.of(
            new CultivationStage("button_spawn", "Spawn Run", 1,
                "Mycelium growth through pasteurized compost. Maintain 22-25C in dark.", 16,
                24, 85, "Complete darkness", 900,
                "Minimal ventilation; maintain CO2 800-900 ppm",
                List.of("Inconsistent colonization", "Competitor molds", "Overheating"),
                List.of("Check compost temperature daily", "Uniform spawn distribution", "Maintain humidity")),
            new CultivationStage("button_casing", "Casing Application", 2,
                "Apply 4-5 cm peat-lime casing layer. Keep moist at all times.", 5,
                22, 92, "Dark", 800,
                "Gentle ventilation 1-2x/day",
                List.of("Cracking casing", "Delayed pinning", "Algae/weed molds"),
                List.of("Casing pH should be 7.0-7.5", "Maintain even moisture", "Apply evenly")),
            new CultivationStage("button_pin", "Pinning", 3,
                "Pinhead initiation requires CO2 drop to 600 ppm and temperature reduction to 18C.", 8,
                18, 90, "Diffuse light 8-10 hours/day", 600,
                "Moderate FAE 3-4x/day",
                List.of("No pins or sparse pinning", "Pin abortion", "Over-pinning"),
                List.of("Reduce temperature gradually", "Increase FAE", "Maintain casing moisture")),
            new CultivationStage("button_fruit", "Fruiting", 4,
                "Mushrooms develop in flushes. First flush yields highest harvest.", 12,
                17, 85, "10-12 hours indirect light/day", 500,
                "Good FAE 4-5x/day; avoid drafts",
                List.of("Open caps prematurely", "Staining on caps", "Small mushrooms"),
                List.of("Harvest before veil breaks", "Maintain consistent conditions", "Water casing lightly")),
            new CultivationStage("button_harvest", "Harvest", 5,
                "Harvest by twisting gently. Three flushes over 21 days. Re-casing between flushes.", 21,
                16, 80, "Ambient light", 500,
                "Moderate FAE 3x/day post-harvest",
                List.of("Low second flush yield", "Post-harvest browning", "Pest infestation"),
                List.of("Remove stumps after harvest", "Re-casing for second flush", "Cool immediately"))
        ));

        speciesTroubleshooting.put(key, Map.ofEntries(
            Map.entry("nopins", "Ensure CO2 dropped to 600 ppm. Casing may be too dry or too wet. Check temperature is 16-18C."),
            Map.entry("competitormolds", "Improve pasteurization of compost. Reduce spawn run temperature. Increase ventilation."),
            Map.entry("opencaps", "Harvest earlier. Mushrooms are maturing faster than expected. Check temperature."),
            Map.entry("smallmushrooms", "Check nutrient content of compost. Reduce pin density. Ensure adequate humidity."),
            Map.entry("staining", "Avoid watering directly on caps. Handle gently during harvest. Reduce humidity swings."),
            Map.entry("lowsecondflush", "Apply fresh casing layer. Maintain moisture. Allow 5-7 days rest between flushes.")
        ));

        speciesWatering.put(key, Map.of(
            "spawnrun", "No watering. Compost should be at 68-70% moisture at spawning.",
            "casingapplication", "Water casing layer to field capacity. Maintain by light daily misting.",
            "pinning", "Light misting only. Keep casing moist but not saturated.",
            "fruiting", "Water casing lightly between flushes. Avoid wetting mushrooms.",
            "harvest", "Reduce watering before harvest. Water casing after harvesting each flush.",
            "general", "Button mushrooms need consistent casing moisture. Use clean water only."
        ));

        speciesVentilation.put(key, Map.of(
            "spawnrun", "Minimum ventilation. Maintain CO2 at 800-900 ppm.",
            "casingapplication", "Introduce gentle fresh air 1-2 times daily.",
            "pinning", "Critical stage. Increase FAE to achieve 600 ppm CO2.",
            "fruiting", "Maintain steady FAE 4-5 exchanges daily. Avoid temperature fluctuations.",
            "harvest", "Moderate ventilation. Reduce to maintain humidity during harvest.",
            "general", "Button mushrooms require careful CO2 management. Monitor levels continuously."
        ));

        speciesRelatedTopics.put(key, List.of("compost preparation", "casing soil recipe", "flush management", "IPM for button mushrooms", "commercial harvesting"));
    }

    private void loadShiitakeData() {
        String key = "shiitake";
        speciesDescriptions.put(key, "Shiitake mushrooms (Lentinula edodes) are prized for their flavor and medicinal properties. Can be cultivated on logs or sawdust blocks.");

        speciesLifecycle.put(key, List.of(
            new CultivationStage("shiitake_spawn", "Spawn Run", 1,
                "Mycelium colonization of logs or substrate blocks. Keep shaded and humid.", 60,
                22, 80, "Shade/indirect light", 900,
                "Low ventilation; stacking allows natural air flow",
                List.of("Slow colonization", "Trichoderma contamination", "Insufficient moisture"),
                List.of("Stack logs in crib style", "Maintain 50-60% log moisture", "Monitor for contamination")),
            new CultivationStage("shiitake_incubation", "Incubation", 2,
                "Full colonization of substrate. Mycelium browning begins.", 90,
                20, 75, "Indirect light", 800,
                "Low ventilation; occasional rotation of logs",
                List.of("Partial colonization", "Bark peeling", "Pest damage"),
                List.of("Rotate logs every 30 days", "Maintain bark integrity", "Shade from direct sun")),
            new CultivationStage("shiitake_browning", "Browning", 3,
                "Mycelium forms protective brown layer. Critical for fruiting induction.", 30,
                18, 85, "Increased light 8-10 hours/day", 600,
                "Moderate FAE 3-4x/day; simulate rain",
                List.of("No browning", "Excessive browning", "Cracking"),
                List.of("Increase light exposure", "Soak logs if too dry", "Protect from wind")),
            new CultivationStage("shiitake_pin", "Pinning", 4,
                "Pin formation after cold shock or soaking. Fruiting bodies emerge.", 10,
                15, 90, "12 hours light/day", 500,
                "High FAE 5-6x/day",
                List.of("No pin formation", "Deformed pins", "Cluster pinning"),
                List.of("Cold shock: soak in cold water 12-24h", "Increase FAE", "Maintain high humidity")),
            new CultivationStage("shiitake_fruit", "Fruiting", 5,
                "Mushrooms develop caps with characteristic brown scales.", 10,
                16, 85, "12 hours indirect light/day", 500,
                "High FAE 6-8x/day",
                List.of("Long stems", "Flat caps", "Cracking caps"),
                List.of("Harvest when caps 70-80% open", "Maintain FAE", "Avoid overwatering caps")),
            new CultivationStage("shiitake_harvest", "Harvest & Rest", 6,
                "Harvest by cutting stems. Rest logs 2-3 months between flushes.", 90,
                18, 70, "Shade/rest condition", 700,
                "Reduced ventilation; let logs rest",
                List.of("Declining yields", "Pest infestation", "Log degradation"),
                List.of("Allow logs to dry partially", "Move logs to shade", "Rest 8-12 weeks"))
        ));

        speciesTroubleshooting.put(key, Map.ofEntries(
            Map.entry("slowcolonization", "Ensure log moisture is 50-60%. Increase temperature to 20-24C. Check for bacterial contamination."),
            Map.entry("nopins", "Logs need cold shock or soaking. Submerge in cold water for 12-24 hours. Increase light."),
            Map.entry("trichoderma", "Remove affected logs. Improve air circulation. Reduce spawn run temperature."),
            Map.entry("longstems", "Increase light intensity and duration. CO2 may be too high. Improve ventilation."),
            Map.entry("flatcaps", "Reduce humidity to 80-85%. Increase air circulation. Check for genetic factors."),
            Map.entry("decliningyields", "Logs may be exhausted. Maximum yield over 3-5 years. Consider replacing old logs.")
        ));

        speciesWatering.put(key, Map.of(
            "spawnrun", "Keep log moisture at 50-60%. Mist logs if they dry out. Stack to retain moisture.",
            "incubation", "Monitor moisture monthly. Mist if bark cracks. Maintain 50% log moisture.",
            "browning", "Light misting to encourage browning. Soak logs if too dry.",
            "pinning", "Soak logs for 12-24 hours if not producing pins. Mist heavily to maintain 90% humidity.",
            "fruiting", "Mist 3-4 times daily between fruiting cycles. Soak logs after each flush.",
            "harvest&rest", "Reduce watering. Let logs dry to 40% moisture for rest period.",
            "general", "Shiitake logs need periodic soaking to maintain productivity. Use clean, non-chlorinated water."
        ));

        speciesVentilation.put(key, Map.of(
            "spawnrun", "Natural air flow through log stacking is sufficient. Avoid enclosed spaces.",
            "incubation", "Low ventilation. Rotate logs monthly for even exposure.",
            "browning", "Increase air movement. Moderate FAE 3-4 exchanges daily.",
            "pinning", "High FAE needed. 5-6 exchanges daily. CO2 target below 500 ppm.",
            "fruiting", "Very high FAE 6-8 exchanges daily. Critical for cap development.",
            "harvest&rest", "Reduce ventilation during rest. Outdoor log cultivation provides natural air flow.",
            "general", "Outdoor cultivation relies on natural ventilation. Indoor requires careful FAE management."
        ));

        speciesRelatedTopics.put(key, List.of("log preparation and selection", "sawdust block cultivation", "cold shocking technique", "shiitake medicinal properties", "seasonal management"));
    }

    private void loadPaddyStrawData() {
        String key = "padrystraw";
        speciesDescriptions.put(key, "Paddy Straw mushrooms (Volvariella volvacea) are tropical mushrooms grown on paddy straw. Fast-growing cycle of 20-25 days from spawning to harvest.");

        speciesLifecycle.put(key, List.of(
            new CultivationStage("padry_spawn", "Spawn Run", 1,
                "Mycelium colonization of pasteurized paddy straw. High temperature tolerance.", 7,
                35, 85, "Dark conditions", 1000,
                "Minimal ventilation; sealed beds",
                List.of("Overheating (>40C)", "Bacterial rot", "Uneven colonization"),
                List.of("Monitor bed temperature", "Maintain 65% straw moisture", "Spread spawn evenly")),
            new CultivationStage("padry_pin", "Pinning", 2,
                "Pin formation after 7-8 days. Requires light and slight temperature drop.", 4,
                32, 90, "10-12 hours indirect light/day", 600,
                "Moderate FAE 3-4x/day",
                List.of("No pin formation", "Pin abortion in heat", "Excessive pinning"),
                List.of("Introduce light gradually", "Maintain humidity", "Avoid temperature >38C")),
            new CultivationStage("padry_fruit", "Fruiting", 3,
                "Rapid development from pins to egg stage. Harvest as eggs or slightly opened.", 5,
                30, 88, "12 hours indirect light/day", 500,
                "Moderate FAE 4-5x/day",
                List.of("Open caps too early", "Small size", "Yellowing"),
                List.of("Harvest in egg stage for premium price", "Maintain high humidity", "Quick cooling after harvest")),
            new CultivationStage("padry_harvest", "Harvest", 4,
                "Harvest in 2-3 flushes. Total cycle 20-25 days. Highest yield in first flush.", 7,
                28, 80, "Ambient light", 500,
                "Reduce FAE post-harvest",
                List.of("Low second flush", "Pest damage", "Rapid spoilage"),
                List.of("Harvest twice daily", "Cool to 4C immediately", "Market within 24 hours"))
        ));

        speciesTroubleshooting.put(key, Map.ofEntries(
            Map.entry("overheating", "Reduce bed thickness to 15-20 cm. Improve ventilation. Monitor temperature twice daily."),
            Map.entry("nopin", "Temperature may be too high (>38C). Ensure light exposure. Check straw moisture."),
            Map.entry("earlyopening", "Harvest sooner. Paddy straw mushrooms mature rapidly in the egg stage."),
            Map.entry("smallsize", "Check nutrient content of straw. Add rice bran supplement. Maintain optimal temperature."),
            Map.entry("yellowing", "Reduce light intensity. Check for bacterial infection. Harvest earlier in development."),
            Map.entry("rapidspoilage", "Cool immediately after harvest. Paddy straw has very short shelf life (24-48 hours).")
        ));

        speciesWatering.put(key, Map.of(
            "spawnrun", "No watering. Straw should be at 65% moisture from initial pasteurization.",
            "pinning", "Mist lightly 2-3 times daily. Maintain 90% humidity in growing room.",
            "fruiting", "Mist 3-4 times daily. Ensure good air circulation after misting.",
            "harvest", "Reduce misting. Keep humidity above 75%.",
            "general", "Paddy straw mushrooms need consistent high humidity. Avoid water pooling on beds."
        ));

        speciesVentilation.put(key, Map.of(
            "spawnrun", "Beds can be covered. Minimal FAE required. Maintain CO2 around 1000 ppm.",
            "pinning", "Uncover beds and introduce FAE 3-4 times daily. CO2 target: 600 ppm.",
            "fruiting", "Maintain FAE 4-5 exchanges daily. Good air circulation prevents bacterial rot.",
            "harvest", "Moderate FAE. Reduce drafts that dry out mushrooms.",
            "general", "Tropical conditions require balancing high humidity with adequate ventilation."
        ));

        speciesRelatedTopics.put(key, List.of("paddy straw preparation", "tropical mushroom farming", "short cycle cultivation", "egg stage harvesting", "value added products"));
    }
}
