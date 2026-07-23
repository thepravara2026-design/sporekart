package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.SpawnRecommendation;
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
public class SpawnRecommendationEngine {

    private static final Logger log = LoggerFactory.getLogger(SpawnRecommendationEngine.class);

    private final Map<String, SpawnRecommendation> spawnMap = new ConcurrentHashMap<>();
    private final Map<String, String> speciesDetails = new ConcurrentHashMap<>();

    public SpawnRecommendationEngine() {
        log.info("Initializing SpawnRecommendationEngine");
    }

    @PostConstruct
    void init() {
        loadAllSpawns();
        loadSpeciesDetails();
        log.info("SpawnRecommendationEngine seeded with {} spawn entries", spawnMap.size());
    }

    public List<SpawnRecommendation> recommendSpawn(String query, String difficulty, String climate) {
        String q = query.toLowerCase().trim();
        return spawnMap.values().stream()
            .filter(s -> difficulty == null || difficulty.isEmpty() || difficulty.equalsIgnoreCase("all")
                || s.difficulty().equalsIgnoreCase(difficulty))
            .filter(s -> climate == null || climate.isEmpty() || climate.equalsIgnoreCase("all")
                || s.climateSuitability().toLowerCase().contains(climate.toLowerCase()))
            .filter(s -> q.isEmpty() || s.speciesName().toLowerCase().contains(q)
                || s.variety().toLowerCase().contains(q)
                || s.spawnType().toLowerCase().contains(q)
                || s.description().toLowerCase().contains(q))
            .sorted(Comparator.comparing(SpawnRecommendation::spawnId))
            .toList();
    }

    public Optional<SpawnRecommendation> getSpawnById(String spawnId) {
        return Optional.ofNullable(spawnMap.get(spawnId));
    }

    public List<SpawnRecommendation> getAllSpawns() {
        return spawnMap.values().stream()
            .sorted(Comparator.comparing(SpawnRecommendation::spawnId))
            .toList();
    }

    public List<SpawnRecommendation> getSpawnsByDifficulty(String difficulty) {
        return spawnMap.values().stream()
            .filter(s -> s.difficulty().equalsIgnoreCase(difficulty))
            .sorted(Comparator.comparing(SpawnRecommendation::spawnId))
            .toList();
    }

    public List<SpawnRecommendation> getSpawnsByClimate(String climate) {
        return spawnMap.values().stream()
            .filter(s -> s.climateSuitability().toLowerCase().contains(climate.toLowerCase()))
            .sorted(Comparator.comparing(SpawnRecommendation::spawnId))
            .toList();
    }

    public Map<String, Object> compareSpawns(List<String> spawnIds) {
        List<SpawnRecommendation> selected = spawnIds.stream()
            .map(spawnMap::get)
            .filter(s -> s != null)
            .toList();

        Map<String, Object> comparison = new LinkedHashMap<>();
        comparison.put("spawnsCompared", selected.size());
        comparison.put("spawns", selected.stream()
            .map(s -> Map.of(
                "spawnId", s.spawnId(),
                "speciesName", s.speciesName(),
                "variety", s.variety(),
                "difficulty", s.difficulty(),
                "tempRange", s.optimalTempLow() + "-" + s.optimalTempHigh() + " C",
                "humidityRange", s.optimalHumidityLow() + "-" + s.optimalHumidityHigh() + "%",
                "spawnRunDays", s.spawnRunDays(),
                "pinningDays", s.pinningDays(),
                "harvestDays", s.harvestDays(),
                "expectedYieldKg", s.expectedYieldKg(),
                "climateSuitability", s.climateSuitability(),
                "commercialSuitability", s.commercialSuitability()
            ))
            .toList());

        if (selected.size() > 1) {
            SpawnRecommendation fastest = selected.stream()
                .min(Comparator.comparingInt(s -> s.spawnRunDays() + s.pinningDays() + s.harvestDays()))
                .orElse(null);
            SpawnRecommendation highestYield = selected.stream()
                .max(Comparator.comparingDouble(SpawnRecommendation::expectedYieldKg))
                .orElse(null);
            SpawnRecommendation easiest = selected.stream()
                .filter(s -> "Beginner".equalsIgnoreCase(s.difficulty()))
                .findFirst().orElse(null);

            comparison.put("fastestCycle", fastest != null ? fastest.speciesName() + " (" + (fastest.spawnRunDays() + fastest.pinningDays() + fastest.harvestDays()) + " days)" : "N/A");
            comparison.put("highestYield", highestYield != null ? highestYield.speciesName() + " (" + highestYield.expectedYieldKg() + " kg)" : "N/A");
            comparison.put("easiestForBeginners", easiest != null ? easiest.speciesName() : "None at beginner level");
        }

        return comparison;
    }

