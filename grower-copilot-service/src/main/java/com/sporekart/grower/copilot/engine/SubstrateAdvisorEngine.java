package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.SubstrateRecommendation;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SubstrateAdvisorEngine {

    private static final Logger log = LoggerFactory.getLogger(SubstrateAdvisorEngine.class);

    private final Map<String, SubstrateRecommendation> substrateMap = new ConcurrentHashMap<>();

    public SubstrateAdvisorEngine() {
        log.info("Initializing SubstrateAdvisorEngine");
    }

    @PostConstruct
    void init() {
        loadAllSubstrates();
        log.info("SubstrateAdvisorEngine seeded with {} substrate entries", substrateMap.size());
    }

    public List<SubstrateRecommendation> recommendSubstrate(String speciesName, String budget, String availability) {
        String speciesKey = speciesName.trim().toLowerCase();
        return substrateMap.values().stream()
            .filter(s -> s.suitableSpecies().stream().anyMatch(sp -> sp.toLowerCase().contains(speciesKey)))
            .filter(s -> budget == null || budget.isEmpty() || budget.equalsIgnoreCase("all")
                || matchesBudget(s.costPerKg(), budget))
            .filter(s -> availability == null || availability.isEmpty() || availability.equalsIgnoreCase("all")
                || s.name().toLowerCase().contains(availability.toLowerCase())
                || s.type().toLowerCase().contains(availability.toLowerCase()))
            .sorted(Comparator.comparingDouble(SubstrateRecommendation::costPerKg))
            .toList();
    }

    public Optional<SubstrateRecommendation> getSubstrateById(String substrateId) {
        return Optional.ofNullable(substrateMap.get(substrateId));
    }

    public List<SubstrateRecommendation> getAllSubstrates() {
        return substrateMap.values().stream()
            .sorted(Comparator.comparing(SubstrateRecommendation::substrateId))
            .toList();
    }

    public Map<String, Object> getSubstratePreparationGuide(String substrateId) {
        SubstrateRecommendation sub = substrateMap.get(substrateId);
        if (sub == null) return Map.of("error", "Substrate not found: " + substrateId);

        Map<String, Object> guide = new LinkedHashMap<>();
        guide.put("substrateName", sub.name());
        guide.put("type", sub.type());
        guide.put("description", sub.description());
        guide.put("preparationDays", sub.preparationDays());
        guide.put("sterilizationMethod", sub.sterilizationMethod());
        guide.put("targetMoisture", sub.moisturePercent() + "%");
        guide.put("costPerKg", sub.costPerKg());
        guide.put("yieldMultiplier", sub.expectedYieldMultiplier());
        guide.put("stepByStepGuide", sub.preparationSteps());
        guide.put("tips", sub.tips());
        return guide;
    }

    public Map<String, Object> getSterilizationMethod(String substrateType) {
        String key = substrateType.trim().toLowerCase();
        List<SubstrateRecommendation> matches = substrateMap.values().stream()
            .filter(s -> s.type().toLowerCase().contains(key) || s.name().toLowerCase().contains(key))
            .toList();

        if (matches.isEmpty()) return Map.of("error", "No substrate found for type: " + substrateType);

        SubstrateRecommendation sub = matches.get(0);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("substrateType", sub.type());
        result.put("recommendedMethod", sub.sterilizationMethod());
        result.put("targetMoisture", sub.moisturePercent() + "%");
        result.put("preparationDays", sub.preparationDays());

        Map<String, String> methodDetails = new LinkedHashMap<>();
        switch (sub.sterilizationMethod().toLowerCase()) {
            case "pasteurization (hot water 65-70c for 1-2 hours)":
                methodDetails.put("temperature", "65-70C");
                methodDetails.put("duration", "1-2 hours");
                methodDetails.put("equipment", "Large drum, heating source, thermometer");
                methodDetails.put("process", "Submerge substrate in hot water, maintain 65-70C for 1-2 hours, drain and cool");
                break;
            case "pasteurization (lime water soak)":
                methodDetails.put("method", "Lime water (calcium hydroxide) soak");
                methodDetails.put("ph", "12-13");
                methodDetails.put("duration", "12-18 hours soak, then wash to neutral pH");
                methodDetails.put("equipment", "Tank, lime, pH meter");
                break;
            case "hot water treatment (80c for 1 hour)":
                methodDetails.put("temperature", "80C");
                methodDetails.put("duration", "1 hour");
                methodDetails.put("equipment", "Boiler, insulated tank");
                methodDetails.put("process", "Heat water to 80C, add substrate, maintain for 1 hour, drain");
                break;
            case "autoclave sterilization (121c, 15 psi, 90 min)":
                methodDetails.put("temperature", "121C");
                methodDetails.put("pressure", "15 psi");
                methodDetails.put("duration", "90 minutes");
                methodDetails.put("equipment", "Autoclave or pressure cooker");
                break;
            case "composting (phase i & ii)":
                methodDetails.put("phase1", "Outdoor composting: 14-18 days, turning every 2-3 days");
                methodDetails.put("phase2", "Pasteurization: 60C for 6 hours, then conditioning at 48-50C for 5-7 days");
                methodDetails.put("equipment", "Compost turner, temperature probes, pasteurization tunnel/room");
                break;
            default:
                methodDetails.put("recommendation", sub.sterilizationMethod());
        }
        result.put("methodDetails", methodDetails);

        if (sub.sterilizationMethod().toLowerCase().contains("pasteurization") || sub.sterilizationMethod().toLowerCase().contains("hot water")) {
            result.put("note", "Pasteurization kills most competitors but preserves beneficial microbes. Preferred for substrate with supplements.");
        } else if (sub.sterilizationMethod().toLowerCase().contains("autoclave")) {
            result.put("note", "Full sterilization kills all microorganisms. Required for supplemented sawdust and grain. Cool completely before spawning.");
        }

        return result;
    }

    public Map<String, Object> calculateSubstrateQuantity(double area, String substrateType) {
        String key = substrateType.trim().toLowerCase();
        Optional<SubstrateRecommendation> sub = substrateMap.values().stream()
            .filter(s -> s.name().toLowerCase().contains(key) || s.type().toLowerCase().contains(key))
            .findFirst();

        if (sub.isEmpty()) return Map.of("error", "Unknown substrate type: " + substrateType);

        SubstrateRecommendation s = sub.get();
        double bedDepthMeters = 0.15;
        double volumeCubicMeters = area * bedDepthMeters;
        double densityKgPerCubicMeter = switch (s.type().toLowerCase()) {
            case "straw" -> 120;
            case "agricultural waste" -> 150;
            case "sawdust" -> 200;
            case "compost" -> 400;
            case "coir" -> 100;
            default -> 150;
        };
        double quantityKg = volumeCubicMeters * densityKgPerCubicMeter;
        double cost = quantityKg * s.costPerKg();

        Map<String, Object> estimate = new LinkedHashMap<>();
        estimate.put("substrateType", s.name());
        estimate.put("area", area + " sq m");
        estimate.put("bedDepth", (bedDepthMeters * 100) + " cm");
        estimate.put("estimatedVolume", String.format("%.2f", volumeCubicMeters) + " cubic meters");
        estimate.put("estimatedQuantity", String.format("%.1f", quantityKg) + " kg");
        estimate.put("costEstimate", "Rs. " + String.format("%.0f", cost));
        estimate.put("pricePerKg", "Rs. " + s.costPerKg());
        return estimate;
    }

    public Map<String, Object> compareSubstrates(List<String> substrateIds) {
        List<SubstrateRecommendation> selected = substrateIds.stream()
            .map(substrateMap::get)
            .filter(s -> s != null)
            .toList();

        Map<String, Object> comparison = new LinkedHashMap<>();
        comparison.put("substratesCompared", selected.size());
        comparison.put("substrates", selected.stream()
            .map(s -> Map.of(
                "substrateId", s.substrateId(),
                "name", s.name(),
                "type", s.type(),
                "moisturePercent", s.moisturePercent(),
                "sterilizationMethod", s.sterilizationMethod(),
                "preparationDays", s.preparationDays(),
                "costPerKg", s.costPerKg(),
                "yieldMultiplier", s.expectedYieldMultiplier(),
                "suitableSpecies", s.suitableSpecies()
            ))
            .toList());

        if (selected.size() > 1) {
            SubstrateRecommendation cheapest = selected.stream()
                .min(Comparator.comparingDouble(SubstrateRecommendation::costPerKg)).orElse(null);
            SubstrateRecommendation highestYield = selected.stream()
                .max(Comparator.comparingDouble(SubstrateRecommendation::expectedYieldMultiplier)).orElse(null);
            SubstrateRecommendation fastest = selected.stream()
                .min(Comparator.comparingInt(SubstrateRecommendation::preparationDays)).orElse(null);

            comparison.put("cheapestOption", cheapest != null ? cheapest.name() + " (Rs. " + cheapest.costPerKg() + "/kg)" : "N/A");
            comparison.put("highestYieldMultiplier", highestYield != null ? highestYield.name() + " (" + highestYield.expectedYieldMultiplier() + "x)" : "N/A");
            comparison.put("fastestToPrepare", fastest != null ? fastest.name() + " (" + fastest.preparationDays() + " days)" : "N/A");
        }

        return comparison;
    }

    private boolean matchesBudget(double cost, String budget) {
        return switch (budget.toLowerCase()) {
            case "low" -> cost <= 5;
            case "medium" -> cost > 5 && cost <= 15;
            case "high" -> cost > 15;
            default -> true;
        };
    }

    private void loadAllSubstrates() {
        addSubstrate(new SubstrateRecommendation(
            "sub_001", "Paddy Straw", "Straw", "Readily available agricultural byproduct. Excellent for Oyster and Paddy Straw mushrooms. "
            + "Lightweight, high cellulose content, good water retention. Requires pasteurization before use.",
            70, "Pasteurization (Hot water 65-70C for 1-2 hours)", 2, 3.0, 1.0,
            List.of("Oyster", "Paddy Straw"),
            List.of(
                "Chop straw into 5-10 cm pieces",
                "Soak in clean water for 4-6 hours",
                "Drain excess water",
                "Hot water treatment at 65-70C for 1-2 hours",
                "Drain and cool to 25-30C",
                "Mix with 5-10% spawn by weight",
                "Fill into bags or trays"
            ),
            List.of("Very low cost", "Widely available in agricultural regions", "Lightweight and easy to handle",
                "Good for beginner cultivation", "Fast preparation (2 days)"),
            List.of("Requires pasteurization", "Breaks down quickly (single flush often)",
                "Low nutrient content without supplementation", "Can harbor competitor molds",
                "Needs chopping before use"),
            List.of("Add 5-10% rice bran for better yield", "Use fresh, mold-free straw only",
                "Maintain 65-70% moisture for best results", "Can be mixed with 20% sawdust for better structure")
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_002", "Wheat Straw", "Straw", "Premium straw substrate with better nutrient profile than paddy straw. "
            + "Preferred for Oyster and Button mushroom cultivation. Slightly more expensive but gives better yields.",
            68, "Pasteurization (Hot water 65-70C for 1.5 hours)", 2, 5.0, 1.2,
            List.of("Oyster", "Button", "Paddy Straw"),
            List.of(
                "Cut wheat straw into 5-8 cm lengths",
                "Pre-soak in water for 6-8 hours",
                "Hot water treatment at 65-70C for 1.5 hours",
                "Drain and cool to room temperature",
                "Check moisture (squeeze test: few drops)",
                "Mix spawn at 5-8% of wet weight",
                "Fill into grow bags or trays"
            ),
            List.of("Better nutrient content than paddy straw", "Good structure for air exchange",
                "Suitable for multiple species", "Higher yield multiplier (1.2x)",
                "Widely available in wheat-growing regions"),
            List.of("More expensive than paddy straw", "May contain pesticide residues",
                "Requires thorough pasteurization", "Higher water holding capacity (can get too wet)",
                "Seasonal availability"),
            List.of("Source from organic farms when possible", "Supplement with 5% wheat bran for button mushrooms",
                "Ideal for spring and autumn cultivation", "Can be reused for compost after mushroom harvest")
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_003", "Cotton Waste (Cottonseed Hulls)", "Agricultural Waste",
            "Cotton waste is a premium substrate for Oyster mushrooms. High protein content, good water retention. "
            + "Produces larger and heavier mushrooms compared to straw-based substrates.",
            65, "Pasteurization (Hot water 65-70C for 1.5 hours)", 2, 8.0, 1.5,
            List.of("Oyster"),
            List.of(
                "Mix cotton waste with 10% cottonseed hulls if available",
                "Pre-wet with clean water for 4 hours",
                "Hot water treatment at 65-70C for 1.5 hours",
                "Drain thoroughly",
                "Cool to 25-28C",
                "Supplement with 5% rice bran (optional)",
                "Fill mushroom bags; press firmly"
            ),
            List.of("Highest yield among straw-based substrates", "High protein content (better nutrition)",
                "Excellent water retention", "Produces larger mushrooms",
                "Preferred by commercial Oyster growers"),
            List.of("Higher cost than straw", "May contain cotton pesticides",
                "Dense; needs thorough pasteurization", "Heavier to handle when wet",
                "Limited availability in non-cotton regions"),
            List.of("Use only from ginning mills (not textile waste)", "Ideal for grey and king oyster varieties",
                "Can be mixed 50:50 with paddy straw to reduce cost", "Monitor for aflatoxin in hot humid storage")
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_004", "Sugarcane Bagasse", "Agricultural Waste",
            "Sugarcane bagasse is a byproduct of sugar extraction. Good substrate for Oyster and Paddy Straw mushrooms. "
            + "Lightweight, fibrous, and readily available in sugar-producing regions.",
            65, "Pasteurization (Hot water 65-70C for 1 hour)", 2, 2.0, 0.9,
            List.of("Oyster", "Paddy Straw"),
            List.of(
                "Collect fresh bagasse (not molasses-contaminated)",
                "Shred into uniform fibers if needed",
                "Soak in water for 2-3 hours",
                "Hot water treatment at 65-70C for 1 hour",
                "Drain and cool",
                "Supplement with 10% rice bran (recommended)",
                "Fill bags with moderate packing"
            ),
            List.of("Very cheap and abundant in sugar regions", "Lightweight and easy to transport",
                "Good mycelium penetration", "Environmentally friendly (waste utilization)",
                "Fast preparation"),
            List.of("Lower yield than cotton waste or straw", "Needs supplementation for best results",
                "Variable quality depending on sugar mill", "Can be too fluffy; needs proper packing",
                "Seasonal availability (post-harvest season)"),
            List.of("Mix 30:70 with paddy straw for better structure", "Use within 2-3 days of collection to avoid spoilage",
                "Add 2% gypsum for pH buffering", "Best for pink and white oyster varieties")
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_005", "Coffee Husk", "Agricultural Waste",
            "Coffee husk (parchment) is an excellent specialty substrate for gourmet mushrooms. "
            + "Contains residual caffeine that stimulates mycelium growth. Good for Oyster and Shiitake.",
            60, "Pasteurization (Hot water 65-70C for 1 hour) or Autoclave", 2, 10.0, 1.3,
            List.of("Oyster", "Shiitake"),
            List.of(
                "Collect dry coffee husk from coffee processing",
                "Pre-wet with water for 6-8 hours",
                "Hot water pasteurization at 70-75C for 1 hour",
                "Drain and cool to 25C",
                "For Shiitake: sterilize in autoclave at 121C for 90 min",
                "Mix spawn at 8-10% of substrate weight",
                "Fill into grow bags with filter patches"
            ),
            List.of("Contains natural stimulants for mycelium", "Good nutrient profile",
                "Specialty substrate for premium mushrooms", "Good water retention",
                "Sustainable use of coffee industry waste"),
            List.of("Limited availability (coffee-growing regions only)", "Higher cost than straw",
                "May need sterilization for Shiitake", "Can be acidic (check pH)",
                "Variable quality between sources"),
            List.of("Add 2% lime to adjust pH to 6.5-7.0", "Mix 50:50 with sawdust for Shiitake blocks",
                "Best for pink and golden oyster varieties", "Store in dry conditions before use")
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_006", "Hardwood Sawdust", "Sawdust",
            "Hardwood sawdust (oak, beech, maple) is the standard substrate for Shiitake and King Oyster cultivation. "
            + "Slow decomposition provides sustained nutrition. Must be supplemented for optimal yields.",
            55, "Autoclave sterilization (121C, 15 psi, 90 min)", 3, 12.0, 1.4,
            List.of("Shiitake", "Oyster"),
            List.of(
                "Use sawdust from hardwood (not softwood like pine)",
                "Sieve to remove large wood chips",
                "Mix with 20% wheat bran or rice bran",
                "Add 2-3% gypsum (calcium sulfate)",
                "Adjust moisture to 55-60%",
                "Fill into autoclavable bags (5-10 kg)",
                "Sterilize at 121C, 15 psi for 90 minutes",
                "Cool completely (24 hours) before spawning"
            ),
            List.of("Ideal for Shiitake and King Oyster", "Long-lasting nutrition for multiple flushes",
                "Good physical structure for air exchange", "Produces high-quality mushrooms",
                "Standardized production process"),
            List.of("Requires autoclave sterilization", "More expensive than straw substrates",
                "Heavier and harder to handle", "Longer preparation time (3 days)",
                "Softwood sawdust unsuitable (resin issues)"),
            List.of("Never use pine, cedar, or treated wood sawdust", "Supplementation critical for yield",
                "Blocks can produce 2-3 flushes", "Target pH 6.0-6.5 with gypsum",
                "Best for indoor controlled environment cultivation")
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_007", "Softwood Sawdust", "Sawdust",
            "Softwood sawdust (pine, spruce) is generally not recommended for mushroom cultivation due to resin content. "
            + "Can be used after proper aging/weathering. Lower yield compared to hardwood.",
            50, "Autoclave sterilization (121C, 15 psi, 120 min)", 5, 8.0, 0.7,
            List.of("Oyster"),
            List.of(
                "Age softwood sawdust for 3-6 months outdoors",
                "Leach with water to remove resins (repeat 2-3 times)",
                "Mix with 30% rice bran for supplementation",
                "Add 5% gypsum",
                "Adjust moisture to 50-55%",
                "Sterilize at 121C for 120 minutes",
                "Cool thoroughly before spawning"
            ),
            List.of("Readily available in many regions", "Lower cost than hardwood sawdust",
                "Can be used after proper treatment", "Lightweight material",
                "Good for substrate blends"),
            List.of("Resins inhibit mycelium growth", "Requires aging/weathering (months)",
                "Lower yield than hardwood", "Longer sterilization needed",
                "Not suitable for Shiitake cultivation"),
            List.of("Only use well-aged sawdust (>6 months)", "Never use fresh softwood sawdust",
                "Mix with 50% hardwood sawdust if available", "Best for Oyster mushrooms only"
            )
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_008", "Mushroom Compost (Phase II)", "Compost",
            "Specialized compost for Button mushrooms (Agaricus bisporus). Two-phase process: outdoor composting "
            + "followed by indoor pasteurization and conditioning. High nutrient content supports multiple flushes.",
            68, "Composting (Phase I & II)", 21, 15.0, 1.8,
            List.of("Button"),
            List.of(
                "Phase I: Mix wheat straw (75%) + horse manure (20%) + gypsum (2%) + water",
                "Outdoor composting: turn pile every 2-3 days for 14-18 days",
                "Phase II: Transfer to pasteurization tunnel",
                "Pasteurize at 60C for 6 hours",
                "Condition at 48-50C for 5-7 days",
                "Cool to 25-28C",
                "Fill beds (15-20 cm depth)",
                "Spawn immediately after filling"
            ),
            List.of("Highest yield multiplier (1.8x)", "Supports 3-4 flushes over 5-6 weeks",
                "Specifically formulated for Button mushrooms", "Well-established commercial process",
                "Multiple harvests from single batch"),
            List.of("Longest preparation time (21 days)", "Most expensive substrate",
                "Requires specialized equipment and knowledge", "Heavy and difficult to handle",
                "Strong odor during Phase I composting"),
            List.of("Horse manure can be replaced with chicken manure (adjust N)", "Proper Phase II conditioning is critical for selectivity",
                "Use temperature probes to monitor compost at all stages", "Ammonia level must be below 10 ppm before spawning"
            )
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_009", "Coco Coir", "Coir",
            "Coco coir is a sustainable byproduct of coconut processing. Excellent water retention, good structure, "
            + "and resists contamination. Often used as casing material or substrate component.",
            75, "Pasteurization (Lime water soak)", 1, 6.0, 0.8,
            List.of("Oyster", "Button"),
            List.of(
                "Buy compressed coco coir bricks",
                "Hydrate with water (1 brick = 5-6 kg wet)",
                "Option: soak in lime water (pH 12-13) for 12-18 hours",
                "Wash with clean water until pH 7.0-7.5",
                "Squeeze to 75% moisture",
                "Mix with 10% vermiculite (optional)",
                "Use as casing layer or mix with straw for substrate"
            ),
            List.of("Excellent water retention", "Resistant to contamination",
                "Sustainable product", "Very fast preparation (1 day)",
                "Good casing material for Button mushrooms"),
            List.of("Low nutrient content (needs supplementation)", "Lower yield when used alone",
                "Not suitable as standalone substrate", "Salt content can be high (check EC)",
                "More expensive than straw"),
            List.of("Buffered coco coir is preferred (low salt)", "Mix 50:50 with straw for oyster substrate",
                "Ideal casing layer for button mushrooms (mix with peat)", "Can be reused after sterilization"
            )
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_010", "Vermiculite", "Mineral",
            "Vermiculite is a lightweight mineral used for moisture retention and as a substrate additive. "
            + "Not used alone but mixed with other substrates or as casing layer component.",
            80, "None (sterile if purchased food-grade)", 0, 20.0, 0.5,
            List.of("Oyster", "Button", "Shiitake"),
            List.of(
                "Purchase food-grade vermiculite (medium grade)",
                "Mix with coco coir (50:50) for casing layer",
                "For substrate: add 10-20% to main substrate",
                "Moisten to desired level",
                "No sterilization needed if food-grade",
                "Use immediately or store dry"
            ),
            List.of("Excellent moisture retention", "Sterile (no contamination risk)",
                "Lightweight and easy to handle", "Improves substrate aeration",
                "Long shelf life when stored dry"),
            List.of("Expensive", "No nutritional value", "Not used as standalone substrate",
                "Mining is environmentally impactful", "Limited benefit in large-scale production"),
            List.of("Use medium grade for best water holding", "Mix with coco coir for button mushroom casing",
                "Add to dry substrates to improve moisture retention", "Can replace perlite in substrate mixes"
            )
        ));

        addSubstrate(new SubstrateRecommendation(
            "sub_011", "Straw + Sawdust Mix", "Mixed",
            "A balanced substrate combining the benefits of straw and sawdust. Straw provides structure and fast colonization, "
            + "while sawdust adds sustained nutrition. Ideal for Oyster and Shiitake cultivation.",
            62, "Pasteurization (Hot water 65-70C for 1.5 hours)", 3, 7.0, 1.3,
            List.of("Oyster", "Shiitake"),
            List.of(
                "Mix 60% chopped wheat/paddy straw with 40% sawdust",
                "Add 5% rice bran (supplement)",
                "Add 2% gypsum",
                "Pre-wet mixture for 6-8 hours",
                "Hot water treatment at 65-70C for 1.5 hours",
                "Drain and cool to 25-28C",
                "Fill grow bags and spawn at 8%"
            ),
            List.of("Balanced nutrition and structure", "Better yield than straw alone (1.3x)",
                "Suitable for multiple species", "Good water retention with aeration",
                "Cost-effective compromise"),
            List.of("Requires two materials (logistics)", "Preparation slightly more complex",
                "Density higher than pure straw", "Sawdust quality affects results",
                "Requires more thorough pasteurization"),
            List.of("Hardwood sawdust preferred for Shiitake", "Straw: hardwood sawdust: 60:40 ideal ratio",
                "Add 10% cotton waste for higher yield", "Best for intermediate and advanced growers"
            )
        ));
    }

    private void addSubstrate(SubstrateRecommendation substrate) {
        substrateMap.put(substrate.substrateId(), substrate);
    }
}
