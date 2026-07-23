package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.DiseaseInfo;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DiseaseAdvisorEngine {

    private static final Logger log = LoggerFactory.getLogger(DiseaseAdvisorEngine.class);

    private final Map<String, DiseaseInfo> diseaseDatabase = new HashMap<>();

    public record DiseaseResponse(
        String diagnosisId,
        List<DiseaseInfo> probableDiseases,
        String primaryDiagnosis,
        String recommendation,
        boolean requiresEscalation
    ) {}

    @PostConstruct
    void seedDiseases() {
        log.info("Seeding disease database with {} entries", 18);
        seed(new DiseaseInfo("D001", "Green Mold", "Trichoderma spp.", "Fungal",
            List.of("Green sporulation on substrate", "Soft decay of mushroom tissue", "Foul odor", "Rapid spread across casing"),
            List.of("Contaminated spawn", "Poor sterilization", "High humidity", "Unclean equipment"),
            0.0, "Remove infected substrate immediately. Apply fungicide (Benomyl 0.1%). Improve ventilation. Reduce humidity to 80-85%. Sterilize all equipment.", List.of("Use sterilized spawn", "Maintain clean environment", "Proper pasteurization of substrate", "Monitor humidity levels"), List.of("Singh et al. 2021, Mushroom Disease Management", "Chang & Miles 2004, Mushrooms: Cultivation"), true, "HIGH", List.of("green_spots_on_substrate", "soft_decay")));
        seed(new DiseaseInfo("D002", "Bacterial Blotch", "Pseudomonas tolaasii", "Bacterial",
            List.of("Brown/amber spots on caps", "Slimy lesions", "Yellow discoloration", "Water-soaked appearance"),
            List.of("High humidity", "Poor air circulation", "Overhead watering", "Contaminated water source"),
            0.0, "Reduce humidity below 85%. Improve air circulation. Remove affected mushrooms. Apply copper-based bactericide. Use chlorinated water (5ppm) for irrigation.", List.of("Avoid overhead watering", "Maintain proper ventilation", "Use clean water", "Control humidity levels"), List.of("Fletcher et al. 1989, Mushroom Science", "Olivier et al. 1978, Bacterial diseases of mushrooms"), false, "MODERATE", List.of("brown_spots_on_caps", "amber_lesions")));
        seed(new DiseaseInfo("D003", "Cobweb Mold", "Cladobotryum spp.", "Fungal",
            List.of("Cobweb-like mycelium on casing", "Rapid spread across surface", "White to gray fluffy growth", "Mushroom decay at base"),
            List.of("High humidity >90%", "Poor ventilation", "Contaminated casing soil", "Overcrowding"),
            0.0, "Remove affected areas immediately. Apply fungicide (Carbendazim 0.05%). Reduce humidity to 80-85%. Increase air exchange. Isolate infected beds.", List.of("Maintain humidity below 90%", "Ensure proper ventilation", "Use pasteurized casing", "Avoid overcrowding"), List.of("Fletcher & Gaze 2008, Mushroom Pest and Disease Control", "Staunton 1995, Cobweb disease control"), true, "HIGH", List.of("cobweb_growth", "fluffy_mycelium")));
        seed(new DiseaseInfo("D004", "Dry Bubble", "Verticillium fungicola", "Fungal",
            List.of("Deformed mushrooms", "Brown spots on caps", "Stunted growth", "Cracked cap surface", "Grayish discoloration"),
            List.of("Contaminated casing soil", "Infected spawn", "Poor hygiene practices", "High humidity"),
            0.0, "Remove and destroy infected mushrooms. Apply fungicide (Prochloraz 0.1%). Reduce humidity. Improve air circulation. Practice strict hygiene between crops.", List.of("Use pasteurized casing", "Sterilize tools and equipment", "Maintain optimal humidity", "Regular monitoring"), List.of("Gaze & Fletcher 2008, Mushroom Pest and Disease Control", "Van Griensven 1988, Cultivation of Mushrooms"), true, "HIGH", List.of("deformed_mushrooms", "brown_spots")));
        seed(new DiseaseInfo("D005", "Wet Bubble", "Mycogone perniciosa", "Fungal",
            List.of("Amber droplets on mushrooms", "Severe mushroom deformation", "Soft watery rot", "White fluffy growth turning brown", "Foul smell"),
            List.of("Contaminated casing", "Excessive humidity", "Poor sanitation", "Infected spawn"),
            0.0, "Destroy all infected beds immediately. Apply fungicide (Prochloraz 0.15%). Reduce humidity to 75-80%. Improve drainage. Steam sterilize casing at 60°C for 30 min.", List.of("Use disease-free spawn", "Pasteurize casing properly", "Maintain humidity 80-85%", "Strict hygiene protocols"), List.of("Fletcher & Gaze 2008, Mushroom Pest and Disease Control", "Umar et al. 2000, Wet bubble disease"), true, "CRITICAL", List.of("amber_droplets", "mushroom_deformation")));
        seed(new DiseaseInfo("D006", "Brown Spot", "Pseudomonas agarici", "Bacterial",
            List.of("Small brown spots on caps", "Lesions 1-3mm diameter", "Spots remain superficial", "No soft rot"),
            List.of("High humidity", "Free water on caps", "Poor ventilation", "Overcrowding"),
            0.0, "Improve air circulation. Reduce humidity. Avoid cap wetting. Apply copper oxychloride spray if severe. Usually self-limiting with environmental control.", List.of("Maintain good ventilation", "Avoid cap wetting", "Proper spacing of beds", "Control humidity"), List.of("Gill & Tsuneda 1997, Bacterial diseases of mushrooms", "Fletcher 1995, Mushroom disease control"), false, "LOW", List.of("small_brown_spots", "superficial_lesions")));
        seed(new DiseaseInfo("D007", "Pink Mold", "Neurospora sitophila", "Fungal",
            List.of("Pink/orange mold on substrate", "Rapid sporulation", "Musty odor", "Competition with mushroom mycelium"),
            List.of("Contaminated substrate", "Poor pasteurization", "High temperature", "High moisture content"),
            0.0, "Remove contaminated substrate. Reduce temperature to 22-25°C. Apply fungicide (Thiabendazole 0.05%). Improve air circulation. Use fresh pasteurized substrate.", List.of("Proper substrate pasteurization", "Maintain optimal temperature", "Use clean spawn", "Monitor moisture levels"), List.of("Staunton 1995, Mushroom Diseases", "Sharma & Kumar 2011, Mushroom Cultivation"), false, "MODERATE", List.of("pink_orange_mold", "rapid_sporulation")));
        seed(new DiseaseInfo("D008", "Black Mold", "Aspergillus niger", "Fungal",
            List.of("Black sporulation on substrate", "Dark powdery patches", "Common in humid conditions", "Competes with mushroom mycelium"),
            List.of("High humidity >85%", "Poor ventilation", "Contaminated substrate", "Warm temperatures"),
            0.0, "Remove affected substrate. Reduce humidity to 75-80%. Improve ventilation. Apply fungicide if severe. Maintain temperature below 28°C.", List.of("Maintain humidity below 85%", "Proper substrate pasteurization", "Good air circulation", "Temperature control"), List.of("Samson et al. 2010, Food and Indoor Fungi", "Pitt & Hocking 2009, Fungi and Food Spoilage"), false, "LOW", List.of("black_sporulation", "dark_patches")));
        seed(new DiseaseInfo("D009", "Bacterial Soft Rot", "Various Pseudomonas spp.", "Bacterial",
            List.of("Soft watery decay of mushroom tissue", "Foul smell", "Complete tissue breakdown", "Water-soaked lesions"),
            List.of("Excessive moisture", "Poor drainage", "Contaminated water", "Wound sites on mushrooms"),
            0.0, "Remove all affected mushrooms immediately. Improve drainage. Reduce watering frequency. Apply copper-based bactericide. Increase air circulation to dry surface.", List.of("Avoid overwatering", "Ensure good drainage", "Handle mushrooms carefully", "Use clean irrigation water"), List.of("Fletcher 1995, Mushroom Disease Control", "Rainey et al. 1992, Bacterial soft rot"), true, "HIGH", List.of("watery_decay", "tissue_breakdown")));
        seed(new DiseaseInfo("D010", "Mite Infestation", "Various Acari spp.", "Pest",
            List.of("Tiny insects visible on gills and stipe", "Brown discoloration of gills", "Stunted mushroom growth", "Webbing on substrate surface"),
            List.of("Infested spawn", "Contaminated casing", "Poor farm hygiene", "Nearby infested farms"),
            0.0, "Apply miticide (Abamectin 0.05%). Remove infested beds. Improve sanitation. Use sticky traps. Maintain temperature below 25°C to slow reproduction.", List.of("Use mite-free spawn", "Maintain clean facility", "Monitor incoming materials", "Install insect screens"), List.of("Fletcher & Gaze 2008, Mushroom Pest and Disease Control", "White 1997, Mushroom pests"), false, "MODERATE", List.of("tiny_insects_on_gills", "brown_discoloration")));
        seed(new DiseaseInfo("D011", "Sciarid Flies", "Lycoriella spp.", "Pest",
            List.of("Larvae damage mycelium", "Adult flies on mushroom surface", "Reduced yield", "Tunneling in stipe"),
            List.of("Open doors/windows", "Contaminated casing", "Nearby infested farms", "Warm conditions"),
            0.0, "Apply insecticide (Permethrin 0.05%). Use yellow sticky traps. Install insect screens. Remove crop debris. Biological control with nematodes (Steinernema feltiae).", List.of("Install insect screens", "Use sticky traps", "Maintain farm hygiene", "Monitor with yellow sticky traps"), List.of("White 1997, Mushroom Pests", "Fletcher & Gaze 2008, Mushroom Pest and Disease Control"), false, "MODERATE", List.of("larvae_in_substrate", "adult_flies")));
        seed(new DiseaseInfo("D012", "Brown Blotch", "Pseudomonas tolaasii", "Bacterial",
            List.of("Brown blotchy lesions on caps", "Irregular spot patterns", "Lesions enlarge over time", "Slimy texture on spots"),
            List.of("High humidity >90%", "Free water on mushroom surface", "Poor air circulation", "Overcrowding"),
            0.0, "Reduce humidity to 80-85%. Improve ventilation. Avoid overhead watering. Apply copper-based bactericide. Remove severely affected mushrooms.", List.of("Maintain humidity below 85%", "Avoid wetting caps", "Ensure good air circulation", "Proper spacing"), List.of("Fletcher 1995, Mushroom Disease Control", "Rainey et al. 1992, Pseudomonas diseases"), false, "MODERATE", List.of("brown_blotchy_lesions", "irregular_spots")));
        seed(new DiseaseInfo("D013", "Verticillium Dry Bubble", "Verticillium fungicola", "Fungal",
            List.of("Gray-white mycelial growth on mushrooms", "Deformed bubble-like structures", "Brown necrotic spots", "Cracked cap surface"),
            List.of("Contaminated casing", "Infected spawn", "Poor sanitation", "High humidity"),
            0.0, "Remove infected mushrooms. Apply fungicide (Prochloraz 0.1%). Reduce humidity to 80-85%. Improve air circulation. Steam treat casing at 60°C.", List.of("Use disease-free spawn", "Pasteurize casing", "Maintain clean environment", "Monitor early signs"), List.of("Fletcher & Gaze 2008, Mushroom Pest and Disease Control", "Van Griensven 1988"), true, "HIGH", List.of("gray_white_mycelial_growth", "necrotic_spots")));
        seed(new DiseaseInfo("D014", "Fusarium Wilt", "Fusarium spp.", "Fungal",
            List.of("Stunted growth", "Yellowing of mushroom tissue", "Wilting appearance", "Reduced yield", "Brown vascular discoloration"),
            List.of("Infected spawn", "Contaminated substrate", "High temperature stress", "Poor nutrition"),
            0.0, "Remove infected mushrooms. Improve environmental conditions. Apply fungicide (Carbendazim 0.1%). Optimize temperature to 22-26°C. Ensure proper nutrition in substrate.", List.of("Use disease-free spawn", "Proper substrate formulation", "Temperature management", "Good farm hygiene"), List.of("Booth 1971, The Genus Fusarium", "Nelson et al. 1983, Fusarium species"), false, "MODERATE", List.of("stunted_growth", "yellowing_tissue")));
        seed(new DiseaseInfo("D015", "Mushroom Virus (La France disease)", "La France virus", "Viral",
            List.of("Slow growth", "Deformed caps", "Brown discoloration", "Reduced yield up to 50%", "Watery mushrooms"),
            List.of("Infected spawn", "Contaminated tools", "Spores from infected mushrooms", "Poor hygiene"),
            0.0, "No cure available. Destroy all infected crops. Sterilize entire facility. Use virus-free spawn. Implement strict quarantine procedures. Replace casing and substrate completely.", List.of("Use virus-free spawn", "Sterilize all equipment", "Implement quarantine", "Regular testing of spawn"), List.of("Hollings 1962, Mushroom viruses", "Van Zaayen 1979, La France disease"), true, "CRITICAL", List.of("deformed_caps", "slow_growth")));
        seed(new DiseaseInfo("D016", "Dieback", "Dactylium dendroides", "Fungal",
            List.of("Browning and dying of mushrooms", "White mycelial growth on casing", "Mushrooms turn brown and collapse", "Reduced yield"),
            List.of("Contaminated casing", "High humidity", "Poor ventilation", "Infected spawn"),
            0.0, "Remove affected mushrooms. Apply fungicide (Carbendazim 0.05%). Improve ventilation. Reduce humidity to 80-85%. Practice crop rotation.", List.of("Use pasteurized casing", "Maintain good ventilation", "Monitor humidity", "Regular cleaning"), List.of("Fletcher & Gaze 2008", "Staunton 1995"), false, "MODERATE", List.of("browning_mushrooms", "collapsed_tissue")));
        seed(new DiseaseInfo("D017", "Bacterial Pit", "Various Pseudomonas spp.", "Bacterial",
            List.of("Small pits on mushroom caps", "Shallow depressions", "Brown discoloration around pits", "Superficial lesions"),
            List.of("High humidity", "Free water on caps", "Poor air circulation", "Overcrowding"),
            0.0, "Improve ventilation. Reduce humidity. Avoid overhead watering. Usually cosmetic and self-limiting. Copper spray if severe.", List.of("Maintain good air circulation", "Avoid cap wetting", "Proper spacing", "Humidity control"), List.of("Fletcher 1995", "Gill & Tsuneda 1997"), false, "LOW", List.of("small_pits_on_caps", "superficial_depressions")));
        seed(new DiseaseInfo("D018", "Casing Contamination", "Various competitor molds", "Various",
            List.of("Weeds and competitor molds in casing", "Abnormal mycelial growth", "Uneven pinhead formation", "Reduced yield"),
            List.of("Poor quality casing", "Inadequate pasteurization", "Contaminated water", "Poor hygiene"),
            0.0, "Remove contaminated casing layer. Re-casing with fresh pasteurized material. Improve environmental control. Adjust pH of casing to 7.0-7.5.", List.of("Use quality casing material", "Proper pasteurization", "Maintain casing pH 7.0-7.5", "Good farm hygiene"), List.of("Van Griensven 1988", "Chang & Miles 2004"), false, "LOW", List.of("competitor_molds", "uneven_growth")));
        log.info("Disease database seeded with {} entries", diseaseDatabase.size());
    }

    private void seed(DiseaseInfo disease) {
        diseaseDatabase.put(disease.diseaseId(), disease);
    }

    public DiseaseResponse diagnoseBySymptoms(List<String> symptoms, String mushroomType, String growthStage) {
        log.debug("Diagnosing by symptoms for mushroomType={}, growthStage={}, symptoms={}", mushroomType, growthStage, symptoms);
        List<DiseaseInfo> ranked = rankBySymptomMatch(symptoms);
        DiseaseInfo primary = ranked.isEmpty() ? null : ranked.get(0);
        boolean escalation = ranked.stream().anyMatch(DiseaseInfo::requiresEscalation);
        String recommendation = buildRecommendation(primary, escalation);
        return new DiseaseResponse(
            UUID.randomUUID().toString(),
            ranked,
            primary != null ? primary.diseaseName() : "No matching disease found",
            recommendation,
            escalation
        );
    }

    public List<DiseaseInfo> diagnoseBySymptoms(List<String> symptoms) {
        log.debug("Diagnosing by symptoms only: {}", symptoms);
        return rankBySymptomMatch(symptoms);
    }

    public DiseaseInfo getDiseaseById(String diseaseId) {
        log.debug("Fetching disease by id: {}", diseaseId);
        return diseaseDatabase.get(diseaseId);
    }

    public List<DiseaseInfo> getAllDiseases() {
        log.debug("Fetching all diseases");
        return List.copyOf(diseaseDatabase.values());
    }

    public List<DiseaseInfo> getDiseasesByCategory(String category) {
        log.debug("Fetching diseases by category: {}", category);
        return diseaseDatabase.values().stream()
            .filter(d -> d.category().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    }

    public List<DiseaseInfo> getDiseasesBySeverity(String severity) {
        log.debug("Fetching diseases by severity: {}", severity);
        return diseaseDatabase.values().stream()
            .filter(d -> d.severity().equalsIgnoreCase(severity))
            .collect(Collectors.toList());
    }

    public List<String> getPreventionTips(String diseaseId) {
        log.debug("Fetching prevention tips for disease: {}", diseaseId);
        DiseaseInfo disease = diseaseDatabase.get(diseaseId);
        if (disease == null) {
            log.warn("Disease not found: {}", diseaseId);
            return List.of();
        }
        return disease.preventionMethods();
    }

    public String getTreatmentPlan(String diseaseId) {
        log.debug("Fetching treatment plan for disease: {}", diseaseId);
        DiseaseInfo disease = diseaseDatabase.get(diseaseId);
        if (disease == null) {
            log.warn("Disease not found: {}", diseaseId);
            return "No treatment plan available for disease ID: " + diseaseId;
        }
        return disease.treatmentPlan();
    }

    public List<DiseaseInfo> getSeasonalDiseaseRisk(int month, String region) {
        log.debug("Calculating seasonal disease risk for month={}, region={}", month, region);
        List<DiseaseInfo> seasonalRisks = new ArrayList<>();
        for (DiseaseInfo disease : diseaseDatabase.values()) {
            double seasonalFactor = computeSeasonalFactor(month, disease);
            if (seasonalFactor > 0.5) {
                DiseaseInfo adjusted = new DiseaseInfo(
                    disease.diseaseId(), disease.diseaseName(), disease.scientificName(),
                    disease.category(), disease.symptoms(), disease.possibleCauses(),
                    seasonalFactor, disease.treatmentPlan(), disease.preventionMethods(),
                    disease.scientificReferences(), disease.requiresEscalation(),
                    disease.severity(), disease.imageIndicators()
                );
                seasonalRisks.add(adjusted);
            }
        }
        seasonalRisks.sort(Comparator.comparing(DiseaseInfo::probabilityScore).reversed());
        return seasonalRisks;
    }

    public List<DiseaseInfo> getCommonDiseases(String speciesName) {
        log.debug("Fetching common diseases for species: {}", speciesName);
        return diseaseDatabase.values().stream()
            .filter(d -> isCommonForSpecies(d, speciesName))
            .collect(Collectors.toList());
    }

    public boolean escalateCase(DiseaseInfo disease) {
        boolean escalate = "HIGH".equalsIgnoreCase(disease.severity()) || "CRITICAL".equalsIgnoreCase(disease.severity());
        log.info("Escalation check for {}: severity={}, escalate={}", disease.diseaseName(), disease.severity(), escalate);
        return escalate;
    }

    private List<DiseaseInfo> rankBySymptomMatch(List<String> symptoms) {
        List<String> lowerSymptoms = symptoms.stream().map(String::toLowerCase).toList();
        List<DiseaseInfo> ranked = new ArrayList<>();
        for (DiseaseInfo disease : diseaseDatabase.values()) {
            long matchCount = disease.symptoms().stream()
                .map(String::toLowerCase)
                .filter(s -> lowerSymptoms.stream().anyMatch(s::contains))
                .count();
            double probability = (double) matchCount / Math.max(disease.symptoms().size(), 1);
            if (probability > 0) {
                ranked.add(new DiseaseInfo(
                    disease.diseaseId(), disease.diseaseName(), disease.scientificName(),
                    disease.category(), disease.symptoms(), disease.possibleCauses(),
                    probability, disease.treatmentPlan(), disease.preventionMethods(),
                    disease.scientificReferences(), disease.requiresEscalation(),
                    disease.severity(), disease.imageIndicators()
                ));
            }
        }
        ranked.sort(Comparator.comparing(DiseaseInfo::probabilityScore).reversed());
        return ranked;
    }

    private String buildRecommendation(DiseaseInfo primary, boolean escalation) {
        if (primary == null) {
            return "No matching disease patterns found. Monitor closely and consult an expert if symptoms persist.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Primary diagnosis: ").append(primary.diseaseName()).append(". ");
        sb.append("Treatment: ").append(primary.treatmentPlan()).append(" ");
        if (escalation) {
            sb.append("ESCALATION REQUIRED: This condition requires immediate expert intervention.");
        }
        return sb.toString();
    }

    private double computeSeasonalFactor(int month, DiseaseInfo disease) {
        Map<Integer, Double> seasonalMap = getSeasonalPattern(disease);
        return seasonalMap.getOrDefault(month, 0.1);
    }

    private Map<Integer, Double> getSeasonalPattern(DiseaseInfo disease) {
        Map<Integer, Double> pattern = new HashMap<>();
        String cat = disease.category().toLowerCase();
        if (cat.contains("fungal")) {
            for (int m = 1; m <= 12; m++) {
                pattern.put(m, (m >= 6 && m <= 9) ? 0.8 : (m >= 3 && m <= 5) ? 0.5 : 0.3);
            }
        } else if (cat.contains("bacterial")) {
            for (int m = 1; m <= 12; m++) {
                pattern.put(m, (m >= 7 && m <= 10) ? 0.7 : (m >= 4 && m <= 6) ? 0.5 : 0.2);
            }
        } else if (cat.contains("pest")) {
            for (int m = 1; m <= 12; m++) {
                pattern.put(m, (m >= 3 && m <= 6) ? 0.8 : (m >= 7 && m <= 9) ? 0.5 : 0.2);
            }
        } else {
            for (int m = 1; m <= 12; m++) {
                pattern.put(m, 0.3);
            }
        }
        return pattern.get(month);
    }

    private boolean isCommonForSpecies(DiseaseInfo disease, String speciesName) {
        String name = speciesName.toLowerCase();
        if (name.contains("oyster")) {
            return List.of("D001", "D002", "D003", "D007", "D010", "D011").contains(disease.diseaseId());
        }
        if (name.contains("button") || name.contains("agaricus")) {
            return List.of("D002", "D004", "D005", "D006", "D012", "D013", "D015").contains(disease.diseaseId());
        }
        if (name.contains("milky") || name.contains("calocybe")) {
            return List.of("D001", "D003", "D008", "D014", "D016").contains(disease.diseaseId());
        }
        if (name.contains("shiitake") || name.contains("lentinula")) {
            return List.of("D001", "D008", "D009", "D010", "D011").contains(disease.diseaseId());
        }
        if (name.contains("paddy") || name.contains("straw") || name.contains("volvariella")) {
            return List.of("D001", "D007", "D008", "D016", "D018").contains(disease.diseaseId());
        }
        return List.of("D001", "D002", "D003", "D008").contains(disease.diseaseId());
    }
}