    public Map<String, Object> getSpeciesDetails(String speciesName) {
        String key = speciesName.trim().toLowerCase();
        String details = speciesDetails.get(key);
        if (details == null) return Map.of("error", "Species details not found for: " + speciesName);

        List<SpawnRecommendation> spawns = spawnMap.values().stream()
            .filter(s -> s.speciesName().toLowerCase().contains(key))
            .toList();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("speciesName", speciesName);
        result.put("description", details);
        result.put("availableVarieties", spawns.stream().map(SpawnRecommendation::variety).toList());
        result.put("spawnCount", spawns.size());
        result.put("spawnRecommendations", spawns.stream()
            .map(s -> Map.of(
                "spawnId", s.spawnId(),
                "variety", s.variety(),
                "difficulty", s.difficulty(),
                "spawnRunDays", s.spawnRunDays(),
                "expectedYieldKg", s.expectedYieldKg()
            ))
            .toList());
        result.put("cultivationHighlights", List.of(
            "Optimal temperature range: " + spawns.stream().mapToDouble(SpawnRecommendation::optimalTempLow).min().orElse(0)
                + "-" + spawns.stream().mapToDouble(SpawnRecommendation::optimalTempHigh).max().orElse(0) + " C",
            "Optimal humidity range: " + spawns.stream().mapToDouble(SpawnRecommendation::optimalHumidityLow).min().orElse(0)
                + "-" + spawns.stream().mapToDouble(SpawnRecommendation::optimalHumidityHigh).max().orElse(0) + "%",
            "Total cycle: " + spawns.stream().mapToInt(s -> s.spawnRunDays() + s.pinningDays() + s.harvestDays()).min().orElse(0)
                + "-" + spawns.stream().mapToInt(s -> s.spawnRunDays() + s.pinningDays() + s.harvestDays()).max().orElse(0) + " days"
        ));
        return result;
    }

