package com.sporekart.trainer.copilot.engine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PracticalCultivationEngine {

    private static final Logger log = LoggerFactory.getLogger(PracticalCultivationEngine.class);

    private static final Map<String, Map<String, Object>> GUIDE_STORE = buildGuideStore();
    private static final Map<String, List<Map<String, Object>>> DISEASE_STORE = buildDiseaseStore();
    private static final Map<String, List<String>> BEST_PRACTICES_STORE = buildBestPracticesStore();
    private static final Map<String, List<Map<String, Object>>> TROUBLESHOOTING_STORE = buildTroubleshootingStore();

    public PracticalCultivationEngine() {
        log.info("PracticalCultivationEngine initialized with {} guides, {} diseases",
                GUIDE_STORE.size(), DISEASE_STORE.size());
    }

    public Map<String, Object> getCultivationGuide(String category) {
        log.info("Fetching cultivation guide for category='{}'", category);
        Map<String, Object> guide = GUIDE_STORE.getOrDefault(category.toLowerCase(), GUIDE_STORE.get("general"));
        Map<String, Object> result = new LinkedHashMap<>(guide);
        result.put("category", category);
        result.put("retrievedAt", LocalDateTime.now().toString());
        return result;
    }

    public Map<String, Object> getCultivationSteps(String mushroomType) {
        log.info("Fetching cultivation steps for mushroomType='{}'", mushroomType);
        List<Map<String, Object>> workflow;
        String type = mushroomType.toLowerCase();
        if (type.contains("oyster") || type.contains("pleurotus"))
            workflow = buildOysterWorkflow();
        else if (type.contains("shiitake") || type.contains("lentinula"))
            workflow = buildShiitakeWorkflow();
        else if (type.contains("button") || type.contains("agaricus"))
            workflow = buildButtonWorkflow();
        else if (type.contains("reishi") || type.contains("ganoderma"))
            workflow = buildReishiWorkflow();
        else if (type.contains("lion") || type.contains("hericium"))
            workflow = buildLionManeWorkflow();
        else
            workflow = buildGeneralWorkflow();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mushroomType", mushroomType);
        result.put("totalSteps", workflow.size());
        result.put("workflow", workflow);
        result.put("estimatedDuration", (workflow.size() * 7) + " to " + (workflow.size() * 14) + " days total");
        return result;
    }

    public Map<String, Object> identifyDisease(List<String> symptoms) {
        log.info("Identifying disease from {} symptoms", symptoms.size());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("symptoms", symptoms);
        List<Map<String, Object>> matches = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : DISEASE_STORE.entrySet()) {
            for (Map<String, Object> info : entry.getValue()) {
                @SuppressWarnings("unchecked")
                List<String> diseaseSymptoms = (List<String>) info.get("symptoms");
                long matchCount = symptoms.stream().filter(s -> diseaseSymptoms.stream().anyMatch(ds -> ds.toLowerCase().contains(s.toLowerCase()))).count();
                if (matchCount > 0) {
                    Map<String, Object> match = new LinkedHashMap<>((Map<String, Object>) info);
                    match.put("disease", entry.getKey());
                    match.put("matchScore", Math.round((double) matchCount / diseaseSymptoms.size() * 100));
                    match.put("confidence", matchCount >= 2 ? "High" : "Medium");
                    matches.add(match);
                }
            }
        }
        matches.sort((a, b) -> Integer.compare((int) b.get("matchScore"), (int) a.get("matchScore")));
        if (matches.isEmpty())
            matches.add(Map.of("disease", "Unknown", "description", "No matching disease found. Isolate affected materials and seek expert diagnosis.", "confidence", "Low"));
        result.put("possibleDiseases", matches);
        return result;
    }

    public Map<String, Object> getBestPractices(String topic) {
        log.info("Fetching best practices for topic='{}'", topic);
        String key = topic.toLowerCase().trim();
        List<String> practices = BEST_PRACTICES_STORE.get(key);
        if (practices == null) {
            for (Map.Entry<String, List<String>> e : BEST_PRACTICES_STORE.entrySet()) {
                if (key.contains(e.getKey()) || e.getKey().contains(key) || e.getValue().stream().anyMatch(v -> v.toLowerCase().contains(key))) {
                    practices = e.getValue();
                    break;
                }
            }
        }
        if (practices == null) practices = BEST_PRACTICES_STORE.get("general");
        return Map.of("topic", topic, "bestPractices", practices, "count", practices.size());
    }

    public Map<String, Object> getTroubleshootingGuide(String issue) {
        log.info("Fetching troubleshooting guide for issue='{}'", issue);
        String key = issue.toLowerCase().trim();
        List<Map<String, Object>> steps = TROUBLESHOOTING_STORE.get(key);
        if (steps == null) {
            for (Map.Entry<String, List<Map<String, Object>>> e : TROUBLESHOOTING_STORE.entrySet()) {
                if (key.contains(e.getKey()) || e.getKey().contains(key)) {
                    steps = e.getValue();
                    break;
                }
            }
        }
        if (steps == null) steps = TROUBLESHOOTING_STORE.get("general");
        return Map.of("issue", issue, "troubleshootingSteps", steps, "count", steps.size());
    }

    public Map<String, Object> getSeasonalGrowTips(int month, String region) {
        log.info("Fetching seasonal tips for month={}, region='{}'", month, region);
        String season = switch (month) { case 3,4,5 -> "Spring"; case 6,7,8 -> "Summer"; case 9,10,11 -> "Autumn"; default -> "Winter"; };
        List<String> tips = new ArrayList<>();
        List<String> mushrooms = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        switch (season) {
            case "Spring" -> { tips.add("Start outdoor beds as soil reaches 10-15\u00b0C"); tips.add("Ideal for shiitake log inoculation"); tips.add("Begin oyster production in semi-outdoor structures"); mushrooms.addAll(List.of("Shiitake", "Oyster", "Morel")); warnings.add("Watch for sudden temperature fluctuations"); }
            case "Summer" -> { tips.add("Focus on warm-season species: oyster, paddy straw, reishi"); tips.add("Maintain fruiting below 28\u00b0C"); tips.add("Increase misting frequency"); mushrooms.addAll(List.of("Oyster (high-temp)", "Paddy Straw", "Reishi")); warnings.add("Trichoderma risk increases in high heat and humidity"); }
            case "Autumn" -> { tips.add("Peak season for most temperate species"); tips.add("Prepare compost for button mushrooms"); tips.add("Begin indoor cultivation as temperatures drop"); mushrooms.addAll(List.of("Button", "Shiitake", "Lion's Mane")); warnings.add("Monitor for sudden cold snaps"); }
            case "Winter" -> { tips.add("Focus on indoor climate-controlled cultivation"); tips.add("Ideal for button mushrooms in compost"); tips.add("Use supplemental heating for incubation"); mushrooms.addAll(List.of("Button", "Shiitake (indoor)", "Enoki")); warnings.add("Maintain humidity as heating dries air"); }
        }
        return Map.of("month", month, "season", season, "region", region, "seasonalTips", tips, "recommendedMushrooms", mushrooms, "cautions", warnings);
    }

    public List<Map<String, Object>> getAllGuides() {
        List<Map<String, Object>> all = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> e : GUIDE_STORE.entrySet())
            all.add(Map.of("category", e.getKey(), "title", e.getValue().get("title"), "description", e.getValue().get("description")));
        return all;
    }

    // ---- Builders ----

    private static Map<String, Map<String, Object>> buildGuideStore() {
        Map<String, Map<String, Object>> store = new LinkedHashMap<>();
        store.put("spawn preparation", Map.of("title", "Spawn Preparation Guide",
            "description", "Complete guide to preparing mushroom spawn from grain selection to full colonization",
            "sections", List.of(
                Map.of("heading", "Grain Selection", "content", "Choose high-quality organic grains: rye, wheat, millet. Rye preferred for nutrition and water-holding capacity. Grains must be free from pesticides."),
                Map.of("heading", "Preparation", "content", "Rinse, soak 12-24h, boil 15-20 min until soft. Drain and dry surface moisture. Target: firm but not sticky."),
                Map.of("heading", "Sterilization", "content", "Fill jars 2/3 full. Sterilize at 121\u00b0C/15 PSI for 90 min (quarts). Cool slowly 24-48h."),
                Map.of("heading", "Inoculation", "content", "Under laminar flow, inject 1-2 mL liquid culture per jar. Work quickly but carefully."),
                Map.of("heading", "Incubation", "content", "22-25\u00b0C in darkness. Shake at 30% colonization. Full colonization: 10-14 days."),
                Map.of("heading", "Storage", "content", "Refrigerate at 2-4\u00b0C for up to 6 months. Warm to room temp before use."))));
        store.put("substrate preparation", Map.of("title", "Substrate Preparation Guide",
            "description", "Comprehensive guide to preparing substrates for various mushroom species",
            "sections", List.of(
                Map.of("heading", "Straw (Oyster)", "content", "Chop 2-4 inch pieces. Hydrate 4-6h. Pasteurize 65-70\u00b0C for 90 min. Moisture 60-65%."),
                Map.of("heading", "Sawdust (Shiitake/Lion's Mane)", "content", "Hardwood sawdust + 10-20% bran. Sterilize 121\u00b0C for 2-3h. Moisture 55-60%."),
                Map.of("heading", "Compost (Button)", "content", "Phase I: manure+straw+gypsum, 6-14 days outdoor. Phase II: 55-60\u00b0C pasteurization, 45-50\u00b0C conditioning."),
                Map.of("heading", "Supplementation", "content", "Wheat bran 10-20%, soy hulls 5-10%. Increases yield but also contamination risk. Always sterilize supplemented substrates."),
                Map.of("heading", "pH & Moisture", "content", "Target pH 6.5-7.0. Gypsum buffers pH and provides calcium. Squeeze test: release 1-2 drops."))));
        store.put("sterilization", Map.of("title", "Sterilization & Aseptic Technique",
            "description", "Complete guide to sterilization methods and sterile conditions",
            "sections", List.of(
                Map.of("heading", "Autoclaving", "content", "121\u00b0C at 15 PSI. 30 min tools, 90 min quart jars, 2-3h large bags. Cool until pressure zero."),
                Map.of("heading", "Chemical", "content", "70% isopropyl alcohol for surfaces. 10% bleach for equipment (rinse thoroughly). 3% H2O2 for sensitive materials."),
                Map.of("heading", "Pasteurization", "content", "65-70\u00b0C for 90 min. Kills harmful organisms while preserving beneficial microbes. For straw substrates."),
                Map.of("heading", "Laminar Flow", "content", "Turn on 15 min before use. Wipe with 70% alcohol. Work 6 inches inside. Never block HEPA filter."),
                Map.of("heading", "Common Mistakes", "content", "Overpacking autoclave, insufficient cooling, using 90% instead of 70% alcohol, not replacing HEPA filters."))));
        store.put("incubation", Map.of("title", "Incubation Management",
            "description", "Guide to optimal incubation conditions",
            "sections", List.of(
                Map.of("heading", "Temperature", "content", "22-25\u00b0C for most gourmet species. Consistency more important than perfection. Avoid fluctuations >3\u00b0C."),
                Map.of("heading", "Humidity", "content", "60-70% during incubation. Too high promotes bacteria. Too low dries substrate."),
                Map.of("heading", "Light", "content", "Complete darkness for most species. Light triggers pinning only after full colonization."),
                Map.of("heading", "Contamination Monitoring", "content", "Inspect every 2-3 days. Isolate and remove contamination immediately. Keep records of rates."))));
        store.put("fruiting", Map.of("title", "Fruiting Conditions Management",
            "description", "Guide to inducing and managing mushroom fruiting",
            "sections", List.of(
                Map.of("heading", "Pinning Induction", "content", "Reduce temp 5-8\u00b0C, increase FAE (CO2 <1000 ppm), humidity 90-95%, indirect light 500-1000 lux."),
                Map.of("heading", "Temperature by Species", "content", "Oyster: 15-20\u00b0C. Shiitake: 15-20\u00b0C. Button: 14-18\u00b0C. Lion's Mane: 18-22\u00b0C. Reishi: 21-27\u00b0C. Enoki: 7-12\u00b0C."),
                Map.of("heading", "FAE & CO2", "content", "Target CO2 500-1000 ppm. Oyster especially sensitive to high CO2 (long stems, small caps). 4-6 air exchanges/hour."),
                Map.of("heading", "Lighting", "content", "500-2000 lux for 8-12h/day. Indirect light. Affects cap color and stem length."))));
        store.put("harvesting", Map.of("title", "Harvesting, Processing & Storage",
            "description", "Complete guide to harvest and post-harvest handling",
            "sections", List.of(
                Map.of("heading", "Harvest Timing", "content", "Oyster: cap edges curled under. Shiitake: cap 70-80% open. Button: cap closed. Morning harvest best."),
                Map.of("heading", "Technique", "content", "Grasp at base, twist gently, pull. Trim dirty ends. Handle by stem. Cool to 2-4\u00b0C within 2 hours."),
                Map.of("heading", "Yield", "content", "First flush: 60-70% of total. 2-3 flushes for oyster. BE: oyster 100-200%, shiitake 60-100%."),
                Map.of("heading", "Packaging", "content", "Fresh: perforated punnets. Vacuum: extends to 2-3 weeks. Dried: moisture 10-12%, airtight containers."),
                Map.of("heading", "Storage", "content", "Fresh: 2-4\u00b0C, 90-95% RH, 7-14 days. Dried: room temp, dark, dry, 6-12 months. Never wash before storage."))));
        store.put("general", Map.of("title", "General Mushroom Cultivation Guide",
            "description", "Overview of mushroom cultivation principles",
            "sections", List.of(
                Map.of("heading", "Overview", "content", "Six main stages: spawn prep, substrate prep, inoculation, incubation, fruiting, harvesting. Each requires specific conditions and sterile technique."),
                Map.of("heading", "Key Principles", "content", "Sterile technique is paramount. Contamination is #1 cause of failure. Environmental control determines success. Keep detailed records."),
                Map.of("heading", "Getting Started", "content", "Begin with oyster on pasteurized straw. Master sterile technique first. Keep records. Join mycology community."))));
        return store;
    }

    private static Map<String, List<Map<String, Object>>> buildDiseaseStore() {
        Map<String, List<Map<String, Object>>> store = new LinkedHashMap<>();
        store.put("Green Mold (Trichoderma)", List.of(Map.of(
            "symptoms", List.of("Green spore masses", "White mycelium turning green", "Rapid spread", "Sweet musty smell"),
            "description", "Caused by Trichoderma species. Most common contaminant in mushroom cultivation.",
            "causes", List.of("Inadequate sterilization", "Contaminated spawn", "Poor air filtration", "High temp and humidity"),
            "treatment", List.of("Isolate and remove affected materials", "Dispose in sealed bags outside", "Disinfect with 10% bleach", "Reduce temperature and humidity"),
            "prevention", List.of("Proper sterilization 121\u00b0C/15 PSI", "Clean spawn from reliable source", "HEPA filtration", "Strict hygiene"))));
        store.put("Bacterial Blotch", List.of(Map.of(
            "symptoms", List.of("Yellow to brown lesions on caps", "Slimy spots", "Sunken discolored areas", "Unpleasant odor"),
            "description", "Caused by Pseudomonas tolaasii. Common in high moisture with poor air exchange.",
            "causes", List.of("Free water on caps", "Humidity above 95%", "Poor air exchange", "Contaminated water"),
            "treatment", List.of("Reduce humidity to 80-85%", "Increase FAE", "Remove affected mushrooms", "Improve drainage"),
            "prevention", List.of("Avoid direct water on mushrooms", "Maintain 85-90% humidity", "Ensure adequate FAE", "Clean water for misting"))));
        store.put("Cobweb Mold", List.of(Map.of(
            "symptoms", List.of("Gray fluffy cobweb growth", "Rapid spread over surface", "Covers beds in 24-48h", "Musty odor"),
            "description", "Caused by Dactylium (Cladobotryum) species. Fast-growing fungal pathogen.",
            "causes", List.of("Contaminated casing", "High humidity with stagnant air", "Overwatering"),
            "treatment", List.of("Apply 3% hydrogen peroxide spray", "Reduce humidity", "Increase ventilation", "Remove infected blocks"),
            "prevention", List.of("Pasteurize casing materials", "Good air circulation", "Monitor humidity", "Regular sanitation"))));
        store.put("Wet Bubble (Mycogone)", List.of(Map.of(
            "symptoms", List.of("Amorphous mushroom masses", "Soft brown rotting tissue", "White fluffy growth turning brown", "Sweet fruity odor"),
            "description", "Caused by Mycogone perniciosa. Affects button and oyster mushrooms.",
            "causes", List.of("Contaminated casing", "High humidity with poor ventilation", "Overhead watering"),
            "treatment", List.of("Remove all affected mushrooms", "Reduce humidity to 80%", "Increase FAE", "Steam clean between crops"),
            "prevention", List.of("Disease-free spawn", "Pasteurize casing", "Proper ventilation", "Avoid overhead watering"))));
        store.put("Dry Bubble (Verticillium)", List.of(Map.of(
            "symptoms", List.of("Split cracked caps", "Brown spots on caps", "Stem deformation", "Reduced yield"),
            "description", "Caused by Verticillium fungicola. Affects button and specialty mushrooms.",
            "causes", List.of("Contaminated casing", "Poor air quality", "Overcrowding", "High temperature"),
            "treatment", List.of("Remove symptomatic mushrooms", "Improve ventilation", "Reduce temperature"),
            "prevention", List.of("Clean casing materials", "Optimal air flow", "Avoid overcrowding"))));
        return store;
    }

    private static Map<String, List<String>> buildBestPracticesStore() {
        Map<String, List<String>> store = new LinkedHashMap<>();
        store.put("sterile technique", List.of(
            "Always work under laminar flow or still-air box",
            "Use 70% isopropyl alcohol - NOT 90% (70% penetrates better)",
            "Change gloves after touching non-sterile surfaces",
            "Flame-sterilize tools until red hot, then cool 15 seconds",
            "Keep movements slow and deliberate",
            "Discard materials touching non-sterile surfaces"));
        store.put("substrate preparation", List.of(
            "Use untreated, pesticide-free materials",
            "Target 60-65% moisture for most substrates",
            "Test pH and adjust to 6.5-7.0",
            "Supplement sawdust with 10-20% bran",
            "Pasteurize straw at 65-70\u00b0C for 90 min",
            "Cool substrate completely before spawning"));
        store.put("environmental control", List.of(
            "Maintain temperature within 2\u00b0C of target",
            "Monitor CO2: <1000 ppm fruiting, <5000 ppm incubation",
            "Keep humidity 85-95% during fruiting",
            "Provide 8-12h indirect light for fruiting",
            "Log environmental data at least 3x daily",
            "Have backup systems for heating/cooling/humidity"));
        store.put("harvesting", List.of(
            "Harvest in morning when mushrooms are crisp",
            "Twist and pull at base",
            "Handle by stem, never by cap",
            "Cool to 2-4\u00b0C within 2 hours",
            "Grade immediately after harvest",
            "Never wash before storage"));
        store.put("general", List.of(
            "Keep detailed records of every batch",
            "Label everything with date, strain, batch number",
            "Inspect crops daily",
            "Clean and sanitize between every batch",
            "Start small and scale up gradually",
            "Test new techniques on small scale first"));
        return store;
    }

    private static Map<String, List<Map<String, Object>>> buildTroubleshootingStore() {
        Map<String, List<Map<String, Object>>> store = new LinkedHashMap<>();
        store.put("no pinning", List.of(
            ts(1, "Is substrate fully colonized?", "Wait for complete colonization"),
            ts(2, "Was temperature dropped?", "Reduce by 5-8\u00b0C to trigger pinning"),
            ts(3, "Is FAE adequate?", "Increase FAE, target CO2 <1000 ppm"),
            ts(4, "Is humidity at 90-95%?", "Increase humidity"),
            ts(5, "Is there adequate light?", "Provide 500-1000 lux 8-12h/day")));
        store.put("contamination", List.of(
            ts(1, "What color?", "Green=Trichoderma, Black=Aspergillus, Pink=Neurospora, Orange=Bacteria"),
            ts(2, "Localized or widespread?", "Localized: remove + margin. Widespread: dispose immediately"),
            ts(3, "Check sterilization records", "Did autoclave reach 121\u00b0C? Check indicator tape"),
            ts(4, "Review aseptic technique", "Were gloves changed? Tools flame-sterilized?"),
            ts(5, "Check spawn quality", "Test on agar. Replace if contaminated")));
        store.put("slow growth", List.of(
            ts(1, "Temperature check", "Optimal 22-25\u00b0C. Below 18\u00b0C slows, above 30\u00b0C kills mycelium"),
            ts(2, "Moisture check", "Squeeze test: should release 1-2 drops"),
            ts(3, "Gas exchange?", "Ensure filter patches not blocked"),
            ts(4, "Spawn viability?", "Test on agar"),
            ts(5, "pH check?", "Ideal 6.5-7.0")));
        store.put("small caps long stems", List.of(
            ts(1, "CO2 levels too high", "Increase FAE immediately. Install CO2 monitor"),
            ts(2, "Light too low", "Increase to 1000-2000 lux"),
            ts(3, "Temperature too high", "Check species-specific range"),
            ts(4, "Genetics", "Some strains naturally produce long stems")));
        store.put("general", List.of(
            ts(1, "Identify symptom precisely", "Document color, location, pattern, smell"),
            ts(2, "Check environmental logs", "Review last 48h for deviations"),
            ts(3, "Review recent changes", "Spawn supplier? New substrate? Different water?"),
            ts(4, "Consult resources", "Manual, forums, or technical support"),
            ts(5, "Isolate if uncertain", "Prevent potential spread")));
        return store;
    }

    private static Map<String, Object> ts(int step, String check, String solution) {
        return new LinkedHashMap<>(Map.of("step", step, "check", check, "solution", solution));
    }

    private List<Map<String, Object>> buildOysterWorkflow() {
        return List.of(
            step("1", "Select oyster strain", "Choose based on local temperature"),
            step("2", "Prepare grain spawn", "2-3 weeks before planned spawning"),
            step("3", "Prepare straw substrate", "Pasteurize 65-70\u00b0C for 90 min, cool"),
            step("4", "Spawn at 5-10% rate", "Mix thoroughly"),
            step("5", "Fill into grow bags", "With filter patches"),
            step("6", "Incubate 22-25\u00b0C, 12-18 days", "Until fully colonized"),
            step("7", "Fruiting: 15-20\u00b0C, 85-95% RH", "Cut slits for emergence"),
            step("8", "Harvest in 5-7 days", "Caps still curled under"),
            step("9", "Soak block for second flush", "2-3 flushes total"),
            step("10", "BE: 100-200%", "6-8 week total cycle"));
    }

    private List<Map<String, Object>> buildShiitakeWorkflow() {
        return List.of(
            step("1", "Select shiitake strain", "Warm or cold weather variety"),
            step("2", "Prepare sawdust + bran", "Sterilize 121\u00b0C for 2-3h"),
            step("3", "Inoculate at 5%", "Mix thoroughly"),
            step("4", "Fill filter patch bags", "Shape into blocks"),
            step("5", "Incubate 20-24\u00b0C, 4-6 weeks", "Brown color is normal"),
            step("6", "Fruiting: 15-20\u00b0C, 85-90% RH", "Remove or cut bags"),
            step("7", "Temperature shock 10-15\u00b0C", "Soak block if needed"),
            step("8", "Harvest 7-14 days after pins", "Cap 70-80% open"),
            step("9", "Rest 2-4 weeks between flushes", "3-4 flushes total"),
            step("10", "BE: 60-100%", "3-5 month total cycle"));
    }

    private List<Map<String, Object>> buildButtonWorkflow() {
        return List.of(
            step("1", "Phase I compost", "Manure+straw+gypsum, 6-14 days"),
            step("2", "Phase II pasteurization", "55-60\u00b0C 6h, then 45-50\u00b0C 5-7 days"),
            step("3", "Spawn at 0.5-1%", "Mix evenly"),
            step("4", "Fill trays 15-20 cm", "Press firmly"),
            step("5", "Incubate 22-25\u00b0C, 12-16 days", "Full colonization"),
            step("6", "Apply casing (peat+lime, pH 7.5)", "3-5 cm depth, keep moist"),
            step("7", "Fruiting: 14-18\u00b0C, 85-90% RH", "Reduce CO2, provide light"),
            step("8", "Harvest caps closed", "Twist and pull"),
            step("9", "Flushes every 7-10 days", "3-5 flushes, 8-10 week cycle"),
            step("10", "BE: 30-50%", ""));
    }

    private List<Map<String, Object>> buildReishiWorkflow() {
        return List.of(
            step("1", "Select Reishi strain", "Antler or conk form"),
            step("2", "Hardwood sawdust + 10-20% bran", "Sterilize 121\u00b0C for 2.5h"),
            step("3", "Inoculate at 5%", "Mix thoroughly"),
            step("4", "Fill large filter patch bags", "5-10 kg per bag"),
            step("5", "Incubate 22-25\u00b0C, 3-6 weeks", "Full colonization"),
            step("6", "Fruiting: 21-27\u00b0C, 80-90% RH", "Cut slit or remove top"),
            step("7", "High CO2=antler, low CO2=conk", "Adjust environment for form"),
            step("8", "Harvest when margin stops growing", "Cut at base, dry immediately"),
            step("9", "One main flush per block", "3-5 month total cycle"),
            step("10", "Dry at 45-50\u00b0C until brittle", "Store airtight"));
    }

    private List<Map<String, Object>> buildLionManeWorkflow() {
        return List.of(
            step("1", "Select Lion's Mane strain", "Tooth fungus"),
            step("2", "Oak sawdust + 20% bran", "60-65% moisture"),
            step("3", "Sterilize 121\u00b0C for 2h", "Filter patch bags"),
            step("4", "Inoculate at 5%", "Liquid culture or grain spawn"),
            step("5", "Incubate 22-24\u00b0C, 2-3 weeks", "Full colonization"),
            step("6", "Fruiting: 18-22\u00b0C, 85-90% RH", "Cut opening"),
            step("7", "Pins 5-7 days, teeth grow 10-14 days", "White icicle formations"),
            step("8", "Harvest before yellowing", "Cut entire cluster"),
            step("9", "Soak for second flush in 2-3 weeks", "2 flushes total"),
            step("10", "Best fresh, 5-7 days refrigerated", "Can be dried or frozen"));
    }

    private List<Map<String, Object>> buildGeneralWorkflow() {
        return List.of(
            step("1", "Select species and strain", "Match to climate and market"),
            step("2", "Acquire quality spawn", "From reliable source"),
            step("3", "Prepare species-specific substrate", "Follow formulation guide"),
            step("4", "Sterilize or pasteurize", "Method depends on substrate"),
            step("5", "Cool and inoculate", "Use aseptic technique"),
            step("6", "Incubate at optimal temp", "Full colonization in darkness"),
            step("7", "Move to fruiting conditions", "Adjust all parameters"),
            step("8", "Harvest at proper maturity", "Handle carefully"),
            step("9", "Manage additional flushes", "Rehydrate if needed"),
            step("10", "Grade, package, store properly", "Maintain cold chain"));
    }

    private Map<String, Object> step(String number, String action, String note) {
        return new LinkedHashMap<>(Map.of("step", number, "action", action, "notes", note));
    }
}
