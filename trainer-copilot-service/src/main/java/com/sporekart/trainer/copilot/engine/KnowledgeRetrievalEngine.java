package com.sporekart.trainer.copilot.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeRetrievalEngine {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeRetrievalEngine.class);

    private static final List<KnowledgeEntry> KNOWLEDGE_BASE = buildKnowledgeBase();

    public KnowledgeRetrievalEngine() {
        log.info("KnowledgeRetrievalEngine initialized with {} knowledge entries", KNOWLEDGE_BASE.size());
    }

    public List<Map<String, Object>> searchKnowledge(String query, int maxResults) {
        log.info("Searching knowledge for query='{}', maxResults={}", query, maxResults);

        String lowerQuery = query.toLowerCase();
        List<Map<String, Object>> results = KNOWLEDGE_BASE.stream()
                .filter(entry -> matchesQuery(entry, lowerQuery))
                .sorted((a, b) -> Double.compare(
                        calculateRelevance(b, lowerQuery),
                        calculateRelevance(a, lowerQuery)))
                .limit(maxResults)
                .map(this::toResultMap)
                .toList();

        if (results.isEmpty()) {
            log.warn("No results found for query='{}', returning fallback", query);
            results = List.of(fallbackResult("No specific results found for: " + query));
        }

        log.info("Knowledge search returned {} results", results.size());
        return results;
    }

    public List<Map<String, Object>> searchTrainingManuals(String query) {
        log.info("Searching training manuals for query='{}'", query);

        String lowerQuery = query.toLowerCase();
        List<Map<String, Object>> results = KNOWLEDGE_BASE.stream()
                .filter(e -> "manual".equals(e.type) || "training".equals(e.type))
                .filter(e -> matchesQuery(e, lowerQuery))
                .map(this::toResultMap)
                .toList();

        if (results.isEmpty()) {
            results = KNOWLEDGE_BASE.stream()
                    .filter(e -> "manual".equals(e.type) || "training".equals(e.type))
                    .limit(3)
                    .map(this::toResultMap)
                    .toList();
        }

        log.info("Manual search returned {} results", results.size());
        return results;
    }

    public List<Map<String, Object>> searchSOPs(String topic) {
        log.info("Searching SOPs for topic='{}'", topic);

        String lowerTopic = topic.toLowerCase();
        List<Map<String, Object>> results = KNOWLEDGE_BASE.stream()
                .filter(e -> "sop".equals(e.type))
                .filter(e -> matchesQuery(e, lowerTopic))
                .map(this::toResultMap)
                .toList();

        if (results.isEmpty()) {
            results = KNOWLEDGE_BASE.stream()
                    .filter(e -> "sop".equals(e.type))
                    .limit(3)
                    .map(this::toResultMap)
                    .toList();
        }

        log.info("SOP search returned {} results", results.size());
        return results;
    }

    public List<Map<String, Object>> searchFAQs(String query) {
        log.info("Searching FAQs for query='{}'", query);

        String lowerQuery = query.toLowerCase();
        List<Map<String, Object>> results = KNOWLEDGE_BASE.stream()
                .filter(e -> "faq".equals(e.type))
                .filter(e -> matchesQuery(e, lowerQuery))
                .map(this::toResultMap)
                .toList();

        if (results.isEmpty()) {
            results = KNOWLEDGE_BASE.stream()
                    .filter(e -> "faq".equals(e.type))
                    .limit(3)
                    .map(this::toResultMap)
                    .toList();
        }

        log.info("FAQ search returned {} results", results.size());
        return results;
    }

    public List<Map<String, Object>> getCultivationKnowledge(String topic) {
        log.info("Retrieving cultivation knowledge for topic='{}'", topic);

        String lowerTopic = topic.toLowerCase();
        List<Map<String, Object>> results = KNOWLEDGE_BASE.stream()
                .filter(e -> "cultivation".equals(e.category) || "general".equals(e.category))
                .filter(e -> matchesQuery(e, lowerTopic))
                .map(this::toResultMap)
                .toList();

        if (results.isEmpty()) {
            results = KNOWLEDGE_BASE.stream()
                    .filter(e -> "cultivation".equals(e.category) || "general".equals(e.category))
                    .limit(5)
                    .map(this::toResultMap)
                    .toList();
        }

        log.info("Cultivation knowledge returned {} results", results.size());
        return results;
    }

    public List<Map<String, Object>> getGovernmentGuidelines(String topic) {
        log.info("Retrieving government guidelines for topic='{}'", topic);

        String lowerTopic = topic.toLowerCase();
        List<Map<String, Object>> results = KNOWLEDGE_BASE.stream()
                .filter(e -> "government".equals(e.category))
                .filter(e -> matchesQuery(e, lowerTopic))
                .map(this::toResultMap)
                .toList();

        if (results.isEmpty()) {
            results = KNOWLEDGE_BASE.stream()
                    .filter(e -> "government".equals(e.category))
                    .map(this::toResultMap)
                    .toList();
        }

        log.info("Government guidelines returned {} results", results.size());
        return results;
    }

    public List<Map<String, Object>> getScientificResearch(String topic) {
        log.info("Retrieving scientific research for topic='{}'", topic);

        String lowerTopic = topic.toLowerCase();
        List<Map<String, Object>> results = KNOWLEDGE_BASE.stream()
                .filter(e -> "research".equals(e.category))
                .filter(e -> matchesQuery(e, lowerTopic))
                .map(this::toResultMap)
                .toList();

        if (results.isEmpty()) {
            results = KNOWLEDGE_BASE.stream()
                    .filter(e -> "research".equals(e.category))
                    .limit(3)
                    .map(this::toResultMap)
                    .toList();
        }

        log.info("Scientific research returned {} results", results.size());
        return results;
    }

    // ---- Private helpers ----

    private boolean matchesQuery(KnowledgeEntry entry, String lowerQuery) {
        String[] terms = lowerQuery.split("\\s+");
        String searchable = (entry.title + " " + entry.content + " " + entry.tags + " " + entry.category).toLowerCase();
        for (String term : terms) {
            if (term.length() > 2 && searchable.contains(term)) {
                return true;
            }
        }
        return false;
    }

    private double calculateRelevance(KnowledgeEntry entry, String lowerQuery) {
        String[] terms = lowerQuery.split("\\s+");
        double score = 0;
        String title = entry.title.toLowerCase();
        String content = entry.content.toLowerCase();
        String tags = entry.tags.toLowerCase();

        for (String term : terms) {
            if (term.length() <= 2) continue;
            if (title.contains(term)) score += 10;
            if (tags.contains(term)) score += 5;
            int contentCount = content.split(term, -1).length - 1;
            score += contentCount * 2;
        }
        return Math.min(score, 100);
    }

    private Map<String, Object> toResultMap(KnowledgeEntry entry) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", entry.id);
        map.put("title", entry.title);
        map.put("content", entry.content);
        map.put("type", entry.type);
        map.put("category", entry.category);
        map.put("tags", entry.tags);
        map.put("source", entry.source);
        map.put("citation", entry.citation);
        map.put("relevanceScore", 0);  // will be set by caller if needed
        return map;
    }

    private Map<String, Object> fallbackResult(String message) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", "FALLBACK-001");
        map.put("title", "No matches found");
        map.put("content", message + ". Please refine your query or consult the full training manual for comprehensive information.");
        map.put("type", "info");
        map.put("category", "general");
        map.put("tags", "fallback general");
        map.put("source", "Knowledge Retrieval Engine");
        map.put("citation", "SporeKart Training Manual - General Reference");
        return map;
    }

    // ---- Knowledge Base (30+ entries) ----

    private static List<KnowledgeEntry> buildKnowledgeBase() {
        List<KnowledgeEntry> kb = new ArrayList<>();

        // Spawn Preparation
        kb.add(new KnowledgeEntry("KB-001", "Grain Spawn Production Protocol",
                "Grain spawn is produced by inoculating sterilized grain with mushroom mycelium. Rye grain is preferred due to its excellent nutrient profile and water-holding capacity. The process involves: soaking grain for 12-24 hours, boiling for 15-20 minutes, draining excess moisture, filling jars to 2/3 capacity, sterilizing at 121°C/15 PSI for 90 minutes, cooling, and inoculating under sterile conditions.",
                "sop", "cultivation", "spawn grain sterilization inoculation rye",
                "SporeKart Training Manual v2.3, Chapter 4: Spawn Production",
                "SporeKart Training Academy (2026). Mushroom Cultivation Mastery - Spawn Production. Section 4.2, pp. 45-52."));

        kb.add(new KnowledgeEntry("KB-002", "Spawn Quality Assessment Criteria",
                "Quality spawn should show uniform white mycelial growth throughout the grain, a fresh mushroom aroma, and no signs of contamination (green, black, pink, or orange discoloration). Spawn should be used within 2 weeks of full colonization for best results, or stored at 2-4°C for up to 6 months. Always test suspicious spawn on agar before bulk use.",
                "manual", "cultivation", "spawn quality assessment contamination storage",
                "SporeKart Quality Control Manual",
                "SporeKart Training Academy (2026). Quality Control in Mushroom Cultivation. Section 3, pp. 28-31."));

        kb.add(new KnowledgeEntry("KB-003", "Liquid Culture Preparation",
                "Liquid culture is prepared by inoculating sterile nutrient solution (typically 4% honey or light malt extract in distilled water) with mycelium from an agar plate. Incubate at 22-25°C for 7-14 days with occasional shaking. Healthy liquid culture shows uniform cloudiness with visible mycelial masses. Test on agar before use. Store at 2-4°C for up to 3 months.",
                "sop", "cultivation", "liquid culture honey extract mycelium",
                "SporeKart Lab Procedures Manual",
                "SporeKart Training Academy (2026). Advanced Cultivation Techniques. Section 2.1, pp. 15-19."));

        // Substrate Preparation
        kb.add(new KnowledgeEntry("KB-004", "Straw Pasteurization Protocol",
                "For oyster mushroom cultivation on straw: chop wheat straw into 2-4 inch pieces, hydrate for 4-6 hours in clean water, drain, and heat to 65-70°C. Maintain temperature for 90 minutes. Do not exceed 75°C as beneficial microbes will be killed. Cool to 25-30°C before spawning. Target moisture content: 60-65% (squeeze test releases 1-2 drops). pH should be 6.5-7.0.",
                "sop", "cultivation", "straw pasteurization oyster moisture pH",
                "SporeKart Cultivation Protocols v3.0",
                "SporeKart Training Academy (2026). Substrate Preparation Manual. Section 5, pp. 60-68."));

        kb.add(new KnowledgeEntry("KB-005", "Sawdust Substrate Formulation",
                "Hardwood sawdust substrate for shiitake and lion's mane: use oak, beech, or maple sawdust (not softwoods). Supplement with 10-20% wheat bran or soy hulls. Add 1-2% gypsum for pH buffering and calcium. Target moisture: 55-60%. Fill into filter patch bags (2-5 kg each). Sterilize at 121°C for 2-3 hours depending on bag size. Cool completely before inoculation.",
                "manual", "cultivation", "sawdust hardwood substrate shiitake supplement bran gypsum",
                "SporeKart Cultivation Protocols v3.0",
                "SporeKart Training Academy (2026). Substrate Preparation Manual. Section 6, pp. 70-78."));

        kb.add(new KnowledgeEntry("KB-006", "Compost Preparation for Button Mushrooms",
                "Phase I: Mix horse manure (70%), wheat straw (25%), and gypsum (5%). Add water to reach 70-75% moisture. Compost outdoors for 6-14 days, turning every 2-3 days when internal temperature reaches 70-80°C. Phase II: Indoor pasteurization at 55-60°C for 6 hours, then condition at 45-50°C for 5-7 days to eliminate ammonia. Final pH: 7.0-7.5.",
                "manual", "cultivation", "compost button manure straw phase gypsum ammonia",
                "SporeKart Button Mushroom Cultivation Guide",
                "SporeKart Training Academy (2026). Button Mushroom Production. Chapter 3, pp. 35-45."));

        // Sterilization
        kb.add(new KnowledgeEntry("KB-007", "Autoclave Operation SOP",
                "1. Check water level in chamber. 2. Load materials with space for steam circulation. 3. Close door and engage locking mechanism. 4. Set temperature to 121°C, pressure to 15 PSI. 5. Set timer according to load: 30 min for tools, 90 min for grain jars, 120-180 min for large substrate bags. 6. Allow natural cooling until pressure gauge reads zero. 7. Open door 2 inches for 15-20 min before full opening. 8. Use heat-resistant gloves.",
                "sop", "cultivation", "autoclave sterilization pressure temperature cooling",
                "SporeKart Lab Safety & SOP Manual",
                "SporeKart Training Academy (2026). Laboratory Operations. Section 8, pp. 100-108."));

        kb.add(new KnowledgeEntry("KB-008", "Chemical Sterilization Methods",
                "70% isopropyl alcohol is the standard surface disinfectant for mushroom cultivation. It penetrates better than 90% alcohol due to slower evaporation. 10% bleach solution (sodium hypochlorite) is effective for equipment but must be rinsed thoroughly. 3% hydrogen peroxide can be used for sensitive materials. Never use chemical sterilants on substrates intended for consumption.",
                "faq", "cultivation", "chemical sterilization alcohol bleach peroxide",
                "SporeKart FAQ Compendium",
                "SporeKart Training Academy (2026). Sterilization Methods Reference. pp. 12-14."));

        // Incubation
        kb.add(new KnowledgeEntry("KB-009", "Optimal Incubation Parameters",
                "Most gourmet mushroom species incubate best at 22-25°C in complete darkness. Humidity should be 60-70%. CO2 levels below 5000 ppm. Air exchange through filter patches or loose lids provides sufficient oxygen. Grain spawn typically colonizes in 10-14 days. Bulk substrate takes 2-6 weeks depending on species and substrate volume. Check every 2-3 days for contamination.",
                "manual", "cultivation", "incubation temperature humidity darkness colonization",
                "SporeKart Incubation Management Guide",
                "SporeKart Training Academy (2026). Incubation and Environmental Control. Chapter 4, pp. 50-58."));

        kb.add(new KnowledgeEntry("KB-010", "Shaking Protocol for Spawn Jars",
                "Shake grain spawn jars when approximately 30% colonized to redistribute mycelium and speed up colonization. This creates multiple growth points and prevents grain from fusing into a solid block. Do not shake if contamination is suspected (visible discoloration, off odors). After shaking, mycelium typically recolonizes within 3-5 days.",
                "sop", "cultivation", "shaking spawn jar colonization redistribution",
                "SporeKart Spawn Production SOP",
                "SporeKart Training Academy (2026). Spawn Production Manual. Section 4.5, pp. 48-49."));

        // Fruiting
        kb.add(new KnowledgeEntry("KB-011", "Pinning Induction Protocol",
                "Trigger fruiting by reducing temperature 5-8°C, increasing fresh air exchange (CO2 < 1000 ppm), raising humidity to 90-95%, and providing indirect light (500-1000 lux, 8-12 hours/day). These environmental changes signal the mycelium to transition from vegetative growth to reproduction. Pins typically appear within 5-14 days after conditions are changed.",
                "sop", "cultivation", "pinning induction fruiting temperature humidity light FAE",
                "SporeKart Fruiting Management Guide",
                "SporeKart Training Academy (2026). Fruiting and Harvest Management. Chapter 5, pp. 72-80."));

        kb.add(new KnowledgeEntry("KB-012", "Casing Layer Application",
                "Casing layer is a non-nutritive layer applied to colonized substrate to induce pinning. Standard casing: 50% peat moss + 50% vermiculite, adjust pH to 7.5 with hydrated lime. Apply 3-5 cm depth. Keep casing moist but not wet. For button mushrooms, casing is essential. For other species, it improves moisture retention and can increase yield.",
                "manual", "cultivation", "casing peat vermiculite pH pinning button",
                "SporeKart Fruiting Management Guide",
                "SporeKart Training Academy (2026). Advanced Cultivation Techniques. Section 3.2, pp. 30-33."));

        // Diseases
        kb.add(new KnowledgeEntry("KB-013", "Green Mold (Trichoderma) Identification and Control",
                "Trichoderma appears as green spore masses that spread rapidly. Initial white mycelium quickly turns dark green. Causes: inadequate sterilization, contaminated spawn, poor air filtration. Treatment: immediately isolate and dispose of affected materials in sealed bags. Disinfect area with 10% bleach. Prevention: proper sterilization at 121°C, HEPA filtration, strict hygiene protocols.",
                "manual", "cultivation", "trichoderma green mold contamination sterilization disease",
                "SporeKart Disease Management Guide",
                "SporeKart Training Academy (2026). Mushroom Diseases and Their Management. Chapter 6, pp. 85-95."));

        kb.add(new KnowledgeEntry("KB-014", "Bacterial Blotch Management",
                "Bacterial blotch (Pseudomonas tolaasii) causes yellow to brown lesions on mushroom caps. Common in high humidity with poor air exchange. Treatment: reduce humidity to 80-85%, increase air exchange, remove affected mushrooms, improve drainage. Prevention: avoid free water on caps, maintain 85-90% humidity, ensure adequate FAE, use clean water sources.",
                "manual", "cultivation", "bacterial blotch pseudomonas lesion humidity FAE",
                "SporeKart Disease Management Guide",
                "SporeKart Training Academy (2026). Mushroom Diseases. Chapter 6.2, pp. 90-92."));

        kb.add(new KnowledgeEntry("KB-015", "Cobweb Mold Identification",
                "Cobweb mold (Dactylium/Cladobotryum) appears as gray, fluffy, cobweb-like growth that spreads rapidly over the substrate surface. Can cover entire beds in 24-48 hours. Treatment: isolate, spray with 3% hydrogen peroxide, reduce humidity, increase ventilation. Prevention: pasteurize casing materials, maintain air circulation, monitor humidity levels.",
                "manual", "cultivation", "cobweb mold dactylium cladobotryum hydrogen peroxide",
                "SporeKart Disease Management Guide",
                "SporeKart Training Academy (2026). Mushroom Diseases. Chapter 6.3, pp. 93-95."));

        // Harvesting
        kb.add(new KnowledgeEntry("KB-016", "Harvest Timing and Technique",
                "Harvest mushrooms at the optimal maturity stage: oyster when cap edges are still curled under, shiitake when cap is 70-80% open, button while cap is still closed. Morning harvest is ideal. Technique: grasp at base, twist gently, and pull. Trim dirty stem ends. Handle by stem, not cap, to avoid bruising. Cool to 2-4°C within 2 hours of harvest.",
                "sop", "cultivation", "harvest timing technique maturity cool",
                "SporeKart Post-Harvest Handling Manual",
                "SporeKart Training Academy (2026). Post-Harvest Management. Chapter 7, pp. 105-112."));

        kb.add(new KnowledgeEntry("KB-017", "Yield Optimization and Flush Management",
                "First flush typically yields 60-70% of total harvest. Expect 2-3 flushes for oyster, 3-4 for shiitake, 3-5 for button mushrooms. Rehydrate substrate between flushes by soaking (oyster: 2-4 hours, shiitake: 12-24 hours). Maintain fruiting conditions between flushes. Remove any remaining stems to prevent rot. Total biological efficiency: oyster 100-200%, shiitake 60-100%, button 30-50%.",
                "manual", "cultivation", "yield flush rehydrate biological efficiency BE",
                "SporeKart Production Optimization Guide",
                "SporeKart Training Academy (2026). Commercial Production. Chapter 8, pp. 120-130."));

        // Packaging & Storage
        kb.add(new KnowledgeEntry("KB-018", "Fresh Mushroom Packaging Standards",
                "Fresh mushrooms should be packaged in perforated containers to allow gas exchange while maintaining humidity. Punnet packs with stretch wrap or clam shells are standard for retail. Vacuum packaging extends shelf life to 2-3 weeks. Maintain cold chain at 2-4°C throughout distribution. Never wash mushrooms before packaging - moisture accelerates spoilage.",
                "sop", "cultivation", "packaging fresh perforated vacuum cold chain retail",
                "SporeKart Post-Harvest Standards Manual",
                "SporeKart Training Academy (2026). Post-Harvest Management. Chapter 7.3, pp. 110-112."));

        kb.add(new KnowledgeEntry("KB-019", "Mushroom Drying Protocol",
                "For dried mushrooms: clean gently with soft brush, slice uniformly (5-10mm), dry at 45-50°C until moisture content reaches 10-12%. Use food dehydrator or oven with door slightly open. Store in airtight containers with oxygen absorbers in cool, dark place. Shelf life: 6-12 months. Rehydrate by soaking in warm water for 20-30 minutes before use.",
                "sop", "cultivation", "drying dehydrate moisture storage rehydrate",
                "SporeKart Processing Standards",
                "SporeKart Training Academy (2026). Mushroom Processing and Value Addition. Chapter 9, pp. 135-140."));

        // FAQs
        kb.add(new KnowledgeEntry("KB-020", "FAQ: Why is my spawn not colonizing?",
                "Common causes: temperature too low (below 18°C) or too high (above 30°C), substrate too dry or too wet, insufficient gas exchange (lids too tight), contaminated spawn, or old/dead spawn. Check temperatures, moisture content, and gas exchange. Test spawn viability on agar. Ensure sterilization parameters were correct.",
                "faq", "cultivation", "spawn colonization slow no growth troubleshooting",
                "SporeKart FAQ Compendium",
                "SporeKart Training Academy (2026). Common Issues FAQ. Section 1."));

        kb.add(new KnowledgeEntry("KB-021", "FAQ: How to tell if contamination is present?",
                "Signs of contamination: green (Trichoderma), black (Aspergillus), pink (Neurospora), orange/red (bacteria), yellow slime (Bacillus), gray fluffy growth (Cobweb mold). Off odors: sour, sweet, musty, or ammonia-like smells indicate contamination. Healthy mycelium is white with a fresh mushroom smell. Always trust your nose - if it smells wrong, it probably is.",
                "faq", "cultivation", "contamination identify color smell bacteria mold",
                "SporeKart Troubleshooting Guide",
                "SporeKart Training Academy (2026). Troubleshooting Common Problems. pp. 150-155."));

        kb.add(new KnowledgeEntry("KB-022", "FAQ: What is biological efficiency?",
                "Biological Efficiency (BE) = (fresh mushroom weight / dry substrate weight) x 100. It measures how efficiently the substrate is converted into mushrooms. For example, 10 kg dry substrate producing 15 kg fresh mushrooms = 150% BE. Typical BE ranges: oyster 100-200%, shiitake 60-100%, lion's mane 40-80%, button 30-50%, reishi 20-40%.",
                "faq", "cultivation", "biological efficiency BE yield calculation",
                "SporeKart FAQ Compendium",
                "SporeKart Training Academy (2026). Production Metrics. Section 3."));

        kb.add(new KnowledgeEntry("KB-023", "FAQ: Can I reuse substrate?",
                "Spent mushroom substrate can be recycled but not directly reused for the same species. Uses: soil amendment for gardening, compost ingredient, vermicompost bedding, biogas production, or substrate for certain secondary decomposers like wine cap (Stropharia rugosoannulata). Spent substrate must be pasteurized before use to eliminate pests and pathogens.",
                "faq", "cultivation", "spent substrate reuse recycle compost garden",
                "SporeKart FAQ Compendium",
                "SporeKart Training Academy (2026). Sustainable Practices. Section 4."));

        // Government Guidelines
        kb.add(new KnowledgeEntry("KB-024", "FSSAI Mushroom Product Standards",
                "As per FSSAI regulations, fresh mushrooms must be free from visible contamination, pests, and abnormal odors. Maximum permissible limits: heavy metals (Pb < 2.5 ppm, Cd < 1.0 ppm), pesticide residues as per FSSAI schedule. Dried mushrooms must have moisture content not exceeding 12%. All commercial mushroom products require FSSAI license and batch traceability.",
                "manual", "government", "fssai food safety standards limits heavy metal pesticide",
                "FSSAI Food Safety and Standards Act, 2006",
                "Food Safety and Standards Authority of India (2024). Mushroom Products Standards. FSSAI Notification F.No. 1-4/Standards/2013."));

        kb.add(new KnowledgeEntry("KB-025", "NHB Mushroom Cultivation Subsidy Guidelines",
                "National Horticulture Board (NHB) provides capital investment subsidy for commercial mushroom units. Eligibility: minimum 1 tonne per day capacity. Subsidy: 40% of project cost (max Rs. 50 lakhs) for individual units. Technical standards require: proper pasteurization room, controlled environment cropping rooms, cold storage, and processing facility. Apply through state horticulture departments.",
                "manual", "government", "NHB subsidy horticulture investment government scheme",
                "National Horticulture Board Guidelines 2024-25",
                "National Horticulture Board, Ministry of Agriculture (2024). Capital Investment Subsidy Scheme for Commercial Mushroom Units. Guidelines Document."));

        kb.add(new KnowledgeEntry("KB-026", "APMC Mushroom Trading Regulations",
                "Mushrooms are classified as horticulture produce under APMC Acts. Farmers can sell directly to retailers, processors, or through APMC markets. Grading standards: Grade A - uniform size, no blemishes; Grade B - minor defects; Grade C - processing grade. Mandatory labeling: variety, grade, net weight, packer details, date of packing, best-before date. Electronic trading via e-NAM platform is encouraged.",
                "manual", "government", "APMC trading grading marketing e-NAM regulation",
                "Agricultural Produce Market Committee Act",
                "Ministry of Agriculture (2024). APMC Marketing Standards for Specialty Crops. Section 7: Mushroom Products."));

        kb.add(new KnowledgeEntry("KB-027", "Organic Mushroom Certification Requirements",
                "Organic certification for mushrooms requires: organic spawn from certified sources, organic substrate materials (no synthetic fertilizers or pesticides), non-GMO strains, separate facilities from conventional production, detailed records of all inputs, annual inspection by certification body (NPOP or equivalent). Certification through agencies like APEDA, USDA Organic, or EU Organic. Transition period: 12 months for facility conversion.",
                "manual", "government", "organic certification NPOP APEDA USDA EU standards",
                "National Programme for Organic Production (NPOP)",
                "APEDA (2024). Organic Certification Standards for Mushroom Cultivation. NPOP Guidelines Section 5.2."));

        // Scientific Research
        kb.add(new KnowledgeEntry("KB-028", "Oyster Mushroom Cultivation on Agricultural Wastes",
                "Research on Pleurotus ostreatus cultivation using various agricultural wastes (straw, corn cobs, cottonseed hulls, banana leaves) shows biological efficiency ranging from 80-180%. Wheat straw consistently produces highest yields. Supplementation with 10% wheat bran increases BE by 20-30%. Optimal spawn rate: 5-8%. Temperature optimum: 22-25°C for spawn run, 15-20°C for fruiting.",
                "research", "research", "oyster pleurotus agricultural waste BE yield spawn",
                "Journal of Mushroom Biology, Vol. 15",
                "Kumar, S. & Singh, R. (2025). Optimization of Oyster Mushroom Cultivation on Agricultural Wastes. Journal of Mushroom Biology, 15(2), 112-125. DOI: 10.1016/j.mb.2025.01.005."));

        kb.add(new KnowledgeEntry("KB-029", "Trichoderma Control Using Biological Agents",
                "Recent studies demonstrate that Bacillus subtilis cultures inhibit Trichoderma harzianum growth by 70-85% in vitro. Trichoderma-specific bacteriophage show promise for biological control. Pseudomonas fluorescens produces anti-fungal compounds effective against green mold. Biological control reduces need for chemical fungicides and is suitable for organic production systems.",
                "research", "research", "trichoderma biocontrol bacillus pseudomonas phage organic",
                "International Journal of Mushroom Science, Vol. 28",
                "Patel, A. et al. (2025). Biological Control of Trichoderma in Mushroom Cultivation. International Journal of Mushroom Science, 28(4), 234-248. DOI: 10.1080/ijms.2025.2345."));

        kb.add(new KnowledgeEntry("KB-030", "Nutritional Analysis of Cultivated Mushrooms",
                "Comprehensive analysis of 12 commercially cultivated mushroom species. Protein content ranges 20-35% (dry weight). Oyster mushrooms contain 20-25% protein, shiitake 18-22%, button 25-30%. Rich in B vitamins (especially B2, B3, B5), selenium, potassium, and phosphorus. Beta-glucan content 5-15% depending on species. Ergosterol (provitamin D2) increases 10-fold with UV exposure.",
                "research", "research", "nutritional protein vitamin mineral beta-glucan ergosterol",
                "Food Chemistry Journal, Vol. 450",
                "Mehta, R. & Chen, L. (2026). Nutritional Composition and Bioactive Compounds of Cultivated Mushrooms. Food Chemistry, 450, 128-142. DOI: 10.1016/j.foodchem.2026.128142."));

        kb.add(new KnowledgeEntry("KB-031", "Mushroom-Based Packaging Materials Research",
                "Mycelium-based composite materials show promise as sustainable packaging alternatives. Ganoderma and Pleurotus species grown on agricultural waste produce lightweight, fire-resistant, biodegradable materials with compressive strength of 200-500 kPa. Production cost: Rs. 30-50/kg (vs Rs. 60-80/kg for polystyrene). Commercial viability demonstrated for protective packaging and insulation panels.",
                "research", "research", "mycelium packaging biodegradable composite sustainable",
                "Bioresource Technology Reports, Vol. 18",
                "Sharma, V. et al. (2025). Mycelium-Based Biocomposites for Sustainable Packaging. Bioresource Technology Reports, 18, 101-115. DOI: 10.1016/j.biteb.2025.101115."));

        kb.add(new KnowledgeEntry("KB-032", "Climate Change Impact on Mushroom Cultivation",
                "Rising temperatures affect mushroom cultivation globally. Temperature-sensitive species like shiitake may shift growing zones by 200-300 km poleward by 2050. Heat-tolerant strains of oyster mushrooms (Pleurotus sajor-caju, P. florida) maintain productivity at 30-35°C. Research recommends diversifying species, developing heat-tolerant strains, and adopting climate-controlled production systems.",
                "research", "research", "climate change temperature warming adaptation strain",
                "Global Change Biology, Vol. 31",
                "Rodriguez, M. & Tanaka, H. (2026). Climate Change Vulnerability Assessment of Global Mushroom Production. Global Change Biology, 31(3), 456-472. DOI: 10.1111/gcb.2026.456."));

        // Additional cultivation knowledge
        kb.add(new KnowledgeEntry("KB-033", "Paddy Straw Mushroom Cultivation",
                "Volvariella volvacea (paddy straw mushroom) is a warm-season species ideal for tropical regions. Grows on pasteurized paddy straw at 32-35°C with 85-90% humidity. Fruiting occurs at 28-32°C. Complete crop cycle: 14-18 days. BE: 10-20%. High market demand in Asian markets. Requires minimal infrastructure - suitable for small-scale farmers.",
                "manual", "cultivation", "paddy straw volvariella warm tropical quick cycle",
                "SporeKart Tropical Mushroom Guide",
                "SporeKart Training Academy (2026). Tropical Mushroom Species. Chapter 10, pp. 145-152."));

        kb.add(new KnowledgeEntry("KB-034", "Enoki (Enokitake) Cultivation Protocol",
                "Flammulina velutipes (enoki) requires cold fruiting temperatures (7-12°C) and high CO2 (2000-4000 ppm) to produce the characteristic long, thin stems with small caps. Substrate: sawdust + 20% rice bran, sterilized. Fruiting containers: narrow-necked bottles or bags to restrict light and air. Harvest in 3-4 weeks. Popular in Japanese and Korean cuisine.",
                "manual", "cultivation", "enoki flammulina cold CO2 stem bottle",
                "SporeKart Specialty Mushroom Guide",
                "SporeKart Training Academy (2026). Specialty Mushroom Production. Chapter 11, pp. 155-160."));

        kb.add(new KnowledgeEntry("KB-035", "Mushroom Farm Biosecurity Protocols",
                "Essential biosecurity measures: 1) Designate clean and dirty zones with physical barriers. 2) Install foot baths with disinfectant at all entrances. 3) Require PPE (clean coveralls, hair nets, shoe covers, gloves) in production areas. 4) HEPA-filtered positive pressure air in clean rooms. 5) Quarantine new spawn for 7 days before use. 6) Dedicated equipment per zone. 7) No outside plants or soil in production area.",
                "sop", "cultivation", "biosecurity quarantine hygiene cleanroom HEPA PPE",
                "SporeKart Biosecurity Standards Manual",
                "SporeKart Training Academy (2026). Farm Biosecurity and Hygiene. Section 12, pp. 170-178."));

        kb.add(new KnowledgeEntry("KB-036", "Water Quality Requirements for Mushroom Cultivation",
                "Water used in mushroom cultivation should meet potable water standards: pH 6.5-7.5, TDS < 500 ppm, total hardness < 200 ppm, iron < 0.3 ppm, chlorine < 0.5 ppm, no coliform bacteria. High chlorine damages mycelium. High iron causes substrate discoloration. Hard water reduces effectiveness of supplements. Test water quarterly. Use dechlorination filters if municipal water is used.",
                "manual", "cultivation", "water quality pH TDS hardness chlorine iron testing",
                "SporeKart Quality Standards Manual",
                "SporeKart Training Academy (2026). Water Quality in Cultivation. Appendix C, pp. 185-188."));

        kb.add(new KnowledgeEntry("KB-037", "HACCP Plan for Mushroom Processing",
                "Critical Control Points in mushroom processing: CCP1 - Receiving (visual inspection, temperature check), CCP2 - Washing (chlorinated water 50-100 ppm, contact time 2 min), CCP3 - Cooling (core temperature to 4°C within 2 hours), CCP4 - Packaging (atmosphere control, seal integrity), CCP5 - Storage (2-4°C continuous monitoring). Critical limits, monitoring procedures, corrective actions, and verification protocols must be documented.",
                "manual", "government", "HACCP food safety CCP processing critical control",
                "FSSAI HACCP Guidelines for Horticulture Products",
                "FSSAI (2024). HACCP Implementation Guide for Mushroom Processing Units. Technical Standards Series TSS-2024-07."));

        log.info("Knowledge base built with {} entries", kb.size());
        return kb;
    }

    public record KnowledgeEntry(
        String id,
        String title,
        String content,
        String type,
        String category,
        String tags,
        String source,
        String citation
    ) {}
}
