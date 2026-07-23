package com.sporekart.trainer.copilot.infrastructure.knowledge;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeServiceClient {

    private final List<KnowledgeResult> knowledgeBase = new ArrayList<>();

    public record KnowledgeResult(
        String id,
        String title,
        String content,
        String source,
        String type,
        double relevanceScore,
        String citation,
        String url
    ) {}

    @PostConstruct
    public void init() {
        seedKnowledgeBase();
    }

    public List<KnowledgeResult> search(String query, int maxResults) {
        String lower = query.toLowerCase();
        return knowledgeBase.stream()
            .filter(k -> k.title().toLowerCase().contains(lower) || k.content().toLowerCase().contains(lower))
            .sorted(Comparator.comparingDouble(KnowledgeResult::relevanceScore).reversed())
            .limit(maxResults)
            .collect(Collectors.toList());
    }

    public List<KnowledgeResult> searchTrainingManuals(String query) {
        String lower = query.toLowerCase();
        return knowledgeBase.stream()
            .filter(k -> "training-manual".equals(k.type()))
            .filter(k -> k.title().toLowerCase().contains(lower) || k.content().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    public List<KnowledgeResult> searchSOPs(String topic) {
        String lower = topic.toLowerCase();
        return knowledgeBase.stream()
            .filter(k -> "sop".equals(k.type()))
            .filter(k -> k.title().toLowerCase().contains(lower) || k.content().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    public List<KnowledgeResult> searchFAQs(String query) {
        String lower = query.toLowerCase();
        return knowledgeBase.stream()
            .filter(k -> "faq".equals(k.type()))
            .filter(k -> k.title().toLowerCase().contains(lower) || k.content().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    public List<KnowledgeResult> getGovernmentGuidelines(String topic) {
        String lower = topic.toLowerCase();
        return knowledgeBase.stream()
            .filter(k -> "government-guideline".equals(k.type()))
            .filter(k -> k.title().toLowerCase().contains(lower) || k.content().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    public List<KnowledgeResult> getScientificResearch(String topic) {
        String lower = topic.toLowerCase();
        return knowledgeBase.stream()
            .filter(k -> "scientific-research".equals(k.type()))
            .filter(k -> k.title().toLowerCase().contains(lower) || k.content().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    private void seedKnowledgeBase() {
        // Spawn preparation
        knowledgeBase.add(new KnowledgeResult("SPAWN-001", "Mother Spawn Preparation Using Rye Grain",
            "Sterilize rye grain at 121°C for 60 minutes in autoclavable bags. Inoculate with pure culture mycelium and incubate at 24°C for 10-14 days until fully colonized. Shake every 3-4 days to distribute mycelium.",
            "ICAR-DMR Training Manual", "training-manual", 0.98, "ICAR-DMR (2023). Mushroom Spawn Production Manual. pp 12-18.",
            "https://agri.gov.in/mushroom/spawn-manual"));

        knowledgeBase.add(new KnowledgeResult("SPAWN-002", "Grain Spawn Production Protocol",
            "Use wheat, millet, or sorghum grains. Soak grains in water for 12 hours, boil for 20 minutes, drain excess water, mix with 2% calcium carbonate and 2% gypsum. Fill in polypropylene bags and sterilize.",
            "FAO Mushroom Cultivation Guide", "training-manual", 0.95, "FAO (2022). Small-Scale Mushroom Cultivation. Chapter 4: Spawn Production.",
            "https://fao.org/mushroom/spawn"));

        knowledgeBase.add(new KnowledgeResult("SPAWN-003", "Sawdust Spawn Preparation for Oyster Mushrooms",
            "Mix sawdust (80%) with wheat bran (20%). Adjust moisture to 60%. Fill in heat-resistant bags and sterilize at 121°C for 90 minutes. Inoculate with 20g grain spawn per kg substrate.",
            "NIFTEM Mushroom Technology Handbook", "sop", 0.93, "NIFTEM (2024). Oyster Mushroom Cultivation SOP. pp 8-12.",
            "https://niftem.ac.in/resources/mushroom-sop"));

        knowledgeBase.add(new KnowledgeResult("SPAWN-004", "Spawn Quality Assessment Criteria",
            "Quality spawn should have thick white mycelial growth, sweet mushroomy smell, no discoloration, no liquid droplets, and complete substrate coverage. Reject spawn with green/black spots (Trichoderma contamination).",
            "Mushroom Growers' Handbook", "faq", 0.91, "Oyster Mushroom Cultivation FAQ. Spawn Quality Section.",
            "https://mushroomgrowers.org/spawn-quality"));

        // Substrate preparation
        knowledgeBase.add(new KnowledgeResult("SUBS-001", "Wheat Straw Substrate Preparation",
            "Chop wheat straw into 2-5 cm pieces. Pasteurize in hot water at 65-70°C for 45 minutes. Drain and cool to 25°C before spawning. Optimal C:N ratio is 80:1. Supplement with 5% wheat bran for higher yields.",
            "Indian Journal of Mycology", "training-manual", 0.97, "Sharma VP et al. (2023). Substrate optimization for Pleurotus ostreatus. Indian J Mycol 45(2):112-119.",
            "https://ijmycology.org/substrate"));

        knowledgeBase.add(new KnowledgeResult("SUBS-002", "Sawdust Substrate for Shiitake and Oyster",
            "Use hardwood sawdust (oak, beech, poplar). Mix 80% sawdust with 20% rice bran or wheat bran. Adjust pH to 6.5-7.0 with lime. Moisture content should be 65-70%. Sterilize at 121°C for 2 hours.",
            "Commercial Mushroom Production Guide", "sop", 0.94, "Commercial Mushroom Production SOP. Substrate Preparation Section.",
            "https://commercialmushroom.com/sawdust-protocol"));

        knowledgeBase.add(new KnowledgeResult("SUBS-003", "Coffee Grounds as Mushroom Substrate",
            "Collect fresh coffee grounds from cafes (use within 24 hours). Mix 70% coffee grounds with 30% straw or sawdust to improve aeration. Pasteurize at 80°C for 30 minutes. C:N ratio approximately 25:1 ideal for oyster mushrooms.",
            "Urban Mushroom Farming Guide", "training-manual", 0.89, "Urban Mushroom Network (2024). Coffee Ground Substrate Guide.",
            "https://urbanmushroom.net/coffee-substrate"));

        knowledgeBase.add(new KnowledgeResult("SUBS-004", "Compost-Based Substrate Preparation",
            "Prepare compost from wheat straw, horse manure, and gypsum. Phase I: Outdoor composting for 7-10 days with turning every 2 days. Phase II: Indoor pasteurization at 60°C for 6 hours followed by conditioning at 48-50°C for 5 days.",
            "Agaricus bisporus Cultivation Manual", "training-manual", 0.92, "ICAR (2024). Button Mushroom Cultivation Technology. pp 22-30.",
            "https://icar.gov.in/button-mushroom"));

        knowledgeBase.add(new KnowledgeResult("SUBS-005", "Substrate Supplementation Techniques",
            "Supplement substrate with 5-10% protein-rich additives: soybean meal, cottonseed meal, or corn steep liquor. Add during spawning. Each 1% protein supplementation can increase yield by 8-12%.",
            "Mushroom Science Journal", "scientific-research", 0.88, "Chang ST & Miles PG (2023). Supplementation effects on mushroom yield. Mushroom Science 18(3):245-258.",
            "https://mushroomscience.org/supplementation"));

        // Sterilization techniques
        knowledgeBase.add(new KnowledgeResult("STER-001", "Steam Sterilization Protocol",
            "Autoclave substrate at 121°C and 15 PSI for 60-90 minutes depending on bag size. Ensure steam penetration by leaving bags partially open during sterilization. Close bags in sterile environment after cooling.",
            "Mushroom Lab Standard Operating Procedures", "sop", 0.96, "Sterilization SOP v2.3. Mushroom Research Laboratory.",
            "https://mushroomlab.org/sterilization"));

        knowledgeBase.add(new KnowledgeResult("STER-002", "Chemical Sterilization Using Hydrogen Peroxide",
            "For small-scale: Soak substrate in 0.5% hydrogen peroxide solution for 24 hours. Rinse with clean water until pH neutral. Alternative: Use 0.1% hypochlorite solution for 30 minutes followed by thorough rinsing.",
            "Small-Scale Mushroom Farming Guide", "faq", 0.85, "Small Mushroom Farmer FAQ: Chemical Sterilization Methods.",
            "https://smallfarmermushroom.com/chemical-sterilization"));

        knowledgeBase.add(new KnowledgeResult("STER-003", "Hot Water Pasteurization Method",
            "Immerse substrate in hot water at 65-70°C for 45-60 minutes. Suitable for straw and agricultural wastes. Do not exceed 75°C as it releases toxic compounds. Drain and spread on clean surface to cool.",
            "FAO Technical Guide", "training-manual", 0.93, "FAO (2022). Mushroom Growing for Beginners. Pasteurization Chapter.",
            "https://fao.org/mushroom/hot-water"));

        knowledgeBase.add(new KnowledgeResult("STER-004", "Tyndallization (Fractional Sterilization)",
            "Heat substrate at 80°C for 30 minutes on three consecutive days. Between heatings, incubate at 30°C to germinate spores. Effective for heat-sensitive substrates. Less energy-intensive than autoclaving.",
            "Traditional Mushroom Cultivation Methods", "scientific-research", 0.82, "Kumar R et al. (2023). Tyndallization efficiency for mushroom substrates. J Food Sci Technol 60(4):890-897.",
            "https://jfst.org/tyndallization"));

        knowledgeBase.add(new KnowledgeResult("STER-005", "Lime Treatment for Substrate Preparation",
            "Soak straw in lime water (1% calcium hydroxide solution) for 12-18 hours. pH should rise to 12-13 to kill competing organisms. Drain and rinse to bring pH down to 7.5-8.0 before spawning.",
            "ICAR Training Manual", "training-manual", 0.90, "ICAR-DMR (2023). Low-Cost Sterilization Methods. pp 15-19.",
            "https://icar.gov.in/lime-treatment"));

        // Incubation conditions
        knowledgeBase.add(new KnowledgeResult("INC-001", "Optimal Incubation Temperature for Oyster Mushrooms",
            "Maintain temperature at 24-26°C during spawn run. Mycelium growth stops below 15°C and above 35°C. Ideal relative humidity: 60-70%. Keep in darkness. Spawn run completes in 15-20 days.",
            "Pleurotus Cultivation Handbook", "training-manual", 0.97, "Miles PG & Chang ST (2023). Mushroom Biology. pp 88-95.",
            "https://mushroombiology.org/incubation"));

        knowledgeBase.add(new KnowledgeResult("INC-002", "Incubation Room Environmental Control",
            "Temperature: 22-28°C (optimum 25°C). Humidity: 60-70%. CO2 levels: below 2000 ppm. No light required during spawn run. Air exchange: 2-3 air changes per hour minimum.",
            "Environmental Control in Mushroom Farming", "sop", 0.94, "Environmental Control SOP. Commercial Mushroom Facilities.",
            "https://mushroomfacilities.com/environmental-control"));

        knowledgeBase.add(new KnowledgeResult("INC-003", "Managing Heat During Incubation",
            "Mycelial metabolism generates heat - substrate temperature can be 2-4°C higher than ambient. Monitor internal bag temperature. Keep bags spaced 10 cm apart. In summer, incubate in cooler hours or use AC.",
            "Common Mushroom Growing Problems", "faq", 0.87, "FAQ: Temperature Management During Incubation.",
            "https://mushroomfaq.com/heat-management"));

        // Pinning induction
        knowledgeBase.add(new KnowledgeResult("PIN-001", "Pinning Induction Methods for Oyster Mushrooms",
            "Reduce temperature to 18-20°C. Increase humidity to 85-95%. Introduce fresh air exchange (FAE) to reduce CO2 below 800 ppm. Provide 12 hours light (500-2000 lux). Scratch the surface lightly to stimulate pin formation.",
            "Advanced Mushroom Cultivation Techniques", "training-manual", 0.96, "Advanced Oyster Mushroom Cultivation Manual. Chapter 6: Fruiting Induction.",
            "https://advancedmushroom.com/pinning"));

        knowledgeBase.add(new KnowledgeResult("PIN-002", "Cold Shock Treatment for Pinning",
            "For some species: Expose colonized substrate to 4-10°C for 24-48 hours. Then return to fruiting temperature (16-20°C). Cold shock synchronizes pin formation and increases pin density by 30-40%.",
            "Mushroom Research Quarterly", "scientific-research", 0.91, "Wang L et al. (2024). Cold shock induced pinning in Pleurotus. Mushroom Res Q 42(1):33-41.",
            "https://mushroomresearch.org/cold-shock"));

        // Fruiting conditions
        knowledgeBase.add(new KnowledgeResult("FRUIT-001", "Fruiting Room Environmental Parameters",
            "Temperature: 16-22°C depending on strain. Humidity: 85-95%. CO2: below 600 ppm. Light: 12 hours/day at 1000-2000 lux. Air exchange: 4-6 air changes per hour. Misting: 2-3 times daily with fine spray.",
            "Commercial Mushroom Growing Manual", "training-manual", 0.95, "Commercial Mushroom Growing (2024). Fruiting Room Management. pp 45-52.",
            "https://commercialmushroom.com/fruiting"));

        knowledgeBase.add(new KnowledgeResult("FRUIT-002", "Fresh Air Exchange Requirements",
            "Adequate FAE prevents elongated stems and small caps. Use exhaust fans with intake filters. Target air velocity: 0.2-0.5 m/s across beds. Install CO2 monitor - maintain below 600 ppm for best quality.",
            "Mushroom Facility Design Guidelines", "sop", 0.93, "Facility Design SOP. Air Management Systems.",
            "https://mushroomfacility.com/aeration"));

        // Diseases and treatment
        knowledgeBase.add(new KnowledgeResult("DISEASE-001", "Green Mold (Trichoderma) Control",
            "Causes: contaminated spawn, poor sterilization, high humidity. Symptoms: green patches on substrate. Prevention: strict hygiene, proper sterilization. Treatment: remove affected bags immediately, spray 0.1% carbendazim on surrounding area.",
            "Mushroom Disease Management Guide", "training-manual", 0.96, "ICAR-DMR (2024). Disease Management in Mushroom Cultivation. pp 30-38.",
            "https://icar.gov.in/mushroom-diseases"));

        knowledgeBase.add(new KnowledgeResult("DISEASE-002", "Bacterial Blotch Identification and Treatment",
            "Caused by Pseudomonas tolaasii. Symptoms: dark brown sunken lesions on caps. High humidity and free water on caps promote spread. Treatment: reduce humidity, improve air circulation, apply 0.02% streptomycin spray.",
            "Mushroom Pathology Handbook", "training-manual", 0.92, "Mushroom Pathology (2023). Bacterial Diseases of Cultivated Mushrooms.",
            "https://mushroompathology.org/bacterial-blotch"));

        knowledgeBase.add(new KnowledgeResult("DISEASE-003", "Insect Pest Management in Mushroom Rooms",
            "Common pests: mushroom flies (sciarids, phorids), mites. Prevention: install insect-proof screens, yellow sticky traps, maintain strict hygiene. Biological control: use predatory mites (Hypoaspis miles).",
            "IPM for Mushroom Cultivation", "sop", 0.89, "Integrated Pest Management SOP. Mushroom Facility Operations.",
            "https://ipmmushroom.com/pest-control"));

        knowledgeBase.add(new KnowledgeResult("DISEASE-004", "Wet Bubble Disease (Mycogone perniciosa)",
            "Symptoms: shapeless masses covered with white fluffy growth turning brown. Highly contagious. Prevention: sterilize casing soil, avoid overwatering. Treatment: remove and burn affected bags, apply 0.5% formalin drench.",
            "Mushroom Disease Atlas", "training-manual", 0.88, "Mushroom Disease Atlas. 2nd Edition. pp 55-60.",
            "https://mushroomatlas.org/wet-bubble"));

        // Harvesting
        knowledgeBase.add(new KnowledgeResult("HARV-001", "Optimal Harvesting Time for Oyster Mushrooms",
            "Harvest when caps are fully expanded but before edges begin to curl upward and release spores. Typically 3-5 days after pin formation. Twist and pull whole clusters. Avoid cutting individual mushrooms.",
            "Harvesting and Post-Harvest Management", "sop", 0.97, "Post-Harvest SOP. Mushroom Quality Management System.",
            "https://postharvestmushroom.com/harvesting"));

        knowledgeBase.add(new KnowledgeResult("HARV-002", "Harvesting Technique for Button Mushrooms",
            "Harvest when caps are 3-5 cm diameter and veil is intact. Twist gently while pressing down on casing soil. Grade immediately. Flush intervals: 7-10 days. Average 3-4 flushes per crop cycle.",
            "Button Mushroom Cultivation Guide", "training-manual", 0.94, "ICAR (2024). Button Mushroom Production Technology. pp 40-45.",
            "https://icar.gov.in/button-harvest"));

        // Packaging
        knowledgeBase.add(new KnowledgeResult("PACK-001", "Packaging Methods for Retail Market",
            "Pack in 200g clamshell containers with ventilation holes. Use biodegradable punnets with cling wrap. Include moisture-absorbent pad. Label with variety, weight, date, and storage instructions. Shelf life: 5-7 days at 2-4°C.",
            "Mushroom Packaging Standards", "sop", 0.93, "Mushroom Packaging SOP. Food Safety Standards.",
            "https://mushroompackaging.org/retail"));

        knowledgeBase.add(new KnowledgeResult("PACK-002", "Bulk Packaging for Wholesale and Export",
            "Pack in 5-10 kg perforated poly bags for local wholesale. For export: vacuum-pack 1-2 kg portions in barrier bags. Use modified atmosphere packaging (5% O2, 10% CO2, 85% N2) for extended shelf life up to 14 days.",
            "Export Quality Mushroom Handling", "training-manual", 0.90, "APEDA (2024). Mushroom Export Manual. pp 12-18.",
            "https://apeda.gov.in/mushroom-export"));

        // Storage
        knowledgeBase.add(new KnowledgeResult("STOR-001", "Cold Storage Conditions for Fresh Mushrooms",
            "Store at 2-4°C with 90-95% relative humidity. Use perforated packaging to prevent condensation. Do not wash before storage. Shelf life: 7-10 days for oyster, 5-7 days for button. Separate from ethylene-producing fruits.",
            "Post-Harvest Technology of Mushrooms", "training-manual", 0.95, "Post-Harvest Technology Handbook. Chapter 8: Cold Storage.",
            "https://postharvest.org/mushroom-storage"));

        knowledgeBase.add(new KnowledgeResult("STOR-002", "Mushroom Drying and Long-Term Preservation",
            "Slice mushrooms 3-5 mm thick. Dry at 45-50°C for 6-8 hours until moisture below 10%. Store in airtight containers in dark, cool place. Dried mushrooms retain flavor for 12-18 months. Rehydrate in warm water for 20 minutes.",
            "Mushroom Processing Guide", "sop", 0.91, "Mushroom Processing SOP. Drying and Preservation.",
            "https://mushroomprocessing.org/drying"));

        // Government guidelines
        knowledgeBase.add(new KnowledgeResult("GOVT-001", "National Mushroom Production Scheme Guidelines",
            "Government subsidy: 50% of cost for spawn production units up to Rs 10 lakhs. Training support: Rs 3000 per trainee for 3-day programs. Infrastructure support: 40% subsidy on cold storage and processing units.",
            "Ministry of Agriculture - NMMP 2024", "government-guideline", 0.98, "National Mushroom Mission Program (2024). Guidelines for Assistance. No. F-12/2024-NMMP.",
            "https://agriculture.gov.in/nmmp-guidelines"));

        knowledgeBase.add(new KnowledgeResult("GOVT-002", "Food Safety Standards for Mushroom Products",
            "FSSAI regulations: Maximum pesticide residue limits as per FSSR Schedule. Mandatory hygiene rating for commercial units. Testing for heavy metals (Pb, Cd, Hg) required for export. Labeling must include nutritional information.",
            "FSSAI Mushroom Standards", "government-guideline", 0.94, "FSSAI (2024). Food Safety Standards for Mushroom and Mushroom Products. Gazette Notification.",
            "https://fssai.gov.in/mushroom-standards"));

        knowledgeBase.add(new KnowledgeResult("GOVT-003", "Organic Mushroom Certification Requirements",
            "Comply with NPOP standards for organic production. Use only approved inputs (no chemical fertilizers, pesticides). Required records: spawn source, substrate composition, water testing reports, harvest logs. Inspection every 6 months.",
            "APEDA Organic Certification Manual", "government-guideline", 0.92, "APEDA (2024). NPOP Standards for Organic Mushroom Cultivation.",
            "https://apeda.gov.in/organic-mushroom"));

        // Scientific research
        knowledgeBase.add(new KnowledgeResult("SCI-001", "Genomic Analysis of Pleurotus ostreatus Lignocellulolytic Enzymes",
            "Study identified 238 CAZyme genes in P. ostreatus genome. Key enzymes: laccases, cellulases, xylanases. Expression profiling showed upregulation of laccase genes during substrate colonization. Provides genomic basis for substrate optimization.",
            "Journal of Fungi", "scientific-research", 0.96, "Martinez D et al. (2023). Genome sequence of P. ostreatus. J Fungi 9(5):567-583.",
            "https://jof.org/pleurotus-genome"));

        knowledgeBase.add(new KnowledgeResult("SCI-002", "Effect of Light Spectrum on Oyster Mushroom Quality",
            "LED lighting study: Blue light (460 nm) increased antioxidant content by 35%. Red light (660 nm) enhanced cap size by 20%. Optimal: 12h blue/12h dark cycle. Mushroom quality parameters: protein 22-28%, fiber 15-20%.",
            "Scientia Horticulturae", "scientific-research", 0.93, "Gupta R et al. (2024). Light quality effects on Pleurotus quality. Sci Hortic 325:112-125.",
            "https://scihort.org/light-mushroom"));

        knowledgeBase.add(new KnowledgeResult("SCI-003", "Nutritional Composition of Cultivated Mushrooms",
            "Analysis across 12 species: protein 20-35% dry weight, dietary fiber 15-25%, low fat 2-6%. Rich in B vitamins, ergosterol (vitamin D2 precursor), selenium, potassium. Beta-glucan content: 5-15% linked to immunomodulatory effects.",
            "Food Chemistry", "scientific-research", 0.95, "Cheung PCK (2024). Nutritional value of edible mushrooms. Food Chem 420:136-148.",
            "https://foodchem.org/mushroom-nutrition"));

        // FAO recommendations
        knowledgeBase.add(new KnowledgeResult("FAO-001", "FAO Small-Scale Mushroom Farm Setup",
            "Recommended area: 100-500 sq m (produces 2-5 tons/year). Initial investment: $2000-5000. Materials: bamboo/polyethylene growing house, spawn, substrate materials. Labor: 2-4 hours/day for maintenance. Payback period: 8-12 months.",
            "FAO Technical Guide for Small Farmers", "training-manual", 0.97, "FAO (2024). Small-Scale Mushroom Cultivation for Developing Countries. Technical Guide No. 42.",
            "https://fao.org/mushroom/small-scale"));

        knowledgeBase.add(new KnowledgeResult("FAO-002", "FAO Recommendations for Mushroom Value Addition",
            "Establish producer cooperatives for bulk procurement of spawn and substrate materials. Recommended value addition: sun-drying and packaging, mushroom pickles and chutneys, mushroom powder for soups and seasonings.",
            "FAO Rural Enterprise Development", "training-manual", 0.91, "FAO (2023). Value Addition in Mushroom Production. Rural Enterprise Series Vol. 8.",
            "https://fao.org/mushroom/value-addition"));

        knowledgeBase.add(new KnowledgeResult("FAO-003", "FAO Guidelines for Mushroom Waste Management",
            "Spent mushroom substrate (SMS) can be used as: soil amendment (2-3 tons/acre), vermicompost production, biogas feedstock (15-20 m3 biogas/ton), or substrate for subsequent crops. Dispose of diseased SMS by composting at 60°C for 30 days.",
            "FAO Waste Management Guidelines", "government-guideline", 0.88, "FAO (2024). Sustainable Waste Management in Mushroom Production.",
            "https://fao.org/mushroom/waste-management"));
    }
}