    private void loadAllSpawns() {
        addSpawn(new SpawnRecommendation("spawn_001", "Oyster", "White", "Grain", "Beginner",
            20, 26, 80, 90, 14, 5, 7, 2.5,
            "Tropical / Subtropical / Temperate", "High",
            List.of("Basic mushroom cultivation workshop", "Oyster farming on straw"),
            "White Oyster is the most common variety. Fast colonization, vigorous growth, suitable for beginners.",
            List.of("Fast growth cycle (26 days)", "High yield per batch", "Wide temperature tolerance",
                "Grows on multiple substrates", "Good for small-scale farming"),
            List.of("Spores can cause respiratory issues", "Short shelf life (3-5 days)",
                "Sensitive to CO2 levels", "Requires frequent harvesting")));

        addSpawn(new SpawnRecommendation("spawn_002", "Oyster", "Grey", "Grain", "Beginner",
            18, 24, 85, 95, 15, 6, 7, 2.8,
            "Temperate / Subtropical", "High",
            List.of("Oyster mushroom farming", "Climate control for mushrooms"),
            "Grey Oyster (also known as Phoenix Oyster) has excellent flavor and shelf life. Prefers cooler conditions.",
            List.of("Superior flavor profile", "Longer shelf life (5-7 days)", "Good disease resistance",
                "High market demand", "Attractive appearance"),
            List.of("Narrower temperature range", "Slower colonization than White Oyster",
                "Needs high humidity during pinning", "More expensive spawn")));

        addSpawn(new SpawnRecommendation("spawn_003", "Oyster", "Pink", "Grain", "Intermediate",
            24, 30, 85, 95, 12, 4, 5, 2.0,
            "Tropical / Subtropical", "Medium",
            List.of("Tropical mushroom cultivation", "Color variety cultivation"),
            "Pink Oyster is a warm-weather variety with striking pink color that fades to pale when cooked.",
            List.of("Fastest Oyster cycle (21 days)", "Unique color for niche markets", "Heat tolerant",
                "Good for tropical regions", "Attractive for farmers markets"),
            List.of("Color fades with cooking/storage", "Lower yield than White Oyster",
                "More sensitive to handling", "Premium pricing needed for profitability")));

        addSpawn(new SpawnRecommendation("spawn_004", "Oyster", "Golden", "Grain", "Intermediate",
            20, 28, 85, 90, 13, 5, 6, 2.3,
            "Tropical / Subtropical", "Medium",
            List.of("Specialty mushroom farming", "Oyster variety selection"),
            "Golden Oyster has bright yellow caps that add visual appeal. Warm-weather adapted variety.",
            List.of("Beautiful golden color", "Good heat tolerance", "Fast growth cycle (24 days)",
                "Niche market potential", "Good yield on straw substrate"),
            List.of("Color sensitive to light levels", "Lower yield in cooler temps",
                "Short shelf life", "Requires proper light management")));

        addSpawn(new SpawnRecommendation("spawn_005", "Oyster", "King", "Grain/Sawdust", "Advanced",
            15, 20, 80, 90, 20, 8, 10, 1.5,
            "Temperate", "Medium",
            List.of("Advanced mushroom cultivation", "King Oyster specialized farming"),
            "King Oyster (Pleurotus eryngii) produces thick stems with small caps. Premium market price. Requires cooler temperatures.",
            List.of("Highest market price among Oysters", "Thick meaty stems (chefs prefer)",
                "Longer shelf life (7-10 days)", "Grows on sawdust blocks", "Lower spore production"),
            List.of("Longer growth cycle (38 days)", "Lower yield per batch", "Requires precise temperature control",
                "More expensive substrate preparation", "Advanced skill level needed")));

        addSpawn(new SpawnRecommendation("spawn_006", "Milky", "White Milky", "Grain", "Intermediate",
            28, 35, 80, 90, 18, 6, 10, 3.0,
            "Tropical", "High",
            List.of("Tropical mushroom farming", "Milky mushroom commercial production"),
            "White Milky mushroom (Calocybe indica) is a tropical species with excellent shelf life and high yield potential.",
            List.of("Excellent shelf life (7-10 days)", "High yield (up to 3 kg per batch)",
                "Heat tolerant (28-35C)", "White color preferred in markets",
                "No specialized equipment needed"),
            List.of("Requires casing layer", "Slower growth than Oyster (34 days)",
                "Needs specific tropical conditions", "Limited substrate options",
                "Sensitive to temperature below 25C")));

        addSpawn(new SpawnRecommendation("spawn_007", "Milky", "APK-1", "Grain", "Beginner",
            30, 36, 80, 90, 16, 7, 10, 3.5,
            "Tropical", "High",
            List.of("Milky mushroom cultivation", "High-yield variety training"),
            "APK-1 is a high-yielding improved variety of Milky mushroom developed for tropical climates. Good for commercial farming.",
            List.of("Highest yield among Milky varieties", "Disease resistant", "Adapted to Indian climate",
                "Uniform fruiting bodies", "Good for commercial scale"),
            List.of("Requires high temperature (30-36C)", "Long growth cycle (33 days)",
                "Needs specific casing soil", "Higher spawn cost",
                "Limited availability outside India")));

        addSpawn(new SpawnRecommendation("spawn_008", "Button", "White Button", "Compost", "Intermediate",
            22, 25, 85, 90, 16, 8, 12, 4.0,
            "Temperate / Subtropical", "Very High",
            List.of("Commercial button mushroom farming", "Compost preparation", "Climate controlled cultivation"),
            "White Button is the most commercially cultivated mushroom worldwide. Requires compost and precise environmental control.",
            List.of("Highest commercial demand", "Well-established market", "Multiple flushes (3-4)",
                "Long harvest window", "Established supply chains"),
            List.of("Requires compost preparation (14-20 days)", "Needs precise temperature control (16-22C)",
                "Requires casing layer", "Higher initial investment",
                "Susceptible to bacterial and fungal diseases")));

        addSpawn(new SpawnRecommendation("spawn_009", "Button", "Cream Button", "Compost", "Intermediate",
            22, 25, 85, 90, 16, 8, 12, 3.8,
            "Temperate / Subtropical", "High",
            List.of("Button mushroom varieties", "Specialty mushroom farming"),
            "Cream Button mushroom has a creamy brown cap variant. Preferred in some European markets.",
            List.of("Premium pricing in some markets", "Similar cultivation to White Button",
                "Attractive color variation", "Good shelf life",
                "Niche market potential"),
            List.of("Lower yield than White Button", "Less consistent market demand",
                "Same compost requirements", "Similar disease susceptibility",
                "Limited spawn availability")));

        addSpawn(new SpawnRecommendation("spawn_010", "Shiitake", "Oakwood", "Sawdust/Plug", "Intermediate",
            18, 24, 80, 90, 60, 10, 10, 1.8,
            "Temperate / Subtropical", "High",
            List.of("Shiitake log cultivation", "Sawdust block preparation", "Mushroom drying and storage"),
            "Oakwood is a standard Shiitake variety suitable for both log and sawdust block cultivation. Rich flavor.",
            List.of("Excellent flavor and texture", "Dried shelf life of 1+ years",
                "High medicinal value", "Premium market price",
                "Multiple flushes over 3-5 years (logs)"),
            List.of("Very long colonization (60+ days)", "Requires log or specialized substrate",
                "Needs cold shock for pinning", "Moderate yield per cycle",
                "Requires long-term planning (log method)"));

        addSpawn(new SpawnRecommendation("spawn_011", "Shiitake", "Lentinula edodes 4080", "Sawdust/Plug", "Advanced",
            16, 22, 85, 90, 55, 8, 10, 2.2,
            "Temperate", "High",
            List.of("Advanced Shiitake cultivation", "Indoor block farming", "Climate management"),
            "Lentinula edodes 4080 is a high-yielding strain developed for indoor sawdust block cultivation. Uniform fruiting.",
            List.of("High yield on sawdust blocks", "Uniform fruiting bodies", "Faster than log cultivation (73 days)",
                "Good for indoor farming", "Consistent quality"),
            List.of("Requires sterilized sawdust blocks", "Higher substrate cost",
                "Needs precise environmental control", "Only 2-3 flushes per block",
                "Requires advanced knowledge")));

        addSpawn(new SpawnRecommendation("spawn_012", "Shiitake", "Winter Shiitake", "Sawdust/Plug", "Advanced",
            10, 16, 85, 95, 65, 12, 12, 1.5,
            "Temperate / Cold", "Medium",
            List.of("Cold climate mushroom farming", "Seasonal Shiitake cultivation"),
            "Winter Shiitake is a cold-adapted variety that fruits at lower temperatures. Suitable for winter cultivation in temperate regions.",
            List.of("Fruits at cool temperatures (10-16C)", "Good winter crop option",
                "Distinct flavor profile", "Natural cold shock from winter temps",
                "Disease resistant in cold conditions"),
            List.of("Very slow colonization (65+ days)", "Poor performance in warm weather",
                "Lower yield than standard Shiitake", "Limited to temperate/cold climates",
                "Long total cycle (89 days)"));

        addSpawn(new SpawnRecommendation("spawn_013", "Paddy Straw", "Volvariella volvacea", "Grain", "Beginner",
            30, 38, 80, 90, 7, 4, 5, 2.0,
            "Tropical", "Medium",
            List.of("Paddy straw mushroom cultivation", "Short cycle farming", "Tropical mushroom basics"),
            "Paddy Straw mushroom has the fastest cultivation cycle (16 days). Grows on pasteurized paddy straw. Ideal for tropical regions.",
            List.of("Fastest cycle of any cultivated mushroom", "Simple substrate (paddy straw)",
                "No casing required", "High temperature tolerance (30-38C)",
                "Low initial investment"),
            List.of("Very short shelf life (24-48 hours)", "Lower yield than other species",
                "Limited to tropical regions", "Straw preparation requires pasteurization",
                "Limited commercial market")));

        addSpawn(new SpawnRecommendation("spawn_014", "Paddy Straw", "Volvariella volvacea Improved", "Grain", "Intermediate",
            30, 38, 80, 90, 7, 4, 6, 2.5,
            "Tropical", "Medium",
            List.of("Improved paddy straw cultivation", "Yield optimization"),
            "Improved Paddy Straw variety with higher yield and slightly longer shelf life. Still requires rapid marketing.",
            List.of("Higher yield than standard variety", "Slightly better shelf life",
                "Same fast growth cycle (17 days)", "Good for tropical smallholders",
                "Low technology requirements"),
            List.of("Still short shelf life (24-36 hours)", "Limited to warm climates",
                "Requires consistent straw quality", "Price fluctuations in local markets",
                "Limited export potential")));

        addSpawn(new SpawnRecommendation("spawn_015", "Paddy Straw", "Volvariella volvacea Tropical", "Grain", "Beginner",
            32, 40, 85, 92, 6, 3, 5, 2.2,
            "Tropical", "Medium",
            List.of("Tropical mushroom farming", "Quick cycle mushroom production"),
            "Tropical Paddy Straw variety bred for extreme heat tolerance. Fastest cycle of all varieties.",
            List.of("Fastest growth cycle (14 days)", "Extreme heat tolerance (32-40C)",
                "Very simple cultivation", "Good for summer production",
                "Minimal equipment needed"),
            List.of("Very short shelf life", "Low yield per batch", "Only suitable for tropical regions",
                "Limited commercial market", "Labor intensive harvesting"));
    }

    private void addSpawn(SpawnRecommendation spawn) {
        spawnMap.put(spawn.spawnId(), spawn);
    }

    private void loadSpeciesDetails() {
        speciesDetails.put("oyster",
            "Oyster mushrooms (Pleurotus spp.) are saprotrophic fungi that grow on a wide range of agricultural wastes. "
            + "They are the easiest mushrooms to cultivate and ideal for beginners. Key features include: fast growth, "
            + "high yield on straw/cotton waste, tolerance to a range of temperatures, and excellent nutritional profile. "
            + "Five main varieties: White, Grey, Pink, Golden, and King Oyster. Total cycle: 21-38 days depending on variety.");
        speciesDetails.put("milky",
            "Milky mushrooms (Calocybe indica) are tropical mushrooms native to India. They are characterized by their pure white color, "
            + "excellent shelf life (7-10 days), and high yield potential. Requires casing layer for fruiting. "
            + "Optimal temperature: 28-35C. Total cycle: 32-36 days. High commercial potential in tropical markets.");
        speciesDetails.put("button",
            "Button mushrooms (Agaricus bisporus) are the most widely cultivated mushrooms globally. Require composted substrate "
            + "and precise environmental control. Produce in 3-4 flushes over 4-6 weeks. Optimal temperature: 16-25C. "
            + "Highest commercial value with established supply chains. Requires significant investment in climate control infrastructure.");
        speciesDetails.put("shiitake",
            "Shiitake mushrooms (Lentinula edodes) are second most cultivated mushroom globally. Can be grown on natural logs "
            + "(3-5 year production cycle) or sterilized sawdust blocks (2-3 flushes over 3-4 months). "
            + "Valued for flavor and medicinal properties (lentinan). Requires cold shock for pinning induction. "
            + "Optimal temperature: 16-24C (varies by strain). Total cycle: 75-90 days on blocks, 6-12 months on logs.");
        speciesDetails.put("padrystraw",
            "Paddy Straw mushrooms (Volvariella volvacea) have the fastest cultivation cycle (14-20 days total) of any "
            + "commercial mushroom. Grown on pasteurized paddy straw without casing. Requires high temperature (30-40C). "
            + "Main limitation is very short shelf life (24-48 hours). Best suited for local markets near production sites. "
            + "Three main varieties: standard, improved, and tropical heat-tolerant.");
    }
}
